package gustavo.calorias.BaseDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import gustavo.calorias.Objetos.registro;


/**
 * Created by gmelgar on 24/02/2016.
 */
public class BaseDatos extends SQLiteOpenHelper {

    private static BaseDatos instanciaBaseDatos;

    private static final String DATABASE_NAME = "calorias_db";
    private static final String DATABASE_TABLE_REGISTRO = "calorias";
    private static final int DATABASE_VERSION = 1;

    private static final String REGISTRO_COLUMNA_ID = "_id";
    private static final String REGISTRO_COLUMNA_NOMBRE = "nombre";
    private static final String REGISTRO_COLUMNA_NOTAS = "notas";
    private static final String REGISTRO_COLUMNA_CALORIAS= "calorias";
    private static final String REGISTRO_COLUMNA_FECHA = "fecha";
    private static final String REGISTRO_COLUMNA_HORA = "Hora";
    private static final String REGISTRO_COLUMNA_RUTAIMAGEN = "rutaImagen";

    private static Context context_;


    public static synchronized BaseDatos obtenerInstancia(Context context) {
        if (instanciaBaseDatos == null) {
            instanciaBaseDatos = new BaseDatos(context.getApplicationContext());
            context_ = context;
        }
        return instanciaBaseDatos;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table " + DATABASE_TABLE_REGISTRO + " (" +
                        REGISTRO_COLUMNA_ID + " integer primary key autoincrement not null, " +
                        REGISTRO_COLUMNA_NOMBRE + " text not null, " +
                        REGISTRO_COLUMNA_NOTAS + " text not null, " +
                        REGISTRO_COLUMNA_CALORIAS + " integer not null, " +
                        REGISTRO_COLUMNA_FECHA + " text not null, " +
                        REGISTRO_COLUMNA_HORA + " text not null, " +
                        REGISTRO_COLUMNA_RUTAIMAGEN + " text not null " +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_REGISTRO);
        onCreate(sqLiteDatabase);
    }

