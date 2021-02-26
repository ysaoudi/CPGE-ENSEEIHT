package ysaoudi.enseeiht.camera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Camera mCamera;
    CameraPreview mPreview;
    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };

    public static boolean checkCameraHardware(Context	context)
    {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static Camera getCameraInstance()
    {
        Camera	c	=	null;
        try
        {
            c	=	Camera.open();	//	attempt	to	get	a	Camera	instance
        }
        catch (Exception	e){
            //	Camera	is	not	available	(in	use	or	does	not	exist)
        }
        return c;	//	returns	null	if	camera	is	unavailable
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCamera = getCameraInstance();
        mPreview = new CameraPreview( this, mCamera );
        FrameLayout preview = (FrameLayout) findViewById( R.id.cameraPreview );
        preview.addView( mPreview );
        Button captureButton	=	(Button)	findViewById(R.id.takePhotoButton);
        captureButton.setOnClickListener(	new View.OnClickListener()	{
            @Override
            public void onClick(View	v)	{
                mCamera.takePicture(null,	null,	mPicture);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if( mCamera != null )
        {
            mCamera.release(); // release the camera for other applications
            mCamera = null;
        }
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        mCamera = getCameraInstance();
        mPreview = new CameraPreview( this, mCamera );
        FrameLayout preview = (FrameLayout) findViewById( R.id.cameraPreview );
        preview.addView( mPreview );
    }

    public static class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private static final String TAG = "debug";
        private SurfaceHolder mHolder;
        private Camera	mCamera;

        public CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera	=	camera;
            //	Install	a	SurfaceHolder.Callback	so	we get notified when the
            //	underlying surface is created	and	destroyed.
            mHolder	=	getHolder();
            mHolder.addCallback(this);
            //	deprecated setting,	but required on Android versions	<3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void surfaceCreated(@NonNull SurfaceHolder holder) {
            //	The	Surface	has	been	created,	now	tell	the	camera	where	to draw	the	preview.
            try
            {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
            catch (IOException e)
            {
                Log.d(TAG,	"Error	setting	camera	preview:	"	+	e.getMessage());
            }

        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.
            if (mHolder.getSurface() == null){
                // preview surface does not exist
                return;
            }
            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e){
                // ignore: tried to stop a non-existent preview
            }
            // set preview size and make any resize, rotate or
            // reformatting changes here
            // start preview with new settings
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            } catch (Exception e){
                Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            }
        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

        }
    }
}