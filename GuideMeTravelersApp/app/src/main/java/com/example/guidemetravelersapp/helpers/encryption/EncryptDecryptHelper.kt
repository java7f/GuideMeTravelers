package com.example.guidemetravelersapp.helpers.encryption

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import com.bumptech.glide.util.Util
import com.example.guidemetravelersapp.BuildConfig
import com.example.guidemetravelersapp.helpers.utils.Utils
import com.google.android.exoplayer2.upstream.DataSink
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.crypto.AesCipherDataSink
import java.io.File
import java.io.FileOutputStream
import java.security.Key
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptDecryptHelper {

    @RequiresApi(Build.VERSION_CODES.M)
    companion object {

        fun encode(fileData: ByteArray): ByteArray {
            val cipher = Cipher.getInstance(Utils.CIPHER_ALGORITHM)
            val key = getPrivateKey()
            cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(ByteArray(cipher.blockSize)))
            return cipher.doFinal(fileData)
        }

        suspend fun decode(fileData: ByteArray): ByteArray {
            val cipher = Cipher.getInstance(Utils.CIPHER_ALGORITHM)
            val key = getPrivateKey()
            val ivParameterSpec = IvParameterSpec(ByteArray(cipher.blockSize))
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec)
            return cipher.doFinal(fileData)
        }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun generateKey(): SecretKey {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, Utils.KEYSTORE_NAME)
            keyGenerator.init(KeyGenParameterSpec.Builder(
                Utils.ENCRYPTION_KEY_NAME,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                ).setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setRandomizedEncryptionRequired(false)
                .build()
            )
            return keyGenerator.generateKey()
        }

        private fun getPrivateKey(): Key {
            val keyStore = KeyStore.getInstance(Utils.KEYSTORE_NAME)
            keyStore.load(null)
            return if(keyStore.containsAlias(Utils.ENCRYPTION_KEY_NAME))
                keyStore.getKey(Utils.ENCRYPTION_KEY_NAME, null)
            else
                generateKey()
        }
    }
}