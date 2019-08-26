package org.alanayala.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.alanayala.bean.ControlCitas;
import org.alanayala.bean.Medico;
import org.alanayala.bean.Pacientes;
import org.alanayala.bean.Recetas;
import org.alanayala.db.Conexion;
import org.alanayala.sistema.Principal;

public class RecetasController implements Initializable {

    private enum operaciones {Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno};
    private operaciones tipoDeOperacion = operaciones.Ninguno;
    private ObservableList<ControlCitas> listarControlCita;
    private ObservableList<Recetas> listarRecetas;
    
    private Principal escenarioPrincipal;
    @FXML private TextField txtDescripcionReceta;
    @FXML private ComboBox cmbCodigoReceta;
    @FXML private ComboBox cmbCodigoControlCita;    
    @FXML private TableColumn colCodigoReceta;
    @FXML private TableColumn colDescripcionReceta;
    @FXML private TableColumn colCodigoControlCita;
    @FXML private TableView tblRecetas;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporteGeneral;
    @FXML private Button btnReporte;            
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        cargarDatos();
        cmbCodigoControlCita.setItems(getControlCitas());
        cmbCodigoReceta.setItems(getRecetas());
        
    }
    
            public ObservableList <Recetas> getRecetas(){
            ArrayList<Recetas> lista = new ArrayList<Recetas>();
       
           try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarRecetas}");
           ResultSet resultado = procedimiento. executeQuery();
           while(resultado.next()){
                lista.add(new Recetas(resultado.getInt("codigoReceta"),resultado.getString("descripcionReceta"),
                     resultado.getInt("codigoControlCita")));
           }
           
           
           }catch(SQLException e){
            e.printStackTrace();
        } 
           return listarRecetas = FXCollections.observableList(lista);
        
        }     
        public ObservableList <ControlCitas> getControlCitas(){
            ArrayList<ControlCitas> lista = new ArrayList<ControlCitas>();
       
           try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarControlcitas}");
           ResultSet resultado = procedimiento. executeQuery();
           while(resultado.next()){
                lista.add(new ControlCitas(resultado.getInt("codigoControlCita"),resultado.getDate("fecha"),resultado.getString("horaInicio"),
                     resultado.getString("horaFin"),  resultado.getInt("codigoMedico"),resultado.getInt("codigoPaciente")));
           }
           
           
           }catch(SQLException e){
            e.printStackTrace();
        } 
           return listarControlCita = FXCollections.observableList(lista);
        
        }
        
        public Recetas buscarRecetas(int codigoReceta)  {
        Recetas resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarRecetas(?)}");
            procedimiento.setInt(1, codigoReceta);
            ResultSet registro = procedimiento.executeQuery();
            
           while (registro.next()){
               resultado = new Recetas(registro.getInt("codigoReceta"),registro.getString("descripcionReceta"),registro.getInt("codigoControlCita"));
               
           }
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        
        return resultado;
    }
        
        public void seleccionar (){
        cmbCodigoReceta.getSelectionModel().select(buscarRecetas(( ( Recetas) tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta()));
        txtDescripcionReceta.setText(( (Recetas) tblRecetas.getSelectionModel().getSelectedItem()).getDescripcionReceta());
        cmbCodigoControlCita.getSelectionModel().select(buscarRecetas(( ( Recetas) tblRecetas.getSelectionModel().getSelectedItem()).getCodigoControlCita())); 
        }          
    
        public void cargarDatos(){        
        tblRecetas.setItems(getRecetas() );
        colCodigoReceta.setCellValueFactory(new PropertyValueFactory <ControlCitas, Integer>("codigoReceta") );
        colDescripcionReceta.setCellValueFactory(new PropertyValueFactory <Recetas, String>("descripcionReceta") );
        colCodigoControlCita.setCellValueFactory(new PropertyValueFactory <ControlCitas, Integer>("codigoControlCita") );
    } 
        
       public void nuevo(){
        switch (tipoDeOperacion){
            case Ninguno:
                activar();
                limpiar();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.Guardar;
            break;   
            case Guardar:
                ingresar();
                desactivar();
                btnAgregar.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.Ninguno;
                cargarDatos();
                break;
        }    
    } 
        
        public void activar(){
        cmbCodigoReceta.setDisable(false);
        txtDescripcionReceta.setDisable(false);
        cmbCodigoControlCita.setDisable(false);
        
        cmbCodigoReceta.setEditable(true);
        txtDescripcionReceta.setEditable(true);
        
    }    
        
        public void limpiar(){
        txtDescripcionReceta.setText("");
        cmbCodigoControlCita.getSelectionModel().clearSelection();
    } 
        
        public void desactivar(){
        cmbCodigoReceta.setDisable(true);
        txtDescripcionReceta.setDisable(true);
        cmbCodigoControlCita.setDisable(true);

        cmbCodigoReceta.setEditable(false);
        txtDescripcionReceta.setEditable(false);
        cmbCodigoControlCita.setEditable(false);
    }
      
        public void ingresar(){
        Recetas registro = new Recetas();
        registro.setDescripcionReceta(txtDescripcionReceta.getText());
        registro.setCodigoControlCita(((ControlCitas)cmbCodigoControlCita.getSelectionModel().getSelectedItem()).getCodigoControlCita());
        
        try{
        PreparedStatement  procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarRecetas(?,?)}");
        procedimiento.setString(1, registro.getDescripcionReceta());
        procedimiento.setInt(2, registro.getCodigoControlCita());

        procedimiento.execute();
        listarRecetas.add(registro);
        
        
    }catch(SQLException e){
        e.printStackTrace();
    }
    
}
    
         public void edit(){
         switch(tipoDeOperacion){
             case Ninguno:
                 if(tblRecetas.getSelectionModel().getSelectedItem() != null){
                 btnEditar.setText("Actualizar");
                 btnReporte.setText("Cancelar");
                 tipoDeOperacion = operaciones.Actualizar;
                 btnAgregar.setDisable(true);
                 btnEliminar.setDisable(true);
                 activar();
                 }
  
                 break;
             
             case Actualizar:
                 actualizar();
                 btnEditar.setText("Editar");
                 btnReporte.setText("Reporte");
                 tipoDeOperacion = operaciones.Ninguno;
                 btnAgregar.setDisable(false);
                 btnEliminar.setDisable(false);
                 desactivar();
                 cargarDatos();
                 
                break;
                 
         }
     }
         
      public void actualizar(){
          try{
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarRecetas (?,?,?)}");
             Recetas registro = (Recetas) tblRecetas.getSelectionModel().getSelectedItem();
             registro.setCodigoControlCita(((Recetas)cmbCodigoReceta.getSelectionModel().getSelectedItem()).getCodigoControlCita());
             registro.setDescripcionReceta(txtDescripcionReceta.getText());
             registro.setCodigoControlCita(((ControlCitas)cmbCodigoControlCita.getSelectionModel().getSelectedItem()).getCodigoControlCita());
               
             procedimiento.setInt(1, registro.getCodigoReceta());
             procedimiento.setString(2, registro.getDescripcionReceta());
             procedimiento.setInt(3, registro.getCodigoControlCita());
             procedimiento.execute();
             
                          
         }catch(SQLException e){
             e.printStackTrace();
         }
      }        
         
               public void eliminar(){
        if(tipoDeOperacion == operaciones.Guardar){
            
            cancelar();
             tipoDeOperacion = operaciones.Ninguno;
        }
       else{
            tipoDeOperacion = operaciones.Eliminar;
        if(tblRecetas.getSelectionModel().getSelectedItem() !=null){
            int respuesta = JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar?", "Eliminar Receta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_NO_OPTION){
                
            try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarRecetas(?)}");
                    procedimiento.setInt(1,((Recetas)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta());
                    procedimiento.execute();
                    listarRecetas.remove(tblRecetas.getSelectionModel().getSelectedIndex());
                    limpiar();
                    cargarDatos();
                
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
        
            else{
                JOptionPane.showMessageDialog(null, "Debe Seleccionar un Registro");
            }
        tipoDeOperacion = operaciones.Ninguno;
        }

         }
        
            public void cancelar(){
            btnAgregar.setText("Nuevo");
            btnEliminar.setText("Eliminar");
            btnReporte.setDisable(false);
            btnEditar.setDisable(false);
            tipoDeOperacion = operaciones.Nuevo;
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
