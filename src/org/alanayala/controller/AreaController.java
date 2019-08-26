package org.alanayala.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.alanayala.bean.Areas;
import org.alanayala.db.Conexion;
import org.alanayala.sistema.Principal;

public class AreaController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno};
    private operaciones tipoDeOperacion = operaciones.Ninguno;
    private ObservableList<Areas> listarAreas;
    @FXML private ComboBox cmbcodigoArea;
    @FXML private TextField txtnombreArea;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    @FXML private TableView tblAreas;
    @FXML private TableColumn colcodigoAreas;
    @FXML private TableColumn colnombreAreas;    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbcodigoArea.setItems(getAreas());        
    }

    public void cargarDatos(){        
        tblAreas.setItems(getAreas() );
        colcodigoAreas.setCellValueFactory(new PropertyValueFactory <Areas, Integer>("codigoArea") );
        colnombreAreas.setCellValueFactory(new PropertyValueFactory <Areas, String>("nombreArea") );

    }     
    
        public ObservableList <Areas> getAreas(){
            ArrayList<Areas> lista = new ArrayList<Areas>();
       
           try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarAreas}");
           ResultSet resultado = procedimiento. executeQuery();
           while(resultado.next()){
               lista.add(new Areas(resultado.getInt("codigoArea"),resultado.getString("nombreArea")));
           }
           
           
           }catch(SQLException e){
            e.printStackTrace();
        } 
           return listarAreas = FXCollections.observableList(lista);
        
        }    
    
        public Areas buscarArea(int codigoArea)  {
        Areas resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarAreas(?)}");
            procedimiento.setInt(1, codigoArea);
            ResultSet registro = procedimiento.executeQuery();
            
           while (registro.next()){
               resultado = new Areas(registro.getInt("codigoArea"),registro.getString("nombreArea"));
           }
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public void seleccionar (){
        cmbcodigoArea.getSelectionModel().select(buscarArea(( ( Areas) tblAreas.getSelectionModel().getSelectedItem()).getCodigoArea())); 
        txtnombreArea.setText(( (Areas) tblAreas.getSelectionModel().getSelectedItem()).getNombreArea());
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
        cmbcodigoArea.setDisable(false);
        txtnombreArea.setDisable(false);

        
        cmbcodigoArea.setEditable(true);
        txtnombreArea.setEditable(true);
    }
    
    public void limpiar(){
        txtnombreArea.setText("");
    } 
    
    public void ingresar(){
        Areas registro = new Areas();
        registro.setNombreArea(txtnombreArea.getText());

   
     try{
        PreparedStatement  procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarAreas(?)}");
        procedimiento.setString(1, registro.getNombreArea());
        procedimiento.execute();
        listarAreas.add(registro);
        
        
    }catch(SQLException e){
        e.printStackTrace();
    }    
  }
    
     public void desactivar(){
        cmbcodigoArea.setDisable(true);
        txtnombreArea.setDisable(true);

        cmbcodigoArea.setEditable(false);
        txtnombreArea.setEditable(false);

   }
    
public void edit(){
         switch(tipoDeOperacion){
             case Ninguno:
                 if(tblAreas.getSelectionModel().getSelectedItem() != null){
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
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarAreas (?,?)}");
             Areas registro = (Areas) tblAreas.getSelectionModel().getSelectedItem();
             registro.setCodigoArea(Integer.parseInt(cmbcodigoArea.getSelectionModel().getSelectedItem().toString()));
             registro.setNombreArea(txtnombreArea.getText());

             procedimiento.setInt(1, registro.getCodigoArea());
             procedimiento.setString(2, registro.getNombreArea());
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
        if(tblAreas.getSelectionModel().getSelectedItem() !=null){
            int respuesta = JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar?", "Eliminar Area", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_NO_OPTION){
                
            try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarAreas(?)}");
                    procedimiento.setInt(1,((Areas)tblAreas.getSelectionModel().getSelectedItem()).getCodigoArea());
                    procedimiento.execute();
                    listarAreas.remove(tblAreas.getSelectionModel().getSelectedIndex());
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
