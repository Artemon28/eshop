package com.itmo.microservices.eshop.catalog.repository

import com.itmo.microservices.eshop.catalog.model.CatalogItemDto
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.UUID

interface ICatalogItemRepository : MongoRepository<CatalogItemDto, UUID>