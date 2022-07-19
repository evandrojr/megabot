package org.evandro.megabot

class ScalpingStrategy : Strategy {

    val points = 12
    val minimunProfit = 0.03

    override var engine: Engine? = null
    override fun trade(): Boolean {

        if (shouldBuy()) {
            engine!!.buyAll()
            return true
        }

        if (shouldSell()) {
            engine!!.sellAll()
            return true
        }


        return false
    }

    fun shouldSell(): Boolean {
        if (engine!!.cryptoAmount <= 0.0) {
            return false
        }
        return shouldSell(engine!!.boughtAtPrice, engine!!.cryptoSellPrice, minimunProfit)
    }

    fun shouldSell(boughtAtPrice: Double, cryptoPrice: Double, profitToSell: Double): Boolean {
        return ((boughtAtPrice + (boughtAtPrice * profitToSell)) <= cryptoPrice)
    }

    fun shouldBuy(): Boolean {
        if (engine!!.fiatAmount <= 0.0) {
            return false
        }
        return shouldBuy(points, engine!!.candles, engine!!.candleIndex, minimunProfit)
    }

    fun shouldBuy(points: Int, candles: List<Candle>, position: Int, minimunProfit: Double): Boolean {

        if (points >= position) {
            return false
        }
        val out = movingAverage(points, candles, position - 1)
        if (out.second == false) {
            return false
        }
        engine!!.boughtAtPrice = candles[position].value
        return (candles[position].value < out.first && (out.first / candles[position].value) > minimunProfit)
    }

    override fun toString(): String = ""
}