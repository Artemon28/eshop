package com.itmo.microservices.eshop.delivery.models

import java.util.UUID

class DeliveryInfoRecord(
    val outcome: DeliverySubmissionOutcome,
    val preparedTime: Long,
    val attempts: Int,
    val submittedTime: Long,
    val transactionId: UUID,
    val submissionStartedTime: Long
) {
}