package com.itmo.microservices.eshop.users.events

import com.itmo.microservices.eshop.users.entity.UserAggregate
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val REGISTER_NEW_USER = "REGISTER_NEW_USER"

@DomainEvent(name = REGISTER_NEW_USER)
data class RegisterUser(
    val userId: UUID,
    val userName: String,
    val userPassword: String
) : Event<UserAggregate>(
    name = REGISTER_NEW_USER,
    createdAt = System.currentTimeMillis(),
)