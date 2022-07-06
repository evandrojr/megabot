package org.evandro.megabot.bot.api

import org.junit.jupiter.api.Test
import kotlin.test.Ignore

internal class ApiRequestTest {

    @Ignore
    @Test
    fun ok() {
        var response =
            """{"error":["1",2,4],"result":{"XXBTZUSD":{"a":["20077.30000","1","1.000"],"b":["20077.20000","11","11.000"],"c":["20077.30000","0.00016433"],"v":["3018.87834438","4378.12653740"],"p":["20045.73577","20139.74411"],"t":[17635,27968],"l":["19828.70000","19828.70000"],"h":["20361.60000","20733.30000"],"o":"20252.50000"}}}"""
        ApiRequest().throwErrors(response)
    }



//    @Test
//    fun filterResult() {
//        var response =
//            """{"error":["1",2,4],"result":{"XXBTZUSD":{"a":["20077.30000","1","1.000"],"b":["20077.20000","11","11.000"],"c":["20077.30000","0.00016433"],"v":["3018.87834438","4378.12653740"],"p":["20045.73577","20139.74411"],"t":[17635,27968],"l":["19828.70000","19828.70000"],"h":["20361.60000","20733.30000"],"o":"20252.50000"}}}"""
//        val v = ApiRequest().filterResult(response)
//        println(v)
//    }
}