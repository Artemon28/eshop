package com.itmo.microservices.eshop.order.model

import java.util.UUID

data class BookingLogRecord(
    val bookingId: UUID,
    val itemId: UUID,
    val status: BookingStatus,
    val amount: Int,
    val timestamp: Long
)