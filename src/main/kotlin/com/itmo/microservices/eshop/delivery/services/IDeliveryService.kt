package com.itmo.microservices.eshop.delivery.services

import com.itmo.microservices.eshop.delivery.models.DeliveryInfoRecord
import java.util.UUID

interface IDeliveryService {
    fun getAvailableDeliveryTimeSlots(number: Int): List<Int>
    fun getOrderDeliveryHistory(orderId: UUID): List<DeliveryInfoRecord>
}