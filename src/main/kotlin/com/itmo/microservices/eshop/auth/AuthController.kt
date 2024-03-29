package com.itmo.microservices.eshop.auth

import com.itmo.microservices.eshop.auth.model.AuthenticationRequest
import com.itmo.microservices.eshop.auth.model.TokenResponseDto
import com.itmo.microservices.eshop.auth.service.IAuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/authentication")
class AuthController(private val authService: IAuthService) {
    @PostMapping
    @Operation(
        summary = "Authenticate",
        responses = [
            ApiResponse(description = "OK", responseCode = "200"),
            ApiResponse(description = "User not found", responseCode = "404", content = [Content()]),
            ApiResponse(description = "Invalid password", responseCode = "403", content = [Content()])]
    )
    fun authenticate(@RequestBody request: AuthenticationRequest): TokenResponseDto = authService.authenticate(request)

    @PostMapping("/refresh")
    @Operation(
        summary = "Refresh authentication",
        responses = [
            ApiResponse(description = "OK", responseCode = "200"),
            ApiResponse(description = "Authentication error", responseCode = "403", content = [Content()])],
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun refresh(authentication: Authentication): TokenResponseDto = authService.refresh(authentication)
}