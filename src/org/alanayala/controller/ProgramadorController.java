package org.alanayala.controller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.alanayala.sistema.Principal;


public class ProgramadorController implements Initializable{
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

     public void MenuPrincipal(){
        escenarioPrincipal.menuPrincipal();
}

}