package com.cifpvdg.cifpvirgendegracia.tpv.Escaner;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cifpvdg.cifpvirgendegracia.tpv.ClasesBD.JSonParser;
import com.cifpvdg.cifpvirgendegracia.tpv.ClasesBD.Producto;
import com.cifpvdg.cifpvirgendegracia.tpv.ClasesBD.VolleySingleton;
import com.cifpvdg.cifpvirgendegracia.tpv.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;

import org.json.JSONObject;

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
    private StringRequest stringRequest;
    private Producto producto;
    private static final String URLCODIGO = "https://tpvdam2.000webhostapp.com/selectCodBarras.php?";
    private static final String URLCODIGOUPDATE = "https://tpvdam2.000webhostapp.com/updateStock.php?";
    private static final String URLCODIGOBORRADO = "https://tpvdam2.000webhostapp.com/borrarProducto.php?";
    private JSONObject a = null;

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

        this.btn_escanear = findViewById(R.id.btn_escaner);
        this.til_nombre = findViewById(R.id.til_nombreProducto);
        this.til_codigoBarras = findViewById(R.id.til_codigoBarras);
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
        habilitarBotones(false, false, false, false, false);
    }

    private void borrarProducto() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Eliminar Producto");
        dialogo.setMessage("¿Deseas borrar el producto?");
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {
                stringRequest = new StringRequest(Request.Method.POST, URLCODIGOBORRADO, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.trim().equalsIgnoreCase("borrado")) {
                            Toast.makeText(getApplicationContext(), "Se ha Borrado con exito", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Borrado fallido", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parametros = new HashMap<>();
                        parametros.put("cod_barras", producto.getCod_barras());
                        return parametros;
                    }
                };

                VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(stringRequest);
                arrancarCamara();
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {

            }
        });
        dialogo.show();
    }

    private void sumarStock() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Actualizar Stock");
        dialogo.setMessage("¿Deseas aumentar el stock?");
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {
                stringRequest = new StringRequest(Request.Method.POST, URLCODIGOUPDATE, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.trim().equalsIgnoreCase("actualiza")) {
                            Toast.makeText(getApplicationContext(), "Se ha Actualizado con exito", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Actualizacion fallida", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parametros = new HashMap<>();
                        int sumar = producto.getCantidad() + Integer.parseInt(txt_stock.getText().toString());

                        parametros.put("cod_barras", producto.getCod_barras());
                        parametros.put("stock", String.valueOf(sumar));
                        return parametros;
                    }
                };

                VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(stringRequest);
                arrancarCamara();
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {

            }
        });
        dialogo.show();
    }

    private void restarStock() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Actualizar Stock");
        dialogo.setMessage("¿Deseas disminuir el stock?");
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {
                stringRequest = new StringRequest(Request.Method.POST, URLCODIGOUPDATE, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.trim().equalsIgnoreCase("actualiza")) {
                            Toast.makeText(getApplicationContext(), "Se ha Actualizado con exito", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Actualizacion fallida", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parametros = new HashMap<>();
                        int sumar = producto.getCantidad() - Integer.parseInt(txt_stock.getText().toString());

                        parametros.put("cod_barras", producto.getCod_barras());
                        parametros.put("stock", String.valueOf(sumar));
                        return parametros;
                    }
                };

                VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(stringRequest);
                arrancarCamara();
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {

            }
        });
        dialogo.show();
    }

    private void arrancarCamara() {
        habilitarBotones(false, false, false, false, false);
        this.til_nombre.getEditText().setText("");
        this.til_codigoBarras.getEditText().setText("");
        this.txt_stock.setText("");
        mCamera = getCameraInstance();

        // Set-up preview screen
        if (mCamera != null) {
            // Create overlay view
            overlay = new OverlayView(this);

            // Create barcode processor for ISBN
            CustomPreviewCallback camCallback = new CustomPreviewCallback(CameraView.PREVIEW_WIDTH, CameraView.PREVIEW_HEIGHT);
            camCallback.setBarcodeDetectedListener(new OnBarcodeListener() {
                @Override
                public void onBarcodeDetected(FirebaseVisionBarcode barcode) {
                    //overlay.setOverlay(fitOverlayRect(barcode.getBoundingBox()), barcode.getRawValue());
                    //overlay.invalidate();
                }
            });

            // Create camera preview
            camView = new CameraView(this, mCamera);

            camView.setPadding(0, 0, 0, 500);
            camView.setPreviewCallback(camCallback);

            // Add view to UI
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
     * Post-processor for preview image streams
     */
    private class CustomPreviewCallback implements Camera.PreviewCallback, OnSuccessListener<List<FirebaseVisionBarcode>>, OnFailureListener {

        public void setBarcodeDetectedListener(OnBarcodeListener mBarcodeDetectedListener) {
            this.mBarcodeDetectedListener = mBarcodeDetectedListener;
        }


        // ML Kit instances
        private FirebaseVisionBarcodeDetectorOptions options;
        private FirebaseVisionBarcodeDetector detector;
        private FirebaseVisionImageMetadata metadata;

        /**
         * Event Listener for post processing
         * <p>
         * We'll set up the detector only for EAN-13 barcode format and ISBN barcode type.
         * This OnBarcodeListener aims of notifying 'ISBN barcode is detected' to other class.
         */
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

            // set-up detector options for find EAN-13 format (commonly used 1-D barcode)
            options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                    .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ALL_FORMATS)//SOPORTA TODOS LOS FORMATOS
                    .build();

            detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);

            // build detector
            metadata = new FirebaseVisionImageMetadata.Builder()
                    .setFormat(ImageFormat.NV21)
                    .setWidth(mImageWidth)
                    .setHeight(mImageHeight)
                    .setRotation(FirebaseVisionImageMetadata.ROTATION_90)
                    .build();
        }

        /**
         * Start detector if camera preview shows
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
         * Barcode is detected successfully
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

                    JSonParser js = new JSonParser();
                    AsyncTask p = js.execute(URLCODIGO, barcode.getRawValue());

                    try {
                        a = (JSONObject) p.get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    til_codigoBarras.getEditText().setText(barcode.getDisplayValue());
                    producto.setCod_barras(barcode.getDisplayValue());

                    if (a != null) {
                        til_nombre.getEditText().setText(a.optString("nombre"));
                        producto.setCodigo(a.optInt("codigo"));
                        producto.setNombre(a.optString("nombre"));
                        producto.setCantidad(a.optInt("cantidad"));
                        producto.setPrecio_compra(a.optDouble("precio_compra"));
                        producto.setPrecio_compra(a.optDouble("precio_venta"));
                        producto.setDescripcion_breve(a.optString("desc_breve"));
                        producto.setDescripcion_breve(a.optString("desc_larga"));
                        producto.setCod_pro_proveedero(a.optInt("cod_pro_proveedor"));
                        producto.setSubactegoria(a.optInt("subcategoria"));

                        habilitarBotones(false, true, true, true, true);
                    } else {
                        habilitarBotones(true, false, false, false, false);
                    }
                }
            }
        }

        @Override
        public void onFailure(@NonNull Exception e) {
            // Task failed with an exception
            Log.i("Barcode", "read fail");
        }
    }

    private void habilitarBotones(boolean crear, boolean update, boolean borrar, boolean sumar, boolean restar) {
        this.btn_crearProduc.setEnabled(crear);
        this.btn_actualizarProduc.setEnabled(update);
        this.btn_borrarProduc.setEnabled(borrar);
        this.btn_sumarStock.setEnabled(sumar);
        this.btn_restarStock.setEnabled(restar);
    }
}