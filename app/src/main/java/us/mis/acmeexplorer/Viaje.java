package us.mis.acmeexplorer;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;

public class Viaje implements Parcelable {

    private Viaje viaje;
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
    private Viaje(String id, String salida, String destino, String regimen, List<String> incluido, String url, Long precio, Calendar inicio,
                  Calendar fin, Double latitud, Double longitud, Boolean favorito) {
        this.id = id;
        this.salida = salida;
        this.destino = destino;
        this.regimen = regimen;
        this.incluido = incluido;
        this.url = url;
        this.precio = precio;
        this.inicio = inicio;
        this.fin = fin;
        this.latitud = latitud;
        this.longitud = longitud;
        this.favorito = favorito;
    }

    public Viaje(ViajeDTO viajeDTO) {
        this.id = viajeDTO.getId();
        this.salida = viajeDTO.getSalida();
        this.destino = viajeDTO.getDestino();
        this.regimen = viajeDTO.getRegimen();
        this.incluido = viajeDTO.getIncluido();
        this.url = viajeDTO.getUrl();
        this.precio = viajeDTO.getPrecio();
        String[] inicioDTOarray = viajeDTO.getInicio().split("-");
        String[] finDTOarray = viajeDTO.getFin().split("-");
        Calendar inicioDTO = Calendar.getInstance();
        Calendar endDateDTO = Calendar.getInstance();
        inicioDTO.set(Integer.parseInt(inicioDTOarray[0]),Integer.parseInt(inicioDTOarray[1]),Integer.parseInt(inicioDTOarray[2]));
        endDateDTO.set(Integer.parseInt(finDTOarray[0]),Integer.parseInt(finDTOarray[1]),Integer.parseInt(finDTOarray[2]));
        this.inicio = inicioDTO;
        this.fin = endDateDTO;
        this.latitud = viajeDTO.getLatitud();
        this.longitud = viajeDTO.getLongitud();
        this.favorito = viajeDTO.isFavorito();
    }

    protected Viaje(Parcel in) {
        id = in.readString();
        salida = in.readString();
        destino = in.readString();
        regimen = in.readString();
        incluido = in.createStringArrayList();
        url = in.readString();
        precio = in.readLong();
        long startDateMiliseconds = in.readLong();
        String startDateTimeZone = in.readString();
        long endDateMiliseconds = in.readLong();
        String endDateTimeZone = in.readString();
        if (startDateMiliseconds != 1L) {
            inicio = new GregorianCalendar(TimeZone.getTimeZone(startDateTimeZone));
            inicio.setTimeInMillis(startDateMiliseconds);
        }
        if (endDateMiliseconds != 1L) {
            fin = new GregorianCalendar(TimeZone.getTimeZone(endDateTimeZone));
            fin.setTimeInMillis(endDateMiliseconds);
        }
        latitud = in.readDouble();
        longitud = in.readDouble();
        favorito = in.readBoolean();
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
                    DatosViajes.regimen[regimen],
                    incluido,
                    DatosViajes.urlFotos[destino],
                    Long.valueOf(precio),
                    inicioRandom,
                    finRandom,
                    DatosViajes.latitudes[destino],
                    DatosViajes.longitudes[destino],
                    favorito));
        }

        return viajes;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Viaje viaje = (Viaje) o;

        return Objects.equals(id, viaje.id) &&
                Objects.equals(salida, viaje.salida) &&
                Objects.equals(destino, viaje.destino) &&
                regimen.equals(viaje.regimen) &&
                incluido.equals(viaje.incluido) &&
                url.equals(viaje.url) &&
                Objects.equals(precio, viaje.precio) &&
                Objects.equals(inicio, viaje.inicio) &&
                Objects.equals(fin, viaje.fin) &&
                latitud == viaje.latitud &&
                longitud == viaje.longitud &&
                favorito == viaje.favorito;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, salida, destino, regimen, incluido, url, precio, inicio, fin, latitud, longitud, favorito);
    }

    @Override
    public String toString() {
        return "Viaje{" +
                "id=" + id +
                ", salida='" + salida + '\'' +
                ", destino='" + destino + '\'' +
                ", regimen='" + regimen + '\'' +
                ", incluido='" + incluido + '\'' +
                ", url='" + url + '\'' +
                ", precio=" + precio +
                ", inicio=" + inicio.get(Calendar.DAY_OF_MONTH) + "/" + inicio.get(Calendar.MONTH) + "/" + inicio.get(Calendar.YEAR) +
                ", fin=" + fin.get(Calendar.DAY_OF_MONTH) + "/" +  fin.get(Calendar.MONTH) + "/" + fin.get(Calendar.YEAR) +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
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
        out.writeList(incluido);
        out.writeString(url);
        out.writeLong(precio);
        out.writeSerializable(inicio);
        out.writeSerializable(fin);
        out.writeLong(inicio.getTimeInMillis());
        out.writeString(inicio.getTimeZone().getID());
        out.writeLong(fin.getTimeInMillis());
        out.writeString(fin.getTimeZone().getID());
        out.writeDouble(latitud);
        out.writeDouble(longitud);
        out.writeBoolean(favorito);
    }

}
