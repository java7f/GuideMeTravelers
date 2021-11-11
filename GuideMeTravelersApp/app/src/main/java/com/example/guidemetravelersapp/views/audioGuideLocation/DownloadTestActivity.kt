package com.example.guidemetravelersapp.views.audioGuideLocation

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.guidemetravelersapp.helpers.encryption.EncryptDecryptHelper
import com.example.guidemetravelersapp.helpers.encryption.FileUtils
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme
import java.lang.Exception

class DownloadTestActivity : ComponentActivity(), OnDownloadListener, Handler.Callback {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            decryptAudio()
            GuideMeTravelersAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting2("Android")
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun encryptAudio(): Boolean {
        Log.i(DownloadTestActivity::class.simpleName, "File is being encrypted")
        try {
            val fileData = FileUtils.readFile(FileUtils.buildFilePath(this, "test.mp3"))
            val fileEncoded = EncryptDecryptHelper.encode(fileData)
            FileUtils.saveFile(fileEncoded, FileUtils.buildFilePath(this, "test.mp3"))
            Log.i(DownloadTestActivity::class.simpleName, "File encrypted")
            return true
        }
        catch (e: Exception) {
            Log.e(DownloadTestActivity::class.simpleName, e.message!!)
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun decryptAudio(): ByteArray? {
        try {
            val fileData = FileUtils.readFile(FileUtils.buildFilePath(this, "bensound-erf.mp3"))
            val fileDecoded = EncryptDecryptHelper.decode(fileData)
            Log.i(DownloadTestActivity::class.simpleName, "File decoded!!")
            FileUtils.saveFile(fileDecoded, FileUtils.buildFilePath(this, "testDecoded.mp3"))
            return fileDecoded
        }
        catch (e: Exception) {
            e.printStackTrace()
            Log.e(DownloadTestActivity::class.simpleName, e.message!!)
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDownloadComplete() {
        Log.i(DownloadTestActivity::class.simpleName, "File downloaded!!")
        encryptAudio()
    }

    override fun onError(error: Error?) {
        Log.e(DownloadTestActivity::class.simpleName, error.toString())
    }

    override fun handleMessage(msg: Message): Boolean {
        Log.i(DownloadTestActivity::class.simpleName, msg.obj.toString())
        return false
    }
}

@Composable
fun Greeting2(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
    GuideMeTravelersAppTheme {
        Greeting2("Android")
    }
}