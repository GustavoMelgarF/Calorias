package gustavo.calorias.clases;

import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import gustavo.calorias.R;

public class MyViewHolder  extends RecyclerView.ViewHolder {

    TextView nombre, notas, calorias, fechaHora;
    ImageView imageView;
    ConstraintLayout marco;

    public MyViewHolder(View itemView) {
        super(itemView);

        nombre= itemView.findViewById(R.id.nombre);
        notas=  itemView.findViewById(R.id.notas);
        calorias=  itemView.findViewById(R.id.calorias);
        fechaHora=  itemView.findViewById(R.id.fechaHora);
        imageView=  itemView.findViewById(R.id.imagen);
        marco=  itemView.findViewById(R.id.marco);

    }

}
