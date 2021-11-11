package com.example.guidemetravelersapp.helpers.encryption

import android.content.Context
import com.bumptech.glide.util.Util
import com.example.guidemetravelersapp.helpers.utils.Utils
import java.io.*
import java.lang.Exception

class FileUtils {

    companion object {

        fun saveFile(encodedByteArray: ByteArray, path: String) {
            try {
                val file = File(path)
                val bos: BufferedOutputStream = BufferedOutputStream(
                    FileOutputStream(file)
                )
                bos.write(encodedByteArray)
                bos.flush()
                bos.close()
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun readFile(path: String): ByteArray {
            val file = File(path)
            val fileContent = ByteArray(file.length().toInt())
            try {
                val bufferedInputStream = BufferedInputStream(FileInputStream(file))
                bufferedInputStream.read(fileContent)
                bufferedInputStream.close()
            }
            catch (e: Exception) {
                e.printStackTrace()
            }

            return fileContent
        }

        fun createTempFile(context: Context, decrypted: ByteArray): File {
            val tempFile = File.createTempFile(Utils.TEMP_FILE_NAME, Utils.TEMP_FILE_EXT, context.cacheDir)
            val fos = FileOutputStream(tempFile)
            fos.write(decrypted)
            fos.close()
            return tempFile
        }

        fun tempFileDescriptor(context: Context, decrypted: ByteArray): FileDescriptor {
            val tempFile = this.createTempFile(context, decrypted)
            val fis = FileInputStream(tempFile)
            return fis.fd
        }

        fun getDirPath(context: Context): String {
            val path = context.getDir(Utils.DIR_NAME, Context.MODE_PRIVATE).absolutePath
            return path
        }

        fun buildFilePath(context: Context, fileName: String): String {
            val path = "${this.getDirPath(context)}${File.separator}$fileName"
            return path
        }

        fun deleteTempFile(context: Context) {
            val path = "${this.getDirPath(context)}${File.separator}${Utils.TEMP_FILE_NAME}${Utils.TEMP_FILE_EXT}"
            val tempFile = File(path)
            if(tempFile.exists())
                tempFile.delete()
        }
    }
}