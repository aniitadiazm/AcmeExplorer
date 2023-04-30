package us.mis.acmeexplorer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseService {
    private static String usuarioId;
    private static FirebaseDatabaseService fService;
    private static FirebaseDatabase fDatabase;

    public static FirebaseDatabaseService getServiceInstance() {
        if (fService == null || fDatabase == null) {
            fService = new FirebaseDatabaseService();
            fDatabase = FirebaseDatabase.getInstance();
            fDatabase.setPersistenceEnabled(false);
        }

        if(usuarioId == null || usuarioId.isEmpty()) {
            usuarioId = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "";
        }
        return fService;
    }

    public DatabaseReference getViaje(String viajeId) {
        return fDatabase.getReference("viaje/" + viajeId).getRef();
    }

    public DatabaseReference getUsuario(String usuarioId) {
        return fDatabase.getReference("usuario/" + usuarioId).getRef();
    }

    public DatabaseReference getAllViajes() {
        return fDatabase.getReference("viaje").getRef();
    }

    public DatabaseReference getAllUsuarios() {
        return fDatabase.getReference("usuario").getRef();
    }

    public DatabaseReference getViajesFavoritosFromUsuario(String usuarioId) {
        return fDatabase.getReference("usuario/" + usuarioId + "/viajesFavoritos").getRef();
    }

    public void setViajeAsFavorito(String usuarioId, String viajeId, DatabaseReference.CompletionListener completionListener) {
        fDatabase.getReference("usuario/" + usuarioId + "/viajesFavoritos").child(viajeId).setValue(viajeId, completionListener);
    }

    public void setViajeAsNoFavorito(String usuarioId, String viajeId, DatabaseReference.CompletionListener completionListener) {
        fDatabase.getReference("usuario/" + usuarioId + "/viajesFavoritos").child(viajeId).removeValue(completionListener);
    }

    public void guardarViaje(Viaje viaje, DatabaseReference.CompletionListener completionListener) {
        ViajeDTO viajeDTO = new ViajeDTO(viaje);
        String viajeId = fDatabase.getReference("viaje").push().getKey();
        viaje.setId(viajeId);
        viajeDTO.setId(viajeId);

        fDatabase.getReference("viaje").child(viajeId).setValue(viajeDTO, completionListener);
    }
    public void guardarUsuario(Usuario usuario, DatabaseReference.CompletionListener completionListener) {
        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
        fDatabase.getReference("usuario").child(usuario.getId()).setValue(usuarioDTO, completionListener);
    }

}
