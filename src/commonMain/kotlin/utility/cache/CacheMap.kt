package utility.cache

interface CacheMap<in K: Any, T: Any?> {

    /**
     * Returns the item for the requested key.  If the item is not currently in
     * the cache, the fetcher will be called to load the requested item.
     * Will return null only if T is a nullable type and the fetcher returned null.
     */
    suspend fun get( key: K ): T

    suspend fun clear()

}
