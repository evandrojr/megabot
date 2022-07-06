package org.evandro.megabot.chart

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart
import org.jfree.chart.plot.CategoryPlot
import org.jfree.chart.renderer.category.IntervalBarRenderer
import org.jfree.chart.ui.ApplicationFrame
import org.jfree.data.xy.DefaultXYDataset
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import org.jfree.ui.RefineryUtilities
import java.awt.Dimension


class ScatterPlot {

    fun getIntervalData(): Array<DoubleArray>? {
        val data = Array(1) { DoubleArray(2) }
        for (i in data.indices) {
            for (j in data.indices) {
                data[i][j] = Math.random() * 10
                print(data[j].toString() + ",")
            }
            println()
        }
        return data
    }


    fun createChart(){

        val dataset : DefaultXYDataset = DefaultXYDataset()
//        dataset.addSeries()

        val series1 = XYSeries("2014")
        series1.add(18.0, 530.0)
        series1.add(20.0, 580.0)
        series1.add(25.0, 740.0)
        series1.add(30.0, 901.0)
        series1.add(40.0, 1300.0)
        series1.add(50.0, 2219.0)

        val series2 = XYSeries("2016")
        series2.add(18.0, 567.0)
        series2.add(20.0, 612.0)
        series2.add(25.0, 800.0)
        series2.add(30.0, 980.0)
        series2.add(40.0, 1210.0)
        series2.add(50.0, 2350.0)

//        dataset.addSeries(series1)
//        val dataset = XYSeriesCollection()
//        dataset.addSeries(series1)
//        dataset.addSeries(series2)


//        dataset.addSeries(series1)
//        val plot = CategoryPlot(dataset, domain, range, IntervalBarRenderer())
//        return ChartFactory.createScatterPlot(
//            "JFreeChart Scatter Plot",  // Chart title
//            "Age",  // X-Axis Label
//            "Weight",  // Y-Axis Label
//            dataset // Dataset for the Chart
//        )
    }

    fun createChart2(): JFreeChart {
        val dataset = XYSeriesCollection()
        val series1 = XYSeries("Boys")
        series1.add(10.0, 20.0)
        series1.add(11.0, 10.0)
        series1.add(12.0, 41.0)
        series1.add(12.0, 39.0)
        series1.add(13.0, 44.0)
        series1.add(14.0, 51.0)
        series1.add(15.0, 52.0)
        series1.add(15.0, 54.0)
        series1.add(16.0, 57.0)
        series1.add(17.0, 62.0)
        series1.add(17.0, 66.0)
        series1.add(18.0, 70.0)
        dataset.addSeries(series1)
        return ChartFactory.createScatterPlot(
            "JFreeChart Scatter Plot",  // Chart title
            "Age",  // X-Axis Label
            "Weight",  // Y-Axis Label
            dataset // Dataset for the Chart
        )
    }

//    @Throws(Exception::class)
//    fun start(stage: Stage) {
//
//        val viewer = ChartViewer(createChart())
//        stage.setScene(Scene(viewer))
//        stage.setTitle("JFreeChart: AreaChart")
//        stage.setWidth(600)
//        stage.setHeight(400)
//        stage.show()
//    }

    fun displayChart(chart: JFreeChart) {
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

}


fun main(args: Array<String>) {
//    val chart = ChartFactory.createCandlestickChart("BTC price", "Time", "USD", createDataSet, true)
    val chart = ScatterPlot().createChart()
    val chart2 = ScatterPlot().createChart2()
//    chart.xyPlot.renderer = CandlestickRenderer().apply {
//        autoWidthMethod = CandlestickRenderer.WIDTHMETHOD_SMALLEST
//    }

    val plot = CategoryPlot()

//    plot.domainAxis = chart.getDomainAxis()
//    plot.rangeAxis = intervalPlot.getRangeAxis()
//    plot.setDataset(0, chart.getDataset())
//        plot.setDataset(1, statisticPlot.getDataset());

    //        plot.setDataset(1, statisticPlot.getDataset());
//    plot.setRenderer(0, intervalPlot.getRenderer())
//        plot.setRenderer(1, statisticPlot.getRenderer());


    ScatterPlot().displayChart(chart2)
}

