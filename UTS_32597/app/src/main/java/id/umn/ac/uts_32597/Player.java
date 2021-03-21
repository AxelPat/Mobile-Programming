package id.umn.ac.uts_32597;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity {

    Bundle extraData; //Data lagu dari list
    ImageView prev, play, next;
    int position;
    SeekBar mSeekBar;
    static MediaPlayer mPlayer;
    TextView name;
    ArrayList<File> musicList;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prev = findViewById(R.id.previous);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        mSeekBar = findViewById(R.id.mSeekBarTime);
        name  = findViewById(R.id.songName);

        //menghentikan media player

        if (mPlayer!=null) {
            mPlayer.stop();
        }

        //passing data dari list
        Intent intent = getIntent();
        extraData = intent.getExtras();

        musicList = (ArrayList)extraData.getParcelableArrayList("songsList");
        position = extraData.getInt("position", 0);

        //memulai media player

        initializeMusicPlayer(position);

        // play button

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position < musicList.size() -1) {
                    position++;
                } else {
                    position = 0;
                }
                initializeMusicPlayer(position);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position<=0) {
                    position = musicList.size();
                } else {
                    position++;
                }
                initializeMusicPlayer(position);
            }
        });

    }

    private void initializeMusicPlayer(int position) {

        //Mereset player ketika null & bermain

        if (mPlayer!=null && mPlayer.isPlaying()) {
            mPlayer.reset();
        }

        //menampilkan judul lagu
        String title = musicList.get(position).getName();
        name.setText(title);

        //akses storage

        Uri uri = Uri.parse(musicList.get(position).toString());


        mPlayer = MediaPlayer.create(this, uri);

        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                //durasi seekbar
                mSeekBar.setMax(mPlayer.getDuration());

                //mengubah button menjadi pause ketika tidak ada lagu
                play.setImageResource(R.drawable.btnpause);
                //start player
                mPlayer.start();
            }
        });

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                play.setImageResource(R.drawable.btnplay);
            }
        });

        //Seekbar
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                //Perubahan Seekbar
                if (fromUser) {
                    mSeekBar.setProgress(progress);
                    mPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mPlayer!=null) {
                    try {
                        if (mPlayer.isPlaying()) {
                            Message message = new Message();
                            message.what = mPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mSeekBar.setProgress(msg.what);
        }
    };

    private void play() {
        if (mPlayer!=null && mPlayer.isPlaying()) {
            mPlayer.pause();

            play.setImageResource(R.drawable.btnplay);
        } else {
            mPlayer.start();
            play.setImageResource(R.drawable.btnpause);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}