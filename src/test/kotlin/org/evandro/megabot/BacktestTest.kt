package org.evandro.megabot

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class BacktestTest {

    @Test
    fun buyProfit() {
        val t = Backtest()
        t.soldAtPrice=20.0
        t.cryptoSellPrice=10.0
        assertEquals(0.5,t.buyProfit())
    }

    @Test
    fun buyProfitCrazy() {
        val t = Backtest()
        t.soldAtPrice=16199.91
        t.cryptoSellPrice=4352.34
        assertEquals(0.7313355444567284,t.buyProfit())
    }


}