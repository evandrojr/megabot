package org.evandro.megabot

class BuyCheapSellExpensiveStrategy(val profitToSell: Double, val profitToBuy: Double) : Strategy {

    override var executer : Executer? =  null


    override fun trade() : Boolean {
        if (shouldSell()) {
            executer!!.sellAll()
            return true
        }

        if (shouldBuy()) {
            executer!!.buyAll()
            return true
        }
        return false
    }

    fun shouldBuy(): Boolean {
        if (executer!!.fiatAmount <= 0.0) {
            return false
        }
        return shouldBuy(executer!!.soldAtPrice, executer!!.cryptoBuyPrice, profitToBuy)
    }

    fun shouldBuy(soldAtPrice: Double, cryptoPrice: Double, profitToBuy: Double): Boolean {
        return (soldAtPrice - (soldAtPrice * profitToBuy)) >= cryptoPrice
    }

    fun shouldSell(): Boolean {
        if (executer!!.cryptoAmount <= 0.0) {
            return false
        }
        return shouldSell(executer!!.boughtAtPrice, executer!!.cryptoSellPrice, profitToSell)
    }

    fun shouldSell(boughtAtPrice: Double, cryptoPrice: Double, profitToSell: Double): Boolean {
        return ((boughtAtPrice + (boughtAtPrice * profitToSell)) <= cryptoPrice)
    }

    override fun toString(): String {
        return "BuyCheapSellExpensiveStrategy(profitToSell=$profitToSell, profitToBuy=$profitToBuy, trade=$executer)"
    }

}