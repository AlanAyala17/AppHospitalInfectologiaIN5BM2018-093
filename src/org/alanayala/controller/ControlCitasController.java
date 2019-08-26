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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.alanayala.bean.ControlCitas;
import org.alanayala.bean.Medico;
import org.alanayala.bean.Pacientes;
import org.alanayala.db.Conexion;
import org.alanayala.ireport.GenerarReporte;
import org.alanayala.sistema.Principal;

public class ControlCitasController implements Initializable{


    
    private enum operaciones {Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno};
    private operaciones tipoDeOperacion = operaciones.Ninguno;
    private ObservableList <Medico> listarMedico;
    private ObservableList<ControlCitas> listarControlCita;
    private ObservableList<Pacientes> listarPacientes;
    
    
    private Principal escenarioPrincipal;
    @FXML private TextField txtHoraInicio;
    @FXML private TextField txtHoraFin;
    private DatePicker dtpFecha;
    @FXML private ComboBox cmbControlCitas;
    @FXML private ComboBox cmbCodigoPaciente;
    @FXML private ComboBox cmbCodigoMedico;
    @FXML private TableColumn colControlCitas;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colHoraInicio;
    @FXML private TableColumn colHoraFin;
    @FXML private TableColumn colCodigoPaciente;
    @FXML private TableColumn colCodigoMedico;
    @FXML private TableView tblControlCitas;
    @FXML private GridPane grpFecha;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporteGeneral;
    @FXML private Button btnReporte;              

@Override
public void initialize(URL location, ResourceBundle resources){

        cargarDatos();
        cmbControlCitas.setItems(getControlCitas());
        cmbCodigoMedico.setItems(getMedicos());
        cmbCodigoPaciente.setItems(getPacientes());
        
        dtpFecha = new DatePicker(Locale.ENGLISH);
        dtpFecha.setDateFormat(new  SimpleDateFormat ("yyyy-MM-dd"));
        dtpFecha.getCalendarView().todayButtonTextProperty().set("Today");
        grpFecha.add(dtpFecha, 0, 0);
    }
        public void cargarDatos(){        
        tblControlCitas.setItems(getControlCitas() );
        colControlCitas.setCellValueFactory(new PropertyValueFactory <ControlCitas, Integer>("codigoControlCita") );
        colFecha.setCellValueFactory(new PropertyValueFactory <ControlCitas, Date>("fecha") );
        colHoraInicio.setCellValueFactory(new PropertyValueFactory <ControlCitas, String>("horaInicio") );
        colHoraFin.setCellValueFactory(new PropertyValueFactory <ControlCitas, String>("horaFin") );
        colCodigoMedico.setCellValueFactory(new PropertyValueFactory <Medico, Integer>("codigoMedico") );
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory <Pacientes, Integer>("codigoPaciente") );
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
                
