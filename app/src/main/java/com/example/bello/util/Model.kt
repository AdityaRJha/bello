package com.example.bello.util

data class User(
    val email: String? = "",
    val emailAlternative: String? = "",
    val username: String? = "",
    val bio: String? = "",
    val imageUrl: String = "",
    val followHashtags: ArrayList<String>? = arrayListOf(),
    val followUsers: ArrayList<String>? = arrayListOf()
)
