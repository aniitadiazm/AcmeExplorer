package us.mis.acmeexplorer;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public class ViajeDTO {

    private String id;
    private String url;
    private String salida;
    private String destino;
    private String inicio;
    private String fin;
    private Long precio;
    private String regimen;
    private List<String> incluido;
    private Boolean favorito;
    private Double latitud;
    private Double longitud;

    public ViajeDTO(){
    }

    public ViajeDTO(Viaje viaje) {
        this.id = viaje.getId();
        this.salida = viaje.getSalida();
        this.destino = viaje.getDestino();
        this.url = viaje.getUrl();
        this.inicio = viaje.getInicio().get(Calendar.YEAR) + "-" + viaje.getInicio().get(Calendar.MONTH) + "-" + viaje.getInicio().get(Calendar.DAY_OF_MONTH);
        this.fin = viaje.getFin().get(Calendar.YEAR) + "-" + viaje.getFin().get(Calendar.MONTH) + "-" + viaje.getFin().get(Calendar.DAY_OF_MONTH);
        this.precio = viaje.getPrecio();
        this.regimen = viaje.getRegimen();
        this.incluido = viaje.getIncluido();
        this.latitud = viaje.getLatitud();
        this.longitud = viaje.getLongitud();
        this.favorito = viaje.isFavorito();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getSalida() { return salida; }
    public void setSalida(String salida) { this.salida = salida; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
    public String getInicio() { return inicio; }
    public void setInicio(String inicio) { this.inicio = inicio; }
    public String getFin() { return fin; }
    public void setFin(String fin) { this.fin = fin; }
    public Long getPrecio() { return precio; }
    public void setPrecio(Long precio) { this.precio = precio; }
    public String getRegimen() { return regimen; }
    public void setRegimen(String regimen) { this.regimen = regimen; }
    public List<String> getIncluido() { return incluido; }
    public void setIncluido(List<String> incluido) { this.incluido = incluido; }
    public boolean isFavorito() { return favorito; }
    public void setFavorito(boolean favorito) { this.favorito = favorito; }
    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    @Override
    public boolean equals(Object object) {

        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        ViajeDTO viajeDTO = (ViajeDTO) object;

        return Objects.equals(id, viajeDTO.id) &&
                Objects.equals(salida, viajeDTO.salida) &&
                Objects.equals(destino, viajeDTO.destino) &&
                Objects.equals(regimen, viajeDTO.regimen) &&
                Objects.equals(incluido, viajeDTO.incluido) &&
                Objects.equals(url, viajeDTO.url) &&
                Objects.equals(precio, viajeDTO.precio) &&
                Objects.equals(inicio, viajeDTO.inicio) &&
                Objects.equals(fin, viajeDTO.fin) &&
                Objects.equals(latitud, viajeDTO.latitud) &&
                Objects.equals(longitud, viajeDTO.longitud) &&
                Objects.equals(favorito, viajeDTO.favorito);


    }

    @Override
    public int hashCode() {
        return Objects.hash(id, salida, destino, regimen, incluido, url, precio, inicio, fin, latitud, longitud, favorito);
    }

    @Override
    public String toString() {
        return "ViajeDTO{" +
                "id=" + id +
                ", salida='" + salida + '\'' +
                ", destino='" + destino + '\'' +
                ", regimen='" + regimen + '\'' +
                ", incluido='" + incluido + '\'' +
                ", url='" + url + '\'' +
                ", precio='" + precio + '\'' +
                ", inicio='" + inicio + '\'' +
                ", fin=" + fin + '\'' +
                ", latitud='" + latitud + '\'' +
                ", longitud='" + longitud + '\'' +
                ", favorito='" + favorito + '\'' +
                '}';
    }

}

