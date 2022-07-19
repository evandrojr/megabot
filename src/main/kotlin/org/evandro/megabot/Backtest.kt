package org.evandro.megabot

class Backtest : Engine{

    val startFiatAmount =  4411.0
    val startCryptoAmount = 1.0

    var strategy: Strategy? = null
    val secondsInADay = 86400.0
    var startCandleIndex = 0
    var endCandleIndex = 0
    override var fiatAmount = startFiatAmount
    override var cryptoAmount = startCryptoAmount
    var time = ""
    override var candles: List<Candle> = ArrayList()
    override var candleIndex = startCandleIndex
    override var boughtAtPrice = -1.0
    override var soldAtPrice = -1.0
    override var cryptoSellPrice = Double.MAX_VALUE
    override var cryptoBuyPrice = Double.MIN_VALUE
    // Maximun Kraken charge
    val charges = 0.0026
    var buys = 0
    var sells = 0

    // Times the trades are evaluated
    var evaluations = 0


    fun setup() {
        fiatAmount = startFiatAmount
        cryptoAmount = startCryptoAmount
        candleIndex = startCandleIndex
        time = candles[candleIndex].time
        cryptoSellPrice = candles[candleIndex].value
        boughtAtPrice = cryptoSellPrice
        soldAtPrice = cryptoSellPrice
    }

    fun setPeriodAllAvailable() {
        startCandleIndex = 0
        endCandleIndex = candles.size - 1
        candleIndex = startCandleIndex
    }

    // Seems to be OK
    fun setPeriod(start: String, end: String) {
        startCandleIndex = -1
        endCandleIndex = -1
        var index = 0
        for (candle in candles) {
            if (candle.time.contains(start) && startCandleIndex == -1) {
                startCandleIndex = index
            }
            if (candle.time.contains(end) && endCandleIndex == -1) {
                endCandleIndex = index
            }
//            if(candles.size - 3 == index){
//                println()
//            }
            if (endCandleIndex != -1 && startCandleIndex != -1) {
                println("startCandleIndex $startCandleIndex endCandleIndex $endCandleIndex")
                return
            }
            ++index
        }
        throw Exception("Unable to setPeriod")
    }

    fun startTime(): String {
        return candles[startCandleIndex].time
    }

    fun endTime(): String {
        return candles[endCandleIndex].time
    }

    fun startPrice(): Double {
        return candles[startCandleIndex].value
    }

    fun endPrice(): Double {
        return candles[endCandleIndex].value
    }

    fun asset(): Double {
        return fiatAmount + charge(cryptoSellPrice * cryptoAmount)
    }

    fun moveNext(): Boolean {

        if (candleIndex + 1 > candles.size - 1 || candleIndex >= endCandleIndex) {
            return false
        }
        evaluations++
        candleIndex++
        time = candles[candleIndex].time
        cryptoSellPrice = candles[candleIndex].value
        return true
    }

    fun charge(value: Double): Double {
        return (1.0 - charges) * value
    }

    fun sellProfit(): Double {
        return profit(boughtAtPrice, cryptoSellPrice)
    }

    fun buyProfit(): Double {
        return profit(soldAtPrice, cryptoSellPrice) * -1.0
    }

    override fun sellAll() {
        if (cryptoAmount > 0) {
            println(
                "SellAll boughtAtPrice: ${round2(boughtAtPrice)} cryptoSellPrice: ${round2(cryptoSellPrice)} profit: ${
                    round2(
                        sellProfit()
                    )
                }"
            )
            fiatAmount += charge(cryptoAmount * cryptoSellPrice)
            cryptoAmount = 0.0
            soldAtPrice = cryptoSellPrice
            sells++

            p()
        }
    }


    override fun buyAll() {
        if (fiatAmount > 0) {
            println(
                "buyAll soldAtPrice:  ${round2(soldAtPrice)} cryptoSellPrice: ${round2(cryptoSellPrice)} profit: ${
                    profitToPerc(
                        buyProfit()
                    )
                }"
            )
            cryptoAmount += charge(fiatAmount / cryptoSellPrice)
            fiatAmount = 0.0
            boughtAtPrice = cryptoSellPrice
            buys++
            p()
        }
    }

    fun trade() {
        while (true) {
            if (!moveNext()) {
                return
            }
            strategy?.trade()
        }
    }

    fun pInitial() {
//        println(candles)
        println("Start time: ${startTime()} End Time: ${endTime()} ")
        println("Start price: ${round2(startPrice())} End price: ${round2(endPrice())}")
        println("Asset: USD: ${round2(asset())}")
        println("Period = ${period()}")
    }

    fun p() {
        println("Total asset: ${round2(asset())} USD ${round2(fiatAmount)} Crypto ${round2(cryptoAmount)}")
//        println("Period = ${period() / secondsInADay} days")
    }

    fun initialAsset(): Double {
        return startFiatAmount + charge(startCryptoAmount * startPrice())
    }

    fun holdAsset(): Double {


        return charge((startFiatAmount/startPrice())* endPrice() + startCryptoAmount * endPrice())
    }

    fun assetStrategyHoldRatio(): Double {
        return asset() / holdAsset()
    }

    fun income(): Double {
        return (asset() - initialAsset()) / initialAsset()
    }

    fun variationCryptoPrice(): Double {
        return (endPrice() - startPrice()) / startPrice()
    }

    fun formatRatioAndProfit(title: String, start: Double, end: Double): String {
        return format4Columns("$title ratio:", round2(end / start), "$title profit:", profitPerc(start, end))
//        return "Ratio: ${round2(end / start)} Profit: ${profitPerc(start, end)}"
    }

    fun gainRatio(start: Double, end: Double): String {
        return "${round2(end / start)}x"
    }

    fun stats() {
        println("*************************************** STATS ***************************************")
        println(format2Columns("Start asset:", round2(initialAsset())))
        println(format2Columns("Strategy asset:", round2(asset())))
        println(format2Columns("Strategy gain ratio:", gainRatio(initialAsset(), asset())))
        println(format2Columns("Strategy profit:", profitPerc(initialAsset(), asset())))
        println()
        println(format2Columns("Hold asset:", round2(holdAsset())))
        println(format2Columns("Hold gain ratio:", gainRatio(initialAsset(), holdAsset())))
        println(format2Columns("Hold profit:", profitPerc(initialAsset(), holdAsset())))
        println()
        println(
            format2Columns(
                "(Strategy / Hold) gain ratio:",
                gainRatio(holdAsset() / initialAsset(), asset() / initialAsset())
            )
        )

        println(
            format2Columns(
                "(Strategy vs Hold) profit:",
                profitPerc(holdAsset(), asset())
            )
        )

        println()
        println(format2Columns("Crypto start price:", round2(startPrice())))
        println(format2Columns("Crypto end price:", round2(endPrice())))
        println(
            format2Columns(
                "Crypto price variation:", round2(
                    variationCryptoPrice() * 100
                ) + "%"
            )
        )
        println()
        println(format2Columns("Evaluations:", evaluations.toString()))
        println(format2Columns("Period:", period()))

        println("Total sells: ${sells}, buys: ${buys}, total transactions ${sells + buys}")
        println(strategy)
    }

    fun period(): String {
        return "${candles[startCandleIndex].time} to ${candles[endCandleIndex].time}"
    }

    override fun toString(): String {
        return "Trade(buys=$buys, sells=$sells)"
    }

}