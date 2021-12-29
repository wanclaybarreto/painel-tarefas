package br.com.wanclaybarreto.paineltarefas.util;

import java.util.Collection;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReportsUtils {
	
	public static void generateReportPDF(String jasperFile, Map<String, Object> params, Collection<?> coll, String outFile) {
		
		try {
			
			JasperReport report = JasperCompileManager.compileReport(jasperFile);
			JasperPrint print = JasperFillManager.fillReport(report, params, new JRBeanCollectionDataSource(coll));
			JasperExportManager.exportReportToPdfFile(print, outFile);
			
		} catch (JRException e) {
			
			e.printStackTrace();
			System.out.println(e.getMessage());
			
		}
		
	}
	
}
