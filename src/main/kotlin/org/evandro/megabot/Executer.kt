package org.evandro.megabot

interface Executer {
    fun sellAll()
    fun buyAll()
    var fiatAmount : Double
    var cryptoAmount : Double
    var soldAtPrice: Double
    var boughtAtPrice: Double
//    var cryptoPrice : Double
    var cryptoSellPrice : Double
    var cryptoBuyPrice: Double
}