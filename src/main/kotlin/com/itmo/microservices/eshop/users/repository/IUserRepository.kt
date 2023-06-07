package com.itmo.microservices.eshop.users.repository

import com.itmo.microservices.eshop.users.models.UserDto
import com.itmo.microservices.eshop.users.models.UserUnit
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface IUserRepository : MongoRepository<UserUnit, UUID> {
    fun findByName(userName: String): Optional<UserUnit>
}