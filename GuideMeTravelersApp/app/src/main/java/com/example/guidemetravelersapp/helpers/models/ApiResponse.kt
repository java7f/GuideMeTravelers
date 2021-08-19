package com.example.guidemetravelersapp.helpers.models

data class ApiResponse<T> (
    var data: T? = null,
    var inProgress: Boolean = true,
    var hasError: Boolean = false,
    var errorMessage: String = ""
)