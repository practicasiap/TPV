package com.cifpvdg.cifpvirgendegracia.tpv.Escaner;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

/** Camera preview screen for portrait mode */
public class CameraView  extends SurfaceView implements SurfaceHolder.Callback{
    // Configuracion de tamaños para la camara
    public static final int PREVIEW_WIDTH = 1280;
    public static final int PREVIEW_HEIGHT = 720;
    public static final int SCREEN_ORIENTATION = 90;
    private Camera.Size mPreviewSize = null;

    // Instancias
    private Camera mCamera;
    private Camera.PreviewCallback mPreviewCallback;

    public CameraView(Context context, Camera camera){
        super(context);
        mCamera=camera;
        SurfaceHolder holder=getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setPreviewCallback(Camera.PreviewCallback previewCallback) {
        mPreviewCallback=previewCallback;
    }

    public Camera.Size getPreviewSize() {
        return mPreviewSize;
    }

    /** Calcula el tamaño de la vista para mostrarla correctamente */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        double originalWidth = MeasureSpec.getSize(widthMeasureSpec);
        double originalHeight = MeasureSpec.getSize(heightMeasureSpec);

        // Switch width and height size for portrait preview screen.
        // Because the camera stream size always assume landscape size.
        int DISPLAY_WIDTH = PREVIEW_HEIGHT;
        int DISPLAY_HEIGHT = PREVIEW_WIDTH;
        if(mPreviewSize != null) {
            DISPLAY_WIDTH = mPreviewSize.height;
            DISPLAY_HEIGHT = mPreviewSize.width;
        }

        // Consider calculated size is overflow
        int calculatedHeight = (int)(originalWidth * DISPLAY_HEIGHT / DISPLAY_WIDTH);
        int finalWidth, finalHeight;
        if (calculatedHeight > originalHeight) {
            finalWidth = (int)(originalHeight * DISPLAY_WIDTH / DISPLAY_HEIGHT);
            finalHeight = (int) originalHeight;
        } else {
            finalWidth = (int) originalWidth;
            finalHeight = calculatedHeight;
        }

        // Set new measures
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try{
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.setPreviewDisplay(null);
            mCamera=null;
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        try{
            mCamera.stopPreview();
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            // Iniciar la camara con sus propiedades
            Camera.Parameters parameters = mCamera.getParameters();

            // Formato de la imagen
            parameters.setPreviewFormat(ImageFormat.NV21);

            // Tamaño de la preview
            mPreviewSize = findSuitablePreviewSize(parameters.getSupportedPreviewSizes());
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);

            // Enfoque automaticamente
            if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }

            mCamera.setParameters(parameters);

            // Modo portrait
            mCamera.setDisplayOrientation(SCREEN_ORIENTATION);

            // Iniciar el callback
            mCamera.setPreviewCallback(mPreviewCallback);

            // Iniciar el preview
            mCamera.startPreview();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Find suitable preview scren size
     * If the value of PREVIEW_WIDTH and PREVIEW_HEIGHT are not supported on chosen camera,
     * find a size value that the most similar size and ratio.
     */
    private Camera.Size findSuitablePreviewSize(List<Camera.Size> supportedPreviewSize) {
        Camera.Size previewSize = null;

        double originalAspectRatio = (double)PREVIEW_WIDTH / (double)PREVIEW_HEIGHT;
        double lastFit = Double.MAX_VALUE, currentFit;
        for(Camera.Size s : supportedPreviewSize){
            if(s.width==PREVIEW_WIDTH && s.height==PREVIEW_HEIGHT){
                previewSize = s;
                break;
            } else if(previewSize == null) {
                lastFit = Math.abs( ((double)s.width / (double)s.height) - originalAspectRatio);
                previewSize = s;
            } else {
                currentFit = Math.abs( ((double)s.width / (double)s.height) - originalAspectRatio);
                if( (currentFit <= lastFit) && (Math.abs(PREVIEW_WIDTH-s.width)<=Math.abs(PREVIEW_WIDTH-previewSize.width)) ) {
                    previewSize = s;
                    lastFit = currentFit;
                }
            }
        }

        return previewSize;
    }

}