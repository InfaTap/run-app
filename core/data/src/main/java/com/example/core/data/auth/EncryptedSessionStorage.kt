package com.example.core.data.auth

import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.example.core.domain.AuthInfo
import com.example.core.domain.SessionStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.content.edit
import kotlinx.serialization.json.Json
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class EncryptedSessionStorage(private val sharedPreferences: SharedPreferences): SessionStorage {
    override suspend fun getAuthInfo(): AuthInfo? {
        return withContext(Dispatchers.IO){
            val encrypted = sharedPreferences.getString(AUTH_INFO, null) ?: return@withContext null
            // Key lost (e.g. restored backup on a new device) -> treat as logged out
            runCatching {
                Json.decodeFromString<AuthInfoSerializable>(decrypt(encrypted)).toAuthInfo()
            }.getOrNull()
        }
    }

    override suspend fun setAuthInfo(authInfo: AuthInfo?) {
        withContext(Dispatchers.IO){
        if (authInfo == null){
            sharedPreferences.edit(commit = true) {
                remove(AUTH_INFO)
            }
            return@withContext
        }
            val json = Json.encodeToString(authInfo.toAuthInfoSerializable())
            sharedPreferences.edit(commit = true) {
                putString(AUTH_INFO, encrypt(json))
            }
        }
    }

    private fun key(): SecretKey {
        val keyStore = KeyStore.getInstance(KEYSTORE).apply { load(null) }
        (keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry)?.let { return it.secretKey }
        return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE).apply {
            init(
                KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(256)
                    .build()
            )
        }.generateKey()
    }

    // Stored as base64(iv + ciphertext)
    private fun encrypt(plain: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION).apply { init(Cipher.ENCRYPT_MODE, key()) }
        return Base64.encodeToString(cipher.iv + cipher.doFinal(plain.toByteArray()), Base64.NO_WRAP)
    }

    private fun decrypt(encoded: String): String {
        val bytes = Base64.decode(encoded, Base64.NO_WRAP)
        val cipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, key(), GCMParameterSpec(128, bytes, 0, IV_SIZE))
        }
        return String(cipher.doFinal(bytes, IV_SIZE, bytes.size - IV_SIZE))
    }

    companion object {
        private const val AUTH_INFO = "auth_info"
        private const val KEYSTORE = "AndroidKeyStore"
        private const val KEY_ALIAS = "auth_info_key"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val IV_SIZE = 12
    }

}
