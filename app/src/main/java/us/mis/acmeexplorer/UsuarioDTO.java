package us.mis.acmeexplorer;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class UsuarioDTO {

    private String id;
    private String nombre;
    private String apellidos;
    private String correo;
    private String foto;
    private List<Viaje> viajesFavoritos = new ArrayList<>();

    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.apellidos = usuario.getApellidos();
        this.correo = usuario.getCorreo();
        this.foto = usuario.getFoto();
        this.viajesFavoritos = usuario.getViajesFavoritos();
    }

    public UsuarioDTO(){
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }

    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getCorreo() { return correo; }

    public void setCorreo(String correo) { this.correo = correo; }

    public String getFoto() { return foto; }

    public void setFoto(String foto) { this.foto = foto; }

    public List<Viaje> getViajesFavoritos() { return viajesFavoritos; }

    public void setViajesFavoritos(List<Viaje> viajesFavoritos) { this.viajesFavoritos = viajesFavoritos; }


}
