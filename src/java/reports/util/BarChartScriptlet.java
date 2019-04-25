package reports.util;
 
/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Contributors:
 * David Gilbert - david.gilbert@object-refinery.com
 */

import java.awt.Color;
import java.awt.Font;
import java.util.Iterator;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.jfree.util.UnitType;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;
import net.sf.jasperreports.renderers.JCommonDrawableRenderer;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: BarChartScriptlet.java,v 1.1 2007/12/21 16:10:05 wilson Exp $
 */
public class BarChartScriptlet extends JRDefaultScriptlet
{ 


	DefaultCategoryDataset dataset = new DefaultCategoryDataset();   

	public void afterDetailEval() throws JRScriptletException
	{
		super.afterDetailEval();
		
		String label = "";
		Double values[][] = null;
		String rows[] = null;
		String cols[] = null;
		
		Iterator list = this.fieldsMap.values().iterator();
		Iterator vlist = this.variablesMap.values().iterator();
		
		rows = new String[]{"row1","row2","row3","row4","row5","row6","row7"};
		cols = new String[]{"col1","col2","col3","col4","col5","col6","col7"};
		values = new Double[][]{{new Double(1),new Double(2),new Double(3),new Double(4),new Double(5),new Double(6),new Double(7)},
								{new Double(11),new Double(12),new Double(13),new Double(14),new Double(15),new Double(16),new Double(17)},
								{new Double(21),new Double(22),new Double(23),new Double(24),new Double(25),new Double(26),new Double(27)},
								{new Double(31),new Double(32),new Double(33),new Double(34),new Double(35),new Double(36),new Double(37)},
								{new Double(41),new Double(42),new Double(43),new Double(44),new Double(45),new Double(46),new Double(47)},
								{new Double(51),new Double(52),new Double(53),new Double(54),new Double(55),new Double(56),new Double(57)},
								{new Double(61),new Double(62),new Double(63),new Double(64),new Double(65),new Double(66),new Double(67)}};
		//dataset.addValue(value1, series1, category1);
		for (int i=0;i<rows.length;i++){
			String series = rows[i];
			for (int j=0;j<cols.length;j++){
				String category = cols[j];
				Double value = values[i][j];
				dataset.addValue(value, series, category);
				System.out.println(series+" = "+ category + " = "+value);
			}
		}
		System.out.println("=============");
	} 
	/**
	 *
	 */
	public void afterReportInit() throws JRScriptletException
	{

		JFreeChart jfreechart = ChartFactory.createBarChart3D(
			"Bar Chart Demo",         // chart title
			"States",                 // X Label              
			"Sell Amount",            // Y Label 
			dataset,                  // data
			PlotOrientation.VERTICAL, // orientation
			true,                     // include legend
			true,                     // tooltips? 
			false                     // URLs? 
		);
				
		CategoryPlot categoryplot = jfreechart.getCategoryPlot();
		CategoryAxis categoryaxis = categoryplot.getDomainAxis();
		categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(0.39269908169872414D));

		LegendTitle legendtitle = (LegendTitle)jfreechart.getSubtitle(0);
		legendtitle.setPosition(RectangleEdge.RIGHT);
		legendtitle.setMargin(new RectangleInsets(UnitType.ABSOLUTE, 0.0D, 4D, 0.0D, 4D));

		IntervalMarker intervalmarker = new IntervalMarker(4000D, 5000D);
		intervalmarker.setLabel("Target Range");
		intervalmarker.setLabelFont(new Font("SansSerif", 2, 11));
		intervalmarker.setLabelAnchor(RectangleAnchor.LEFT); 
		intervalmarker.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
		intervalmarker.setPaint(new Color(222, 222, 255, 128)); 
		categoryplot.addRangeMarker(intervalmarker, Layer.BACKGROUND);

		CategoryItemRenderer categoryitemrenderer = categoryplot.getRenderer();   
		categoryitemrenderer.setItemLabelsVisible(true);
		BarRenderer barrenderer = (BarRenderer)categoryitemrenderer;
		barrenderer.setMaximumBarWidth(0.050000000000000003D);

		barrenderer.setItemLabelGenerator(new LabelGenerator());
		barrenderer.setItemLabelsVisible(true);

		ItemLabelPosition itemlabelposition1 = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER_LEFT, TextAnchor.CENTER_LEFT, -1.5707963267948966D);
		barrenderer.setPositiveItemLabelPositionFallback(itemlabelposition1);

		categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

		/*
		jfreechart.setBackgroundPaint(Color.white); 
		CategoryPlot categoryplot = jfreechart.getCategoryPlot();
		categoryplot.setBackgroundPaint(Color.lightGray); 
		categoryplot.setDomainGridlinePaint(Color.white);
		categoryplot.setDomainGridlinesVisible(true);
		categoryplot.setRangeGridlinePaint(Color.white);
		NumberAxis numberaxis = (NumberAxis)categoryplot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		BarRenderer barrenderer = (BarRenderer)categoryplot.getRenderer();
		barrenderer.setDrawBarOutline(false);
		GradientPaint gradientpaint = new GradientPaint(0.0F, 0.0F, Color.blue, 0.0F, 0.0F, new Color(0, 0, 64));
		GradientPaint gradientpaint1 = new GradientPaint(0.0F, 0.0F, Color.green, 0.0F, 0.0F, new Color(0, 64, 0));
		GradientPaint gradientpaint2 = new GradientPaint(0.0F, 0.0F, Color.red, 0.0F, 0.0F, new Color(64, 0, 0));
		barrenderer.setSeriesPaint(0, gradientpaint);
		barrenderer.setSeriesPaint(1, gradientpaint1);
		barrenderer.setSeriesPaint(2, gradientpaint2);
		CategoryAxis categoryaxis = categoryplot.getDomainAxis();
		categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(0.52359877559829882D));
		   */
		this.setVariableValue("Chart", new JCommonDrawableRenderer(jfreechart));
	}

	static class LabelGenerator extends StandardCategoryItemLabelGenerator
	{

		public String generateItemLabel(CategoryDataset categorydataset, int i, int j)
		{
			return categorydataset.getRowKey(i).toString();
		}

		LabelGenerator()
		{
		}
	}

}
