package com.itmo.microservices.eshop.auth.model

data class TokenResponseDto(val accessToken: String, val refreshToken: String)