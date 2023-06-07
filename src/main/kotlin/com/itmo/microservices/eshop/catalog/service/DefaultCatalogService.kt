package com.itmo.microservices.eshop.catalog.service

import com.itmo.microservices.eshop.catalog.entity.ProductAggregate
import com.itmo.microservices.eshop.catalog.entity.Product
import com.itmo.microservices.eshop.catalog.model.DepositProductDto
import com.itmo.microservices.eshop.catalog.model.CatalogItemDto
import com.itmo.microservices.eshop.catalog.repository.ICatalogItemRepository
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import ru.quipy.core.EventSourcingService
import java.util.*

@Service
class DefaultCatalogService(
    private val catalogItemESService: EventSourcingService<UUID, ProductAggregate, Product>,
    private val catalogItemRepository: ICatalogItemRepository
) : ICatalogService {
    override fun getCatalog(size: Int?, available: Boolean?): List<CatalogItemDto> {
        val catalog = catalogItemRepository.findAll()
        val getSize = if (size == null || size > catalog.size) {
            catalog.size
        } else {
            size
        }
        var items = catalog.subList(0, getSize).toList()
        if (available == null) {
            return items
        }

        items = if (available) {
            items.filter { it.amount > 0 }
        } else {
            items.filter { it.amount == 0 }
        }
        return items
    }

    override fun getProduct(id: UUID): CatalogItemDto {
        val item: CatalogItemDto
        try {
            item = catalogItemRepository.findById(id).get()
        } catch (e: NoSuchElementException) {
            throw HttpClientErrorException(HttpStatus.NOT_FOUND, "", HttpHeaders.EMPTY, byteArrayOf(), null)
        }
        return item
    }

    override fun addProductCatalog(request: DepositProductDto): CatalogItemDto {
        val event = catalogItemESService.create {
            it.depositProduct(request)
        }
        val catalogItemState = catalogItemESService.getState(event.productId)
        val catalogItemDto = catalogItemState!!.toDto()
        catalogItemRepository.save(catalogItemDto)
        return catalogItemDto
    }

    fun bookCatalogItem(itemId: UUID, amount: Int, failedItems: MutableSet<UUID>) {
        val itemState = catalogItemESService.getState(itemId)
        if (itemState == null || itemState.getAmount() < amount) {
            failedItems.add(itemId)
        } else {
            catalogItemESService.update(itemId) {
                it.withdrawProduct(amount)
            }
            val catalogItemState = catalogItemESService.getState(itemId)
            val catalogItemDto = catalogItemState!!.toDto()
            catalogItemRepository.save(catalogItemDto)
        }
    }

    override fun bookProduct(itemsMap: Map<UUID, Int>): Set<UUID> {
        val failedItems: MutableSet<UUID> = mutableSetOf()
        for (item in itemsMap.entries) {
            bookCatalogItem(item.key, item.value, failedItems)
        }
        return failedItems
    }

    fun unbookCatalogItem(itemId: UUID, amount: Int) {
        catalogItemESService.update(itemId) {
            it.unbookProduct(amount)
        }
        val catalogItemState = catalogItemESService.getState(itemId)
        val catalogItemDto = catalogItemState!!.toDto()
        catalogItemRepository.save(catalogItemDto)
    }

    override fun unbookProduct(itemsMap: Map<UUID, Int>) {
        for (item in itemsMap.entries) {
            unbookCatalogItem(item.key, item.value)
        }
    }

}