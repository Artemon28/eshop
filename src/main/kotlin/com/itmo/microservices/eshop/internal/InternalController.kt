package com.itmo.microservices.eshop.internal

import com.itmo.microservices.eshop.catalog.model.CatalogItemDto
import com.itmo.microservices.eshop.catalog.model.DepositProductDto
import com.itmo.microservices.eshop.catalog.service.ICatalogService
import com.itmo.microservices.eshop.delivery.models.DeliveryInfoRecord
import com.itmo.microservices.eshop.delivery.services.IDeliveryService
import com.itmo.microservices.eshop.order.model.BookingLogRecord
import com.itmo.microservices.eshop.order.service.IOrderService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/_internal")
class InternalController(
    private val catalogItemService: ICatalogService,
    private val orderService: IOrderService,
    private val deliveryService: IDeliveryService
) {
    @PostMapping("/catalogItem")
    @Operation(
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun createCatalogItem(
        @RequestBody request: DepositProductDto
    ): CatalogItemDto = catalogItemService.addProductCatalog(request)

    @GetMapping("/bookingHistory/{booking_id}")
    @Operation(
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun getBookingItems(@PathVariable(value = "booking_id") bookingId: UUID): List<BookingLogRecord> =
        orderService.getBookingItems(bookingId)

    @GetMapping("/deliveryLog/{order_id}")
    @Operation(
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun getOrderDeliveryHistory(@PathVariable(value = "order_id") orderId: UUID): List<DeliveryInfoRecord> =
        deliveryService.getOrderDeliveryHistory(orderId)
}