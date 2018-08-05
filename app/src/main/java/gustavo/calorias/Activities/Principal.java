package gustavo.calorias.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import gustavo.calorias.BaseDatos.BaseDatos;
import gustavo.calorias.Adaptadores.MyAdapter;
import gustavo.calorias.Listeners.SwipeableRecyclerViewTouchListener;
import gustavo.calorias.Objetos.registro;
import gustavo.calorias.R;

public class Principal extends AppCompatActivity {

    RecyclerView recyclerView;
    MyAdapter adapter;
    ArrayList<registro> listado;
    int caloriasTotales = 0;
    int caloriasConsumidasDia = 0;
    TextView tv_consumidas, tv_quedan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nuevoRegistro();
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        caloriasTotales = Integer.parseInt(prefs.getString("calorias", "1600"));
        caloriasConsumidasDia = BaseDatos
                .obtenerInstancia(getApplicationContext())
                .obtenerCaloriasTotalesDia(
                        new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE).format(new Date())
                );

        tv_consumidas = findViewById(R.id.tvConsumidas);
        tv_quedan = findViewById(R.id.tv_quedan);

        tv_consumidas.setText(String.valueOf(caloriasConsumidasDia));
        tv_quedan.setText(String.valueOf(caloriasTotales-caloriasConsumidasDia));
        obtenerListadoHoy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, Preferencias.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void nuevoRegistro(){
        Intent intent = new Intent(this, NuevoRegistro.class);
        intent.putExtra("editar", new registro("", "", 0, ""));
        finish();
        startActivity(intent);
    }

    private void obtenerListadoHoy(){

        listado = BaseDatos
                .obtenerInstancia(getApplicationContext())
                .obtenerRegistrosPorFecha(
                        new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE).format(new Date())
                );
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //ADAPTER
        adapter=new MyAdapter(Principal.this,listado);
        recyclerView.setAdapter(adapter);

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {

                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                itemRemoved(reverseSortedPositions);
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                itemRemoved(reverseSortedPositions);
                            }
                        });

        recyclerView.addOnItemTouchListener(swipeTouchListener);
    }

    private void itemRemoved(int[] reverseSortedPositions){
        for (int position : reverseSortedPositions) {
            BaseDatos.obtenerInstancia(Principal.this).eliminarRegistroPorId((int)listado.get(position).getId());
            listado.remove(position);
            adapter.notifyItemRemoved(position);
        }
        adapter.notifyDataSetChanged();
    }

}
