package org.evandro.megabot

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date



class Transaction {
    val file = "stats/transactions.txt"

    fun write(transaction: String){
        val out = "${now()},$transaction\n"
        println(out)
        File(file).appendText(out)
    }

    fun now() : String{
        val formatter = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss z")
        val date = Date(System.currentTimeMillis())
        return formatter.format(date)
    }
}