        public ObservableList <Pacientes> getPacientes(){
          
        ArrayList <Pacientes> lista = new ArrayList<Pacientes>();
        try{ PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarPacientes}");
             ResultSet resultado = procedimiento.executeQuery();    
              while(resultado.next()){
        lista.add(new Pacientes(resultado.getInt("codigoPaciente"),
                resultado.getString("DPI"),
                resultado.getString("apellidos"),
                resultado.getString("nombres"),
                resultado.getDate("fechaNacimiento"),
                resultado.getInt("edad"),
                resultado.getString("direccion"),
                resultado.getString("ocupacion"),
                resultado.getString("sexo")));
        }
    }catch(SQLException e){
    e.printStackTrace();
            }
   return listarPacientes = FXCollections.observableList(lista);
    } 
        
        
        
        public ControlCitas buscarControlCitas(int codigoControlCita)  {
        ControlCitas resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarControlCitas(?)}");
            procedimiento.setInt(1, codigoControlCita);
            ResultSet registro = procedimiento.executeQuery();
            
           while (registro.next()){
               resultado = new ControlCitas(registro.getInt("codigoControlCita"),registro.getDate("fecha"),registro.getString("horaInicio"),
                            registro.getString("horaFin"),registro.getInt("codigoMedico"),registro.getInt("codigoPaciente"));
               
           }
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        
        return resultado;
    }


        public void seleccionar (){
        cmbControlCitas.getSelectionModel().select(buscarControlCitas(( ( ControlCitas) tblControlCitas.getSelectionModel().getSelectedItem()).getCodigoControlCita())); 
        dtpFecha.selectedDateProperty().set(((ControlCitas) tblControlCitas.getSelectionModel().getSelectedItem()).getFecha());
        txtHoraInicio.setText(( (ControlCitas) tblControlCitas.getSelectionModel().getSelectedItem()).getHoraInicio());
        txtHoraFin.setText(( (ControlCitas) tblControlCitas.getSelectionModel().getSelectedItem()).getHoraFin());
        cmbCodigoMedico.getSelectionModel().select(buscarControlCitas(( ( ControlCitas) tblControlCitas.getSelectionModel().getSelectedItem()).getCodigoMedico()));
        cmbCodigoPaciente.getSelectionModel().select(buscarControlCitas(( ( ControlCitas) tblControlCitas.getSelectionModel().getSelectedItem()).getCodigoPaciente())); 
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
        cmbControlCitas.setDisable(false);
        dtpFecha.setDisable(false);
        txtHoraInicio.setDisable(false);
        txtHoraFin.setDisable(false);
        cmbCodigoMedico.setDisable(false);
        cmbCodigoPaciente.setDisable(false);

        cmbControlCitas.setEditable(true);
        txtHoraInicio.setEditable(true);
        txtHoraFin.setEditable(true);
        
    }
    
    public void limpiar(){
        txtHoraInicio.setText("");
        txtHoraFin.setText("");
        cmbCodigoMedico.getSelectionModel().clearSelection();
        cmbCodigoPaciente.getSelectionModel().clearSelection();
    }     
    
    public void desactivar(){
        cmbControlCitas.setDisable(true);
        dtpFecha.setDisable(true);
        txtHoraInicio.setDisable(true);
        txtHoraFin.setDisable(true);
        cmbCodigoMedico.setDisable(true);
        cmbCodigoPaciente.setDisable(true);

        cmbControlCitas.setEditable(false);
        txtHoraInicio.setEditable(false);
        txtHoraFin.setEditable(false);
        cmbCodigoMedico.setEditable(false);
        cmbCodigoPaciente.setEditable(false);
    }
    
    public void ingresar(){
        ControlCitas registro = new ControlCitas();
        registro.setFecha(dtpFecha.getSelectedDate());
        registro.setHoraInicio(txtHoraInicio.getText());
        registro.setHoraFin(txtHoraFin.getText());
        registro.setCodigoMedico(((Medico)cmbCodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
        registro.setCodigoPaciente(((Pacientes)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
        
    
     try{
        PreparedStatement  procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarControlCitas(?,?,?,?,?)}");
        procedimiento.setDate(1, new java.sql.Date (registro.getFecha().getTime()));
        procedimiento.setString(2, registro.getHoraInicio());
        procedimiento.setString(3, registro.getHoraFin());
        procedimiento.setInt(4, registro.getCodigoMedico());
        procedimiento.setInt(5, registro.getCodigoPaciente());
        procedimiento.execute();
        listarControlCita.add(registro);
        
        
    }catch(SQLException e){
        e.printStackTrace();
    }
    
}
    
     public void edit(){
         switch(tipoDeOperacion){
             case Ninguno:
                 if(tblControlCitas.getSelectionModel().getSelectedItem() != null){
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
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarControlCitas (?,?,?,?,?,?)}");
             ControlCitas registro = (ControlCitas) tblControlCitas.getSelectionModel().getSelectedItem();
             registro.setCodigoControlCita(((ControlCitas)cmbControlCitas.getSelectionModel().getSelectedItem()).getCodigoControlCita());
             registro.setFecha(dtpFecha.getSelectedDate());
             registro.setHoraFin(txtHoraInicio.getText());
             registro.setHoraFin(txtHoraFin.getText());
             registro.setCodigoMedico(((Medico)cmbCodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
             registro.setCodigoPaciente(((Pacientes)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
             
            
             procedimiento.setInt(1, registro.getCodigoControlCita());
             procedimiento.setDate(2,new java.sql.Date (registro.getFecha().getTime()));
             procedimiento.setString(3, registro.getHoraInicio());
             procedimiento.setString(4, registro.getHoraFin());
             procedimiento.setInt(5, registro.getCodigoMedico());
             procedimiento.setInt(6, registro.getCodigoPaciente());
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
        if(tblControlCitas.getSelectionModel().getSelectedItem() !=null){
            int respuesta = JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar?", "Eliminar Control de Cita", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_NO_OPTION){
                
            try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarControlCitas(?)}");
                    procedimiento.setInt(1,((ControlCitas)tblControlCitas.getSelectionModel().getSelectedItem()).getCodigoControlCita());
                    procedimiento.execute();
                    listarControlCita.remove(tblControlCitas.getSelectionModel().getSelectedIndex());
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
    
    public void imprimirReporte(){
    if(tblControlCitas.getSelectionModel().getSelectedItem()!= null){
    int codigoControlCita = ((ControlCitas)tblControlCitas.getSelectionModel().getSelectedItem()).getCodigoControlCita();
    Map parametros = new HashMap();
    parametros.put("p_codigoControlCita", codigoControlCita);
    GenerarReporte.mostrarReporte("ReportControlCita.jasper", "ReporteControlCitas", parametros);
    
 }else{
                    JOptionPane.showMessageDialog(null," Debe Seleccionar un Elemento");
                }
 
 }
}    


