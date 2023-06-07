package com.itmo.microservices.eshop.auth.service

import com.itmo.microservices.eshop.auth.model.AuthenticationRequest
import com.itmo.microservices.eshop.auth.model.TokenResponseDto
import org.springframework.security.core.Authentication

interface IAuthService {
    fun authenticate(request: AuthenticationRequest): TokenResponseDto
    fun refresh(authentication: Authentication): TokenResponseDto
}