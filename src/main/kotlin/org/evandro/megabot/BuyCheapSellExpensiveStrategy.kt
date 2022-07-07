package org.evandro.megabot

class BuyCheapSellExpensiveStrategy(val profitToSell: Double, val profitToBuy: Double) : Strategy {

    override var engine : Engine? =  null


    override fun trade() : Boolean {
        if (shouldSell()) {
            engine!!.sellAll()
            return true
        }

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
        return shouldBuy(engine!!.soldAtPrice, engine!!.cryptoBuyPrice, profitToBuy)
    }

    fun shouldBuy(soldAtPrice: Double, cryptoPrice: Double, profitToBuy: Double): Boolean {
        return (soldAtPrice - (soldAtPrice * profitToBuy)) >= cryptoPrice
    }

    fun shouldSell(): Boolean {
        if (engine!!.cryptoAmount <= 0.0) {
            return false
        }
        return shouldSell(engine!!.boughtAtPrice, engine!!.cryptoSellPrice, profitToSell)
    }

    fun shouldSell(boughtAtPrice: Double, cryptoPrice: Double, profitToSell: Double): Boolean {
        return ((boughtAtPrice + (boughtAtPrice * profitToSell)) <= cryptoPrice)
    }

    override fun toString(): String {
        return "BuyCheapSellExpensiveStrategy(profitToSell=$profitToSell, profitToBuy=$profitToBuy, trade=$engine)"
    }

}