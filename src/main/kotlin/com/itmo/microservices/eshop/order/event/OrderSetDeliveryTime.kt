package com.itmo.microservices.eshop.order.event

import com.itmo.microservices.eshop.order.entity.OrderAggregate
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val ORDER_DELIVERY_TIME = "ORDER_DELIVERY_TIME"

@DomainEvent(name = ORDER_DELIVERY_TIME)
data class OrderSetDeliveryTime(
    val orderId: UUID,
    val deliveryDuration: Int
) : Event<OrderAggregate>(
    name = ORDER_DELIVERY_TIME,
    createdAt = System.currentTimeMillis(),
)