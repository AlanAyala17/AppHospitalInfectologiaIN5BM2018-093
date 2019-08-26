
package org.alanayala.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.alanayala.bean.RegistrarUsuario;
import org.alanayala.bean.TipoUsuario;
import org.alanayala.db.Conexion;
import org.alanayala.ireport.GenerarReporte;
import org.alanayala.sistema.Principal;


public class LoginController implements Initializable {
      private enum operaciones{NUEVO, AGREGAR, GUARDAR, ACTUALIZAR, NINGUNO,EDITAR,ELIMINAR, CANCELAR};
        private Principal EscenarioPrincipal; 
        private operaciones tipoDeOperacion = operaciones.NINGUNO;  
        private ObservableList <TipoUsuario> listaTipo;

        @FXML private TextField txtUsuario; 
        @FXML private PasswordField pswContrasena; 
        @FXML private ComboBox cmbTipoDeUsuario; 
        @FXML private TableView tblUsuario;
        @FXML private Button btnReporte; 
        @FXML private Button btnLogin; 
        @FXML private Button btnCancelar; 
        @FXML private Button btnRegistrar;
        
        
        
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbTipoDeUsuario.setItems(getTipo());
        
    }
    
    
        public void login (){
        String usuarioLogin= txtUsuario.getText();
        String usuarioContrasena = pswContrasena.getText();
        String tipo = String.valueOf(cmbTipoDeUsuario.getSelectionModel().getSelectedItem());
        if(usuarioLogin.equals("Alan") && usuarioContrasena.equals("aknf1988") && tipo.equals("1/Admin")){
            this.setEscenarioPrincipal(EscenarioPrincipal);
            JOptionPane.showMessageDialog(null, "Bienvenido");
            menuPrincipal(); 
        }else{
            if(usuarioLogin.equals("Adrian") && usuarioContrasena.equals("skere") && tipo.equals("2/Root")){
            this.setEscenarioPrincipal(EscenarioPrincipal);
            JOptionPane.showMessageDialog(null, "Bienvenido");
            menuPrincipal(); 
            }else{
            if(usuarioLogin.equals(usuarioLogin) && usuarioContrasena.equals(usuarioContrasena) && tipo.equals("3/Invitado"))
            this.setEscenarioPrincipal(EscenarioPrincipal);
            JOptionPane.showMessageDialog(null, "Bienvenido");
            menuPrincipal(); 
                } 
        }
    }
        
        public void registrar(){

        String tipo = String.valueOf(cmbTipoDeUsuario.getSelectionModel().getSelectedItem());
        if(tipo.equals("1/Admin")){
            this.setEscenarioPrincipal(EscenarioPrincipal);
            ventanaRegistros();
             }else {
            if(tipo.equals("2/Root")){
                this.setEscenarioPrincipal(EscenarioPrincipal);
            ventanaRegistros();
            }else {
                if(tipo.equals("3/Invitado"))
                    JOptionPane.showMessageDialog(null, "No tiene Permisos para Ingresar");
                    ventanaLogin();
                    }
                }
        }
        
                    public void reporteUsuario(){
                String tipo = String.valueOf(cmbTipoDeUsuario.getSelectionModel().getSelectedItem());
                if(tipo.equals("1/Admin")){
                 Map parametros = new HashMap();
                  GenerarReporte.mostrarReporte("reporteUsuario.jasper", "Reporte de  Usuario", parametros);
                }else{
                   if(tipo.equals("2/Root")){
                 Map parametros = new HashMap();
                  GenerarReporte.mostrarReporte("reporteUsuario.jasper", "Reporte de  Usuario", parametros);
                  }else {
                    if(tipo.equals("3/Invitado"))
                        JOptionPane.showMessageDialog(null, "No tiene permisos para esta Opcion");
                     }
                }
            }
       
                    
        public ObservableList <TipoUsuario> getTipo(){
        ArrayList <TipoUsuario> lista = new ArrayList <TipoUsuario>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTipoUsuario}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add( new TipoUsuario (resultado.getInt("codigoTipoUsuario"),
                                            resultado.getString("descripcion")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipo = FXCollections.observableList(lista);
    }
        
            public TipoUsuario buscarTipo(int codigoTipoUsuario){
        TipoUsuario resultado = null ;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTipoUsuario(?)}");
            procedimiento.setInt(1, codigoTipoUsuario);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next())
                resultado = new TipoUsuario (registro.getInt("codigoTipoUsuario"),
                                            registro.getString("descripcion"));
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }

    
    
    
    public void desactivarControles(){
        txtUsuario.setEditable(false);
    }
    
    public void activarControles(){
        txtUsuario.setEditable(true);
    }
    
    public void limpiarControles(){
        txtUsuario.setText("");
    }


            
    public Principal getEscenarioPrincipal() {
        return EscenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal EscenarioPrincipal) {
        this.EscenarioPrincipal = EscenarioPrincipal;
    }
    
     public void menuPrincipal (){
        EscenarioPrincipal.menuPrincipal();
    }
     
     public void ventanaRegistros(){
         EscenarioPrincipal.ventanaRegistrarUsuario();
     }
     
     public void ventanaLogin(){
         EscenarioPrincipal.ventanaLogin();
     }
     
     
    
}
