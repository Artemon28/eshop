package com.itmo.microservices.eshop.catalog.entity

import ru.quipy.core.annotations.AggregateType
import ru.quipy.domain.Aggregate

@AggregateType(aggregateEventsTableName = "catalog-item-aggregate")
class ProductAggregate : Aggregate