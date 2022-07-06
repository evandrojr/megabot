package org.evandro.megabot.bot

import org.evandro.megabot.BuyCheapSellExpensiveStrategy
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

@Ignore
internal class BotTest {

    @Test
    fun trade() {
        var b = Bot()
        b.trade()
    }


    //    {"error":[],"result":{"txid":["OWLRLL-JRDKN-3AUK7A"],"descr":{"order":"sell 0.00010000 XBTUSDT @ limit 40183.7"}}}
    @Test
    fun placeSellOrder() {
        val b = Bot()
        b.initialize(BuyCheapSellExpensiveStrategy(0.5, 0.5))
        b.placeSellOrder("XBTUSDT", 40183.70, 0.0001)
    }

    @Test
    fun sell() {
        val b = Bot()
        b.initialize(BuyCheapSellExpensiveStrategy(0.5, 0.5))
        val balancesString = b.refreshBalances()
        val cryptoAmount = b.getBalance("XXBT", balancesString)
        b.placeSellOrder("XBTUSDT", 40183.70, cryptoAmount)
    }

    @Test
    fun placeSellAllOrder() {
        val b = Bot()
        b.initialize(BuyCheapSellExpensiveStrategy(0.5, 0.5))
        val ticker = b.getTickerPair("XBTUSDT")
        val sellPrice = b.getSellPrice(ticker)
        val buyPrice = b.getBuyPrice(ticker)
        println("$sellPrice $buyPrice")
        println()

        b.placeSellAllOrder("XBTUSDT", sellPrice)
    }

    @Test
    fun placeBuyAllOrder() {
        val b = Bot()
        b.initialize(BuyCheapSellExpensiveStrategy(0.5, 0.5))
        val ticker = b.getTickerPair("XBTUSDT")
        val sellPrice = b.getSellPrice(ticker)
        val buyPrice = b.getBuyPrice(ticker)
        println("$sellPrice $buyPrice")
        println()

        b.placeBuyAllOrder("XBTUSDT", buyPrice)
    }





    @Test
    fun buyAll() {
        val b = Bot()
        b.initialize(BuyCheapSellExpensiveStrategy(0.5, 0.5))
        val balancesString = b.refreshBalances()
        val fiatAmount = b.getBalance("USDT", balancesString)
        val afterCharges = b.valueAfterMaximunFees(fiatAmount)
        val ticker = b.getTickerPair("XBTUSDT")
        val crypto = afterCharges / b.getBuyPrice(ticker)
        b.placeBuyOrder("XBTUSDT", 1.70, crypto)
    }

    @Test
    fun feeCalculator(){
        val charged= 0.005
        val value = 1.9237
//        val * perc = charge
        println("Perc = ${(charged/value)}")

    }

    @Test
    fun placeBuyOrder() {
        val b = Bot()
//        println("${(3384994832.434323/3.43433232).formatUS(8)}")
        b.initialize(BuyCheapSellExpensiveStrategy(0.5, 0.5))
        b.placeBuyOrder("XBTUSDT", 183.70, 0.0001)
    }


    @Test
    fun cancelOrder() {
        val b = Bot()
//        println("${(3384994832.434323/3.43433232).formatUS(8)}")
        b.initialize(BuyCheapSellExpensiveStrategy(0.5, 0.5))
        b.loadSecrets()
        b.cancelOrder("OWLRLL-JRDKN-3AUK7A")
    }

    @Test
    fun getBuyPrice() {
//        Bot().getBuyPrice()
    }

    @Test
    fun getTicker() {
        println(Bot().getTickerPair("XBTUSDT"))
    }

//    @Test
//    fun getBalance() {
//        val b = Bot()
//        b.initialize()
//        println(b.getBalance("XXBT",b.refreshBalances()))
//    }
}