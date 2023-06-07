package com.itmo.microservices.eshop.catalog.event

import com.itmo.microservices.eshop.catalog.entity.ProductAggregate
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event

const val WITHDRAW_PRODUCT = "WITHDRAW_PRODUCT"

@DomainEvent(name = WITHDRAW_PRODUCT)
data class CatalogWithdrawProduct(
    val amount: Int
) : Event<ProductAggregate>(
    name = WITHDRAW_PRODUCT,
    createdAt = System.currentTimeMillis(),
)