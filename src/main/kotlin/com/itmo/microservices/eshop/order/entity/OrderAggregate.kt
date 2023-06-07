package com.itmo.microservices.eshop.order.entity

import ru.quipy.core.annotations.AggregateType
import ru.quipy.domain.Aggregate

@AggregateType(aggregateEventsTableName = "order-aggregate")
class OrderAggregate: Aggregate {
}