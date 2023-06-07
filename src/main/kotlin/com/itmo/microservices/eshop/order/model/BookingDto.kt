package com.itmo.microservices.eshop.order.model

import org.springframework.data.annotation.Id
import java.util.UUID

data class BookingDto(
    @Id
    val id: UUID,
    val failedItems: Set<UUID>
)