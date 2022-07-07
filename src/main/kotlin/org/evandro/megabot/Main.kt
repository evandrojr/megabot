package org.evandro.megabot;

import org.evandro.megabot.bot.Bot


fun main(args: Array<String>) {
    val mode = 2
    val modeChoice = when(mode){
        1-> tradeBot()
        2-> benchmarkBacktests()
        3-> backtestSingleRun()
        else -> println("Invalid mode")
    }
}

fun tradeBot(){
    println("Hello Megabot!")
    val bot = Bot()
    val strategy = BuyCheapSellExpensiveStrategy(0.05, 0.2)
    strategy.engine = bot
    bot.initialize(strategy)
    bot.trade()
}

fun benchmarkBacktests() {
    println("Starting benchmark of backtests")
    Benchmark().start()
}

fun backtestSingleRun() {
    println("Starting single backtest")
    val candles = CsvToCandles().readAll()
    val buyCheapSellExpensiveStrategy = BuyCheapSellExpensiveStrategy(0.01, 0.08)
    val backtest = Backtest()
    backtest.strategy = buyCheapSellExpensiveStrategy
    backtest.candles = candles
    buyCheapSellExpensiveStrategy.engine = backtest
    backtest.setPeriod("2022-06-01", "2022-06-22")
    backtest.setup()
    backtest.trade()
    backtest.pInitial()
    backtest.stats()
}
