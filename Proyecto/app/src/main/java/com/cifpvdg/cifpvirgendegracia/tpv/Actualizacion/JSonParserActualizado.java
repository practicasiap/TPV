package com.cifpvdg.cifpvirgendegracia.tpv.Actualizacion;

import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;

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

public class JSonParserActualizado extends AsyncTask {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    private static final String TAG_NOM = "nom";
    private static final String TAG_SUBC = "subC";
    private static final String TAG_CODBAR = "codBar";
    private static final String TAG_CODPRO = "codPro";
    private static final String TAG_PREC = "preC";
    private static final String TAG_PREV = "preV";
    private static final String TAG_DESC = "desc";
    private static final String TAG_DESCB = "descB";
    private static final String TAG_CANT = "cant";
    private String nom;
    private String subC;
    private String codBar;
    private String codPro;
    private String preC;
    private String preV;
    private String cant;
    private String desc;
    private String descB;

    // constructor
    public JSonParserActualizado(String nom, String subC, String codB, String codPro, String preC, String preV, String desc, String descB, String cant) {
        this.codBar = codB;
        this.nom = nom;
        this.subC = subC;
        this.codPro = codPro;
        this.preC = preC;
        this.preV = preV;
        this.desc = desc;
        this.descB = descB;
        this.cant = cant;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        HttpURLConnection connection = null;
        BufferedReader readerRespuesta = null;
        BufferedWriter writerLlamada = null;
        OutputStream streamLlamada = null;
        InputStream streamRespuesta = null;
        StringBuffer bufferRespuesta = null;


        try{
            URL url = new URL(String.valueOf(objects[0]));
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();


            //Establecemos los parámetros
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter(TAG_NOM, this.nom)
                    .appendQueryParameter(TAG_CANT, this.cant)
                    .appendQueryParameter(TAG_PREC, this.preC)
                    .appendQueryParameter(TAG_PREV, this.preV)
                    .appendQueryParameter(TAG_DESCB, this.descB)
                    .appendQueryParameter(TAG_DESC, this.desc)
                    .appendQueryParameter(TAG_CODPRO, this.codPro)
                    .appendQueryParameter(TAG_SUBC, this.subC)
                    .appendQueryParameter(TAG_CODBAR, this.codBar);
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
            String line ="";
            while ((line = readerRespuesta.readLine()) != null){
                bufferRespuesta.append(line);
            }

            //Convertimos a String el buffer de String
            //creamos un JSON a partir de la respuesta
            json = bufferRespuesta.toString();
            System.out.println(json);
            jObj = new JSONObject(json);

            //Cerramos
            readerRespuesta.close();
            streamRespuesta.close();
            writerLlamada.close();
            streamLlamada.close();

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

    }
}