package com.itmo.microservices.eshop.catalog

import com.itmo.microservices.eshop.catalog.entity.ProductAggregate
import com.itmo.microservices.eshop.catalog.event.CatalogDepositProduct
import org.springframework.stereotype.Component
import ru.quipy.streams.AggregateSubscriptionsManager
import javax.annotation.PostConstruct

@Component
class CatalogSubscriber(
    private val manager: AggregateSubscriptionsManager,
) {
    @PostConstruct
    fun init() {
        manager.createSubscriber(ProductAggregate::class, "catalog::catalog-subscriber") {
            `when`(CatalogDepositProduct::class) { event ->
                println("Got new catalog create event to process: $event")
            }
        }
    }
}