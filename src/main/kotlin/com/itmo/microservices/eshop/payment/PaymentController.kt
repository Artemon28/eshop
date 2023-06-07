package com.itmo.microservices.eshop.payment

import com.itmo.microservices.eshop.payment.models.UserAccountFinancialLogRecordDto
import com.itmo.microservices.eshop.payment.services.IPaymentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class PaymentController(private val paymentService: IPaymentService) {
    @GetMapping("/finlog")
    @Operation(
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun getUserFinancialHistory(
        @RequestParam(required = false) orderId: UUID?, @Parameter(hidden = true) @AuthenticationPrincipal user: UserDetails
    ): List<UserAccountFinancialLogRecordDto> = paymentService.getUserFinancialHistory(orderId, user)
}