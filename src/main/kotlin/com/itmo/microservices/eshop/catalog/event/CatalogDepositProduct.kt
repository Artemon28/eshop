package com.itmo.microservices.eshop.catalog.event

import com.itmo.microservices.eshop.catalog.entity.ProductAggregate
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.UUID

const val DEPOSIT_PRODUCT_TO_CATALOG = "DEPOSIT_PRODUCT_TO_CATALOG"

@DomainEvent(name = DEPOSIT_PRODUCT_TO_CATALOG)
data class CatalogDepositProduct(
    val productId: UUID,
    val productName: String,
    val description: String,
    val amount: Int,
    val price: Int
) : Event<ProductAggregate>(
    name = DEPOSIT_PRODUCT_TO_CATALOG,
    createdAt = System.currentTimeMillis(),
)