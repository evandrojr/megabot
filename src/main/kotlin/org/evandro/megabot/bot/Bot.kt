package org.evandro.megabot.bot

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import net.minidev.json.JSONArray
import org.evandro.megabot.Executer
import org.evandro.megabot.Log
import org.evandro.megabot.Strategy
import org.evandro.megabot.bot.api.KrakenApi
import org.evandro.megabot.formatUS
import java.io.File


// ASK menor preço que se quer vender
// Bid maior preço que se quer comprar

//The term "bid" refers to the highest price a buyer will pay to buy a specified
//number of shares of a stock at any given time. The term "ask" refers to the
//lowest price at which a seller will sell the stock.
//The bid price will almost always be lower than the ask or “offer,” price.

class Bot : Executer {

    val api = KrakenApi()
    var strategy: Strategy? = null
    val maximunFee = 0.0026
    var ticker = ""

    override var fiatAmount: Double = 0.0
    override var cryptoAmount: Double = 0.0
    override var soldAtPrice: Double = 0.0
    override var boughtAtPrice: Double = 0.0
    override var cryptoSellPrice: Double = Double.MAX_VALUE
    override var cryptoBuyPrice: Double = Double.MAX_VALUE


    var tradesExecutionCount = 0

    // Avoid crazy loops that may kill my money
    val tradesExecutionMax = 100

    fun valueAfterMaximunFees(value : Double) : Double{
        return (1.0-maximunFee*1.5) * value
    }

    fun placeSellAllOrder(pair: String, price: Double): String {
        val input: MutableMap<String, String> = HashMap()
        val balancesString = refreshBalances()
        val cryptoAmount = getBalance("XXBT", balancesString)

        input.put("pair", pair);
        input.put("type", "sell");
        input.put("ordertype", "limit")
        input.put("price", price.formatUS(8))
        input.put("volume", valueAfterMaximunFees(cryptoAmount).formatUS(8))
        Log().info(input.toString())
        var response: String
        response = api.queryPrivate(KrakenApi.Method.ADD_ORDER, input);
        System.out.println(response);
        tradesExecutionCount++
        return response
//        return ""
    }


    fun placeBuyAllOrder(pair: String, price: Double): String {
        val input: MutableMap<String, String> = HashMap()
        input.put("pair", pair);
        input.put("type", "buy");
        input.put("ordertype", "limit")
        input.put("price", price.formatUS(8))

        val balancesString = refreshBalances()
        val fiatAmount = getBalance("USDT", balancesString)
        val volume = valueAfterMaximunFees(fiatAmount / price)
        input.put("volume", volume.formatUS(8))
        Log().info(input.toString())

//        input.put("volume", valueAfterMaximunFees(volume).formatUS(8))
        var response: String
        response = api.queryPrivate(KrakenApi.Method.ADD_ORDER, input);
        System.out.println(response);
        tradesExecutionCount++
        return response
//        return ""
    }

    fun placeSellOrder(pair: String, price: Double, volume: Double): String {
        val input: MutableMap<String, String> = HashMap()
        input.put("pair", pair);
        input.put("type", "sell");
        input.put("ordertype", "limit")
        input.put("price", price.formatUS(8))
        input.put("volume", volume.formatUS(8))
//        input.put("volume", valueAfterMaximunFees(volume).formatUS(8))
        var response: String
        response = api.queryPrivate(KrakenApi.Method.ADD_ORDER, input);
        System.out.println(response);
        return response
    }

//    {"error":[],"result":{"txid":["OQVYOS-3DTXB-4LE53R"],"descr":{"order":"buy 0.00010000 XBTUSDT @ limit 183.7"}}}
    fun placeBuyOrder(pair: String, price: Double, volume: Double): String {
        val input: MutableMap<String, String> = HashMap()
        input.put("pair", pair);
        input.put("type", "buy");
        input.put("ordertype", "limit")
        input.put("price", price.formatUS(8))
        input.put("volume", (volume).formatUS(8))

//        input.put("volume", valueAfterMaximunFees(volume).formatUS(8))
        var response: String
        response = api.queryPrivate(KrakenApi.Method.ADD_ORDER, input);
        System.out.println(response);
        return response
    }

    // {"error":[],"result":{"count":1}}
    fun cancelOrder(orderId: String) : String {
        val input: MutableMap<String, String> = HashMap()
        input.put("txid", orderId)
        var response: String
        response = api.queryPrivate(KrakenApi.Method.CANCEL_ORDER, input);
        System.out.println(response);
        return response
    }


    fun updateBalances(){
        val balancesString = refreshBalances()
        cryptoAmount = getBalance("XXBT", balancesString)
        fiatAmount = getBalance("USDT", balancesString)
    }

