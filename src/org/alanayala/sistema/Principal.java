package org.alanayala.sistema;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.alanayala.controller.AreaController;
import org.alanayala.controller.CargosController;
import org.alanayala.controller.ContactoUrgenciaController;
import org.alanayala.controller.ControlCitasController;
import org.alanayala.controller.LoginController;
import org.alanayala.controller.MedicoController;
import org.alanayala.controller.PacienteController;
import org.alanayala.controller.menuPrincipalController;
import org.alanayala.db.Conexion;
import org.alanayala.controller.ProgramadorController;
import org.alanayala.controller.RecetasController;
import org.alanayala.controller.RegistroUsuarioController;
import org.alanayala.controller.TelefonoMedicoController;


public class Principal extends Application {
    public final String PAQUETE_VISTA = "/org/alanayala/view/";
    public Stage escenarioPrincipal;
    private Scene escena;
    
    @Override
    public void start(Stage escenarioPrincipal)  {
        
        
/*   try{
    PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedicos(?)}");
    procedimiento.setInt (1, 2);
    ResultSet registro = procedimiento.executeQuery();
    while(registro.next()){
            System.out.println(registro.getInt("codigoMedico"));
            System.out.println(registro.getInt("licenciaMedica"));
            System.out.println(registro.getString("nombres"));
            System.out.println(registro.getString("apellidos"));
            System.out.println(registro.getDate("horaEntrada"));
            System.out.println(registro.getDate("horaSalida"));
            System.out.println(registro.getInt("turnoMaximo"));
            System.out.println(registro.getString("Sexo"));

            
            
            }
        }catch(Exception e){
            e.printStackTrace();
        }*/
        
      this.escenarioPrincipal = escenarioPrincipal;
      escenarioPrincipal.setTitle("Hospital de Infectología");
      ventanaLogin();   
      escenarioPrincipal.show();
    }

    public void menuPrincipal(){
        try{
            menuPrincipalController menuPrincipal = (menuPrincipalController)cambiarEscena("menuPrincipalView.fxml",789,689);
            menuPrincipal.setEscenarioPrincipal(this);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
        public void ventanaMedicos(){
        try{
            MedicoController medicoController = (MedicoController)cambiarEscena("MedicoView.fxml",789,689);
            medicoController.setEscenarioPrincipal(this);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
        
         public void ventanaProgramadores(){
        try{
            ProgramadorController programadorController = (ProgramadorController)cambiarEscena("ProgramadorView.fxml",789,689);
            programadorController.setEscenarioPrincipal(this);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
         
           public void ventanaTelefonoMedicos(){
        try{
            TelefonoMedicoController telefonoMedicoController = (TelefonoMedicoController)cambiarEscena("TelefonoMedicoView.fxml",789,689);
            telefonoMedicoController.setEscenarioPrincipal(this);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }

           public void ventanaPacientes(){
        try{
            PacienteController pacienteController = (PacienteController)cambiarEscena("PacientesView.fxml",789,689);
            pacienteController.setEscenarioPrincipal(this);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
           
    }

           public void ventanaContactoUrgencias(){
        try{
            ContactoUrgenciaController contactoUrgenciaController = (ContactoUrgenciaController)cambiarEscena("ContactoUrgenciaView.fxml",789,689);
            contactoUrgenciaController.setEscenarioPrincipal(this);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
           
    }           
      public void ventanaAreas(){
        try{
            AreaController areaController = (AreaController)cambiarEscena("AreaView.fxml",789,689);
            areaController.setEscenarioPrincipal(this);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
           
    } 
      
      public void ventanaCargos(){
        try{
            CargosController CargoController = (CargosController)cambiarEscena("CargoView.fxml",789,689);
            CargoController.setEscenarioPrincipal(this);
            
        }catch(Exception e){
            e.printStackTrace();
        }
       
      }
      
      public void ventanaControlCitas() {
          try{
              ControlCitasController ControlCitaController = (ControlCitasController)cambiarEscena("ControlCitasView.fxml",789,689);
              ControlCitaController.setEscenarioPrincipal(this);
          
        }catch(Exception e){
            e.printStackTrace();
        }
      }
      
          public void ventanaRecetas() {
          try{
              RecetasController RecetaController = (RecetasController)cambiarEscena("RecetasView.fxml",789,689);
              RecetaController.setEscenarioPrincipal(this);
          
        }catch(Exception e){
            e.printStackTrace();
        }
      }
          
          public void ventanaLogin() {
          try{
              LoginController loginController = (LoginController)cambiarEscena("LoginView.fxml",789,689);
              loginController.setEscenarioPrincipal(this);
          
        }catch(Exception e){
            e.printStackTrace();
        }
      }
      
          public void ventanaRegistrarUsuario() {
          try{
              RegistroUsuarioController RegistroUsuarioController = (RegistroUsuarioController)cambiarEscena("UsuarioView.fxml",789,689);
              RegistroUsuarioController.setEscenarioPrincipal(this);
          
        }catch(Exception e){
            e.printStackTrace();
        }
      }
         
           
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
        
    }
   
    public static void main(String[] args) {
        launch(args);
    }


    
}
