package com.itmo.microservices.eshop.payment.services

import com.itmo.microservices.eshop.payment.models.PaymentSubmissionDto
import com.itmo.microservices.eshop.payment.models.UserAccountFinancialLogRecordDto
import com.itmo.microservices.eshop.users.models.UserUnit
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

interface IPaymentService {
    fun payOrder(orderId: UUID): PaymentSubmissionDto
    fun getUserFinancialHistory(orderId: UUID?, user: UserDetails): List<UserAccountFinancialLogRecordDto>
}