    fun trade() {

        cryptoAmount = 0.0
        fiatAmount = 0.0

        updateBalances()
        ticker = getTickerPair("XBTUSDT")
        soldAtPrice = getSellPrice(ticker)
        boughtAtPrice = getBuyPrice(ticker)


//        val ticker = getTickerPair("XBTUSDT")
//        strategy.startPrice = getBuyPrice(ticker);

        while (tradesExecutionCount <= tradesExecutionMax) {
            try {
                ticker = getTickerPair("XBTUSDT")
                cryptoBuyPrice = getBuyPrice(ticker)
                cryptoSellPrice = getSellPrice(ticker)
                strategy!!.trade()
                Thread.sleep(20_000)
                updateBalances()
            }catch (e: Exception){
                var msg = ""
                if(e.message!=null){
                    msg = e.message!!
                }
                Log().error(msg)
            }

        }

//        println("$sellPrice $buyPrice")
//        val input = Map<String, String>()
//        input.put("pair", "XXBTUSDT");
//        response = api.queryPublic(KrakenApi.Method.TICKER, input).toString();
//        System.out.println(response);
//
//        input.put("pair", "XBTUSD");
//        response = api.queryPublic(KrakenApi.Method.ASSET_PAIRS, input).toString();
//        System.out.println(response);
//        input.clear()


//        input["pair"] = "XXBT,USDT"
//        response = api.queryPublic(KrakenApi.Method.ASSET_PAIRS, input)!!
//        println(response)//


//
//        input.clear();
//        input.put("asset", "z");
//        response = api.queryPrivate(Method.BALANCE, input);
//        System.out.println(response);
    }

    fun initialize(strategy: Strategy) {
        this.strategy = strategy
        api.setKey(apiKey())
        api.setSecret(privateKey())
        Log().info("Starting BOT ")
    }

    //        var jsonString = """{"XXBTZUSD":{"a":["20026.30000","3","3.000"],"b":["20026.10000","1","1.000"],"c":["20024.00000","0.01815000"],"v":["2983.38658295","4399.65695112"],"p":["20045.76813","20146.95136"],"t":[17265,28023],"l":["19828.70000","19828.70000"],"h":["20361.60000","20733.30000"],"o":"20252.50000"}}"""
    fun getTickerPair(pair: String): String {
        val input: MutableMap<String, String> = HashMap()
        input.put("pair", pair);
        var response: String
        response = api.queryPublic(KrakenApi.Method.TICKER, input);
        System.out.println(response);
        return response
    }

    //{"error":[],"result":{"ZUSD":"199.4757","USDT":"0.00000000","XXBT":"0.0005001900"}}
    fun refreshBalances(): String {
        val input: MutableMap<String, String> = HashMap()
        // Does not matter it brings all
        input.put("asset", "USDT");
        val response = api.queryPrivate(KrakenApi.Method.BALANCE, input);
        System.out.println(response);
        return response
    }

    var apiKey = ""
    var privateKey = ""

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

    fun getBalance(currency: String, balancesString: String): Double {
        //{"error":[],"result":{"ZUSD":"199.4757","USDT":"0.00000000","XXBT":"0.0005001900"}}
        val context: DocumentContext = JsonPath.parse(balancesString)
        return (context.read<String>("$['result']['$currency']")).toDouble()
    }

    // ASK menor preço que se quer vender
    fun getBuyPrice(ticker: String): Double {
//        var jsonString = """{"XXBTZUSD":{"a":["20026.30000","3","3.000"],"b":["20026.10000","1","1.000"],"c":["20024.00000","0.01815000"],"v":["2983.38658295","4399.65695112"],"p":["20045.76813","20146.95136"],"t":[17265,28023],"l":["19828.70000","19828.70000"],"h":["20361.60000","20733.30000"],"o":"20252.50000"}}"""
        val context: DocumentContext = JsonPath.parse(ticker)
        val askArray = context.read<List<Any>>("$['result'][*]['a'][0]") as JSONArray
//        println(askArray[0])
        return askArray[0].toString().toDouble()
    }

    // BID maior preço que se quer comprar
    fun getSellPrice(ticker: String): Double {
//        val d = 2914.65119685
//        var jsonString = """{"XXBTZUSD":{"a":["20026.30000","3","3.000"],"b":["20026.10000","1","1.000"],"c":["20024.00000","0.01815000"],"v":["2983.38658295","4399.65695112"],"p":["20045.76813","20146.95136"],"t":[17265,28023],"l":["19828.70000","19828.70000"],"h":["20361.60000","20733.30000"],"o":"20252.50000"}}"""
        val context: DocumentContext = JsonPath.parse(ticker)
        val bidArray = context.read<List<Any>>("$['result'][*]['b'][0]") as JSONArray
        println(bidArray[0])
        return bidArray[0].toString().toDouble()
    }

    override fun sellAll() {
        val sellPrice = getSellPrice(ticker)
        placeSellAllOrder("XBTUSDT", sellPrice)
        soldAtPrice = sellPrice
    }

    override fun buyAll() {
        val buyPrice = getBuyPrice(ticker)
        placeBuyAllOrder("XBTUSDT", buyPrice)
        boughtAtPrice = buyPrice
    }




}