package com.itmo.microservices.eshop.order.event

import com.itmo.microservices.eshop.order.entity.OrderAggregate
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*


const val ORDER_BOOKED1 = "ORDER_BOOKED_EVENT"

@DomainEvent(name = ORDER_BOOKED1)
data class OrderFinalize(
    val orderId: UUID,
    val bookingId: UUID,
    val failedItems: Set<UUID>
) : Event<OrderAggregate>(
    name = ORDER_BOOKED1,
    createdAt = System.currentTimeMillis(),
)