package com.itmo.microservices.eshop.payment.models

import java.util.UUID

class PaymentSubmissionDto(
    val timestamp: Long,
    val transactionId: UUID
) {
}