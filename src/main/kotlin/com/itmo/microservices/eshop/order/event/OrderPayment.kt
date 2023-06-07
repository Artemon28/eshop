package com.itmo.microservices.eshop.order.event

import com.itmo.microservices.eshop.order.entity.OrderAggregate
import com.itmo.microservices.eshop.payment.models.PaymentLogRecordDto
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val PAY_ORDER = "PAY_ORDER"

@DomainEvent(name = PAY_ORDER)
data class OrderPayment(
    val orderId: UUID,
    val paymentRecord: PaymentLogRecordDto
) : Event<OrderAggregate>(
    name = PAY_ORDER,
    createdAt = System.currentTimeMillis(),
)