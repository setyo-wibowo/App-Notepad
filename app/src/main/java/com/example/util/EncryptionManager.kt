package com.example.util

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionManager {
    private const val ALGORITHM = "AES/GCM/NoPadding"
    private const val TAG_LENGTH_BIT = 128
    private const val IV_LENGTH_BYTE = 12
    private const val KEY_SIZE = 256

    // In a real app, this key should be stored in the Android Keystore
    // For this demo, we'll simulate a key derived from a master password.
    private val masterKey: SecretKey by lazy {
        val keyGen = KeyGenerator.getInstance("AES")
        keyGen.init(KEY_SIZE)
        keyGen.generateKey()
    }

    data class EncryptedData(val ciphertext: String, val iv: String)

    fun encrypt(plaintext: String): EncryptedData {
        val cipher = Cipher.getInstance(ALGORITHM)
        val iv = ByteArray(IV_LENGTH_BYTE)
        SecureRandom().nextBytes(iv)
        val parameterSpec = GCMParameterSpec(TAG_LENGTH_BIT, iv)
        
        cipher.init(Cipher.ENCRYPT_MODE, masterKey, parameterSpec)
        val ciphertextBytes = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))
        
        return EncryptedData(
            ciphertext = Base64.encodeToString(ciphertextBytes, Base64.NO_WRAP),
            iv = Base64.encodeToString(iv, Base64.NO_WRAP)
        )
    }

    fun decrypt(ciphertext: String, iv: String): String {
        return try {
            val cipher = Cipher.getInstance(ALGORITHM)
            val ivBytes = Base64.decode(iv, Base64.NO_WRAP)
            val ciphertextBytes = Base64.decode(ciphertext, Base64.NO_WRAP)
            val parameterSpec = GCMParameterSpec(TAG_LENGTH_BIT, ivBytes)
            
            cipher.init(Cipher.DECRYPT_MODE, masterKey, parameterSpec)
            val plaintextBytes = cipher.doFinal(ciphertextBytes)
            String(plaintextBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            "Error: Decryption Failed"
        }
    }
}
