package com.itmo.microservices.eshop.catalog.entity

import com.itmo.microservices.eshop.catalog.event.CatalogWithdrawProduct
import com.itmo.microservices.eshop.catalog.event.CatalogDepositProduct
import com.itmo.microservices.eshop.catalog.event.UnbookProduct
import com.itmo.microservices.eshop.catalog.model.DepositProductDto
import com.itmo.microservices.eshop.catalog.model.CatalogItemDto
import com.itmo.microservices.eshop.common.exception.ValidationErrorException
import org.springframework.data.annotation.Id
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.UUID

class Product : AggregateState<UUID, ProductAggregate> {
    @Id
    private lateinit var productId: UUID
    private lateinit var name: String
    private lateinit var description: String
    private var amount: Int = 0
    private var price: Int = 0

    override fun getId(): UUID = productId

    fun toDto(): CatalogItemDto {
        return CatalogItemDto(
            id = productId,
            title = name,
            description = description,
            amount = amount,
            price = price
        )
    }

    fun getAmount(): Int {
        return amount
    }

    fun depositProduct(depositProductDto: DepositProductDto): CatalogDepositProduct {
        return CatalogDepositProduct(
            productId = UUID.randomUUID(),
            productName = depositProductDto.title,
            description = depositProductDto.description,
            amount = depositProductDto.amount,
            price = depositProductDto.price
        )
    }

    fun withdrawProduct(amount: Int): CatalogWithdrawProduct {
        return CatalogWithdrawProduct(
            amount = amount
        )
    }

    fun unbookProduct(amount: Int): UnbookProduct {
        return UnbookProduct(
            amount = amount
        )
    }

    @StateTransitionFunc
    fun depositProduct(event: CatalogDepositProduct) {
        this.productId = event.productId
        this.name = event.productName
        this.description = event.description
        this.amount = event.amount
        this.price = event.price
    }

    @StateTransitionFunc
    fun withdrawProduct(event: CatalogWithdrawProduct) {
        if (amount < event.amount) {
            throw ValidationErrorException("withdrawProduct transition requires end amount less than zero")
        }
        amount -= event.amount
    }

    @StateTransitionFunc
    fun unbookProduct(event: UnbookProduct) {
        amount += event.amount
    }
}