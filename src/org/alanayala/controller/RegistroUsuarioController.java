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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.alanayala.bean.RegistrarUsuario;
import org.alanayala.bean.TipoUsuario;
import org.alanayala.db.Conexion;
import org.alanayala.ireport.GenerarReporte;
import org.alanayala.sistema.Principal;


public class RegistroUsuarioController implements Initializable  {
      private enum operaciones{NUEVO, AGREGAR, GUARDAR, ACTUALIZAR, NINGUNO,EDITAR,ELIMINAR, CANCELAR};
        private Principal escenarioPrincipal; 
        private operaciones tipoDeOperacion = operaciones.NINGUNO; 
        private ObservableList <RegistrarUsuario> listaRegistrar;
        private ObservableList <TipoUsuario> listaTipo;
        
        @FXML private TableView tblUsuario;
        @FXML private TableColumn colCodigoUsuario; 
        @FXML private TableColumn colFecha; 
        @FXML private TableColumn colEstado;
        @FXML private TableColumn colHora;
        @FXML private TableColumn colLogin; 
        @FXML private TableColumn colTipo;
        @FXML private TextField txtContrasena; 
        @FXML private TextField txtLogin; 
        @FXML private TextField txtHora; 
        @FXML private TextField txtFecha; 
        @FXML private Button btnAgregar; 
        @FXML private Button btnEditar;
        @FXML private Button btnEliminar;
        @FXML private CheckBox chbEstado;
        @FXML private ComboBox cmbTipo;
        
        
    @Override
    public void initialize(URL location, ResourceBundle resources) {
         cargarDatos();
         cmbTipo.setItems(getTipo());
    }
    

    
    public void cargarDatos(){
        tblUsuario.setItems(getRegistro());
        colCodigoUsuario.setCellValueFactory(new PropertyValueFactory<RegistrarUsuario, Integer> ("codigoUsuario"));
        colLogin.setCellValueFactory(new PropertyValueFactory<RegistrarUsuario, String>("usuarioLogin"));
        colEstado.setCellValueFactory(new PropertyValueFactory<RegistrarUsuario, String>("usuarioEstado"));
        colFecha.setCellValueFactory(new PropertyValueFactory<RegistrarUsuario, String>("usuarioFecha"));
        colHora.setCellValueFactory(new PropertyValueFactory <RegistrarUsuario, String>("usuarioHora"));
        colTipo.setCellValueFactory(new PropertyValueFactory <RegistrarUsuario, String>("codigoTipoUsuario"));
    }
    
