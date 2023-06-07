package com.itmo.microservices.eshop.order.entity

import com.itmo.microservices.eshop.order.event.*
import com.itmo.microservices.eshop.order.model.OrderDto
import com.itmo.microservices.eshop.order.model.OrderStatus
import com.itmo.microservices.eshop.payment.models.PaymentLogRecordDto
import org.springframework.data.annotation.Id
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

class Order: AggregateState<UUID, OrderAggregate> {
    @Id
    private lateinit var orderId: UUID
    private var timeCreated: Long = 0
    private lateinit var status: OrderStatus
    private lateinit var products: MutableMap<UUID, Int>
    private var deliveryDuration: Int? = null
    private lateinit var paymentHistory: MutableList<PaymentLogRecordDto>
    private var bookingId: UUID? = null

    override fun getId(): UUID = orderId

    fun getStatus(): OrderStatus = status

    fun getBookingId(): UUID?{
        return bookingId
    }

    fun getItemsMap(): MutableMap<UUID, Int> = products

    fun toDto(): OrderDto {
        return OrderDto(
            id = orderId,
            timeCreated = timeCreated,
            status = status,
            itemsMap = products,
            deliveryDuration = deliveryDuration,
            paymentHistory = paymentHistory
        )
    }

    fun createOrder(): OrderCreated {
        return OrderCreated(
            orderId = UUID.randomUUID()
        )
    }

    fun addItem(itemId: UUID, amount: Int): OrderAddItem {
        return OrderAddItem(
            orderId = orderId,
            itemId = itemId,
            amount = amount
        )
    }

    fun setDeliveryTime(deliveryDuration: Int): OrderSetDeliveryTime {
        return OrderSetDeliveryTime(
            orderId = orderId,
            deliveryDuration = deliveryDuration
        )
    }

    fun finalizeOrder(bookingId: UUID, failedItems: Set<UUID>): OrderFinalize {
        return OrderFinalize(
            orderId = orderId,
            bookingId = bookingId,
            failedItems = failedItems
        )
    }

    fun unbookOrder(): OrderUnbook {
        return OrderUnbook(
            orderId = orderId
        )
    }

    fun orderPayment(paymentRecord: PaymentLogRecordDto): OrderPayment {
        return OrderPayment(
            orderId = orderId,
            paymentRecord = paymentRecord
        )
    }

    fun showPaymentHistory(): List<PaymentLogRecordDto>{
        return paymentHistory
    }

    fun successDeliveryDone(): SuccessDelivery{
        return SuccessDelivery(
            orderId = orderId
        )
    }

    fun failDelivery(): FailDelivery{
        return FailDelivery(
            orderId = orderId
        )
    }


    @StateTransitionFunc
    fun createOrder(event: OrderCreated) {
        orderId = event.orderId
        timeCreated = event.createdAt
        status = OrderStatus.COLLECTING
        products = mutableMapOf()
        deliveryDuration = null
        paymentHistory = mutableListOf()
    }

    @StateTransitionFunc
    fun addItem(event: OrderAddItem) {
        status = OrderStatus.COLLECTING
        products[event.itemId] = event.amount
//        if (products[event.itemId] == null){
//            products[event.itemId] = event.amount
//        } else {
//            products[event.itemId] = products[event.itemId]!! + event.amount
//        }
    }

    @StateTransitionFunc
    fun setDeliveryTime(event: OrderSetDeliveryTime) {
        deliveryDuration = event.deliveryDuration
    }

    @StateTransitionFunc
    fun finalizeOrder(event: OrderFinalize) {
        status = OrderStatus.BOOKED
        bookingId = event.bookingId
        for (failed in event.failedItems) {
            products.remove(failed)
        }
    }

    @StateTransitionFunc
    fun unbookOrder(event: OrderUnbook) {
        status = OrderStatus.COLLECTING
        bookingId = null
    }

    @StateTransitionFunc
    fun orderPayment(event: OrderPayment) {
        paymentHistory.add(event.paymentRecord)
        status = OrderStatus.PAID
    }

    @StateTransitionFunc
    fun successDeliveryDone(event: SuccessDelivery){
        status = OrderStatus.COMPLETED
    }

    @StateTransitionFunc
    fun failDelivery(event: FailDelivery){
        status = OrderStatus.REFUND
    }
}