package org.evandro.megabot

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class Db {

    /**
     * Connect to the test.db database
     * @return the Connection object
     */
    private fun connect(): Connection? {
        // SQLite connection string
        val url = "jdbc:sqlite:/home/j/lab/megabot/data/binance_0.1.db"
//        val url = "jdbc:sqlite:/home/j/lab/megabot/poloniex_0.1.db"
        var conn: Connection? = null
        try {
            conn = DriverManager.getConnection(url)
        } catch (e: SQLException) {
            println(e.message)
        }
        return conn
    }

//    sqlite> PRAGMA table_info('candles_USDT_NEO');
//    0|id|INTEGER|0||1
//    1|start|INTEGER|0||0
//    2|open|REAL|1||0
//    3|high|REAL|1||0
//    4|low|REAL|1||0
//    5|close|REAL|1||0
//    6|vwp|REAL|1||0
//    7|volume|REAL|1||0
//    8|trades|INTEGER|1||0
//    fun selectAll() : List<Candle> {
//        val sql = "SELECT id, start, vwp from candles_USDT_NEO;"
//        val candles : MutableList<Candle> = ArrayList();
//        try {
//            connect().use { conn ->
//                conn!!.createStatement().use { stmt ->
//                    stmt.executeQuery(sql).use { rs ->
//                        while (rs.next()) {
//                            val candle = Candle(rs.getLong("id"), rs.getLong("start"),rs.getDouble("vwp"));
//                            candles.add(candle)
//                        }
//                    }
//                }
//            }
//        } catch (e: SQLException) {
//            println(e.message)
//        }
//        return candles;
//    }

}