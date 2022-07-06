package org.evandro.megabot

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date



class Log {
    val file = "stats/log.txt"

    fun info(text: String){
        val out = "${now()} [INFO] $text\n"
        println(out)
        File(file).appendText(out)
    }

    fun error(text: String){
        val out = "${now()} [ERROR] $text\n"
        println(out)
        File(file).appendText(out)
    }

    fun now() : String{
        val formatter = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss z")
        val date = Date(System.currentTimeMillis())
        return formatter.format(date)
    }
}