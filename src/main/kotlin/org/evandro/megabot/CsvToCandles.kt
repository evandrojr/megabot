package org.evandro.megabot

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File

class CsvToCandles {
    fun readAll(): List<Candle> {
//        val file: File = File("Binance_BTCUSDT_1h.csv")
        val file: File = File("data/Binance_BTCUSDT_d-2017-2022.csv")
        val rows: List<Map<String, String>> = csvReader().readAllWithHeader(file.readText(Charsets.UTF_8))
        val candles: MutableList<Candle> = ArrayList();

        for (row in rows) {
            val candle = Candle(row.get("date")!!.toString(), row.get("close")!!.toDouble())
            candles.add(candle)
        }
        return candles
    }
}