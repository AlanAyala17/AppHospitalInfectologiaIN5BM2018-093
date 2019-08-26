package org.alanayala.controller;


import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
//import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.alanayala.bean.Medico;
import org.alanayala.db.Conexion;
import org.alanayala.sistema.Principal;


public class MedicoController implements Initializable{
    
    private enum operaciones {Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno};
    private operaciones tipoDeOperacion = operaciones.Ninguno;
    private ObservableList<Medico> listarMedico;
    
    private Principal escenarioPrincipal;
    private DatePicker dtphoraEntrada;
    private DatePicker dtphoraSalida;
    @FXML private ComboBox cmbcodigoMedico;
    @FXML private TextField txtlicenciaMedica;
    @FXML private TextField txtnombres;
    @FXML private TextField txtapellidos;
    @FXML private TextField txtsexo;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    @FXML private GridPane grphoraEntrada;
    @FXML private GridPane grphoraSalida;
    @FXML private TableView tblMedicos;
    @FXML private TableColumn colcodigoMedico;
    @FXML private TableColumn collicenciaMedica;
    @FXML private TableColumn colnombres;
    @FXML private TableColumn colapellidos;
    @FXML private TableColumn colhoraEntrada;
    @FXML private TableColumn colhoraSalida;
    @FXML private TableColumn colsexo;
    
   
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbcodigoMedico.setItems(getMedicos());
        
        dtphoraEntrada = new DatePicker(Locale.ENGLISH);
        dtphoraEntrada.setDateFormat(new  SimpleDateFormat ("yyyy-MM-dd"));
        dtphoraEntrada.getCalendarView().todayButtonTextProperty().set("Today");
        grphoraEntrada.add(dtphoraEntrada, 0, 0);
        
