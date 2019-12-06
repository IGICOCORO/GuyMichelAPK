package bi.udev.guymichel;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ToggleButton;
import android.widget.ToggleButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

/**
 * Created by KonstrIctor on 28/11/2019.
 */

class AlertSettings extends Builder {

    private SharedPreferences.Editor settings;
    private View vueAlert;
    private ToggleButton radio_en, radio_fr, radio_run, radio_rw;
    private ToggleButton radio_vert;
    private ToggleButton radio_marron;
    private ToggleButton radio_bleu;
    private MainActivity activity;
    private Window window;

    public AlertSettings(Context context, MainActivity activity) {
        super(context);
        this.activity = activity;
        vueAlert = activity.getLayoutInflater().inflate(R.layout.settings_layout, null);
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        settings = sharedPreferences.edit();
        init();
        this.setView(vueAlert);
        this.setTitle(R.string.action_settings);
    }

    private void init() {
        radio_vert = (ToggleButton) vueAlert.findViewById(R.id.radio_vert);
        radio_bleu = (ToggleButton) vueAlert.findViewById(R.id.radio_bleu);
        radio_marron = (ToggleButton) vueAlert.findViewById(R.id.radio_marron);
        radio_fr = (ToggleButton) vueAlert.findViewById(R.id.radio_fr);
        radio_run = (ToggleButton) vueAlert.findViewById(R.id.radio_run);
        radio_en = (ToggleButton) vueAlert.findViewById(R.id.radio_en);
        radio_rw = (ToggleButton) vueAlert.findViewById(R.id.radio_rw);

        radio_bleu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctToggleColor(v);
            }
        });
        radio_vert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctToggleColor(v);
            }
        });
        radio_marron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctToggleColor(v);
            }
        });
        radio_fr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctToggleLangue(v);
            }
        });

        radio_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctToggleLangue(v);
            }
        });
        radio_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctToggleLangue(v);
            }
        });

        this.setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        this.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changerLangue();
                changerCouleur();
                settings.commit();
            }
        });
    }

    private void changerCouleur() {
        if(radio_marron.isChecked()){
            settings.putString("color", "MARRON");
            activity.changeColor();

        }else if(radio_bleu.isChecked()){
            settings.putString("color", "BLEU");
            activity.changeColor();
        }else  if(radio_vert.isChecked()){
            settings.putString("color", "VERT");
            activity.changeColor();
        }
    }

    private void changerLangue() {
        if(radio_fr.isChecked()){
            settings.putString("language", "fr");
            activity.loadLocale();
//            activity.recreate();
            refresh(activity);
        }else if(radio_run.isChecked()){
            settings.putString("language", "rn");
            activity.loadLocale();
//            activity.recreate();
            refresh(activity);
        }else if(radio_en.isChecked()){
            settings.putString("language", "en");
            activity.loadLocale();
//            activity.recreate();
            refresh(activity);
        }else if(radio_rw.isChecked()){
        settings.putString("language", "rw");
        activity.loadLocale();
//            activity.recreate();
        refresh(activity);
        }
    }

    private void refresh(MainActivity activity) {
        activity.finish();
        activity.overridePendingTransition(0, 0);
        activity.startActivity(activity.getIntent());
        activity.overridePendingTransition(0, 0);
    }


    public void correctToggleColor(View view) {
        ToggleButton selected = (ToggleButton) view;
        if (radio_marron.getId() != selected.getId())
            radio_marron.setChecked(false);
        if (radio_vert.getId() != selected.getId())
            radio_vert.setChecked(false);
        if (radio_bleu.getId() != selected.getId())
            radio_bleu.setChecked(false);
    }

    public void correctToggleLangue(View view) {
        ToggleButton selected = (ToggleButton) view;
        if (radio_en.getId() != selected.getId())
            radio_en.setChecked(false);
        if (radio_run.getId() != selected.getId())
            radio_run.setChecked(false);
        if (radio_fr.getId() != selected.getId())
            radio_fr.setChecked(false);
    }
}
