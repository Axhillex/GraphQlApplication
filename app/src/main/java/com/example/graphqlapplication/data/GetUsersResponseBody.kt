package com.example.graphqlapplication.data

data class GetUsersResponseBody(
    val data: GetUsersResponseData? = null,
)

data class GetUsersResponseData(
    val users: UserDataList? = null,
)

data class UserDataList(
    val data: ArrayList<UserData?> = ArrayList(),
)
