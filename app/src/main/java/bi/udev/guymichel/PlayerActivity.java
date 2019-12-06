package bi.udev.guymichel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

public class PlayerActivity extends AppCompatActivity {

	public TextView lbl_title, lbl_author, lbl_current_time, lbl_remaining_time;
	public ImageButton btn_play, btn_stop;
	public ImageView avatar;
    private String titre, author, photo, audio, online, date, id;
    private SeekBar seek_progression;
    private LinearLayout player_background;
    SharedPreferences playerPreferences, colorPreferences;
    private Handler mHandler;
    private Boolean prepared = false;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        playerPreferences = getSharedPreferences("player_cache", Context.MODE_PRIVATE);
        colorPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);

        lbl_title = (TextView) findViewById(R.id.lbl_title_mini_player);
        lbl_author = (TextView) findViewById(R.id.lbl_author_mini_player);
        lbl_remaining_time = (TextView) findViewById(R.id.lbl_remaining_time);
        lbl_current_time = (TextView) findViewById(R.id.lbl_current_time);
        btn_play = (ImageButton) findViewById(R.id.btn_play_mini_player);
        btn_stop = (ImageButton) findViewById(R.id.btn_stop_mini_player);
        avatar = (ImageView) findViewById(R.id.img_avatar_mini_player);
        seek_progression = (SeekBar) findViewById(R.id.seek_progression);
        player_background = (LinearLayout) findViewById(R.id.player_background);

        String color = colorPreferences.getString("color", "sans");
        if(color.equalsIgnoreCase("BLEU")){
            player_background.setBackgroundColor(getResources().getColor(R.color.colorPrimaryBlue));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDarkBlue));
            }
        }else if(color.equalsIgnoreCase("VERT")){
            player_background.setBackgroundColor(getResources().getColor(R.color.colorPrimaryGreen));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDarkGreen));
            }
        }else{
            player_background.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }

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
            id = getIntent().getStringExtra("id");

            SharedPreferences.Editor session = playerPreferences.edit();
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
            titre = playerPreferences.getString("titre", "sans");
            author = playerPreferences.getString("author", "sans");
            photo = playerPreferences.getString("photo", "sans");
            audio = playerPreferences.getString("audio", "sans");
            audio = playerPreferences.getString("date", "sans");
            audio = playerPreferences.getString("id", "-1");
            online = playerPreferences.getString("online", "");

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
            }
        });

        mHandler = new Handler();
        PlayerActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (MainActivity.mediaPlayer.getDuration() != MainActivity.mediaPlayer.getCurrentPosition()) {
                    int current_position = MainActivity.mediaPlayer.getCurrentPosition();
                    seek_progression.setProgress(current_position);

                    String ellapsed_time = convertInTimeFormat(current_position);
                    int remaing = MainActivity.mediaPlayer.getDuration() - current_position;
                    String remaining_time = "-" + convertInTimeFormat(remaing);
                    lbl_current_time.setText(ellapsed_time);
                    lbl_remaining_time.setText(remaining_time);

                }
                mHandler.postDelayed(this, 1000);
            }
        });
//        if(online.equalsIgnoreCase("true")){
//            btns_delete_share.setVisibility(View.GONE);
//        }
    }

    private String convertInTimeFormat(int time){
        String str_time = "";
        int min = (time/1000)/60;
        int sec = (time/1000)%60;

        if(min<10) str_time="0";
        str_time+= min+":";
        if(sec<10) str_time+="0";
        str_time+=sec;

        return str_time;
    }

	public void playMusic(View view) throws IOException {
        seek_progression.setMax(MainActivity.mediaPlayer.getDuration());
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
        MainActivity.lbl_playing.setVisibility(View.GONE);
        MainActivity.lbl_playing.setText("");
    }

    public void deleteMusic(View v) {
        new AlertDialog.Builder(PlayerActivity.this)
                .setIcon(R.drawable.error_icon)
                .setTitle(R.string.suppression)
                .setMessage(getString(R.string.confirmation_suppr)+titre)
                .setPositiveButton(R.string.supprimer, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new ArchiveModel(PlayerActivity.this).delete(id);
                        finish();
                    }
                })
                .setNegativeButton(R.string.annuler, null)
                .show();
    }

    public void shareMusic(View v) {
        String path = new Host(PlayerActivity.this).getDirPath() + File.separator + audio;
        ApplicationInfo appinfo = getApplicationContext().getApplicationInfo();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("audio/mp3");
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
        startActivity(Intent.createChooser(intent, getString(R.string.envoyer_avec)));
    }
}
