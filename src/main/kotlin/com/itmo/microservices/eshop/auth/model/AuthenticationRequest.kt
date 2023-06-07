package com.itmo.microservices.eshop.auth.model

data class AuthenticationRequest(val name: String, val password: String)