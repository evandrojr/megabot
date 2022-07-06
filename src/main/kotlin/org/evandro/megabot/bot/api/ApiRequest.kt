package org.evandro.megabot.bot.api


import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import org.evandro.megabot.Log
import java.io.*
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class ApiRequest {

    private val ERROR_NULL_METHOD = "The API method can't be null."
    private val ERROR_NULL_SIGNATURE = "The signature can't be null."
    private val ERROR_NULL_KEY = "The key can't be null."
    private val ERROR_NO_PARAMETERS = "The parameters can't be null or empty."
    private val ERROR_INCOMPLETE_PRIVATE_METHOD =
        "A private method request requires the API key, the message signature and the method parameters."

    private val GITHUB_NYG = "github.nyg"
    private val REQUEST_API_SIGN = "API-Sign"
    private val REQUEST_API_KEY = "API-Key"
    private val REQUEST_USER_AGENT = "User-Agent"
    private val REQUEST_POST = "POST"

    private val PUBLIC_URL = "https://api.kraken.com/0/public/"
    private val PRIVATE_URL = "https://api.kraken.com/0/private/"

    private val AMPERSAND = "&"
    private val EQUAL_SIGN = "="

    /** The request URL.  */
    private var url: URL? = null

    /** The request message signature.  */
    private var signature: String? = null

    /** The API key.  */
    private var key: String? = null

    /** The request's POST data.  */
    private var postData: StringBuilder? = null

    /** Tells whether the API method is public or private.  */
    private var isPublic = false

    /**
     * Executes the request and returns its response.
     *
     * @return the request's response
     * @throws IOException if the underlying [HttpsURLConnection] could
     * not be set up or executed
     */
    @Throws(IOException::class)
    fun execute(): String {
        var connection: HttpsURLConnection? = null
        try {
            connection = url!!.openConnection() as HttpsURLConnection
            connection.requestMethod = REQUEST_POST
            connection!!.addRequestProperty(REQUEST_USER_AGENT, GITHUB_NYG)

            // set key & signature is method is private
            if (!isPublic) {
                check(!(key == null || signature == null || postData == null)) { ERROR_INCOMPLETE_PRIVATE_METHOD }
                connection.addRequestProperty(REQUEST_API_KEY, key)
                connection.addRequestProperty(REQUEST_API_SIGN, signature)
            }

            // write POST data to request
            Log().info(postData.toString())

            if (postData != null && !postData.toString().isEmpty()) {
                connection.doOutput = true
                OutputStreamWriter(connection.outputStream).use { out -> out.write(postData.toString()) }
            }
            BufferedReader(InputStreamReader(connection.inputStream)).use { `in` ->
                val response = StringBuilder()
                var line: String?
                while (`in`.readLine().also { line = it } != null) {
                    response.append(line)
                }
                throwErrors(response.toString())
                return response.toString()
            }
        } finally {
            connection!!.disconnect()
        }
    }

//    @Throws(MalformedURLException::class)
    fun throwErrors(response: String) {
        val context: DocumentContext = JsonPath.parse(response)
        val errors = context.read<List<Any>>("$['error']")
        if (errors.isNotEmpty()) {
            val out = "API request error: $errors"
            Log().error(out)
            throw Exception(out)
        }
    }

//    fun filterResult(response: String) : String{
//        val context: DocumentContext = JsonPath.parse(response)
//        val result = context.read<String>("$['result']") as String
//        return result.toString()
//    }

    /**
     * Sets the API method of the request.
     *
     * @param method the API method
     * @return the path of the request taking the method into account
     * @throws MalformedURLException if the request URL could not be created
     * with the method name
     */
    @Throws(MalformedURLException::class)
    fun setMethod(method: KrakenApi.Method?): String? {
        requireNotNull(method) { ERROR_NULL_METHOD }
        isPublic = method.isPublic
        url = URL((if (isPublic) PUBLIC_URL else PRIVATE_URL) + method.field)
        return url!!.path
    }

    /**
     * Sets the parameters of the API method. Only supports "1-dimension" map.
     * Nulls for keys or values are converted to the string "null".
     *
     * @param parameters a map containing parameter names and values.
     * @return the parameters in POST data format, or null if the parameters are
     * null or empty
     * @throws UnsupportedEncodingException if the named encoding is not
     * supported
     * @throws IllegalArgumentException if the map is null of empty
     */
    @Throws(UnsupportedEncodingException::class)
    fun setParameters(parameters: MutableMap<String, String>): String? {
        require(!(parameters == null || parameters.isEmpty())) { ERROR_NO_PARAMETERS }
        postData = StringBuilder()
        for ((key1, value) in parameters) {
            postData!!.append(key1).append(EQUAL_SIGN).append(KrakenUtils().urlEncode(value)).append(AMPERSAND)
        }
        return postData.toString()
    }

    /**
     * Sets the value of the API-Key request property.
     *
     * @param key the key
     * @throws IllegalArgumentException if the key is null
     */
    fun setKey(key: String?) {
        requireNotNull(key) { ERROR_NULL_KEY }
        this.key = key
    }

    /**
     * Sets the value of the API-Sign request property.
     *
     * @param signature the signature
     * @throws IllegalArgumentException if the signature is null
     */
    fun setSignature(signature: String?) {
        requireNotNull(signature) { ERROR_NULL_SIGNATURE }
        this.signature = signature
    }
}