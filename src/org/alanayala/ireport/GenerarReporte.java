package org.alanayala.ireport;


import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.alanayala.db.Conexion;

public class GenerarReporte {
    public static void mostrarReporte(String nombreReporte, String titulo,Map parametros){
            InputStream reporte = GenerarReporte.class.getResourceAsStream(nombreReporte);
    try{
        JasperReport reporteMaestro = (JasperReport) JRLoader.loadObject(reporte);
        JasperPrint reporteInpreso = JasperFillManager.fillReport(reporteMaestro, parametros,Conexion.getInstancia().getConexion());
        JasperViewer visor = new JasperViewer(reporteInpreso, false);
        visor.setTitle(titulo);
        visor.setVisible(true);
        
   
    }catch(Exception e){
    e.printStackTrace();
    }
    
    }
}