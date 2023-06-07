package com.itmo.microservices.eshop.users.models

import com.itmo.microservices.eshop.payment.models.UserAccountFinancialLogRecordDto
import com.itmo.microservices.eshop.users.events.RegisterUser
import org.springframework.data.annotation.Id
import java.util.*

class UserUnit (
    @Id
    val id: UUID,
    val name: String,
    val finHistory: MutableList<UserAccountFinancialLogRecordDto>
) {
    constructor(event: RegisterUser) : this(
    id = event.userId,
    name = event.userName,
    finHistory = mutableListOf()
    )
}