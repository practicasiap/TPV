package com.cifpvdg.cifpvirgendegracia.tpv.Actualizacion;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.cifpvdg.cifpvirgendegracia.tpv.ClasesBD.Producto;
import com.cifpvdg.cifpvirgendegracia.tpv.R;
import com.cifpvdg.cifpvirgendegracia.tpv.RFoto.GestionFoto;
import com.cifpvdg.cifpvirgendegracia.tpv.RVideo.CapturaVideo;

import java.util.concurrent.ExecutionException;

public class ActualizadoActivity extends AppCompatActivity {

    private Producto prod;
    private Button cambiante;
    private Button sumar;
    private Button restar;
    private EditText nombre;
    private EditText cod;
    private EditText codBarras;
    private EditText codPro;
    private EditText preC;
    private EditText preV;
    private EditText subCat;
    private EditText desc;
    private EditText descB;
    private EditText cantidad;
    private TextView lblCod;

    private static String url = "https://tpvdam2.000webhostapp.com/insert.php?";
    private static String url2 = "https://tpvdam2.000webhostapp.com/actualizarProducto.php?";

    private static final String[] PERMISO_CAMARA = new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int PERMISO_GARANTIZADO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizado);


        cambiante = (Button) findViewById(R.id.btnCambiante);
        sumar = (Button) findViewById(R.id.btnMas);
        restar = (Button) findViewById(R.id.btnMenos);
        lblCod = (TextView) findViewById(R.id.textView5);

        recuperarTexts();
        Intent i = getIntent();
        prod = (Producto) i.getSerializableExtra("prod");

        System.out.println("--------------------------------------------------------------------------------------" + prod.getCodigo() + "-------------------------------------------------------------------------------------------------------------");


        if (this.prod.getCodigo() != 0) {
            this.cambiante.setText(R.string.mod);
            this.cambiante.setTag("mod");
            this.cod.setVisibility(View.VISIBLE);
            this.lblCod.setVisibility(View.VISIBLE);
            LeerProducto();
            Toast.makeText(getApplicationContext(), "El producto ya existe, puede modificarlo", Toast.LENGTH_SHORT).show();
        } else {
            if(this.prod.getCodigo() == 0){
                this.cambiante.setText(R.string.ania);
                this.cambiante.setTag("an");
                this.cod.setVisibility(View.INVISIBLE);
                this.lblCod.setVisibility(View.INVISIBLE);
                LeerProducto();

            }

        }

        System.out.println("--------------------------------------------------------------------------------------" + cambiante.getTag() + "-------------------------------------------------------------------------------------------------------------");

        cambiante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag() == "mod"){
                    ModificarProducto(null);
                }else{
                    if(v.getTag() == "an"){
                        InsertarProdcuto(null);
                    }
                }
            }
        });
    }

    public void NuevaFoto(View view){
        Intent i = new Intent(this, GestionFoto.class);
        String cod = this.prod.getCod_barras()+"";
        i.putExtra("prod", cod);
        this.startActivity(i);
    }

    public void NuevoVideo(View view){
        if (this.isPermissionGranted()){
            this.iniciarVideoActivity();
        }
    }

    public void sumar(View view){
        String number = this.cantidad.getText().toString();
        int num;

        number = number.trim();

        if(number == ""){
            Toast.makeText(getApplicationContext(), "Por favor introduzca un valor", Toast.LENGTH_SHORT).show();
        }else{
            num = Integer.parseInt(number);
            num++;
            this.cantidad.setText(num + "");
        }
    }

    public void restar(View view){
        String number = this.cantidad.getText().toString();
        int num;

        number = number.trim();

        if(number == ""){
            Toast.makeText(getApplicationContext(), "Por favor introduzca un valor", Toast.LENGTH_SHORT).show();
        }else{
            num = Integer.parseInt(number);
            num--;
            this.cantidad.setText(num + "");
        }
    }

    public void InsertarProdcuto(View view) {

        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Añadir producto");
        dialogo.setMessage("¿Deseas añadir el producto?");
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {


                JSonParser js = new JSonParser(nombre.getText().toString(), Integer.parseInt(cantidad.getText().toString()), Double.parseDouble(preC.getText().toString()), Double.parseDouble(preV.getText().toString()), codBarras.getText().toString(), descB.getText().toString(), desc.getText().toString(), Integer.parseInt(codPro.getText().toString()), Integer.parseInt(subCat.getText().toString()));
                AsyncTask asyn = js.execute(url);

                String respuesta = null;

                try {
                    respuesta = (String) asyn.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("-------------------------------------------------------------------------" + respuesta + "--------------------------------------------------------------------------------------------------------------");

                if (respuesta.trim().contains("insertado")) {
                    Toast.makeText(getApplicationContext(), "Se ha añadido con exito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Insercion Fallida", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {

            }
        });
        dialogo.show();

    }

    public void ModificarProducto(View view) {

        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Modificar producto");
        dialogo.setMessage("¿Deseas modificar el producto?");
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {

                JSonParserActualizado js = new JSonParserActualizado(nombre.getText().toString(), Integer.parseInt(cantidad.getText().toString()), Double.parseDouble(preC.getText().toString()), Double.parseDouble(preV.getText().toString()), descB.getText().toString(), desc.getText().toString(), Integer.parseInt(codPro.getText().toString()), Integer.parseInt(subCat.getText().toString()), codBarras.getText().toString());
                AsyncTask asyn = js.execute(url2);

                String respuesta = null;

                try {
                    respuesta = (String) asyn.get();
                    System.out.println("-----------------------------------------------" + respuesta + "-------------------------------------------------------------------");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (respuesta.trim().contains("actualizado")) {
                    Toast.makeText(getApplicationContext(), "Se ha modificado con exito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Modificado Fallido", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialogo.show();
    }


    private void recuperarTexts() {
        nombre = (EditText) findViewById(R.id.txtNombre);
        cod = (EditText) findViewById(R.id.txtCod);
        codBarras = (EditText) findViewById(R.id.txtCodBar);
        codPro = (EditText) findViewById(R.id.txtCodProv);
        preC = (EditText) findViewById(R.id.txtPreC);
        preV = (EditText) findViewById(R.id.txtPreV);
        subCat = (EditText) findViewById(R.id.txtSubCa);
        desc = (EditText) findViewById(R.id.txtDesc);
        descB = (EditText) findViewById(R.id.txtDescB);
        cantidad = (EditText) findViewById(R.id.txtCantidad);
    }

    public void  LeerProducto() {

        if(this.prod.getCodigo() != 0){
            this.nombre.setText(this.prod.getNombre());
            this.cod.setText(this.prod.getCodigo() + "");
            this.codPro.setText(this.prod.getCod_pro_proveedero() + "");
            this.codBarras.setText(this.prod.getCod_barras() + "");
            this.cantidad.setText(this.prod.getCantidad() + "");
            this.preC.setText(this.prod.getPrecio_compra() + "");
            this.preV.setText(this.prod.getPrecio_venta() + "");
            this.desc.setText(this.prod.getDescripcion_larga());
            this.descB.setText(this.prod.getDescripcion_breve());
            this.subCat.setText(this.prod.getSubactegoria() + "");
        }else{
            if(this.prod.getCodigo() == 0){
                this.codBarras.setText(this.prod.getCod_barras() + "");
            }
        }
    }

    private boolean isPermissionGranted(){
        int versionSDK = Build.VERSION.SDK_INT;
        boolean retorno;
        if (versionSDK >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, PERMISO_CAMARA, PERMISO_GARANTIZADO);
                retorno =  false;
            }else{
                retorno = true;
            }
        }else{
            retorno = true;
        }

        return retorno;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PERMISO_GARANTIZADO == requestCode){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                this.iniciarVideoActivity();
            }
            return;
        }
    }

    private void iniciarVideoActivity(){
        Intent i = new Intent(this, CapturaVideo.class);
        i.putExtra("prod", this.prod);
        this.startActivity(i);
    }
}
