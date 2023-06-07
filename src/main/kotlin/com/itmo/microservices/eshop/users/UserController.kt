package com.itmo.microservices.eshop.users

import com.itmo.microservices.eshop.users.models.CreateUserDto
import com.itmo.microservices.eshop.users.models.UserDto
import com.itmo.microservices.eshop.users.services.IUserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: IUserService) {
    @PostMapping
    @Operation(
        summary = "Register new user",
        responses = [
            ApiResponse(description = "OK", responseCode = "200"),
            ApiResponse(description = "Bad request", responseCode = "400", content = [Content()])]
    )
    fun register(@RequestBody request: CreateUserDto): UserDto = userService.registerUser(request)

    @GetMapping("/{user_id}")
    @Operation(
        summary = "Get current user info",
        responses = [
            ApiResponse(description = "OK", responseCode = "200"),
            ApiResponse(description = "User not found", responseCode = "404", content = [Content()])],
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun getUser(
        @Parameter(hidden = true) @AuthenticationPrincipal user: UserDetails,
        @PathVariable(value = "user_id") userId: UUID
    ): UserDto = userService.getUser(userId)
}