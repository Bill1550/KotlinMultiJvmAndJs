package utility.cache

open class TimeValidator<T>(
        private val timeToLive: Long,
        private val errorTimeout: Long,
        private val currentTimeSource: ()->Long
) : CacheValidator<T, Long> {


    override fun createContext(entry: T): Long {
        return currentTimeSource()
    }

    override fun createContext(t: Throwable): Long {
        return currentTimeSource()
    }

    override fun isFresh(entry: CacheEntry<T, Long>): Boolean {
        return when ( entry ) {
            is CacheEntry.Data<*,*> -> currentTimeSource() - entry.context < timeToLive
            is CacheEntry.Error<*,*> -> currentTimeSource() - entry.context < errorTimeout
        }
    }

}