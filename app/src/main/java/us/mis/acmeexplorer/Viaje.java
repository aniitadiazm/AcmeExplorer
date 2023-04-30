package us.mis.acmeexplorer;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Viaje implements Parcelable {

    private String id;
    private String url;
    private String salida;
    private String destino;
    private Calendar inicio;
    private Calendar fin;
    private Long precio;
    private String regimen;

    List<String> incluido;
    private boolean favorito;

    private Double latitud;
    private Double longitud;

    public static final Parcelable.Creator<Viaje> CREATOR = new Parcelable.Creator<Viaje>() {
        public Viaje createFromParcel(Parcel in) {
            return new Viaje(in);
        }

        public Viaje[] newArray(int size) {
            return new Viaje[size];
        }
    };

    public Viaje(){

    }
    private Viaje(String id, String salida, String destino, String url, Calendar inicio, Calendar fin,
                  String regimen, Double latitud, Double longitud, List<String> incluido, Long precio, Boolean favorito) {
        this.id = id;
        this.salida = salida;
        this.destino = destino;
        this.url = url;
        this.inicio = inicio;
        this.fin = fin;
        this.precio = precio;
        this.regimen = regimen;
        this.latitud = latitud;
        this.longitud = longitud;
        this.incluido = incluido;
        this.favorito = favorito;
    }

    public Viaje(ViajeDTO viajeDTO) {
        this.id = viajeDTO.getId();
        this.salida = viajeDTO.getSalida();
        this.destino = viajeDTO.getDestino();
        this.url = viajeDTO.getUrl();
        String[] inicioDTOarray = viajeDTO.getInicio().split("-");
        String[] finDTOarray = viajeDTO.getFin().split("-");
        Calendar inicioDTO = Calendar.getInstance();
        Calendar endDateDTO = Calendar.getInstance();
        inicioDTO.set(Integer.parseInt(inicioDTOarray[0]),Integer.parseInt(inicioDTOarray[1]),Integer.parseInt(inicioDTOarray[2]));
        endDateDTO.set(Integer.parseInt(finDTOarray[0]),Integer.parseInt(finDTOarray[1]),Integer.parseInt(finDTOarray[2]));
        this.inicio = inicioDTO;
        this.fin = endDateDTO;
        this.precio = viajeDTO.getPrecio();
        this.regimen = viajeDTO.getRegimen();
        this.incluido = viajeDTO.getIncluido();
        this.favorito = viajeDTO.isFavorito();
        this.latitud = viajeDTO.getLatitud();
        this.longitud = viajeDTO.getLongitud();
    }

    protected Viaje(Parcel in) {
        id = in.readString();
        url = in.readString();
        salida = in.readString();
        destino = in.readString();
        if (in.readByte() == 0) {
            precio = null;
        } else {
            precio = in.readLong();
        }
        regimen = in.readString();
        latitud = in.readDouble();
        longitud = in.readDouble();
        incluido = in.createStringArrayList();
        favorito = in.readByte() != 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSalida() {
        return salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Calendar getInicio() {
        return inicio;
    }

    public void setInicio(Calendar inicio) {
        this.inicio = inicio;
    }

    public Calendar getFin() {
        return fin;
    }

    public void setFin(Calendar fin) {
        this.fin = fin;
    }

    public Long getPrecio() {
        return precio;
    }

    public void setPrecio(Long precio) {
        this.precio = precio;
    }

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

    public static List<Viaje> generarViajes(int max){
        ArrayList<Viaje> viajes = new ArrayList<>(max);
        Random r = new Random();

        for (int i = 0; i < max; i++){
            int salida = r.nextInt(DatosViajes.salidas.length);
            int destino = r.nextInt(DatosViajes.destinos.length);
            int randomMes = r.nextInt(9)+3;
            int randomDia = r.nextInt(30)+1;
            int randomDias = r.nextInt(25-5)+5;

            Calendar inicioRandom = Calendar.getInstance();
            Calendar finRandom = Calendar.getInstance();

            inicioRandom.set(2023,randomMes, randomDia);
            finRandom.set(2023,randomMes, randomDia);
            finRandom.add(Calendar.DATE, randomDias);

            int regimen = r.nextInt(DatosViajes.regimen.length);

            List<String> incluido = new ArrayList<>(2);
            for(int j = 0; j < 3; j++) {
                int incluidoRandom = r.nextInt(12);
                String incluir = DatosViajes.incluido[incluidoRandom];
                if(!incluido.contains(incluir)) {
                    incluido.add(incluir);
                } else {
                    j--;
                }

            }

            int precio = r.nextInt(850) + 150;
            boolean favorito = false;

            viajes.add(new Viaje(String.valueOf(i+1),
                    DatosViajes.salidas[salida],
                    DatosViajes.destinos[destino],
                    DatosViajes.urlFotos[destino],
                    inicioRandom,
                    finRandom,
                    DatosViajes.regimen[regimen],
                    DatosViajes.latitudes[destino],
                    DatosViajes.longitudes[destino],
                    incluido,
                    Long.valueOf(precio),
                    favorito));
        }

        return viajes;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Viaje viaje = (Viaje) o;

        return favorito == viaje.favorito &&
                Objects.equals(id, viaje.id) &&
                Objects.equals(salida, viaje.salida) &&
                Objects.equals(destino, viaje.destino) &&
                regimen.equals(viaje.regimen) &&
                incluido.equals(viaje.incluido) &&
                url.equals(viaje.url) &&
                Objects.equals(inicio, viaje.inicio) &&
                Objects.equals(fin, viaje.fin) &&
                Objects.equals(precio, viaje.precio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, salida, destino,  inicio, fin, regimen, incluido, url, precio, favorito);
    }

    @Override
    public String toString() {
        return "Viaje{" +
                "id=" + id +
                ", salida='" + salida + '\'' +
                ", destino='" + destino + '\'' +
                ", inicio=" + inicio.get(Calendar.DAY_OF_MONTH) + "/" + inicio.get(Calendar.MONTH) + "/" + inicio.get(Calendar.YEAR) +
                ", fin=" + fin.get(Calendar.DAY_OF_MONTH) + "/" +  fin.get(Calendar.MONTH) + "/" + fin.get(Calendar.YEAR) +
                ", regimen='" + regimen + '\'' +
                ", incluido='" + incluido + '\'' +
                ", url='" + url + '\'' +
                ", precio=" + precio +
                ", favorito=" + favorito +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(salida);
        out.writeString(destino);
        out.writeString(regimen);
        out.writeStringList(incluido);
        out.writeString(url);
        if (inicio == null) {
            out.writeLong(1L);
            out.writeString("");
        } else {
            out.writeLong(inicio.getTimeInMillis());
            out.writeString(fin.getTimeZone().getID());
        }
        if (fin == null) {
            out.writeLong(1L);
            out.writeString("");
        } else {
            out.writeLong(inicio.getTimeInMillis());
            out.writeString(fin.getTimeZone().getID());
        }
        out.writeLong(precio);
    }

}
