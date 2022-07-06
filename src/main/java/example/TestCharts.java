
/*
 * Created on Apr 5, 2006
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedRangeCategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.DefaultCategoryItemRenderer;
import org.jfree.chart.renderer.category.IntervalBarRenderer;
import org.jfree.chart.renderer.category.StatisticalBarRenderer;
import org.jfree.chart.renderer.category.StatisticalLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.jfree.data.statistics.StatisticalCategoryDataset;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.OHLCDataset;

/**
 * @author lauke
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TestCharts
{
    public static void main (String args[])
    {
        try
        {
            TestCharts testCandleChart = new TestCharts();

            testCandleChart.run();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void run() throws Exception
    {
        JPanel main = new JPanel(new BorderLayout());

        System.out.println("What kind of chart do you want ?");
        System.out.println("Candle chart -> 1");
        System.out.println("Interval chart -> 2");
        System.out.println("Statistic chart -> 3");
        System.out.println("A combined of Interval and Statistic -> 4");
        System.out.print("==>");

        BufferedReader bufReader = new BufferedReader(new InputStreamReader(System.in));
        int choice = Integer.parseInt(bufReader.readLine());

        JFreeChart chart = null;

        if (choice == 1)
        {
            System.out.println("Creating Candle Chart");
            chart = createCandleChart();
        }
        else if (choice == 2)
        {
            System.out.println("Creating Interval Chart");
            chart = createIntervalChart();
        }
        else if (choice == 3)
        {
            System.out.println("Creating Statistical Chart");
            chart = createStatisticChart();
        }
        else if (choice == 4)
        {
            System.out.println("Creating Combined Plot of Interval and Statistical");
            chart = createCombinedPlot();
        }
        else
        {
            System.out.println("choice " + choice + " is invalid");
        }

        ChartPanel chartPanel = new ChartPanel(chart);

        main.add(chartPanel, BorderLayout.CENTER);

        JFrame frame = new JFrame("JFreeChart");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(main, BorderLayout.CENTER);
        main.setPreferredSize(new Dimension(600, 400));
        frame.pack();
        class WL extends WindowAdapter
        {
            boolean open = true;
            public void windowClosing(WindowEvent wEv)
            {
                open = false;
            }

            public void waitForWindowClosed() throws InterruptedException
            {
                while (open)
                {
                    Thread.sleep(1000);
                }
            }
        };
        WL wl = new WL();
        frame.addWindowListener(wl);
        frame.setVisible(true);
        frame.requestFocus();
        wl.waitForWindowClosed();
    }

    private OHLCDataItem [] getCandleData()
    {
        int n = 10;

        OHLCDataItem [] result = new OHLCDataItem[n];

        for (int index = 0; index < n; index++)
        {
            System.out.println("i is " + index);

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, (index * -1));
            Date date = cal.getTime();

            double random = Math.random();

            result[index] = new OHLCDataItem(date, random * 10, random *30, random * 5, random * 12, random * 1000);

            System.out.println("Open ->" + (random*10) + ", high->" + (random *30) + ", low->" + (random *5) + ", close->" + (random*12) + ", volumn->" + (random*1000));
        }

        return result;
    }

    private JFreeChart createCandleChart()
    {
        OHLCDataItem [] items = getCandleData();
        OHLCDataset ohlcdataset = new DefaultOHLCDataset( new Comparable ()
        {
            public int compareTo(Object obj)
            {
                return 0;
            }
        }, items);


//JFreecChart chart = new JFreeChart("Candle Chart", null, items, );

        JFreeChart chart = ChartFactory.createCandlestickChart("Candle Chart", "Date", "Value", ohlcdataset, true);

        return chart;
    }

    public double [][] getIntervalData()
    {
        double [][] data = new double[1][3];

        for (int i = 0 ; i < data.length; i++)
        {
            for (int j = 0; j < data.length; j++)
            {
                data[i][j] = Math.random() * 10;
                System.out.print(data[j] + ",");
            }

            System.out.println();
        }

        return data;
    }

    private CategoryPlot createIntervalPlot()
    {
        System.out.println("getting start data");

        double [][] starts = getIntervalData();

        System.out.println("getting end data");

        double [][] ends = getIntervalData();

        CategoryDataset dataset = new DefaultIntervalCategoryDataset(starts, ends);
        CategoryAxis domain = new CategoryAxis("Timeseries");
        ValueAxis range = new NumberAxis("Data");

        CategoryPlot plot = new CategoryPlot(dataset, domain, range, new IntervalBarRenderer());

        return plot;
    }

    private JFreeChart createIntervalChart()
    {
        Plot plot = createIntervalPlot();

        JFreeChart chart = new JFreeChart("Interval Chart", plot);

        return chart;
    }

    private CategoryPlot createStatisticPlot()
    {
        DefaultStatisticalCategoryDataset dataset = new DefaultStatisticalCategoryDataset();


        int numOfRow = 1;
        int numOfSeries = 3;

        for (int i = 0; i < numOfRow; i++)
        {
            for (int j = 0; j < numOfSeries; j++)
            {
                Integer row = new Integer(i);
                Integer col = new Integer(j);

                double mean = Math.random() * 10;
                double std = Math.random() * 5;

                dataset.add(mean, std, row, col);

                System.out.println("mean->" + mean + ", +std->" + (mean + std) + ", -std->" + (mean - std) + " for row->" + row + " and col->" + col);
            }
        }

        CategoryAxis domain = new CategoryAxis("Timeseries");
        ValueAxis range = new NumberAxis("Data");

        StatisticalLineAndShapeRenderer renderer = new StatisticalLineAndShapeRenderer();

//        renderer.setLinesVisible(false);
/*renderer.setSeriesFillPaint(0, Color.BLUE);
renderer.setBasePaint(Color.BLUE);
renderer.setBaseFillPaint(Color.BLUE);
renderer.setBaseOutlinePaint(Color.BLUE);
renderer.setFillPaint(Color.BLUE);*/
//        renderer.setPaint(Color.BLUE);

        CategoryPlot plot = new CategoryPlot(dataset, domain, range, renderer);

        return plot;
    }

    private JFreeChart createStatisticChart()
    {
        Plot plot = createStatisticPlot();

        JFreeChart chart = new JFreeChart("Statistical Chart", plot);

        return chart;
    }

    private JFreeChart createCombinedPlot()
    {
        CategoryPlot intervalPlot = createIntervalPlot();
        CategoryPlot statisticPlot = createStatisticPlot();

        ValueAxis range = new NumberAxis("Data");

        CategoryPlot plot = new CategoryPlot();

        plot.setDomainAxis(intervalPlot.getDomainAxis());
        plot.setRangeAxis(intervalPlot.getRangeAxis());
        plot.setDataset(0, intervalPlot.getDataset());
        plot.setDataset(1, statisticPlot.getDataset());

        plot.setRenderer(0, intervalPlot.getRenderer());
        plot.setRenderer(1, statisticPlot.getRenderer());

        JFreeChart chart = new JFreeChart("Combined Chart", plot);

        return chart;
    }
}