package us.mis.acmeexplorer;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class Usuario implements Parcelable {

    private Usuario usuario;
    private String id;
    private String foto;
    private String nombre;
    private String apellidos;
    private String correo;
    private List<Viaje> viajesFavoritos = new ArrayList<>();

    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

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

    public Usuario() {
    }
    public Usuario(UsuarioDTO usuarioDTO) {
        this.id = usuarioDTO.getId();
        this.nombre = usuarioDTO.getNombre();
        this.apellidos = usuarioDTO.getApellidos();
        this.correo = usuarioDTO.getCorreo();
        this.foto = usuarioDTO.getFoto();
        this.viajesFavoritos = usuarioDTO.getViajesFavoritos();
    }

    public Usuario(FirebaseUser firebaseUsuario) {
        this.id = firebaseUsuario.getUid();
        this.correo = firebaseUsuario.getEmail();
        this.nombre = firebaseUsuario.getDisplayName();
        if(firebaseUsuario.getPhotoUrl() != null) {
            this.foto = firebaseUsuario.getPhotoUrl().toString();
        }
    }

    private Usuario(Parcel in) {
        id = in.readString();
        nombre = in.readString();
        apellidos = in.readString();
        correo = in.readString();
        foto = in.readString();
        in.readList(this.viajesFavoritos, Viaje.class.getClassLoader());
        usuario = in.readParcelable(Usuario.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(nombre);
        out.writeString(apellidos);
        out.writeString(correo);
        out.writeString(foto);
        out.writeList(viajesFavoritos);
        out.writeParcelable(usuario, flags);
    }

   /* private void writeMapAsBundle(Parcel dest, Map<String, String> map) {
        Bundle bundle = new Bundle();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                bundle.putSerializable(entry.getKey(), entry.getValue());
            }
        }
        dest.writeBundle(bundle);
    }


    private void readMapFromBundle(Parcel in, Map<String, String> map, ClassLoader keyClassLoader) {
        Bundle bundle = in.readBundle(keyClassLoader);
        for (String key : bundle.keySet()) {
            map.put(key, bundle.getString(key));
        }
    }*/


}
