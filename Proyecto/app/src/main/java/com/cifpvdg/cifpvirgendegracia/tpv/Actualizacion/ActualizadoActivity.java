package com.cifpvdg.cifpvirgendegracia.tpv.Actualizacion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cifpvdg.cifpvirgendegracia.tpv.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ActualizadoActivity extends AppCompatActivity {

    private Producto prod;
    private int modo;
    private String codBar;
    private Button cambiante;
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

    private static String url = "https://tpvdam2.000webhostapp.com/insert.php";

    private static final String TAG_SUCCESS = "success";
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizado);

        cambiante = (Button) findViewById(R.id.btnCambiante);

        recuperarTexts();
        Intent i = getIntent();
        prod = (Producto) i.getSerializableExtra("product");

        if (this.prod.getCodigo() != 0) {
            this.cambiante.setText(R.string.mod);
            LeerProducto();
        } else {
            if(this.prod.getCodigo() == 0){
                this.cambiante.setText(R.string.ania);
                LeerProducto();
                InsertarProdcuto(null);
            }

        }
    }


    public void InsertarProdcuto(View view) {
        new Insertar().execute(nombre.getText().toString(), cantidad.getText().toString(), preC.getText().toString(), preV.getText().toString(), codBarras.getText().toString(), descB.getText().toString(), desc.getText().toString(), codPro.getText().toString(), subCat.getText().toString());
    }

    class Insertar extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ActualizadoActivity.this);
            pDialog.setMessage("Cargando el producto. Por favor, espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            JSonParser jParser = new JSonParser(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]);

            System.out.println(args[5]);

            //obtener el JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url);

            return null;
        }


        protected void onProgressUpdate(String... args) {

        }


        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
        }

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

        this.nombre.setText(this.prod.getNombre());
        this.cod.setText(this.prod.getCodigo() + "");
        this.codPro.setText(this.prod.getCodProdProvee() + "");
        this.codBarras.setText(this.prod.getCodBarras() + "");
        this.cantidad.setText(this.prod.getCantidad() + "");
        this.preC.setText(this.prod.getPrecioCompra() + "");
        this.preV.setText(this.prod.getPrecioVenta() + "");
        this.desc.setText(this.prod.getDescLarga());
        this.descB.setText(this.prod.getDescBreve());
        this.subCat.setText(this.prod.getSubCategoria() + "");

    }

}