        dtphoraSalida = new DatePicker(Locale.ENGLISH);
        dtphoraSalida.setDateFormat(new  SimpleDateFormat ("yyyy-MM-dd"));
        dtphoraSalida.getCalendarView().todayButtonTextProperty().set("Today");
        grphoraSalida.add(dtphoraSalida, 0, 0);        
    }
    
    public void cargarDatos(){        
        tblMedicos.setItems(getMedicos() );
        colcodigoMedico.setCellValueFactory(new PropertyValueFactory <Medico, Integer>("codigoMedico") );
        collicenciaMedica.setCellValueFactory(new PropertyValueFactory <Medico, Integer>("licenciaMedica") );
        colnombres.setCellValueFactory(new PropertyValueFactory <Medico, String>("nombres") );
        colapellidos.setCellValueFactory(new PropertyValueFactory <Medico, String>("apellidos") );
        colhoraEntrada.setCellValueFactory(new PropertyValueFactory <Medico, Date>("horaEntrada") );
        colhoraSalida.setCellValueFactory(new PropertyValueFactory <Medico, Date>("horaSalida") );
        colsexo.setCellValueFactory(new PropertyValueFactory <Medico, String>("sexo") );
    }  
    
        public ObservableList <Medico> getMedicos(){
            ArrayList<Medico> lista = new ArrayList<Medico>();
       
           try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMedicos}");
           ResultSet resultado = procedimiento. executeQuery();
           while(resultado.next()){
               lista.add(new Medico(resultado.getInt("codigoMedico"),resultado.getInt("licenciaMedica"),resultado.getString("nombres"),resultado.getString("apellidos"),
                        resultado.getDate("horaEntrada"),resultado.getDate("horaSalida"),resultado.getString("sexo")));
           }
           
           
           }catch(SQLException e){
            e.printStackTrace();
        } 
           return listarMedico = FXCollections.observableList(lista);
        
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
    
    public Medico buscarMedico(int codigoMedico)  {
        Medico resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedicos(?)}");
            procedimiento.setInt(1, codigoMedico);
            ResultSet registro = procedimiento.executeQuery();
            
           while (registro.next()){
               resultado = new Medico(registro.getInt("codigoMedico"),registro.getInt("licenciaMedica"),registro.getString("nombres"),
                       registro.getString("apellidos"),registro.getDate("horaEntrada"),registro.getDate("horaSalida"),registro.getString("sexo"));
           }
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public void seleccionar (){
        cmbcodigoMedico.getSelectionModel().select(buscarMedico(( ( Medico) tblMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico())); 
        txtlicenciaMedica.setText(""+( (Medico) tblMedicos.getSelectionModel().getSelectedItem()).getLicenciaMedica());
        txtnombres.setText(( (Medico) tblMedicos.getSelectionModel().getSelectedItem()).getNombres());
        txtapellidos.setText(((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getApellidos());
        txtsexo.setText(( (Medico) tblMedicos.getSelectionModel().getSelectedItem()).getSexo());
        dtphoraEntrada.selectedDateProperty().set(((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getHoraEntrada());
        dtphoraSalida.selectedDateProperty().set(((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getHoraSalida());
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
        cmbcodigoMedico.setDisable(false);
        txtlicenciaMedica.setDisable(false);
        txtnombres.setDisable(false);
        txtapellidos.setDisable(false);
        txtsexo.setDisable(false);
        dtphoraEntrada.setDisable(false);
        dtphoraSalida.setDisable(false);
        
        cmbcodigoMedico.setEditable(true);
        txtlicenciaMedica.setEditable(true);
        txtnombres.setEditable(true);
        txtapellidos.setEditable(true);
        txtsexo.setEditable(true);
   
    }
    
    public void limpiar(){
        txtlicenciaMedica.setText("");
        txtnombres.setText("");
        txtapellidos.setText("");
        txtsexo.setText("");
    }
    
    public void ingresar(){
        Medico registro = new Medico();
        registro.setLicenciaMedica(Integer.parseInt(txtlicenciaMedica.getText()));
        registro.setNombres(txtnombres.getText());
        registro.setApellidos(txtapellidos.getText());
        registro.setHoraEntrada(dtphoraEntrada.getSelectedDate());
        registro.setHoraSalida(dtphoraSalida.getSelectedDate());
        registro.setSexo(txtsexo.getText());
   
     try{
        PreparedStatement  procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedicos(?,?,?,?,?,?)}");
        procedimiento.setInt(1, registro.getLicenciaMedica());
        procedimiento.setString(2, registro.getNombres());
        procedimiento.setString(3, registro.getApellidos());
        procedimiento.setDate(4, new java.sql.Date (registro.getHoraEntrada().getTime()));
        procedimiento.setDate(5, new java.sql.Date (registro.getHoraSalida().getTime()));
        procedimiento.setString(6, registro.getSexo());
        procedimiento.execute();
        listarMedico.add(registro);
        
        
    }catch(SQLException e){
        e.printStackTrace();
    }
    
    
    }

     public void desactivar(){
        cmbcodigoMedico.setDisable(true);
        txtlicenciaMedica.setDisable(true);
        txtnombres.setDisable(true);
        txtapellidos.setDisable(true);
        txtsexo.setDisable(true);
        dtphoraEntrada.setDisable(true);
        dtphoraSalida.setDisable(true);
        
        cmbcodigoMedico.setEditable(false);
        txtlicenciaMedica.setEditable(false);
        txtnombres.setEditable(false);
        txtapellidos.setEditable(false);
        txtsexo.setEditable(false);
   
    }
    
     public void edit(){
         switch(tipoDeOperacion){
             case Ninguno:
                 if(tblMedicos.getSelectionModel().getSelectedItem() != null){
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
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarMedicos (?,?,?,?,?,?,?)}");
             Medico registro = (Medico) tblMedicos.getSelectionModel().getSelectedItem();
             registro.setCodigoMedico(Integer.parseInt(cmbcodigoMedico.getSelectionModel().getSelectedItem().toString()));
             registro.setLicenciaMedica(Integer.parseInt(txtlicenciaMedica.getText()));
             registro.setNombres(txtnombres.getText());
             registro.setApellidos(txtapellidos.getText());
             registro.setHoraEntrada(dtphoraEntrada.getSelectedDate());
             registro.setHoraSalida(dtphoraSalida.getSelectedDate()); 
             registro.setSexo(txtsexo.getText());
             
             procedimiento.setInt(1, registro.getCodigoMedico());
             procedimiento.setInt(2, registro.getLicenciaMedica());
             procedimiento.setString(3, registro.getNombres());
             procedimiento.setString(4, registro.getApellidos());
             procedimiento.setDate(5,new java.sql.Date (registro.getHoraEntrada().getTime()));
             procedimiento.setDate(6, new java.sql.Date (registro.getHoraSalida().getTime()));
             procedimiento.setString(7, registro.getSexo());
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
        if(tblMedicos.getSelectionModel().getSelectedItem() !=null){
            int respuesta = JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar?", "Eliminar Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_NO_OPTION){
                
            try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMedicos(?)}");
                    procedimiento.setInt(1,((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico());
                    procedimiento.execute();
                    listarMedico.remove(tblMedicos.getSelectionModel().getSelectedIndex());
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





}


