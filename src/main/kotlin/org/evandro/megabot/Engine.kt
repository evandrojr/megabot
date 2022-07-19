package org.evandro.megabot

interface Engine {
    fun sellAll()
    fun buyAll()
    val candleIndex: Int
    val candles: List<Candle>
    var fiatAmount : Double
    var cryptoAmount : Double
    var soldAtPrice: Double
    var boughtAtPrice: Double
//    var cryptoPrice : Double
    var cryptoSellPrice : Double
    var cryptoBuyPrice: Double
}