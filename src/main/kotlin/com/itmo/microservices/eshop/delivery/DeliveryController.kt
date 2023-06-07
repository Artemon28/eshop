package com.itmo.microservices.eshop.delivery

import com.itmo.microservices.eshop.delivery.services.IDeliveryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/delivery")
class DeliveryController(private val deliveryService: IDeliveryService) {
    @GetMapping("/slots")
    @Operation(
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun getAvailableDeliveryTimeSlots(@RequestParam(required = false) number: Int): List<Int> =
        deliveryService.getAvailableDeliveryTimeSlots(number)
}