package com.itmo.microservices.eshop.payment.services

import com.itmo.microservices.eshop.catalog.service.ICatalogService
import com.itmo.microservices.eshop.common.exception.NotFoundException
import com.itmo.microservices.eshop.order.entity.Order
import com.itmo.microservices.eshop.order.entity.OrderAggregate
import com.itmo.microservices.eshop.order.repository.IOrderRepository
import com.itmo.microservices.eshop.payment.models.PaymentLogRecordDto
import com.itmo.microservices.eshop.payment.models.PaymentStatus
import com.itmo.microservices.eshop.payment.models.PaymentSubmissionDto
import com.itmo.microservices.eshop.payment.models.UserAccountFinancialLogRecordDto
import com.itmo.microservices.eshop.users.entity.UserAggregate
import com.itmo.microservices.eshop.users.entity.UserState
import com.itmo.microservices.eshop.users.models.UserUnit
import com.itmo.microservices.eshop.users.repository.IUserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import ru.quipy.core.EventSourcingService
import java.util.*

@Service
class DefaultPaymentService(
    private val userESService: EventSourcingService<UUID, UserAggregate, UserState>,
    private val userRepository: IUserRepository
    ) :IPaymentService {
    override fun payOrder(orderId: UUID): PaymentSubmissionDto {
        TODO("Not yet implemented")
    }

    override fun getUserFinancialHistory(orderId: UUID?, user: UserDetails): List<UserAccountFinancialLogRecordDto> {
        val userUnit: UserUnit
        try {
            userUnit = userRepository.findByName(user.username).get()
        } catch (e: NoSuchElementException) {
            throw NotFoundException()
        }
        return userUnit.finHistory
    }
}