    private BaseDatos(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public boolean insertarRegistro(registro registro){

        SQLiteDatabase db = this.getWritableDatabase();

        Boolean exito = false;
        Long id_creado;

        ContentValues contentValues = new ContentValues();
        contentValues.put(REGISTRO_COLUMNA_NOMBRE, registro.getNombre());
        contentValues.put(REGISTRO_COLUMNA_NOTAS, registro.getNotas());
        contentValues.put(REGISTRO_COLUMNA_CALORIAS, registro.getCalorias());
        contentValues.put(REGISTRO_COLUMNA_FECHA, registro.getFecha());
        contentValues.put(REGISTRO_COLUMNA_HORA, registro.getHora());
        contentValues.put(REGISTRO_COLUMNA_RUTAIMAGEN, registro.getRutaImagen());

        try{
            id_creado = db.insert(DATABASE_TABLE_REGISTRO, null, contentValues);
        }catch (Exception e){
            id_creado = null;
        }

        if (id_creado != null)
            if (id_creado > 0)
                exito = true;

        return exito;
    }



    public boolean actualizar_Registro(registro registro){
        SQLiteDatabase db = this.getWritableDatabase();

        Boolean exito;

        String filtro = "_id=" + String.valueOf(registro.getId());

        ContentValues contentValues = new ContentValues();
        contentValues.put(REGISTRO_COLUMNA_ID, registro.getId());
        contentValues.put(REGISTRO_COLUMNA_NOMBRE, registro.getNombre());
        contentValues.put(REGISTRO_COLUMNA_NOTAS, registro.getNotas());
        contentValues.put(REGISTRO_COLUMNA_CALORIAS, registro.getCalorias());
        contentValues.put(REGISTRO_COLUMNA_FECHA, registro.getFecha());
        contentValues.put(REGISTRO_COLUMNA_HORA, registro.getHora());
        contentValues.put(REGISTRO_COLUMNA_RUTAIMAGEN, registro.getRutaImagen());


        db.update(DATABASE_TABLE_REGISTRO, contentValues, filtro, null);

        try{
            db.update(DATABASE_TABLE_REGISTRO, contentValues, filtro, null);
            exito = true;
        }catch (Exception e){
            exito = false;
        }

        db.close();

        return exito;
    }

    public ArrayList<registro> obtenerRegistrosPorFecha(String fechaBuscar){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<registro> listaRegistros = new ArrayList<>();


        Cursor cursor = db.query(
                DATABASE_TABLE_REGISTRO,
                new String[]{
                        REGISTRO_COLUMNA_ID,
                        REGISTRO_COLUMNA_NOMBRE,
                        REGISTRO_COLUMNA_NOTAS,
                        REGISTRO_COLUMNA_CALORIAS,
                        REGISTRO_COLUMNA_FECHA,
                        REGISTRO_COLUMNA_HORA,
                        REGISTRO_COLUMNA_RUTAIMAGEN
                },
                "fecha = ?",
                new String[]{fechaBuscar},
                null /* groupBy */,
                null /* having */,
                REGISTRO_COLUMNA_HORA+" DESC"
        );

        if (cursor.moveToFirst()){
            do {
                registro registro = new registro();
                registro.setId(cursor.getInt(cursor.getColumnIndex(REGISTRO_COLUMNA_ID)));
                registro.setNombre(cursor.getString(cursor.getColumnIndex(REGISTRO_COLUMNA_NOMBRE)));
                registro.setNotas(cursor.getString(cursor.getColumnIndex(REGISTRO_COLUMNA_NOTAS)));
                registro.setCalorias(cursor.getInt(cursor.getColumnIndex(REGISTRO_COLUMNA_CALORIAS)));
                registro.setFecha(cursor.getString(cursor.getColumnIndex(REGISTRO_COLUMNA_FECHA)));
                registro.setHora(cursor.getString(cursor.getColumnIndex(REGISTRO_COLUMNA_HORA)));
                registro.setRutaImagen(cursor.getString(cursor.getColumnIndex(REGISTRO_COLUMNA_RUTAIMAGEN)));

                listaRegistros.add(registro);

            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listaRegistros;
    }

    public int obtenerCaloriasTotalesDia(String fechaBuscar){
        SQLiteDatabase db = this.getReadableDatabase();
        int caloriasDevolver = 0;


        Cursor cursor = db.query(
                DATABASE_TABLE_REGISTRO,
                new String[]{
                        REGISTRO_COLUMNA_ID,
                        REGISTRO_COLUMNA_NOMBRE,
                        REGISTRO_COLUMNA_NOTAS,
                        REGISTRO_COLUMNA_CALORIAS,
                        REGISTRO_COLUMNA_FECHA,
                        REGISTRO_COLUMNA_HORA,
                        REGISTRO_COLUMNA_RUTAIMAGEN
                },
                "fecha = ?",
                new String[]{fechaBuscar},
                null /* groupBy */,
                null /* having */,
                REGISTRO_COLUMNA_HORA+" DESC"
        );

        if (cursor.moveToFirst()){
            do {
                caloriasDevolver += cursor.getInt(cursor.getColumnIndex(REGISTRO_COLUMNA_CALORIAS));

            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return caloriasDevolver;
    }

    public Boolean eliminarRegistroPorId(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Boolean resultado = false;

        Cursor cursor = db.query(
                DATABASE_TABLE_REGISTRO,
                new String[]{
                        REGISTRO_COLUMNA_ID,
                        REGISTRO_COLUMNA_NOMBRE,
                        REGISTRO_COLUMNA_NOTAS,
                        REGISTRO_COLUMNA_CALORIAS,
                        REGISTRO_COLUMNA_FECHA,
                        REGISTRO_COLUMNA_HORA,
                        REGISTRO_COLUMNA_RUTAIMAGEN
                },
                "_id = ?",
                new String[]{String.valueOf(id)},
                null /* groupBy */,
                null /* having */,
                null /* orderBy */
        );

        if (cursor.moveToFirst()){
            String filtro = "_id=" + String.valueOf(id);

            db.delete(DATABASE_TABLE_REGISTRO, filtro, null);
            resultado = true;
        }

        cursor.close();
        db.close();

        return resultado;
    }


}

