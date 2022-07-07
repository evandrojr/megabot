package org.evandro.megabot

import org.nield.kotlinstatistics.median
import java.io.File
import kotlin.system.measureTimeMillis

class Benchmark {

    var assetRatios: MutableList<Double> = ArrayList()
    var results: MutableList<Quintuple<Double, Double, Double, Double, Double>> = ArrayList()
    var winnerAssetRatio: Double = Double.MIN_VALUE
    var winnerStrategy: Strategy = BuyCheapSellExpensiveStrategy(0.0, 0.0)
    var winnerBacktest: Backtest = Backtest()
    var totalTime = 0L
    var candles: List<Candle> = ArrayList()

    val profitToSellStartPerc = 1
    val profitToSellEndPerc = 50
    val profitToBuyStartPerc = 1
    val profitToBuyEndPerc = 50
    val tendency = "down"

    fun start() {
        totalTime = measureTimeMillis {

            candles = CsvToCandles().readAll()
            for (profitToSell in profitToSellStartPerc..profitToSellEndPerc) {
                for (profitToBuy in profitToBuyStartPerc..profitToBuyEndPerc) {
//                    if(tendency=="down"){
//                        if(profitToSell >= profitToBuy ){
//                            continue;
//                        }
//                    }
                    val backtest = Backtest()
                    val buyCheapSellExpensiveStrategy =
                        BuyCheapSellExpensiveStrategy(profitToSell.toDouble() / 100.0, profitToBuy.toDouble() / 100)
                    backtest.strategy = buyCheapSellExpensiveStrategy
                    backtest.candles = candles
                    buyCheapSellExpensiveStrategy.engine = backtest
//                    backtest.setPeriodAllAvailable()
                    backtest.setPeriod("2017-08-17", "2022-07-05")
                    backtest.setup()
                    backtest.trade()
                    backtest.pInitial()
//                    trade.stats()
                    assetRatios.add(backtest.assetStrategyHoldRatio())
                    backtest.stats()
                    val assetStrategyHoldRatio = backtest.assetStrategyHoldRatio()
                    if (winnerAssetRatio < assetStrategyHoldRatio) {
                        winnerAssetRatio = assetStrategyHoldRatio
                        winnerStrategy = buyCheapSellExpensiveStrategy
                        winnerBacktest = backtest
                    }
                    val profit = profit(backtest.initialAsset(), backtest.asset()) * 100.0
                    results.add(
                        Quintuple(
                            profit,
                            assetStrategyHoldRatio,
                            buyCheapSellExpensiveStrategy.profitToSell,
                            buyCheapSellExpensiveStrategy.profitToBuy,
                            0.0
                        )
                    )
                }
            }
        }
        stats();
    }

    fun stats() {
        winnerBacktest.strategy = winnerStrategy
        winnerBacktest.candles = candles
        winnerStrategy.engine = winnerBacktest
        winnerBacktest.setup()
        winnerBacktest.trade()
        winnerBacktest.pInitial()
        winnerBacktest.stats()
        println("Simulation total time ${totalTime}")

        println("winnerStrategy: $winnerStrategy winnerAssetRatio: $winnerAssetRatio Total time: $totalTime")
        println("Average assetStrategyHoldRatio ${assetRatios.average()}")
        println("Median assetStrategyHoldRatio ${assetRatios.median()}")

        println("Worst assetStrategyHoldRatio ${assetRatios.min()}")
        println("Best assetStrategyHoldRatio ${assetRatios.max()}")

        println()
        println()
        var output = ""
        for (result in results) {
            output += "${result.first},${result.second},${result.third},${result.fourth}\n"
        }
        File("stats/output.csv").writeText(output)
    }

}