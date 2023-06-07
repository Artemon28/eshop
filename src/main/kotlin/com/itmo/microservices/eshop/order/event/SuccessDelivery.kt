package com.itmo.microservices.eshop.order.event

import com.itmo.microservices.eshop.order.entity.OrderAggregate
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val ORDER_WAS_DELIVERED = "ORDER_WAS_DELIVERED"

@DomainEvent(name = ORDER_WAS_DELIVERED)
data class SuccessDelivery(
    val orderId: UUID
) : Event<OrderAggregate>(
    name = ORDER_WAS_DELIVERED,
    createdAt = System.currentTimeMillis(),
)