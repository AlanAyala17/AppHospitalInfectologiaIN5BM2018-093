package org.alanayala.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import org.alanayala.sistema.Principal;
        
public class menuPrincipalController implements Initializable{
    private Principal escenarioPrincipal;
    @Override
    
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaMedico(){
         this.escenarioPrincipal.ventanaMedicos();
    }
    
    public void ventaProgramador(){
        this.escenarioPrincipal.ventanaProgramadores();
    }
    
    public void ventanaTelefonoMedico(){
        this.escenarioPrincipal.ventanaTelefonoMedicos();
    }
    
    public void ventanaPaciente(){
        this.escenarioPrincipal.ventanaPacientes();
    }
    
    public void ventanaContactoUrgencia(){
        this.escenarioPrincipal.ventanaContactoUrgencias();
    }    
    
    public void ventanaArea(){
        this.escenarioPrincipal.ventanaAreas();
    }  

    public void ventanaCargo(){
        this.escenarioPrincipal.ventanaCargos();
    }    
    
    public void ventanaControlCita(){
        this.escenarioPrincipal.ventanaControlCitas();
    }   
    
    public void ventanaReceta(){
        this.escenarioPrincipal.ventanaRecetas();
    }
    
    
    
    public void cerrarApp (ActionEvent event){
        Platform.exit();
        System.exit(0);
    }
   
    
    public void salir(){
        System.exit(0);
}
    
}
