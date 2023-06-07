package com.itmo.microservices.eshop.order.event

import com.itmo.microservices.eshop.order.entity.OrderAggregate
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val ORDER_ADD_ITEM = "ORDER_ADD_ITEM"

@DomainEvent(name = ORDER_ADD_ITEM)
data class OrderAddItem(
    val orderId: UUID,
    val itemId: UUID,
    val amount: Int
) : Event<OrderAggregate>(
    name = ORDER_ADD_ITEM,
    createdAt = System.currentTimeMillis(),
)