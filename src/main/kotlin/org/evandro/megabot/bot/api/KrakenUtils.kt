package org.evandro.megabot.bot.api

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.nio.charset.Charset
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


class KrakenUtils {

    private val ERROR_NULL_INPUT = "Input can't be null."
    private val ERROR_NULL_ARRAYS = "Given arrays can't be null."

    private val UTF8 = "UTF-8"

    private val SHA256 = "SHA-256"
    private val HMAC_SHA512 = "HmacSHA512"

    fun base64Decode(input: String?): ByteArray? {
        return Base64.getDecoder().decode(input)
    }

    fun base64Encode(data: ByteArray?): String? {
        return Base64.getEncoder().encodeToString(data)
    }

    fun concatArrays(a: ByteArray?, b: ByteArray?): ByteArray? {
        require(!(a == null || b == null)) { ERROR_NULL_ARRAYS }
        val concat = ByteArray(a.size + b.size)
        for (i in concat.indices) {
            concat[i] = if (i < a.size) a[i] else b[i - a.size]
        }
        return concat
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
    fun hmacSha512(key: ByteArray?, message: ByteArray?): ByteArray? {
        val mac = Mac.getInstance(HMAC_SHA512)
        mac.init(SecretKeySpec(key, HMAC_SHA512))
        return mac.doFinal(message)
    }

    @Throws(NoSuchAlgorithmException::class)
    fun sha256(message: String?): ByteArray? {
        val md = MessageDigest.getInstance(SHA256)
        return md.digest(stringToBytes(message))
    }

    fun stringToBytes(input: String?): ByteArray? {
        requireNotNull(input) { ERROR_NULL_INPUT }
        return input.toByteArray(Charset.forName(UTF8))
    }

    @Throws(UnsupportedEncodingException::class)
    fun urlEncode(input: String?): String? {
        return URLEncoder.encode(input, UTF8)
    }

    private fun KrakenUtils() {}
}