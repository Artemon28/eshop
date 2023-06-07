package com.itmo.microservices.eshop.order.service

import com.itmo.microservices.eshop.order.model.BookingDto
import com.itmo.microservices.eshop.order.model.BookingLogRecord
import com.itmo.microservices.eshop.order.model.OrderDto
import com.itmo.microservices.eshop.payment.models.PaymentLogRecordDto
import com.itmo.microservices.eshop.payment.models.PaymentSubmissionDto
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

interface IOrderService {
    fun createOrder(): OrderDto
    fun getOrder(orderId: UUID): OrderDto
    fun addItemToCart(orderId: UUID, itemId: UUID, amount: Int?)
    fun setDeliveryTimeForOrder(orderId: UUID, slot: Int?)
    fun finalizeOrder(orderId: UUID): BookingDto
    fun getBookingItems(bookingId: UUID): List<BookingLogRecord>
    fun orderPay(orderId: UUID, user: UserDetails): PaymentSubmissionDto
    fun unbookOrder(orderId: UUID): OrderDto
    fun paymentHistoryOfTheOrder(orderId: UUID?): List<PaymentLogRecordDto>
}