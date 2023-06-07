package com.itmo.microservices.eshop.catalog.config

import com.itmo.microservices.eshop.catalog.entity.ProductAggregate
import com.itmo.microservices.eshop.catalog.entity.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.quipy.core.EventSourcingService
import ru.quipy.core.EventSourcingServiceFactory
import java.util.*

@Configuration
class CatalogConfig {
    @Autowired
    private lateinit var eventSourcingServiceFactory: EventSourcingServiceFactory

    @Bean
    fun catalogItemESService(): EventSourcingService<UUID, ProductAggregate, Product> =
        eventSourcingServiceFactory.create()
}