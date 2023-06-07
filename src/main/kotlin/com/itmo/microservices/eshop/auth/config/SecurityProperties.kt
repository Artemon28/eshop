package com.itmo.microservices.eshop.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.time.Duration

@ConfigurationProperties("security")
@Component
class SecurityProperties {
    var secret: String = "JaNdRgUjXn2r5u8x/A?D(G+KbPeShVmY"
    var tokenLifetime: Duration = Duration.ofMinutes(15)
    var refreshTokenLifetime: Duration = Duration.ofDays(30)
}