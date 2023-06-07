package com.itmo.microservices.eshop.order.repository

import com.itmo.microservices.eshop.order.model.OrderDto
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface IOrderRepository: MongoRepository<OrderDto, UUID>