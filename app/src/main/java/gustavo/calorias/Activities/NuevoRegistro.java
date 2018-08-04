package gustavo.calorias.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import gustavo.calorias.BaseDatos.BaseDatos;
import gustavo.calorias.Objetos.registro;
import gustavo.calorias.R;

public class NuevoRegistro extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_CODE = 101;
    ImageView imageView;
    EditText etNombre, etNotas, etCalorias;
    String ruta;
    Uri imageUri;

    registro editar;

    private registro nuevoRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_registro);

        editar = getIntent().getParcelableExtra("editar");

        imageView = findViewById(R.id.imageView);
        etNombre = findViewById(R.id.etNombre);
        etNotas = findViewById(R.id.etNotas);
        etCalorias = findViewById(R.id.etCalorias);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);

                } else if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_PERMISSION_CODE);

                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    try {
                        imageUri = FileProvider.getUriForFile(
                                NuevoRegistro.this,
                                getApplicationContext().getPackageName() + "gustavo.calorias.clases.GenericFileProvider",
                                createImageFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
        Button btnAlmacenar = findViewById(R.id.btnAlmacenar);

        btnAlmacenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editar.getNombre().equals("")) {
                    if (!etNombre.getText().toString().equals("") &&
                            !etNotas.getText().toString().equals("") &&
                            !etCalorias.getText().toString().equals("") &&
                            !ruta.equals("")) {
                        nuevoRegistro = new registro(
                                etNombre.getText().toString(),
                                etNotas.getText().toString(),
                                Integer.parseInt(etCalorias.getText().toString()),
                                ruta
                        );
                        BaseDatos.obtenerInstancia(getApplicationContext()).insertarRegistro(nuevoRegistro);
                        Intent intent = new Intent(NuevoRegistro.this, Principal.class);
                        startActivity(intent);
                    }
                }else{
                    if (!etNombre.getText().toString().equals("") &&
                            !etNotas.getText().toString().equals("") &&
                            !etCalorias.getText().toString().equals("") &&
                            !ruta.equals("")) {
                        nuevoRegistro = new registro(
                                etNombre.getText().toString(),
                                etNotas.getText().toString(),
                                Integer.parseInt(etCalorias.getText().toString()),
                                ruta,
                                editar.getFecha(),
                                editar.getHora(),
                                editar.getId()
                        );
                        BaseDatos.obtenerInstancia(getApplicationContext()).actualizar_Registro(nuevoRegistro);
                        Intent intent = new Intent(NuevoRegistro.this, Principal.class);
                        startActivity(intent);
                    }
                }
            }
        });

        if (!editar.getNombre().equals("")){
            etNombre.setText(editar.getNombre());
            etCalorias.setText(String.valueOf(editar.getCalorias()));
            etNotas.setText(editar.getNotas());
            imageView.setImageBitmap(RotateBitmap(BitmapFactory.decodeFile(editar.getRutaImagen())));
        }
    }

    private static Bitmap RotateBitmap(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "read external granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }

    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(ruta);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);

            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = managedQuery(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection, null, null, null);
            int column_index_data = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToLast();

            ruta = cursor.getString(column_index_data);
            galleryAddPic();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.FRANCE).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        ruta = "file:" + image.getAbsolutePath();
        return image;
    }




}