    public ObservableList <RegistrarUsuario> getRegistro(){
        ArrayList <RegistrarUsuario> lista = new ArrayList <RegistrarUsuario>();
        try{ 
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarUsuario()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new RegistrarUsuario(resultado.getInt("codigoUsuario"),
                                                                resultado.getString("usuarioLogin"),
                                                                resultado.getString("usuarioContrasena"),
                                                                resultado.getBoolean("usuarioEstado"),
                                                                resultado.getString("usuarioFecha"),
                                                                resultado.getString("usuarioHora"),
                                                                resultado.getInt("codigoTipoUsuario")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaRegistrar = FXCollections.observableList(lista);
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
        
    public void seleccionarDatos(){
       txtLogin.setText(((RegistrarUsuario)tblUsuario.getSelectionModel().getSelectedItem()).getUsuarioLogin());
       txtContrasena.setText(((RegistrarUsuario)tblUsuario.getSelectionModel().getSelectedItem()).getUsuarioContrasena());
       chbEstado.setText(String.valueOf((RegistrarUsuario)tblUsuario.getSelectionModel().getSelectedItem()));
       txtFecha.setText(((RegistrarUsuario)tblUsuario.getSelectionModel().getSelectedItem()).getUsuarioFecha());
       txtHora.setText(((RegistrarUsuario)tblUsuario.getSelectionModel().getSelectedItem()).getUsuarioHora());
       cmbTipo.getSelectionModel().select(buscarTipo(((RegistrarUsuario)tblUsuario.getSelectionModel().getSelectedItem()).getCodigoTipoUsuario()));
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
    
    
    public RegistrarUsuario buscarRegistrar(int codigoUsuario){
        RegistrarUsuario resultado = null; 
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarUsuario(?)}");
            procedimiento.setInt(1, codigoUsuario);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new RegistrarUsuario (registro.getInt("codigoUsuario"),
                                                    registro.getString("usuarioLogin"),
                                                    registro.getString("usuarioContrasena"),
                                                    registro.getBoolean("usuarioEstado"),
                                                    registro.getString("usuarioFecha"),
                                                    registro.getString("usarioHora"),
                                                    registro.getInt("codigoTipoUsuario"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  resultado; 
    }
    
    public void editar(){
        switch (tipoDeOperacion){
            case NINGUNO: 
                if(tblUsuario.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnEliminar.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un Usuario");
                }
                break;
            case ACTUALIZAR: 
                actualizar();
                btnEditar.setText("Editar");
                btnEliminar.setText("Eliminar");
                btnAgregar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarUsuarios(?,?,?,?,?,?,?)}");
            RegistrarUsuario registro = ((RegistrarUsuario)tblUsuario.getSelectionModel().getSelectedItem());
            registro.setUsuarioLogin(txtLogin.getText());
            registro.setUsuarioContrasena(txtContrasena.getText());
            registro.setUsuarioEstado(((RegistrarUsuario)tblUsuario.getSelectionModel().getSelectedItem()).getUsuarioEstado());
            registro.setUsuarioFecha(txtFecha.getText());
            registro.setUsuarioHora(txtHora.getText());
            registro.setCodigoTipoUsuario(((RegistrarUsuario)tblUsuario.getSelectionModel().getSelectedItem()).getCodigoTipoUsuario());
            procedimiento.setInt(1, registro.getCodigoUsuario());
            procedimiento.setString(2, registro.getUsuarioLogin());
            procedimiento.setString(3, registro.getUsuarioContrasena());
            procedimiento.setBoolean(4, registro.getUsuarioEstado());
            procedimiento.setString(5, registro.getUsuarioFecha());
            procedimiento.setString(6, registro.getUsuarioHora());
            procedimiento.setInt(7, registro.getCodigoTipoUsuario());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnEliminar.setText("Eliminar");
                btnEditar.setText("Cancelar");
                btnAgregar.setDisable(false);
                tipoDeOperacion = operaciones.NUEVO;
                break;
            default:
            if(tblUsuario.getSelectionModel().getSelectedItem() != null){
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el Usuario?", "Eliminar Usuario", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION)
                    try{
                        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarUsuarios(?)}");
                        procedimiento.setInt(1, ((RegistrarUsuario)tblUsuario.getSelectionModel().getSelectedItem()).getCodigoUsuario());
                        procedimiento.execute();
                        listaRegistrar.remove(tblUsuario.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
            }else {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un Usuario");
            }
                
        }
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnAgregar.setText("Guardar");
                btnEditar.setText("Cancelar");
                btnEliminar.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR: 
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEditar.setText("Editar");
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
        }
    }
    
    public void guardar(){
        RegistrarUsuario registro  = new RegistrarUsuario();
        registro.setUsuarioLogin(txtLogin.getText());
        registro.setUsuarioContrasena(txtContrasena.getText()) ;
        registro.setUsuarioEstado(chbEstado.isSelected());
        registro.setUsuarioFecha(txtFecha.getText());
        registro.setUsuarioHora(txtHora.getText());
        registro.setCodigoTipoUsuario(((TipoUsuario)cmbTipo.getSelectionModel().getSelectedItem()).getCodigoTipoUsuario());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarUsuario(?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getUsuarioLogin());
            procedimiento.setString(2, registro.getUsuarioContrasena());
            procedimiento.setBoolean(3, registro.getUsuarioEstado());
            procedimiento.setString(4, registro.getUsuarioFecha());
            procedimiento.setString(5, registro.getUsuarioHora());
            procedimiento.setInt(6, registro.getCodigoTipoUsuario());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void desactivarControles(){
        txtContrasena.setEditable(false);
        txtLogin.setEditable(false);
        txtHora.setEditable(false);
        txtFecha.setEditable(false);
    }
    
    public void activarControles(){
        txtContrasena.setEditable(true);
        txtLogin.setEditable(true);
        txtHora.setEditable(true);
        txtFecha.setEditable(true);
    }
    
    public void limpiarControles(){
        txtContrasena.setText("");
        txtLogin.setText("");
        txtHora.setText("");
        txtFecha.setText("");
    }
 
            public void imprimirReporte (){
            if (tblUsuario.getSelectionModel().getSelectedItem()  != null){
            int codigoUsuario = ((RegistrarUsuario) tblUsuario.getSelectionModel().getSelectedItem()).getCodigoUsuario();
            Map parametros = new HashMap();
            parametros.put("p_codigoUsuario", codigoUsuario);
            GenerarReporte.mostrarReporte("Reporte de Usuario.jasper", "Reporte de Usuario", parametros);
        }else{
                JOptionPane.showMessageDialog(null, "Debe seleccionar un registro" );
                }
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
    
    public void ventanaLogin(){
        this.escenarioPrincipal.ventanaLogin();
}
    }