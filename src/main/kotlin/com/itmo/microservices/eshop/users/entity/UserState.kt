package com.itmo.microservices.eshop.users.entity

import com.itmo.microservices.eshop.order.model.OrderDto
import com.itmo.microservices.eshop.payment.models.UserAccountFinancialLogRecordDto
import com.itmo.microservices.eshop.users.events.FinancialOperation
import com.itmo.microservices.eshop.users.events.RegisterUser
import com.itmo.microservices.eshop.users.models.CreateUserDto
import com.itmo.microservices.eshop.users.models.UserDto
import com.itmo.microservices.eshop.users.models.UserUnit
import org.springframework.data.annotation.Id
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.UUID

class UserState : AggregateState<UUID, UserAggregate> {
    @Id
    private lateinit var userId: UUID
    private lateinit var userName: String
    private lateinit var userPassword: String
    override fun getId(): UUID = userId
    fun getPassword(): String = userPassword
    private lateinit var financialHistory: MutableList<UserAccountFinancialLogRecordDto>

    fun userDetails(): UserDetails = User(userName, userPassword, emptyList())

    fun toUnit(): UserUnit {
        return UserUnit(
            id = userId,
            name = userName,
            finHistory = financialHistory
        )
    }

    fun toDto(): UserDto {
        return UserDto(
            id = userId,
            name = userName,
        )
    }

    fun createUser(request: CreateUserDto): RegisterUser = RegisterUser(
        userId = UUID.randomUUID(),
        userName = request.name,
        userPassword = request.password
    )

    fun newFinancialActivity(request: UserAccountFinancialLogRecordDto): FinancialOperation = FinancialOperation(
        userId = userId,
        financialRecord = request
    )

    @StateTransitionFunc
    fun createUser(event: RegisterUser) {
        this.userId = event.userId
        this.userName = event.userName
        this.userPassword = event.userPassword
        this.financialHistory = mutableListOf()
    }

    @StateTransitionFunc
    fun newFinancialActivity(event: FinancialOperation) {
        financialHistory.add(event.financialRecord)
    }
}