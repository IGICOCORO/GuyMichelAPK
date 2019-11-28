package bi.udev.guymichel;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by KonstrIctor on 28/11/2019.
 */

class AlertSettings extends Builder {

    private SharedPreferences.Editor settings;
    private View vueAlert;
    private RadioGroup langage_radio_group, color_radio_group;
    private RadioButton radio_en, radio_fr, radio_run, radio_vert, radio_marron, radio_bleu;

    public AlertSettings(Context context, Activity activity) {
        super(context);
        vueAlert = activity.getLayoutInflater().inflate(R.layout.settings_layout, null);
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        settings = sharedPreferences.edit();
        init();
        this.setView(vueAlert);
        this.setTitle(R.string.action_settings);
    }

    private void init() {
        langage_radio_group = (RadioGroup) vueAlert.findViewById(R.id.language_radio_group);
        color_radio_group = (RadioGroup) vueAlert.findViewById(R.id.color_radio_group);
        radio_vert = (RadioButton) vueAlert.findViewById(R.id.radio_vert);
        radio_bleu = (RadioButton) vueAlert.findViewById(R.id.radio_bleu);
        radio_marron = (RadioButton) vueAlert.findViewById(R.id.radio_marron);
        radio_fr = (RadioButton) vueAlert.findViewById(R.id.radio_fr);
        radio_run = (RadioButton) vueAlert.findViewById(R.id.radio_run);
        radio_en = (RadioButton) vueAlert.findViewById(R.id.radio_en);

        this.setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        this.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changerLangue(langage_radio_group.getCheckedRadioButtonId());
                changerCouleur(color_radio_group.getCheckedRadioButtonId());
                settings.commit();
            }
        });
    }

    private void changerCouleur(int checkedRadioButtonId) {
        if(radio_marron.getId() == checkedRadioButtonId){
            settings.putString("color", "MARRON");
        }else if(radio_bleu.getId() == checkedRadioButtonId){
            settings.putString("color", "BLEU");
        }else{
            settings.putString("color", "VERT");
        }
    }

    private void changerLangue(int checkedRadioButtonId) {
        if(radio_fr.getId() == checkedRadioButtonId){
            settings.putString("language", "French");
        }else if(radio_run.getId() == checkedRadioButtonId){
            settings.putString("language", "Kirundi");
        }else{
            settings.putString("language", "English");
        }
    }

}
