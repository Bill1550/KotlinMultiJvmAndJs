package model.data

import kotlinx.serialization.Serializable

@Serializable
data class ErrorApiResp(
    val code: Int,
    val message: String
) : ApiResp