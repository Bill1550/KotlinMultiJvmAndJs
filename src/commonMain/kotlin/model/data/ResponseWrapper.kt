package model.data

import kotlinx.serialization.Serializable

@Serializable
data class ResponseWrapper(
    val data: List<ApiResp>
)