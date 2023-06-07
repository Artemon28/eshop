package com.itmo.microservices.eshop.catalog.model

import org.springframework.data.annotation.Id
import java.util.*

class CatalogItemDto (
    @Id
    val id: UUID,
    val title: String,
    var description: String,
    val price: Int = 1000,
    var amount: Int
)
//{
//    constructor(event: CatalogDepositProduct) : this(
//        id = event.id,
//        title = event.title,
//        description = event.description,
//        amount = event.amount,
//        price = event.price
//    )
//}