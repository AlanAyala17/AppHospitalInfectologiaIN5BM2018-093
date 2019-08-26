package org.alanayala.bean;

public class RegistrarUsuario {
    private int codigoUsuario; 
    private String usuarioLogin; 
    private String usuarioContrasena; 
    private Boolean usuarioEstado; 
    private String usuarioFecha; 
    private String usuarioHora; 
    private int codigoTipoUsuario; 

    public RegistrarUsuario() {
    }

    public RegistrarUsuario(int codigoUsuario, String usuarioLogin, String usuarioContrasena, Boolean usuarioEstado, String usuarioFecha, String usuarioHora, int codigoTipoUsuario) {
        this.codigoUsuario = codigoUsuario;
        this.usuarioLogin = usuarioLogin;
        this.usuarioContrasena = usuarioContrasena;
        this.usuarioEstado = usuarioEstado;
        this.usuarioFecha = usuarioFecha;
        this.usuarioHora = usuarioHora;
        this.codigoTipoUsuario = codigoTipoUsuario;
    }



    public int getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(int codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getUsuarioLogin() {
        return usuarioLogin;
    }

    public void setUsuarioLogin(String usuarioLogin) {
        this.usuarioLogin = usuarioLogin;
    }

    public String getUsuarioContrasena() {
        return usuarioContrasena;
    }

    public void setUsuarioContrasena(String usuarioContrasena) {
        this.usuarioContrasena = usuarioContrasena;
    }

    public Boolean getUsuarioEstado() {
        return usuarioEstado;
    }

    public void setUsuarioEstado(Boolean usuarioEstado) {
        this.usuarioEstado = usuarioEstado;
    }

    public String getUsuarioFecha() {
        return usuarioFecha;
    }

    public void setUsuarioFecha(String usuarioFecha) {
        this.usuarioFecha = usuarioFecha;
    }

    public String getUsuarioHora() {
        return usuarioHora;
    }

    public void setUsuarioHora(String usuarioHora) {
        this.usuarioHora = usuarioHora;
    }

    public int getCodigoTipoUsuario() {
        return codigoTipoUsuario;
    }

    public void setCodigoTipoUsuario(int codigoTipoUsuario) {
        this.codigoTipoUsuario = codigoTipoUsuario;
    }
    
        @Override
    public String toString() {
        return " " + codigoUsuario ;
    }
    
 
}
