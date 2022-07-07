package org.evandro.megabot

class ScalpingStrategy : Strategy{

    val points = 12
    val minimunProfit = 0.01
    override var engine: Engine? = null
    override fun trade() : Boolean {
//        if (shouldSell()) {
//            engine!!.sellAll()
//            return true
//        }

        if (shouldBuy()) {
            engine!!.buyAll()
            return true
        }
        return false
    }

    fun shouldBuy(): Boolean {
        if (engine!!.fiatAmount <= 0.0) {
            return false
        }
        return shouldBuy(points, engine!!.candles, profitToBuy)
    }

    fun shouldBuy(points: Int, candles: List<Candle>, position: Int): Boolean {

//        fun movingAverage(points: Int, candles: List<Candle>, position: Int)

        return (soldAtPrice - (soldAtPrice * profitToBuy)) >= cryptoPrice
    }

    override fun toString(): String = ""
}