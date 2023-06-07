package com.itmo.microservices.eshop.order.event

import com.itmo.microservices.eshop.order.entity.OrderAggregate
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val ORDER_UNBOOKED = "ORDER_UNBOOKED"

@DomainEvent(name = ORDER_UNBOOKED)
data class OrderUnbook(
    val orderId: UUID
) : Event<OrderAggregate>(
    name = ORDER_UNBOOKED,
    createdAt = System.currentTimeMillis(),
)