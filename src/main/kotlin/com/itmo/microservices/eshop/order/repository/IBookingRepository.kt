package com.itmo.microservices.eshop.order.repository

import com.itmo.microservices.eshop.order.model.BookingLogRecord
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface IBookingRepository: MongoRepository<BookingLogRecord, UUID> {
    fun findAllByBookingId(bookingId: UUID): Optional<List<BookingLogRecord>>
}