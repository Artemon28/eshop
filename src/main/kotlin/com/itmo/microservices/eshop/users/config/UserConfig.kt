package com.itmo.microservices.eshop.users.config

import com.itmo.microservices.eshop.users.entity.UserAggregate
import com.itmo.microservices.eshop.users.entity.UserState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.quipy.core.EventSourcingService
import ru.quipy.core.EventSourcingServiceFactory
import java.util.*

@Configuration
class UserConfig {
    @Autowired
    private lateinit var eventSourcingServiceFactory: EventSourcingServiceFactory

    @Bean
    fun userESService(): EventSourcingService<UUID, UserAggregate, UserState> =
        eventSourcingServiceFactory.create()
}