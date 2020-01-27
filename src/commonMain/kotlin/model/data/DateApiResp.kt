package model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName( "date" )
class DateApiResp(
    val year: Int,
    val month: Int,
    val day: Int
) : ApiResp