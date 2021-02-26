package ysaoudi.enseeiht.player;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements OnClickListener, SurfaceHolder.Callback {

    private Button pickButton;
    private Button playButton;
    private Button pauseButton;
    private Button fromStartButton;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private int currentPosition;
    private SeekBar progressBar;
    private BarUpdateTask updateTask;

    private static final int SELECT_VIDEO = 100;

    private static final String TAG = "Video Player Activity";

    private String videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickButton = this.findViewById(R.id.selectVideo);
        playButton = this.findViewById(R.id.play);
        pauseButton = this.findViewById(R.id.togglePause);
        fromStartButton = this.findViewById(R.id.restart);
        progressBar = this.findViewById(R.id.progressBar);
        progressBar.setEnabled(false);
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    if(mediaPlayer.isPlaying())
                        mediaPlayer.seekTo(progress);
                    else{
                        mediaPlayer.start();
                        mediaPlayer.seekTo(progress);
                        mediaPlayer.pause();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        pickButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        fromStartButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);

        playButton.setEnabled(false);
        pauseButton.setEnabled(false);
        fromStartButton.setEnabled(false);

        // recover the SurfaceView from resources
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        // Get the surfaceHolder from it
        surfaceHolder = surfaceView.getHolder();

        // and assign to it the call back this class implements
        surfaceHolder.addCallback(this);

        // this is a compatibility check, setType has been deprecated since HoneyComb,
        // it guarantees back compatibility
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mediaPlayer = new MediaPlayer();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == pickButton.getId()) {
            //pickVideo
            Intent pickIntent = new Intent(Intent.ACTION_PICK);
            pickIntent.setDataAndType(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*");

            startActivityForResult(pickIntent, SELECT_VIDEO);

        } else if (v.getId() == playButton.getId()) {
            //playVideo
            Intent playIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUri));

            startActivity(playIntent);
        } else if (v.getId() == fromStartButton.getId()) {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(this, Uri.parse(videoUri));
                mediaPlayer.prepare();
                progressBar.setProgress(mediaPlayer.getCurrentPosition());
            } catch (IOException e) {
                e.printStackTrace();
            }
            progressBar.setEnabled(true);
            progressBar.setMax(mediaPlayer.getDuration());
            if(updateTask == null || updateTask.isCancelled()){
                updateTask = new BarUpdateTask();
                updateTask.execute(progressBar);
            }
            mediaPlayer.start();
        } else if (v.getId() == pauseButton.getId()) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                progressBar.setEnabled(true);
                mediaPlayer.start();
                progressBar.setProgress(mediaPlayer.getCurrentPosition());
            }
            progressBar.setMax(mediaPlayer.getDuration());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            super.onActivityResult(requestCode, resultCode, intent);

            if (resultCode == RESULT_OK && requestCode == SELECT_VIDEO) {
                videoUri = intent.getData().toString();

                TextView text = findViewById(R.id.uri);
                String toShow = videoUri;
                Log.d(TAG, toShow);
                text.setText(toShow);

                playButton.setEnabled(true);
                pauseButton.setEnabled(true);
                fromStartButton.setEnabled(true);
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer.setDisplay(surfaceHolder);
        surfaceHolder.setFixedSize(1050, 900);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(videoUri != null && mediaPlayer != null){
            currentPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            if(updateTask != null)
                updateTask.cancel(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if(videoUri != null && currentPosition > 0){
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(this, Uri.parse(videoUri));
                mediaPlayer.prepare();
                mediaPlayer.seekTo(currentPosition);
                updateTask = new BarUpdateTask();
                updateTask.execute(progressBar);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class BarUpdateTask extends AsyncTask<SeekBar, Integer, Void> {
        @Override
        protected Void doInBackground(SeekBar... seekBars) {
            while(!isCancelled()){
                publishProgress(mediaPlayer.getCurrentPosition());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(mediaPlayer.getCurrentPosition());
        }
    }

}