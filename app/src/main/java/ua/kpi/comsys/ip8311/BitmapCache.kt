package ua.kpi.comsys.ip8311

// Code taken from
// https://handyopinion.com/how-to-save-and-load-image-from-cache-using-lrucache-in-kotlin-android/

import android.graphics.Bitmap
import android.util.LruCache
import com.tomclaw.cache.DiskLruCache

var CACHE_SIZE = (500 * 1024).toLong()
var cache: DiskLruCache? = null

private const val DISK_CACHE_SIZE = 1024 * 1024 * 10 // 10MB
private const val DISK_CACHE_SUBDIR = "thumbnails"
...
private var diskLruCache: DiskLruCache? = null
private val diskCacheLock = ReentrantLock()
private val diskCacheLockCondition: Condition = diskCacheLock.newCondition()
private var diskCacheStarting = true

override fun onCreate(savedInstanceState: Bundle?) {
    ...
    // Initialize memory cache
    ...
    // Initialize disk cache on background thread
    val cacheDir = getDiskCacheDir(this, DISK_CACHE_SUBDIR)
    InitDiskCacheTask().execute(cacheDir)
    ...
}

internal inner class InitDiskCacheTask : AsyncTask<File, Void, Void>() {
    override fun doInBackground(vararg params: File): Void? {
        diskCacheLock.withLock {
            val cacheDir = params[0]
            diskLruCache = DiskLruCache.open(cacheDir, DISK_CACHE_SIZE)
            diskCacheStarting = false // Finished initialization
            diskCacheLockCondition.signalAll() // Wake any waiting threads
        }
        return null
    }
}

internal inner class  BitmapWorkerTask : AsyncTask<Int, Unit, Bitmap>() {
    ...

    // Decode image in background.
    override fun doInBackground(vararg params: Int?): Bitmap? {
        val imageKey = params[0].toString()

        // Check disk cache in background thread
        return getBitmapFromDiskCache(imageKey) ?:
        // Not found in disk cache
        decodeSampledBitmapFromResource(resources, params[0], 100, 100)
            ?.also {
                // Add final bitmap to caches
                addBitmapToCache(imageKey, it)
            }
    }
}

fun addBitmapToCache(key: String, bitmap: Bitmap) {
    // Add to memory cache as before
    if (getBitmapFromMemCache(key) == null) {
        memoryCache.put(key, bitmap)
    }

    // Also add to disk cache
    synchronized(diskCacheLock) {
        diskLruCache?.apply {
            if (!containsKey(key)) {
                put(key, bitmap)
            }
        }
    }
}

fun getBitmapFromDiskCache(key: String): Bitmap? =
    diskCacheLock.withLock {
        // Wait while disk cache is started from background thread
        while (diskCacheStarting) {
            try {
                diskCacheLockCondition.await()
            } catch (e: InterruptedException) {
            }

        }
        return diskLruCache?.get(key)
    }

// Creates a unique subdirectory of the designated app cache directory. Tries to use external
// but if not mounted, falls back on internal storage.
fun getDiskCacheDir(context: Context, uniqueName: String): File {
    // Check if media is mounted or storage is built-in, if so, try and use external cache dir
    // otherwise use internal cache dir
    val cachePath =
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
            || !isExternalStorageRemovable()) {
            context.externalCacheDir.path
        } else {
            context.cacheDir.path
        }

    return File(cachePath + File.separator + uniqueName)
}