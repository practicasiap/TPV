package com.cifpvdg.cifpvirgendegracia.tpv.Escaner;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.cifpvdg.cifpvirgendegracia.tpv.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cifpvdg.cifpvirgendegracia.tpv.Actualizacion.ActualizadoActivity;
import com.cifpvdg.cifpvirgendegracia.tpv.ClasesBD.JSonParserCodBarras;
import com.cifpvdg.cifpvirgendegracia.tpv.ClasesBD.JSonParserDeleteParser;
import com.cifpvdg.cifpvirgendegracia.tpv.ClasesBD.JSonParserUpdateStock;
import com.cifpvdg.cifpvirgendegracia.tpv.ClasesBD.Producto;
import com.cifpvdg.cifpvirgendegracia.tpv.ClasesBD.VolleySingleton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Camera Preview Activity
 * Muestra el escaner y el overlay del escaner
 */
public class CameraPreviewActivity extends AppCompatActivity {

    //Layout
    private EditText txt_stock;
    private TextInputLayout til_nombre;
    private TextInputLayout til_codigoBarras;
    private TextInputLayout til_stock;
    private Button btn_crearProduc;
    private Button btn_actualizarProduc;
    private Button btn_borrarProduc;
    private Button btn_sumarStock;
    private Button btn_restarStock;

    private Camera mCamera;
    private CameraView camView;
    private OverlayView overlay;
    private double overlayScale = -1;
    private FrameLayout preview;
    private Button btn_escanear;

    //Consulta sobre el codgio
    private Producto producto;
    private static final String URLCODIGO = "https://tpvdam2.000webhostapp.com/selectCodBarras.php?";
    private static final String URLCODIGOUPDATE = "https://tpvdam2.000webhostapp.com/updateStock.php?";
    private static final String URLCODIGOBORRADO = "https://tpvdam2.000webhostapp.com/borrarProducto.php?";
    private ProgressBar progreBar;

    //Drawable
    private Drawable drawableCheck;
    private Drawable drawableError;
    private Drawable drawableStock;


    private interface OnBarcodeListener {
        void onBarcodeDetected(FirebaseVisionBarcode barcode);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fix orientation : portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Set layout
        setContentView(R.layout.escaner_barra);

        this.drawableCheck = getDrawable(R.drawable.ic_check_black_24dp);
        this.drawableError = getDrawable(R.drawable.ic_close_black_24dp);
        this.drawableStock = getDrawable(R.drawable.ic_store_black_24dp);

        this.progreBar = findViewById(R.id.progressBar);
        this.btn_escanear = findViewById(R.id.btn_escaner);
        this.til_nombre = findViewById(R.id.til_nombreProducto);
        this.til_codigoBarras = findViewById(R.id.til_codigoBarras);
        this.til_stock = findViewById(R.id.til_stockProducto);
        this.txt_stock = findViewById(R.id.txt_stock);

        this.btn_crearProduc = findViewById(R.id.btn_añadir);
        this.btn_actualizarProduc = findViewById(R.id.btn_modificar);
        this.btn_borrarProduc = findViewById(R.id.btn_delete);
        this.btn_sumarStock = findViewById(R.id.btn_sumarStock);
        this.btn_restarStock = findViewById(R.id.btn_restarStock);

