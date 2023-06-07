package com.itmo.microservices.eshop.users.events

import com.itmo.microservices.eshop.payment.models.FinancialOperationType
import com.itmo.microservices.eshop.payment.models.UserAccountFinancialLogRecordDto
import com.itmo.microservices.eshop.users.entity.UserAggregate
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val NEW_FINANCIAL_OPERATION = "NEW_FINANCIAL_OPERATION"

@DomainEvent(name = NEW_FINANCIAL_OPERATION)
data class FinancialOperation(
    val userId: UUID,
    val financialRecord: UserAccountFinancialLogRecordDto
) : Event<UserAggregate>(
    name = NEW_FINANCIAL_OPERATION,
    createdAt = System.currentTimeMillis(),
)