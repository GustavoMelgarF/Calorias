package gustavo.calorias.clases;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import gustavo.calorias.R;
import gustavo.calorias.nuevoRegistro;

public class MyAdapter  extends RecyclerView.Adapter<MyViewHolder> {

    Context c;
    ArrayList<registro> listado;

    public MyAdapter(Context c, ArrayList<registro> listado) {
        this.c = c;
        this.listado = listado;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.model,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        //BIND DATA
        holder.nombre.setText(listado.get(position).getNombre());
        holder.notas.setText(listado.get(position).getNombre());
        holder.calorias.setText(String.valueOf(listado.get(position).getCalorias()));
        holder.fechaHora.setText(String.valueOf(listado.get(position).getFecha())+" "+String.valueOf(listado.get(position).getHora()));
        holder.imageView.setImageBitmap(RotateBitmap(BitmapFactory.decodeFile(listado.get(position).getRutaImagen())));

        holder.marco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, nuevoRegistro.class);
                intent.putExtra("editar", listado.get(position));
                c.startActivity(intent);
            }
        });

    }

    private static Bitmap RotateBitmap(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    public int getItemCount() {
        return listado.size();
    }
}