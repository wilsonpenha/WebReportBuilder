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
import java.awt.GradientPaint;
import java.util.Iterator;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.fill.JRFillField;
import net.sf.jasperreports.renderers.JCommonDrawableRenderer;
 
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.jfree.util.Rotation;
import org.jfree.util.UnitType;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: BarChart2Scriptlet.java,v 1.1 2007/12/21 16:10:05 wilson Exp $
 */
public class BarChart2Scriptlet extends JRDefaultScriptlet
{ 


	DefaultCategoryDataset dataset = new DefaultCategoryDataset();   

	public void afterDetailEval() throws JRScriptletException
	{
		super.afterDetailEval();
		
		String label = "";
		Double value1 = null;
		Double value2 = null;
		Iterator list = this.fieldsMap.values().iterator();
		Iterator vlist = this.variablesMap.values().iterator();
		String series1 = "Total Amount by State";
		String series2 = "Total Amount by Product";
		String category1 = "";
		String category2 = "";

		while (vlist.hasNext()){
			JRVariable variable = (JRVariable)vlist.next(); 
			System.out.println("Variable : "+variable.getName());
			if (variable.getName().equals("stateQtyTotal")){
				value1=(Double)this.getVariableValue(variable.getName());
			}
			if (variable.getName().equals("stateTotal")){
				value2=(Double)this.getVariableValue(variable.getName());
			}
		} 
		while (list.hasNext()){ 
			JRFillField field = (JRFillField)list.next();
			//System.out.println(field.getName()+" : "+this.getFieldValue(field.getName()));
			if (field.getName().equals("STATE"))
				category1=(String)this.getFieldValue(field.getName());
			//if (field.getName().equals("QUANTITY"))
			//	value=(Double)this.getFieldValue(field.getName());
		}
		dataset.addValue(value1, series1, category1);
		dataset.addValue(value2, series2, category1);
		System.out.println("=============");
	} 
	/**
	 *
	 */
	public void afterReportInit() throws JRScriptletException
	{

		JFreeChart jfreechart = ChartFactory.createBarChart(
			"Bar Chart Demo",         // chart title
			"States",                 // X Label
			"Sell Amount",            // Y Label
			dataset,                  // data
			PlotOrientation.VERTICAL, // orientation
			true,                     // include legend
			true,                     // tooltips?
			false                     // URLs?
		);
				
		jfreechart.setBackgroundPaint(Color.white);
		CategoryPlot categoryplot = jfreechart.getCategoryPlot();
		categoryplot.setBackgroundPaint(Color.lightGray);
		categoryplot.setDomainGridlinePaint(Color.white);
		categoryplot.setRangeGridlinePaint(Color.white);

		LegendTitle legendtitle = (LegendTitle)jfreechart.getSubtitle(0);
		legendtitle.setPosition(RectangleEdge.RIGHT);
		legendtitle.setMargin(new RectangleInsets(UnitType.ABSOLUTE, 0.0D, 4D, 0.0D, 4D));

		IntervalMarker intervalmarker = new IntervalMarker(200D, 250D);
		intervalmarker.setLabel("Target Range");
		intervalmarker.setLabelFont(new Font("SansSerif", 2, 11));
		intervalmarker.setLabelAnchor(RectangleAnchor.LEFT);
		intervalmarker.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
		intervalmarker.setPaint(new Color(222, 222, 255, 128));
		categoryplot.addRangeMarker(intervalmarker, Layer.BACKGROUND);

		NumberAxis numberaxis = (NumberAxis)categoryplot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		BarRenderer barrenderer = (BarRenderer)categoryplot.getRenderer();
		barrenderer.setDrawBarOutline(false);
		barrenderer.setItemMargin(0.10000000000000001D);

		GradientPaint gradientpaint = new GradientPaint(0.0F, 0.0F, Color.blue, 0.0F, 0.0F, new Color(0, 0, 64));
		GradientPaint gradientpaint1 = new GradientPaint(0.0F, 0.0F, Color.green, 0.0F, 0.0F, new Color(0, 64, 0));
		GradientPaint gradientpaint2 = new GradientPaint(0.0F, 0.0F, Color.red, 0.0F, 0.0F, new Color(64, 0, 0));

		barrenderer.setSeriesPaint(0, gradientpaint);
		barrenderer.setSeriesPaint(1, gradientpaint1);
		barrenderer.setSeriesPaint(2, gradientpaint2); 

		barrenderer.setItemLabelGenerator(new LabelGenerator());
		barrenderer.setItemLabelsVisible(true);
		//ItemLabelPosition itemlabelposition = new ItemLabelPosition(ItemLabelAnchor.INSIDE12, TextAnchor.CENTER_RIGHT, TextAnchor.CENTER_RIGHT, -1.5707963267948966D);
		//barrenderer.setPositiveItemLabelPosition(itemlabelposition);

		ItemLabelPosition itemlabelposition1 = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER_LEFT, TextAnchor.CENTER_LEFT, -1.5707963267948966D);
		barrenderer.setPositiveItemLabelPositionFallback(itemlabelposition1);

		CategoryAxis categoryaxis = categoryplot.getDomainAxis();
		categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		/*   */
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
