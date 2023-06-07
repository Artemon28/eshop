package com.itmo.microservices.eshop.users.services

import com.itmo.microservices.eshop.common.exception.NotFoundException
import com.itmo.microservices.eshop.payment.models.UserAccountFinancialLogRecordDto
import com.itmo.microservices.eshop.users.models.CreateUserDto
import com.itmo.microservices.eshop.users.models.UserDto
import com.itmo.microservices.eshop.users.entity.UserAggregate
import com.itmo.microservices.eshop.users.entity.UserState
import com.itmo.microservices.eshop.users.models.UserUnit
import com.itmo.microservices.eshop.users.repository.IUserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.quipy.core.EventSourcingService
import java.util.*

@Suppress("UnstableApiUsage")
@Service
class DefaultUserService(
    private val userESService: EventSourcingService<UUID, UserAggregate, UserState>,
    private val userRepository: IUserRepository,
    private val passwordEncoder: PasswordEncoder
) : IUserService {
    override fun getUser(userId: UUID): UserDto {
        val user: UserUnit
        try {
            user = userRepository.findById(userId).get()
        } catch (e: NoSuchElementException) {
            throw NotFoundException()
        }
        return UserDto(user.id, user.name)
    }

    override fun getUserState(userName: String): UserState {
        val user: UserUnit
        try {
            user = userRepository.findByName(userName).get()
        } catch (e: NoSuchElementException) {
            throw NotFoundException()
        }
        return userESService.getState(user.id)!!
    }

    override fun registerUser(request: CreateUserDto): UserDto {
        request.password = passwordEncoder.encode(request.password)
        val event = userESService.create {
            it.createUser(request)
        }
        val item = UserUnit(event)
        userRepository.save(item)
        return UserDto(item.id, item.name)
    }

    override fun recordFinancialOperation(userName: String, request: UserAccountFinancialLogRecordDto): UserDto {
        val user: UserUnit
        try {
            user = userRepository.findByName(userName).get()
        } catch (e: NoSuchElementException) {
            throw NotFoundException()
        }
        userESService.update(user.id) {it.newFinancialActivity(request)}
        val updUser = userESService.getState(user.id)

        userRepository.save(updUser!!.toUnit())
        return updUser.toDto()
    }
}