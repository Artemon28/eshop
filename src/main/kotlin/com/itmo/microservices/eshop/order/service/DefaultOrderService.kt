package com.itmo.microservices.eshop.order.service

import com.itmo.microservices.eshop.auth.service.JwtTokenManager
import com.itmo.microservices.eshop.catalog.service.ICatalogService
import com.itmo.microservices.eshop.common.exception.BadRequestException
import com.itmo.microservices.eshop.common.exception.NotFoundException
import com.itmo.microservices.eshop.order.entity.OrderAggregate
import com.itmo.microservices.eshop.order.entity.Order
import com.itmo.microservices.eshop.order.model.*
import com.itmo.microservices.eshop.order.repository.IBookingRepository
import com.itmo.microservices.eshop.order.repository.IOrderRepository
import com.itmo.microservices.eshop.payment.models.*
import com.itmo.microservices.eshop.users.entity.UserAggregate
import com.itmo.microservices.eshop.users.entity.UserState
import com.itmo.microservices.eshop.users.models.UserDto
import com.itmo.microservices.eshop.users.models.UserUnit
import com.itmo.microservices.eshop.users.repository.IUserRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import ru.quipy.core.EventSourcingService
import java.util.*
import java.util.concurrent.ThreadLocalRandom

@Service
class DefaultOrderService(
    private val orderESService: EventSourcingService<UUID, OrderAggregate, Order>,
    private val catalogService: ICatalogService,
    private val orderRepository: IOrderRepository,
    private val userESService: EventSourcingService<UUID, UserAggregate, UserState>,
    private val userRepository: IUserRepository,
    private val tokenManager: JwtTokenManager,
    private val bookingRepository: IBookingRepository
) : IOrderService {
    override fun createOrder(): OrderDto {
        val event = orderESService.create { it.createOrder() }
        val orderState = orderESService.getState(event.orderId)
            ?: throw NotFoundException("Order was not created")
        val orderDto = orderState.toDto()
        orderRepository.save(orderDto)
        return orderDto
    }

    override fun getOrder(orderId: UUID): OrderDto {
        val orderDto: OrderDto
        try {
            orderDto = orderRepository.findById(orderId).get()
        } catch (e: NoSuchElementException) {
            throw NotFoundException("Order was not created")
        }
        return orderDto
    }

    override fun addItemToCart(orderId: UUID, itemId: UUID, amount: Int?) {
        var resultAmount = amount ?: 1
        val catalogItem = catalogService.getProduct(itemId)

        val orderState: Order = orderESService.getState(orderId)
            ?: throw BadRequestException("Error: orderId " + orderId + "does not exist")

        if (orderState.getStatus() == OrderStatus.BOOKED) {
            catalogService.unbookProduct(orderState.getItemsMap())
            val prevAmount = orderState.getItemsMap()[itemId]
            if (prevAmount != null){
                resultAmount = amount!! + prevAmount
            }
        }

        if (catalogItem.amount < resultAmount) {
            throw BadRequestException("Error: addItemToCart method requires more amount than item has")
        }
        orderESService.update(orderId) { it.addItem(itemId, resultAmount) }
        val changedOrderState = orderESService.getState(orderId)
        val orderDto = changedOrderState!!.toDto()
        orderRepository.save(orderDto)
    }

    override fun setDeliveryTimeForOrder(orderId: UUID, slot: Int?) {
        if (slot == null) {
            throw BadRequestException("Error: Slot not found in parameters")
        }
        orderESService.update(orderId) { it.setDeliveryTime(slot) }
        val changedOrderState = orderESService.getState(orderId)
        val orderDto = changedOrderState!!.toDto()
        orderRepository.save(orderDto)
    }

    fun createBookingLogRecord(
        bookingId: UUID,
        itemId: UUID,
        amount: Int,
        failedItems: Set<UUID>,
        timestamp: Long
    ) {
        val status: BookingStatus = if (failedItems.contains(itemId)) {
            BookingStatus.FAILED
        } else {
            BookingStatus.SUCCESS
        }
        val bookingRecord = BookingLogRecord(
            bookingId = bookingId,
            itemId = itemId,
            status = status,
            amount = amount,
            timestamp = timestamp
        )
        bookingRepository.save(bookingRecord)
    }

    override fun finalizeOrder(orderId: UUID): BookingDto {
        val orderState = orderESService.getState(orderId)
            ?: throw NotFoundException("Order was not found while booking")

        val status = orderState.getStatus()
        if (status == OrderStatus.BOOKED) {
            throw Exception("Error: order $orderId is already booked")
        }

        if (status != OrderStatus.COLLECTING) {
            throw Exception("Error: order $orderId has status $status and can not be finalized")
        }
        val productMap = orderState.getItemsMap()
        if (productMap.isEmpty()){
            throw Exception("Error: order $orderId doesn't have any products")
        }

        val bookingId = UUID.randomUUID()
        val failedItems = catalogService.bookProduct(orderState.getItemsMap())
        val event = orderESService.update(orderId) {
            it.finalizeOrder(bookingId, failedItems)
        }
        val changedOrderState = orderESService.getState(orderId)
        val orderDto = changedOrderState!!.toDto()
        orderRepository.save(orderDto)

        val bookingDto = BookingDto(bookingId, failedItems)
        for (item in orderState.getItemsMap()) {
            createBookingLogRecord(
                bookingDto.id,
                item.key,
                item.value,
                failedItems,
                event.createdAt
            )
        }
        return bookingDto
    }

    override fun unbookOrder(orderId: UUID): OrderDto{
        val orderState = orderESService.getState(orderId)
            ?: throw NotFoundException("Order was not found while booking")

        val status = orderState.getStatus()
        if (status != OrderStatus.BOOKED) {
            throw Exception("Error: order $orderId is not booked, his status is $status")
        }

        val event = orderESService.update(orderId) {
            it.unbookOrder()
        }
        catalogService.unbookProduct(orderState.getItemsMap())
        val changedOrderState = orderESService.getState(orderId)
        val orderDto = changedOrderState!!.toDto()
        orderRepository.save(orderDto)

        return orderDto
    }

    override fun getBookingItems(bookingId: UUID): List<BookingLogRecord> {
        return bookingRepository.findAllByBookingId(bookingId).get()
    }

    override fun orderPay(orderId: UUID, user: UserDetails): PaymentSubmissionDto {
        val orderState: Order = orderESService.getState(orderId)
            ?: throw Exception("Error: orderId $orderId does not exist")

        if (orderState.getStatus() != OrderStatus.BOOKED) {
            throw Exception("Error: order $orderId isn't finalized")
        }
        val transactionId = UUID.randomUUID()
        val orderPaymentLogRecordDto = PaymentLogRecordDto(timestamp = 10, PaymentStatus.SUCCESS, 1, transactionId)

        orderESService.update(orderId) { it.orderPayment(orderPaymentLogRecordDto) }
        val changedOrderState = orderESService.getState(orderId)
        val orderDto = changedOrderState!!.toDto()
        orderRepository.save(orderDto)

        var userName = user.username

        val userUnit: UserUnit
        try {
            userUnit = userRepository.findByName(userName).get()
        } catch (e: NoSuchElementException) {
            throw NotFoundException()
        }
        var finActivity = UserAccountFinancialLogRecordDto(FinancialOperationType.WITHDRAW, 1, orderId, transactionId, timestamp = 10)
        userESService.update(userUnit.id) {it.newFinancialActivity(finActivity)}
        var updUser = userESService.getState(userUnit.id)

        userRepository.save(updUser!!.toUnit())
        checkDeliveryResponse(orderId, changedOrderState.getBookingId()!!, updUser.getId(), transactionId)
        return PaymentSubmissionDto(orderPaymentLogRecordDto.timestamp, orderPaymentLogRecordDto.transactionId)
    }

    @Scheduled(fixedDelay = 1000)
    fun checkDeliveryResponse(orderId: UUID, bookingId: UUID, userId: UUID, transactionId: UUID) {
        val orderState: Order = orderESService.getState(orderId)
            ?: throw Exception("Error: orderId $orderId does not exist")

        if (orderState.getStatus() != OrderStatus.PAID) {
            throw Exception("Error: order $orderId isn't paid")
        }
        orderESService.update(orderId) { it.unbookOrder() }
        orderESService.update(orderId) { it.failDelivery() }
        catalogService.unbookProduct(orderState.getItemsMap())
        val changedOrderState = orderESService.getState(orderId)
        val orderDto = changedOrderState!!.toDto()
        orderRepository.save(orderDto)

        val userUnit: UserUnit
        try {
            userUnit = userRepository.findById(userId).get()
        } catch (e: NoSuchElementException) {
            throw NotFoundException()
        }
        var finActivity = UserAccountFinancialLogRecordDto(FinancialOperationType.REFUND, 1, orderId, transactionId, timestamp = 10)
        userESService.update(userUnit.id) {it.newFinancialActivity(finActivity)}
        var updUser = userESService.getState(userUnit.id)
        userRepository.save(updUser!!.toUnit())
    }


    override fun paymentHistoryOfTheOrder(orderId: UUID?): List<PaymentLogRecordDto> {
        if (orderId == null){
            throw Exception("I'm so sorry, I don't have user payment history :-(")
        }
        var orderState : Order = orderESService.getState(orderId)
            ?: throw Exception("Error: orderId $orderId does not exist")

        return orderState.showPaymentHistory()
    }
}