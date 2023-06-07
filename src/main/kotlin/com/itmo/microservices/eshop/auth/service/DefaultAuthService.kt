package com.itmo.microservices.eshop.auth.service

import com.itmo.microservices.eshop.auth.model.AuthenticationRequest
import com.itmo.microservices.eshop.auth.model.TokenResponseDto
import com.itmo.microservices.eshop.common.exception.AccessDeniedException
import com.itmo.microservices.eshop.users.services.IUserService
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class DefaultAuthService(
    private val userService: IUserService,
    private val tokenManager: JwtTokenManager,
    private val passwordEncoder: PasswordEncoder
) : IAuthService {
    override fun authenticate(request: AuthenticationRequest): TokenResponseDto {
        val userState = userService.getUserState(request.name)
        if (!passwordEncoder.matches(request.password, userState.getPassword()))
            throw AccessDeniedException("Invalid password")

        val accessToken = tokenManager.generateToken(userState.userDetails())
        val refreshToken = tokenManager.generateRefreshToken(userState.userDetails())
        return TokenResponseDto(accessToken, refreshToken)
    }

    override fun refresh(authentication: Authentication): TokenResponseDto {
        val refreshToken = authentication.credentials as String
        val principal = authentication.principal as UserDetails
        val accessToken = tokenManager.generateToken(principal)
        return TokenResponseDto(accessToken, refreshToken)
    }
}