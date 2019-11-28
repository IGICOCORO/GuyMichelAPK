package bi.udev.guymichel;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;

public class PlayerActivity extends AppCompatActivity {

	public TextView lbl_title, lbl_author, lbl_current_time, lbl_remaining_time;
	public ImageButton btn_play, btn_stop;
	public ImageView avatar;
    private String titre, author, photo, audio, online, date;
    private SeekBar seek_progression;
    SharedPreferences sharedPreferences;
    private Handler mHandler;
    private Boolean prepared = false;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        sharedPreferences = getSharedPreferences("player_cache", Context.MODE_PRIVATE);

        lbl_title = (TextView) findViewById(R.id.lbl_title_mini_player);
        lbl_author = (TextView) findViewById(R.id.lbl_author_mini_player);
        lbl_remaining_time = (TextView) findViewById(R.id.lbl_remaining_time);
        lbl_current_time = (TextView) findViewById(R.id.lbl_current_time);
        btn_play = (ImageButton) findViewById(R.id.btn_play_mini_player);
        btn_stop = (ImageButton) findViewById(R.id.btn_stop_mini_player);
        avatar = (ImageView) findViewById(R.id.img_avatar_mini_player);
        seek_progression = (SeekBar) findViewById(R.id.seek_progression);

        if (MainActivity.mediaPlayer == null) {
            MainActivity.mediaPlayer = new MediaPlayer();
        }
        if (getIntent().hasExtra("audio")) {
            titre = getIntent().getStringExtra("titre");
            author = getIntent().getStringExtra("author");
            photo = getIntent().getStringExtra("photo");
            audio = getIntent().getStringExtra("audio");
            date = getIntent().getStringExtra("date");
            online = getIntent().getStringExtra("online");

            SharedPreferences.Editor session = sharedPreferences.edit();
            session.putString("titre", titre);
            session.putString("author", author);
            session.putString("photo", photo);
            session.putString("audio", audio);
            session.putString("date", date);
            session.putString("online", online);
            session.commit();

            MainActivity.mediaPlayer.stop();
            MainActivity.mediaPlayer.reset();

        } else {
            prepared = true;
            titre = sharedPreferences.getString("titre", "sans");
            author = sharedPreferences.getString("author", "sans");
            photo = sharedPreferences.getString("photo", "sans");
            audio = sharedPreferences.getString("audio", "sans");
            audio = sharedPreferences.getString("date", "sans");
            online = sharedPreferences.getString("online", "");

            if (MainActivity.mediaPlayer.isPlaying()) {
                btn_play.setBackgroundResource(R.drawable.pause_icon);
            } else {
                btn_play.setBackgroundResource(R.drawable.play_icon);
            }
        }

        lbl_title.setText(titre);
        lbl_author.setText(author);
        Glide.with(getApplicationContext()).load("http://" + Host.url + photo).into(avatar);

//        seek_progression.setMax(MainActivity.mediaPlayer.getDuration());

        seek_progression.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MainActivity.mediaPlayer.seekTo(seekBar.getProgress());
                seek_progression.setProgress(seekBar.getProgress());
            }
        });

        mHandler = new Handler();
        PlayerActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (MainActivity.mediaPlayer.getDuration() != MainActivity.mediaPlayer.getCurrentPosition()) {
                    int current_position = MainActivity.mediaPlayer.getCurrentPosition();
                    seek_progression.setProgress(current_position / 1000);

                    String ellapsed_time = convertInTimeFormat(current_position);
                    int remaing = MainActivity.mediaPlayer.getDuration() - current_position;
                    String remaining_time = "-" + convertInTimeFormat(remaing);
                    lbl_current_time.setText(ellapsed_time);
                    lbl_remaining_time.setText(remaining_time);

                }
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    private String convertInTimeFormat(int time){
        String str_time = "";
        int min = (time/1000)/60;
        int sec = (time/1000)%60;

        if(min<60) str_time="0";
        str_time+= min+":";
        if(sec<10) str_time+="0";
        str_time+=sec;

        return str_time;
    }

	public void playMusic(View view) throws IOException {
        if (MainActivity.mediaPlayer.isPlaying()) {
            MainActivity.mediaPlayer.pause();
            btn_play.setBackgroundResource(R.drawable.play_icon);
        } else {
            if (!prepared) {
                Toast.makeText(PlayerActivity.this, R.string.audio_is_loading, Toast.LENGTH_LONG).show();
                if(online.equalsIgnoreCase("false")) {
                    MainActivity.mediaPlayer.setDataSource(new Host(getApplicationContext()).getDirPath() + File.separator + audio);
                }else{
                    MainActivity.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    MainActivity.mediaPlayer.setDataSource("http://"+Host.url + audio);
                }
                MainActivity.mediaPlayer.prepareAsync();
                MainActivity.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        prepared = true;
                        MainActivity.mediaPlayer.start();
                        btn_play.setBackgroundResource(R.drawable.pause_icon);
                    }
                });
            }else{
                MainActivity.mediaPlayer.start();
                btn_play.setBackgroundResource(R.drawable.pause_icon);
            }
        }
        MainActivity.lbl_playing.setVisibility(View.VISIBLE);
        MainActivity.lbl_playing.setText(getString(R.string.playing)+titre +getString(R.string.du)+date);
	}
	public void stopMusic(View view){
        MainActivity.mediaPlayer.seekTo(0);
        MainActivity.mediaPlayer.pause();
        btn_play.setBackgroundResource(R.drawable.play_icon);
        MainActivity.lbl_playing.setVisibility(View.INVISIBLE);
        MainActivity.lbl_playing.setText("");
    }
}
