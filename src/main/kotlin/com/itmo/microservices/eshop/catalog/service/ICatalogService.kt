package com.itmo.microservices.eshop.catalog.service

import com.itmo.microservices.eshop.catalog.model.CatalogItemDto
import com.itmo.microservices.eshop.catalog.model.DepositProductDto
import java.util.UUID

interface ICatalogService {
    fun getCatalog(size: Int?, available: Boolean?): List<CatalogItemDto>
    fun getProduct(id: UUID): CatalogItemDto
    fun addProductCatalog(request: DepositProductDto): CatalogItemDto
    fun bookProduct(itemsMap: Map<UUID, Int>): Set<UUID>
    fun unbookProduct(itemsMap: Map<UUID, Int>)
}