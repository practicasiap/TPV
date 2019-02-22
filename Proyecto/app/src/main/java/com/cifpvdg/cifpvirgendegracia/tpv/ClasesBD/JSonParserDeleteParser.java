package com.cifpvdg.cifpvirgendegracia.tpv.ClasesBD;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
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

public class JSonParserDeleteParser extends AsyncTask {

    private static String respuesta = "";
    private static final String TAG_PID = "cod_barras";
    private ProgressBar progressBar;

    // constructor
    public JSonParserDeleteParser(ProgressBar pg) {
        this.progressBar = pg;
    }

    @Override
    protected Object doInBackground(Object[] urls) {

        String UrlDelete = String.valueOf(urls[0]);
        String cod_barra = String.valueOf(urls[1]);

        HttpURLConnection connection = null;
        BufferedReader readerRespuesta = null;
        BufferedWriter writerLlamada = null;
        OutputStream streamLlamada = null;
        InputStream streamRespuesta = null;
        StringBuffer bufferRespuesta = null;


        try {
            URL url = new URL(UrlDelete);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();

            //Le indicamos por el codigo de barra que estamos buscando
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter(TAG_PID, cod_barra);
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
        this.progressBar.setVisibility(View.GONE);
    }

}
