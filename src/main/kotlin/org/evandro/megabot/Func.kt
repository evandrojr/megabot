package org.evandro.megabot

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun format2Columns(field: String, value: String): String {
    return "${field.padEnd(40)}${value.padStart(45)}"
}

fun format4Columns(f1: String, v1: String, f2: String, v2: String): String {
    return "${f1.padEnd(15)}${v1.padStart(15)}${" ".padStart(20)}${f2.padEnd(15)}${v2.padStart(15)}"
}

fun round2(num: Double): String {
    val numberFormat = NumberFormat.getCurrencyInstance()
    numberFormat.setMaximumFractionDigits(2)
    return numberFormat.format(num).replace("R\$ ", "")
}

fun secondsToDate(seconds: Long): String {
    val date = Date(seconds * 1000)
    val sdf = SimpleDateFormat("d/MM/yyyy HH:mm:ss")
    return sdf.format(date)
}

fun profitPerc(start: Double, end: Double): String {
    return "${round2(profit(start, end) * 100.0)}%"
}

fun profitToPerc(value: Double): String {
    return "${round2(value * 100.0)}%"
}

fun profit(start: Double, end: Double): Double {
    if (start == 0.0) {
        return 0.0
    }
    return (end - start) / start
}

fun Double.formatUS(digits: Int) = "%.${digits}f".format(this).replace(",", ".")

fun movingAverage(points: Int, candles: List<Candle>, position: Int): Pair<Double, Boolean> {

    if (points - 1 > position || position - 1 > candles.size) {
        return Pair(Double.NaN, false)
    }
    var sum = 0.0
    for (i in 1..points) {
        val index = position - i
        sum+=candles[index].value
    }
    return Pair(sum / points.toDouble(), true)
}
