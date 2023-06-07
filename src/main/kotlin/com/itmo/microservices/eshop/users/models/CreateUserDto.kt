package com.itmo.microservices.eshop.users.models

data class CreateUserDto(
    val name: String,
    // TODO change to val
    var password: String
)