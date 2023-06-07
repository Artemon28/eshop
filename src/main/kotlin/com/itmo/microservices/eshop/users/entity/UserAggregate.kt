package com.itmo.microservices.eshop.users.entity

import ru.quipy.core.annotations.AggregateType
import ru.quipy.domain.Aggregate

@AggregateType(aggregateEventsTableName = "user-aggregate")
class UserAggregate : Aggregate