package com.itmo.microservices.eshop.payment.models

import java.util.*

class PaymentLogRecordDto(
    val timestamp: Long,
    val status: PaymentStatus,
    val amount: Int,
    val transactionId: UUID
) {
}