package example.plot

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.renderer.xy.CandlestickRenderer
import org.jfree.chart.ui.ApplicationFrame
import org.jfree.data.xy.*
import org.jfree.ui.RefineryUtilities
import java.awt.Dimension
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


data class Candle(val date: Date, val high: Double, val low: Double, val open: Double, val close: Double, val volume: Double)

fun <T> MutableList<T>.removeFirst(element: T) {
    val index = this.indexOf(element)
    removeAt(index)
}

fun newCandle(localDate: LocalDate): Candle {
    fun d() = Random().nextDouble() * 100
    val volume = d() * 100
    val doubles = listOf(d(), d(), d(), d())
    val high = doubles.max()!!
    val low = doubles.min()!!
    val (open, close) = doubles.toMutableList().apply {
        removeFirst(high)
        removeFirst(low)
    }
    val date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
    return Candle(date, high, low, open, close, volume)
}

private val createDataSet: OHLCDataset = run {
    val today = LocalDate.now()
    val candles = (1..30).map { i -> newCandle(today.plusDays(i.toLong())) }

    val dates = candles.map { it.date }.toTypedArray()
    val opens = candles.map { it.open }.toDoubleArray()
    val highs = candles.map { it.high }.toDoubleArray()
    val lows = candles.map { it.low }.toDoubleArray()
    val closes = candles.map { it.close }.toDoubleArray()
    val volumes = candles.map { it.volume }.toDoubleArray()
    DefaultHighLowDataset("btc", dates, highs, lows, opens, closes, volumes)
}

private fun createDataset(): XYDataset? {
    val series = XYSeries("2016")
    series.add(18.0, 567.0)
    series.add(20.0, 612.0)
    series.add(25.0, 800.0)
    series.add(30.0, 980.0)
    series.add(40.0, 1410.0)
    series.add(50.0, 2350.0)
    val dataset = XYSeriesCollection()
    dataset.addSeries(series)
    return dataset
}

fun main(args: Array<String>) {

    val dataset = createDataset()

    val chart = ChartFactory.createXYLineChart(
        "Average salary per age",
        "Age",
        "Salary (€)",
        dataset,
        PlotOrientation.VERTICAL,
        true,
        true,
        false
    );
//    chart.xyPlot.renderer = CandlestickRenderer().apply {
//        autoWidthMethod = CandlestickRenderer.WIDTHMETHOD_SMALLEST
//    }
    displayChart(chart)
}

private fun displayChart(chart: JFreeChart) {
    val frame = ApplicationFrame("JFreeChart KLine Demo")
    frame.contentPane = ChartPanel(chart).apply {
        fillZoomRectangle = true
        isMouseWheelEnabled = true
        preferredSize = Dimension(1000, 500)
    }
    frame.pack()
    RefineryUtilities.centerFrameOnScreen(frame)
    frame.isVisible = true
}
