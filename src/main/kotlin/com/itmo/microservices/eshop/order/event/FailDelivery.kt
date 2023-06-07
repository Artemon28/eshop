package com.itmo.microservices.eshop.order.event

import com.itmo.microservices.eshop.order.entity.OrderAggregate
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val ORDER_DELIVERY_FAIL = "ORDER_DELIVERY_FAIL"

@DomainEvent(name = ORDER_DELIVERY_FAIL)
data class FailDelivery(
    val orderId: UUID
) : Event<OrderAggregate>(
    name = ORDER_DELIVERY_FAIL,
    createdAt = System.currentTimeMillis(),
)