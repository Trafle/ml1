package ua.kpi.comsys.ip8311

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.tomclaw.cache.DiskLruCache
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


val CACHE_SIZE: Long = 10 * 1024 * 1024 // 10 MB
lateinit var cacheDir: File
lateinit var cache: DiskLruCache

class Cacher private constructor() {

    companion object {

        fun saveBitmap(name: String, bitmap: Bitmap): Boolean {
            try {
                val bitmapFile = File.createTempFile("tmp_", null)
                val os: OutputStream = BufferedOutputStream(FileOutputStream(bitmapFile))
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
                os.close()
                cache.put(name, bitmapFile)
            } catch (e: Exception) {
                return false
            }
            return true
        }

        fun getBitmap(name: String): Bitmap? {
            var bitmap: Bitmap? = null
            try {
                val filePath = cache[name].path
                bitmap = BitmapFactory.decodeFile(filePath)
            } catch (e: Exception) {
                return null
            }
            return bitmap
        }

    }

}