package com.cifpvdg.cifpvirgendegracia.tpv.ClasesBD;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
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

public class JSonParser extends AsyncTask {
    private static JSONObject jObj = null;
    private static String json = "";
    private static final String TAG_PID = "cod_barras";

    // constructor
    public JSonParser() {

    }

    @Override
    protected Object doInBackground(Object[] urls) {

        HttpURLConnection connection = null;
        BufferedReader readerRespuesta = null;
        BufferedWriter writerLlamada = null;
        OutputStream streamLlamada = null;
        InputStream streamRespuesta = null;
        StringBuffer bufferRespuesta = null;


        try {
            URL url = new URL(String.valueOf(urls[0]));
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();

            //Le indicamos por el codigo de barra que estamos buscando
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter(TAG_PID, String.valueOf(urls[1]));
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

}
