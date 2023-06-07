package com.itmo.microservices.eshop.catalog.model

data class DepositProductDto(
    val title: String,
    val description: String,
    val price: Int,
    val amount: Int
)