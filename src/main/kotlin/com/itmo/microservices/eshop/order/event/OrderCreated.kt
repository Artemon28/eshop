package com.itmo.microservices.eshop.order.event

import com.itmo.microservices.eshop.order.entity.OrderAggregate
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val ORDER_CREATED = "ODER_CREATED_EVENT"

@DomainEvent(name = ORDER_CREATED)
data class OrderCreated(
    val orderId: UUID
) : Event<OrderAggregate>(
    name = ORDER_CREATED,
    createdAt = System.currentTimeMillis(),
)