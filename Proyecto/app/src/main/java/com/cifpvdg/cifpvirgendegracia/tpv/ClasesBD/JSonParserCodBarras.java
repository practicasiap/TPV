package com.cifpvdg.cifpvirgendegracia.tpv.ClasesBD;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class JSonParserCodBarras extends AsyncTask {
    //Layaouts
    private TextInputLayout til_nombre;
    private TextInputLayout til_codigoBarras;
    private TextInputLayout til_stock;
    private Button btn_crearProduc;
    private Button btn_actualizarProduc;
    private Button btn_borrarProduc;
    private Button btn_sumarStock;
    private Button btn_restarStock;


    private Producto producto;
    private static JSONObject jObj = null;
    private static String json = "";
    private static final String TAG_PID = "cod_barras";
    private ProgressBar progressBar;

    // constructor
    public JSonParserCodBarras(TextInputLayout tilNo, TextInputLayout tilStock, TextInputLayout codBarras, Button crear, Button actualizar, Button borrar, Button sumar, Button restar, ProgressBar pg, Producto pro) {
        this.til_nombre = tilNo;
        this.til_stock = tilStock;
        this.til_codigoBarras = codBarras;
        this.btn_crearProduc = crear;
        this.btn_actualizarProduc = actualizar;
        this.btn_borrarProduc = borrar;
        this.btn_sumarStock = sumar;
        this.btn_restarStock = restar;

        this.progressBar = pg;
        this.producto = pro;
    }

    @Override
    protected Object doInBackground(Object[] urls) {

        String UrlCodBarrass = String.valueOf(urls[0]);
        this.producto.setCod_barras(String.valueOf(urls[1]));

        HttpURLConnection connection = null;
        BufferedReader readerRespuesta = null;
        BufferedWriter writerLlamada = null;
        OutputStream streamLlamada = null;
        InputStream streamRespuesta = null;
        StringBuffer bufferRespuesta = null;

        try {
            URL url = new URL(UrlCodBarrass);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();

            //Le indicamos por el codigo de barra que estamos buscando
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter(TAG_PID, this.producto.getCod_barras());
            String parametros = builder.build().getEncodedQuery();

            //Creamos un stream de salida y escribimos en él los parámetros POST
            streamLlamada = connection.getOutputStream();
            writerLlamada = new BufferedWriter(new OutputStreamWriter(streamLlamada));
            writerLlamada.write(parametros);
            writerLlamada.flush();

            //Obtenemos la respuesta
            streamRespuesta = connection.getInputStream();
            readerRespuesta = new BufferedReader(new InputStreamReader(streamRespuesta));

            //Leemos los valores devueltos y los añadimos a un StringBuffer
            bufferRespuesta = new StringBuffer();
            String line = "";
            while ((line = readerRespuesta.readLine()) != null) {
                bufferRespuesta.append(line);
            }

            //Convertimos a String el buffer de String
            //creamos un JSON a partir de la respuesta

            json = bufferRespuesta.toString();

            //Comprobamos que la consulta ha sido correcta
            if (!json.contains("success")) {
                jObj = new JSONObject(json);
                //Cerramos todo
            }else{
                jObj = null;
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Devolvemos el JSON
        return jObj;
    }

    @Override
    protected void onPostExecute(Object o) {

        til_codigoBarras.getEditText().setText(this.producto.getCod_barras());

        if (jObj != null) {
            til_nombre.getEditText().setText(jObj.optString("nombre"));
            til_stock.getEditText().setText(String.valueOf(jObj.optString("cantidad")));

            producto.setCodigo(jObj.optInt("codigo"));
            producto.setNombre(jObj.optString("nombre"));
            producto.setCantidad(jObj.optInt("cantidad"));
            producto.setPrecio_compra(jObj.optDouble("precio_compra"));
            producto.setPrecio_venta(jObj.optDouble("precio_venta"));
            producto.setDescripcion_breve(jObj.optString("desc_breve"));
            producto.setDescripcion_larga(jObj.optString("desc_larga"));
            producto.setCod_pro_proveedero(jObj.optInt("cod_pro_proveedor"));
            producto.setSubactegoria(jObj.optInt("subcategoria"));

            habilitarBotones(false, true, true, true, true);
        } else {
            habilitarBotones(true, false, false, false, false);
        }

        this.progressBar.setVisibility(View.GONE);
    }

    private void habilitarBotones(boolean crear, boolean update, boolean borrar, boolean sumar, boolean restar) {
        this.btn_crearProduc.setEnabled(crear);
        this.btn_actualizarProduc.setEnabled(update);
        this.btn_borrarProduc.setEnabled(borrar);
        this.btn_sumarStock.setEnabled(sumar);
        this.btn_restarStock.setEnabled(restar);
    }
}
