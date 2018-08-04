package gustavo.calorias.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import gustavo.calorias.BaseDatos.BaseDatos;
import gustavo.calorias.Adaptadores.MyAdapter;
import gustavo.calorias.Objetos.registro;
import gustavo.calorias.R;

public class Principal extends AppCompatActivity {

    RecyclerView recyclerView;

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, Preferencias.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void nuevoRegistro(){
        Intent intent = new Intent(this, NuevoRegistro.class);
        intent.putExtra("editar", new registro("", "", 0, ""));
        startActivity(intent);
    }

    private void obtenerListadoHoy(){

        ArrayList<registro> listado = BaseDatos
                .obtenerInstancia(getApplicationContext())
                .obtenerRegistrosPorFecha(
                        new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE).format(new Date())
                );
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //ADAPTER
        MyAdapter adapter=new MyAdapter(Principal.this,listado);
        recyclerView.setAdapter(adapter);
    }

}
