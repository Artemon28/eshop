package com.itmo.microservices.eshop.catalog.event

import com.itmo.microservices.eshop.catalog.entity.ProductAggregate
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event

const val UNBOOK_PRODUCT = "UNBOOK_PRODUCT"

@DomainEvent(name = UNBOOK_PRODUCT)
data class UnbookProduct(
    val amount: Int
) : Event<ProductAggregate>(
    name = UNBOOK_PRODUCT,
    createdAt = System.currentTimeMillis(),
)