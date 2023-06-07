package com.itmo.microservices.eshop.order

import com.itmo.microservices.eshop.order.model.BookingDto
import com.itmo.microservices.eshop.order.model.OrderDto
import com.itmo.microservices.eshop.order.service.IOrderService
import com.itmo.microservices.eshop.payment.models.PaymentSubmissionDto
import com.itmo.microservices.eshop.payment.services.IPaymentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: IOrderService,
    private val paymentService: IPaymentService
) {
    @PostMapping
    @Operation(
        summary = "Create order",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun createOrder(): OrderDto = orderService.createOrder()

    @GetMapping("/{order_id}")
    @Operation(
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun getOrder(@PathVariable(value = "order_id") orderId: UUID): OrderDto =
        orderService.getOrder(orderId)

    @PutMapping("/{order_id}/items/{item_id}")
    @Operation(
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun addItemToCart(
        @PathVariable(value = "order_id") orderId: UUID,
        @PathVariable(value = "item_id") itemId: UUID,
        @RequestParam(required = false) amount: Int?
    ) = orderService.addItemToCart(orderId, itemId, amount)

    @PostMapping("/{order_id}/delivery")
    @Operation(
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun setDeliveryTimeForOrder(
        @PathVariable(value = "order_id") orderId: UUID,
        @RequestParam(required = false) slot: Int
    ) = orderService.setDeliveryTimeForOrder(orderId, slot)

    @PostMapping("/{order_id}/bookings")
    @Operation(
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun bookOrder(@PathVariable(value = "order_id") orderId: UUID): BookingDto =
        orderService.finalizeOrder(orderId)

    @PostMapping("/{order_id}/unbook")
    @Operation(
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun unbook(@PathVariable(value = "order_id") orderId: UUID): OrderDto =
        orderService.unbookOrder(orderId)

    @PostMapping("/{order_id}/payment")
    @Operation(
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun payOrder(@PathVariable(value = "order_id") orderId: UUID, @Parameter(hidden = true) @AuthenticationPrincipal user: UserDetails): PaymentSubmissionDto =
        orderService.orderPay(orderId, user)
}

/*
500 users 5 threads
start time: 2023-03-29 16:28:32.891
end time:   2023-03-29 16:33:17.410
5 min

1000 users 10 threads
start time: 2023-03-29 16:34:11.811
end rime:   2023-03-29 16:49:55.492
 */