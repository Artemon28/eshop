package com.itmo.microservices.eshop.catalog

import com.itmo.microservices.eshop.catalog.model.CatalogItemDto
import com.itmo.microservices.eshop.catalog.service.ICatalogService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class CatalogController(private val service: ICatalogService) {
    @GetMapping("/items")
    @Operation(
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun getCatalog(
        @RequestParam(required = false) size: Int?,
        @RequestParam(required = false) available: Boolean?
    ): List<CatalogItemDto> = service.getCatalog(size, available)

    @GetMapping("/items/{catalog_item_id}")
    @Operation(
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun getCatalogItem(
        @PathVariable(value = "catalog_item_id") id: UUID
    ): CatalogItemDto = service.getProduct(id)
}