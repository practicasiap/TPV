package com.cifpvdg.cifpvirgendegracia.tpv.RVideo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.cifpvdg.cifpvirgendegracia.tpv.ClasesBD.Producto;
import com.cifpvdg.cifpvirgendegracia.tpv.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class CapturaVideo extends AppCompatActivity {

    private Button guardar;
    private VideoView videoView;
    private static final int GRABAR_VIDEO = 1;
    private String UPLOAD_URL ="https://tpvdam2.000webhostapp.com/subirVideo.php";
    private MediaController mc;
    private Uri video;
    private Producto producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capturavideo);
        this.guardar = (Button)findViewById(R.id.bGuardar);
        this.guardar.setEnabled(false);
        this.videoView = (VideoView)findViewById(R.id.vViewVideo);
        Intent i = this.getIntent();
        this.producto = (Producto) i.getSerializableExtra("prod");

        this.mc = new MediaController(this);
        this.mc.setAnchorView(this.videoView);
        this.videoView.setMediaController(this.mc);
    }

    public void grabarVideo(View view) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 20);

        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, GRABAR_VIDEO);
            this.guardar.setEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == GRABAR_VIDEO) {
            this.video = data.getData();
            this.videoView.setVideoURI(video);
            this.videoView.start();
        }
    }

    public void guardarVideo(View view){
        if (this.video != null) {
            String codificado = obtenerCodificado();
            final ProgressDialog loading = ProgressDialog.show(this,"Subiendo...","Espere por favor...",false,false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            loading.dismiss();
                            Toast.makeText(CapturaVideo.this, "Video subido con éxito.", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            loading.dismiss();
                            Toast.makeText(CapturaVideo.this, "Error al subir el video.", Toast.LENGTH_SHORT).show();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() {
                    String codificado = obtenerCodificado();
                    Map<String,String> elmapa = new Hashtable<String, String>();
                    elmapa.put("video", codificado);
                    elmapa.put("cba", producto.getCod_barras());
                    return elmapa;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            System.out.println("---------------------------------------------------------------------------------------Request creada---------------------------------------------------------------");
            requestQueue.add(stringRequest);
            System.out.println("---------------------------------------------------------------------------------------Request añadida---------------------------------------------------------------");
        }else{
            Toast.makeText(this, "No hay video para almacenar.", Toast.LENGTH_LONG).show();
        }
    }

    private String obtenerCodificado(){
        String path = getRealPathFromURI(this.video);
        File f = new File(path);
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
        String codificado = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(f));
            baos = new ByteArrayOutputStream();
            byte[] b = new byte[(int) f.length()];
            int byteleido;

            while ((byteleido = bis.read(b)) != -1) {
                baos.write(b, 0, byteleido);
            }
            byte[] codificar = baos.toByteArray();
            codificado = Base64.encodeToString(codificar, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return codificado;
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Video.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        cursor.moveToFirst();
        String result = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
        cursor.close();
        return result;
    }
}
