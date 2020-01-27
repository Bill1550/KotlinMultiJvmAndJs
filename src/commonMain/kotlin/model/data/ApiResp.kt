package model.data

/**
 * Basically  a marker interface
 */
interface ApiResp

enum class ApiRespType {
    TIME,
    DATE,
    ZONE
}

inline fun <reified  T: ApiResp> List<ApiResp>.findResp(): T? =
    firstOrNull { it is T }?.let { it as? T }