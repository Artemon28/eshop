package com.itmo.microservices.eshop.delivery.services

import com.itmo.microservices.eshop.delivery.models.DeliveryInfoRecord
import org.springframework.stereotype.Service
import java.util.*

@Service
class DefaultDeliveryService : IDeliveryService {
    override fun getAvailableDeliveryTimeSlots(number: Int): List<Int> {
        var slots: MutableList<Int> = mutableListOf<Int>()
        for (i in 0..number){
            slots.add(i)
        }
        return slots
    }

    override fun getOrderDeliveryHistory(orderId: UUID): List<DeliveryInfoRecord> {
        TODO("Not yet implemented")
    }
}