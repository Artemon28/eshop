package com.itmo.microservices.eshop.order.model

import com.itmo.microservices.eshop.order.event.OrderCreated
import com.itmo.microservices.eshop.payment.models.PaymentLogRecordDto
import org.springframework.data.annotation.Id
import java.util.UUID

data class OrderDto(
    @Id
    val id: UUID,
    val timeCreated: Long,
    val status: OrderStatus,
    val itemsMap: Map<UUID, Int>,
    val deliveryDuration: Int?,
    val paymentHistory: List<PaymentLogRecordDto>
)