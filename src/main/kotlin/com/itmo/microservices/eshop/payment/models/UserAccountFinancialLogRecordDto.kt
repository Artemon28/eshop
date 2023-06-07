package com.itmo.microservices.eshop.payment.models

import java.util.UUID

class UserAccountFinancialLogRecordDto(
    val type: FinancialOperationType,
    val amount: Int,
    val orderId: UUID,
    val paymentTransactionId: UUID,
    val timestamp: Long
) {
}