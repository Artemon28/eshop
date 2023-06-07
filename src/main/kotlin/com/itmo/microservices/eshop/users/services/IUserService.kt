package com.itmo.microservices.eshop.users.services

import com.itmo.microservices.eshop.payment.models.UserAccountFinancialLogRecordDto
import com.itmo.microservices.eshop.users.models.CreateUserDto
import com.itmo.microservices.eshop.users.models.UserDto
import com.itmo.microservices.eshop.users.entity.UserState
import java.util.UUID

interface IUserService {
    fun getUser(userId: UUID): UserDto
    fun getUserState(userName: String): UserState
    fun registerUser(request: CreateUserDto): UserDto
    fun recordFinancialOperation(userName: String, request: UserAccountFinancialLogRecordDto): UserDto
}