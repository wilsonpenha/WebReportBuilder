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

import java.util.Iterator;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.fill.JRFillField;
import net.sf.jasperreports.renderers.JCommonDrawableRenderer;
 
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: PieChart3DScriptlet.java,v 1.1 2007/12/21 16:10:05 wilson Exp $
 */
public class PieChart3DScriptlet extends JRDefaultScriptlet
{ 


	DefaultPieDataset dataset = new DefaultPieDataset();   

	public void afterDetailEval() throws JRScriptletException
	{
		super.afterDetailEval(); 
		
		String label = "";
		Double value = null;
		Iterator list = this.fieldsMap.values().iterator();
		Iterator vlist = this.variablesMap.values().iterator();
		while (vlist.hasNext()){
			JRVariable variable = (JRVariable)vlist.next(); 
			System.out.println("Variable : "+variable.getName());
			if (variable.getName().equals("stateTotal"))
				value=(Double)this.getVariableValue(variable.getName());
		}
		while (list.hasNext()){
			JRFillField field = (JRFillField)list.next();
			//System.out.println(field.getName()+" : "+this.getFieldValue(field.getName()));
			if (field.getName().equals("STATE"))
				label=(String)this.getFieldValue(field.getName());
			//if (field.getName().equals("QUANTITY"))
			//	value=(Double)this.getFieldValue(field.getName());
		}
		dataset.setValue(label, value);
		System.out.println("=============");
	} 
	/**
	 *
	 */
	public void afterReportInit() throws JRScriptletException
	{

		JFreeChart chart = 
			ChartFactory.createPieChart3D( 
				"Pie Chart 3D Demo 1",
				dataset,
				true,
				true,
				false
				);
				
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		plot.setNoDataMessage("No data to display");

		/*   */
		this.setVariableValue("Chart", new JCommonDrawableRenderer(chart));
	}


}