        btn_escanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrancarCamara();
            }
        });
        btn_sumarStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_stock.getText().toString().length() > 0) {
                    sumarStock();
                } else {
                    Toast.makeText(getApplicationContext(), "Selecciona una cantidad", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_restarStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_stock.getText().toString().length() > 0) {
                    if (producto.getCantidad() >= Integer.parseInt(txt_stock.getText().toString())) {
                        restarStock();
                    } else {
                        Toast.makeText(getApplicationContext(), "El stock a borrar es superior al actual", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Selecciona una cantidad", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_borrarProduc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarProducto();
            }
        });
        arrancarCamara();

        btn_crearProduc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearProducto();
            }
        });
        btn_actualizarProduc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearProducto();
            }
        });

        limpiarCampos();
    }

    private void crearProducto() {
        Intent i = new Intent(this, ActualizadoActivity.class);

        i.putExtra("prod", this.producto);

        this.startActivity(i);
    }

    private void borrarProducto() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Eliminar Producto");
        dialogo.setMessage("¿Deseas borrar el producto?");
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {
                progreBar.setVisibility(View.VISIBLE);
                JSonParserDeleteParser js = new JSonParserDeleteParser(progreBar);
                AsyncTask asyn = js.execute(URLCODIGOBORRADO, producto.getCod_barras());

                String respuesta = null;

                try {
                    respuesta = (String) asyn.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (respuesta.trim().contains("borrado")) {
                    Toast.makeText(getApplicationContext(), "Se ha Borrado con exito", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                } else {
                    Toast.makeText(getApplicationContext(), "Borrado Fallido", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {

            }
        });
        dialogo.show();
    }

    private void sumarStock() {
        final int sumar = producto.getCantidad() + Integer.parseInt(txt_stock.getText().toString());

        progreBar.setVisibility(View.VISIBLE);
        JSonParserUpdateStock js = new JSonParserUpdateStock(til_stock, drawableCheck, drawableError, drawableStock, progreBar, producto);
        AsyncTask asyn = js.execute(URLCODIGOUPDATE, sumar);

        String respuesta = null;

        try {
            respuesta = (String) asyn.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (respuesta.trim().contains("actualiza")) {
            Toast.makeText(getApplicationContext(), "Se ha Actualizado con exito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Actualizado Fallido", Toast.LENGTH_SHORT).show();
        }
    }


    private void restarStock() {
        final int resta = producto.getCantidad() - Integer.parseInt(txt_stock.getText().toString());

        progreBar.setVisibility(View.VISIBLE);
        JSonParserUpdateStock js = new JSonParserUpdateStock(til_stock, drawableCheck, drawableError, drawableStock, progreBar, producto);
        AsyncTask asyn = js.execute(URLCODIGOUPDATE, resta);

        String respuesta = null;

        try {
            respuesta = (String) asyn.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (respuesta.trim().contains("actualiza")) {
            Toast.makeText(getApplicationContext(), "Se ha Actualizado con exito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Actualizado Fallido", Toast.LENGTH_SHORT).show();
        }
    }


    private void arrancarCamara() {
        limpiarCampos();

        mCamera = getCameraInstance();


        if (mCamera != null) {
            overlay = new OverlayView(this);

            // Crea al procesador del codigo de barras
            CustomPreviewCallback camCallback = new CustomPreviewCallback(CameraView.PREVIEW_WIDTH, CameraView.PREVIEW_HEIGHT);
            camCallback.setBarcodeDetectedListener(new OnBarcodeListener() {
                @Override
                public void onBarcodeDetected(FirebaseVisionBarcode barcode) {
                    //overlay.setOverlay(fitOverlayRect(barcode.getBoundingBox()), barcode.getRawValue());
                    //overlay.invalidate();
                }
            });

            // Crea la view de la camara
            camView = new CameraView(this, mCamera);

            camView.setPadding(0, 0, 0, 500);
            camView.setPreviewCallback(camCallback);

            // añade la view a la interfaz
            preview = findViewById(R.id.frm_preview);
            preview.addView(camView);
            preview.addView(overlay);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            if (mCamera != null) mCamera.release();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    /**
     * Get facing back camera instance
     */
    public static Camera getCameraInstance() {
        int camId = -1;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); ++i) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                camId = i;
                break;
            }
        }

        if (camId == -1) return null;

        Camera c = null;
        try {
            c = Camera.open(camId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    /**
     * Calculate overlay scale factor
     */
    private Rect fitOverlayRect(Rect r) {
        if (overlayScale <= 0) {
            Camera.Size prevSize = camView.getPreviewSize();
            overlayScale = (double) overlay.getWidth() / (double) prevSize.height;
        }

        return new Rect((int) (r.left * overlayScale), (int) (r.top * overlayScale), (int) (r.right * overlayScale), (int) (r.bottom * overlayScale));
    }

    /**
     * El post procesado de la foto
     */
    private class CustomPreviewCallback implements Camera.PreviewCallback, OnSuccessListener<List<FirebaseVisionBarcode>>, OnFailureListener {

        public void setBarcodeDetectedListener(OnBarcodeListener mBarcodeDetectedListener) {
            this.mBarcodeDetectedListener = mBarcodeDetectedListener;
        }


        // ML Kit instances
        private FirebaseVisionBarcodeDetectorOptions options;
        private FirebaseVisionBarcodeDetector detector;
        private FirebaseVisionImageMetadata metadata;


        private OnBarcodeListener mBarcodeDetectedListener = null;

        /**
         * size of input image
         */
        private int mImageWidth, mImageHeight;

        /**
         * Constructor
         *
         * @param imageWidth  preview image width (px)
         * @param imageHeight preview image height (px)
         */
        CustomPreviewCallback(int imageWidth, int imageHeight) {
            mImageWidth = imageWidth;
            mImageHeight = imageHeight;

            // Inicias el detector con todos los formatos
            options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                    .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ALL_FORMATS)//SOPORTA TODOS LOS FORMATOS
                    .build();

            detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);

            metadata = new FirebaseVisionImageMetadata.Builder()
                    .setFormat(ImageFormat.NV21)
                    .setWidth(mImageWidth)
                    .setHeight(mImageHeight)
                    .setRotation(FirebaseVisionImageMetadata.ROTATION_90)
                    .build();
        }

        /**
         * Inicia el detector si se muestra cosas
         */
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            try {
                detector.detectInImage(FirebaseVisionImage.fromByteArray(data, metadata))
                        .addOnSuccessListener(this)
                        .addOnFailureListener(this);
            } catch (Exception e) {
                Log.d("CameraView", "parse error");
            }
        }

        /**
         * Cuando el codigo de barras es obtenido
         */
        @Override
        public void onSuccess(List<FirebaseVisionBarcode> barcodes) {
            // Task completed successfully
            for (FirebaseVisionBarcode barcode : barcodes) {
                Log.d("Barcode", "value : " + barcode.getRawValue());

                int valueType = barcode.getValueType();

                //Si el codigo escaneado es de tipo producto
                if (valueType == FirebaseVisionBarcode.TYPE_PRODUCT) {
                    mBarcodeDetectedListener.onBarcodeDetected(barcode);
                    mCamera.stopPreview();//PARAR LA ACTIVIDAD CUANDO ENCUENTRA EL PRODUCTO

                    producto = new Producto();

                    progreBar.setVisibility(View.VISIBLE);
                    JSonParserCodBarras js = new JSonParserCodBarras(til_nombre, til_stock, til_codigoBarras, btn_crearProduc, btn_actualizarProduc, btn_borrarProduc, btn_sumarStock, btn_restarStock, progreBar, producto);
                    js.execute(URLCODIGO, barcode.getRawValue());

                }
            }
        }

        @Override
        public void onFailure(@NonNull Exception e) {
            // Task failed
            Log.i("Barcode", "read fail");
        }
    }

    private void limpiarCampos() {
        this.btn_crearProduc.setEnabled(false);
        this.btn_actualizarProduc.setEnabled(false);
        this.btn_borrarProduc.setEnabled(false);
        this.btn_sumarStock.setEnabled(false);
        this.btn_restarStock.setEnabled(false);

        this.til_stock.getEditText().setCompoundDrawablesWithIntrinsicBounds(drawableStock, null, null, null);
        this.til_stock.getEditText().setText("");
        this.til_nombre.getEditText().setText("");
        this.til_codigoBarras.getEditText().setText("");
        this.txt_stock.setText("");
    }
}