package org.evandro.megabot

import org.apache.commons.codec.binary.Base64
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.File
import java.io.InputStreamReader
import java.math.BigDecimal
import java.net.URL
import java.security.MessageDigest
import javax.crypto.Cipher.SECRET_KEY
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.net.ssl.HttpsURLConnection


class Order {

    var apiKey = ""
    var privateKey = ""
    val baseUrl = "https://api.kraken.com"



    fun loadSecrets() {
        val secure = File("secure").readLines()
        apiKey = secure[0];
        privateKey = secure[1]
    }

    fun apiKey(): String {
        if (apiKey == "") {
            loadSecrets()
        }
        return apiKey
    }

    fun privateKey(): String {
        if (privateKey == "") {
            loadSecrets()
        }
        return privateKey
    }

    fun nonce():String {
       return System.currentTimeMillis().toString()
    }
    /**
     * Create payload for kraken
     *
     * @param limitOrderDto
     * @return
     */
    fun createPayloadAndSendLimitOrder(amount: BigDecimal, limit: Double) {
        val pair = "XBTUSD"



        /**
         * To place market order you just need to change ordertype as market.
         */
        val data = "nonce=" + nonce() + "&price=" + amount + "&volume=" + limit +
                "&type=sell&ordertype=limit&pair=" + pair + "&expiretm=" + (System.currentTimeMillis() + 6000) / 1000
        val answer = post(
            baseUrl + "/0/private/AddOrder", data,
            calculateSignature(nonce(), data, "/0/private/AddOrder")
        )
    }

    fun getBalance(){
        val data = "nonce=" + nonce()
        val answer = post(
            baseUrl + "/0/private/Balance", data,
            calculateSignature(nonce(), data, "/0/private/AddOrder")
        )

    }

    /**
     * This method is used to send data to kraken
     *
     * @param address
     * @param output
     * @param signature
     * @return
     */
    fun post(address: String?, data: String?, signature: String?): String {
        var answer = ""
        var c: HttpsURLConnection? = null
        try {
            val u = URL(address)
            c = u.openConnection() as HttpsURLConnection?
            c!!.requestMethod = "POST"
            c.setRequestProperty("API-Key", apiKey())
            c.setRequestProperty("API-Sign", signature)
            c.doOutput = true
            val os = DataOutputStream(c.outputStream)
            os.writeBytes(data)
            os.flush()
            os.close()
            var br: BufferedReader? = null
            if (c.responseCode >= 400) {
                println("response code =" + c.responseCode)
                println("response message = " + c.responseMessage)
                println("here")
                System.exit(1)
            }
            br = BufferedReader(InputStreamReader(c.inputStream))
            var line: String
            br.forEachLine { answer+=it }
            println(answer)
        } catch (x: Exception) {
            x.printStackTrace()
        } finally {
            c!!.disconnect()
        }
        return answer
    }


    /**
     * This method is used for signature calculation
     *
     * @param nonce
     * @param data
     * @param uri
     * @return
     */
    fun calculateSignature(nonce: String, data: String, uri: String): String? {
        var signature: String? = ""
        try {
            val md = MessageDigest.getInstance("SHA-256")
            md.update((nonce + data).toByteArray())
            val mac = Mac.getInstance("HmacSHA512")
            mac.init(SecretKeySpec(Base64.decodeBase64(privateKey()), "HmacSHA512"))
            mac.update(uri.toByteArray())
            signature = String(Base64.encodeBase64(mac.doFinal(md.digest())))
        } catch (e: Exception) {
        }
        return signature
    }
}