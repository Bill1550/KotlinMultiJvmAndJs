package model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName( "time" )
data class TimeApiResp(
    val hour: Int,
    val min: Int,
    val sec: Int
) : ApiResp