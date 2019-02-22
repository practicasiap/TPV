package com.cifpvdg.cifpvirgendegracia.tpv.ClasesBD;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSonParserUpdateStock extends AsyncTask {
    private TextInputLayout til_stock;

    private Drawable drawableCheck;
    private Drawable drawableError;
    private Drawable drawableStock;
    private String suma;

    private Producto producto;
    private static String respuesta = "";
    private static final String TAG_PID = "cod_barras";
    private static final String TAG_STOCK = "stock";
    private ProgressBar progressBar;

    // constructor
    public JSonParserUpdateStock(TextInputLayout tilStock, Drawable check, Drawable error, Drawable stock, ProgressBar pg, Producto pro) {
        this.til_stock = tilStock;
        this.drawableCheck = check;
        this.drawableError = error;
        this.drawableStock = stock;

        this.progressBar = pg;
        this.producto = pro;
    }

    @Override
    protected Object doInBackground(Object[] urls) {

        String UrlUpdate = String.valueOf(urls[0]);
        suma = String.valueOf(urls[1]);

        HttpURLConnection connection = null;
        BufferedReader readerRespuesta = null;
        BufferedWriter writerLlamada = null;
        OutputStream streamLlamada = null;
        InputStream streamRespuesta = null;
        StringBuffer bufferRespuesta = null;


        try {
            URL url = new URL(UrlUpdate);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();

            //Le indicamos por el codigo de barra que estamos buscando
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter(TAG_PID, this.producto.getCod_barras())
                    .appendQueryParameter(TAG_STOCK, suma);
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

            respuesta = bufferRespuesta.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Devolvemos el JSON
        return respuesta;
    }

    @Override
    protected void onPostExecute(Object o) {
        if (respuesta.trim().equalsIgnoreCase("actualiza")) {
            producto.setCantidad(Integer.parseInt(suma));
            til_stock.getEditText().setCompoundDrawablesWithIntrinsicBounds(drawableStock, null, drawableCheck, null);
            til_stock.getEditText().setText(String.valueOf(producto.getCantidad()));
        } else {
            til_stock.getEditText().setCompoundDrawablesWithIntrinsicBounds(drawableStock, null, drawableError, null);
        }
        this.progressBar.setVisibility(View.GONE);
    }
}
