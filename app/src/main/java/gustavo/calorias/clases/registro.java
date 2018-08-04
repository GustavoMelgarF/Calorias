package gustavo.calorias.clases;


import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class registro implements Parcelable {

    private String nombre;

    private String notas;

    private int calorias;

    private String rutaImagen;

    private String fecha;

    private String hora;

    private Long id;

    public registro(String nombre, String notas, int calorias, String rutaImagen) {
        this.nombre = nombre;
        this.notas = notas;
        this.calorias = calorias;
        this.rutaImagen = rutaImagen;
        this.fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE).format(new Date());
        this.hora = new SimpleDateFormat("HH:mm:ss", Locale.FRANCE).format(new Date());
    }

    public registro(String nombre, String notas, int calorias, String rutaImagen, String fecha, String hora, Long id) {
        this.nombre = nombre;
        this.notas = notas;
        this.calorias = calorias;
        this.rutaImagen = rutaImagen;
        this.fecha = fecha;
        this.hora = hora;
        this.id = id;
    }

    public registro() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public int getCalorias() {
        return calorias;
    }

    public void setCalorias(int calorias) {
        this.calorias = calorias;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    protected registro(Parcel in) {
        nombre = in.readString();
        notas = in.readString();
        calorias = in.readInt();
        rutaImagen = in.readString();
        fecha = in.readString();
        hora = in.readString();
        id = in.readByte() == 0x00 ? null : in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(notas);
        dest.writeInt(calorias);
        dest.writeString(rutaImagen);
        dest.writeString(fecha);
        dest.writeString(hora);
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(id);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<registro> CREATOR = new Parcelable.Creator<registro>() {
        @Override
        public registro createFromParcel(Parcel in) {
            return new registro(in);
        }

        @Override
        public registro[] newArray(int size) {
            return new registro[size];
        }
    };
}
