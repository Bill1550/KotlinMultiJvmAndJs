package model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName( "zone" )
data class ZoneApiResp(
    val id: String,
    val name: String
) : ApiResp