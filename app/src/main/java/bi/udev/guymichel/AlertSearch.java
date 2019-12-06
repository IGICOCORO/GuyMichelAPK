package bi.udev.guymichel;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by KonstrIctor on 28/11/2019.
 */

class AlertSearch extends Builder {
    private View vueAlert;

    private SharedPreferences.Editor settings;
    private EditText champ_keyword;
    private RadioGroup search_radio_group;
    private NumberPicker spinner_year;
    private NumberPicker spinner_month;
    private NumberPicker spinner_day;

    ArrayList<String> years = new ArrayList<>();
    String[] months;
    final ArrayList<String> days = new ArrayList<>();

    public AlertSearch(Context context, final MainActivity activity, final OnOk onOk) {
        super(context);

        vueAlert = activity.getLayoutInflater().inflate(R.layout.search_layout, null);
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        settings = sharedPreferences.edit();

        this.setView(vueAlert);
        this.setTitle(R.string.recherche);

        champ_keyword = (EditText) vueAlert.findViewById(R.id.champ_keyword);
        search_radio_group = (RadioGroup) vueAlert.findViewById(R.id.search_radio_group);
        spinner_year = (NumberPicker) vueAlert.findViewById(R.id.spinner_year);
        spinner_month = (NumberPicker) vueAlert.findViewById(R.id.spinner_month);
        spinner_day = (NumberPicker) vueAlert.findViewById(R.id.spinner_day);
        
        spinner_year.setMinValue(2018);
        spinner_year.setMaxValue(new Date().getYear()+1900);
//        spinner_year.setDisplayedValues(x);
        spinner_year.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        months = new String[]{activity.getString(R.string.janvier),activity.getString(R.string.février)
                            ,activity.getString(R.string.mars),activity.getString(R.string.avril)
                            ,activity.getString(R.string.mai),activity.getString(R.string.juin)
                            ,activity.getString(R.string.juillet),activity.getString(R.string.août)
                            ,activity.getString(R.string.septembre),activity.getString(R.string.octobre)
                            ,activity.getString(R.string.novembre),activity.getString(R.string.décembre)
                };

        spinner_month.setMinValue(1);
        spinner_month.setMaxValue(12);
        spinner_month.setDisplayedValues(months);
        spinner_month.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        int fevrier = 28;
        if(spinner_year.getValue()%4==0) fevrier = 29;
        final int[] days_number = {31, fevrier, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        spinner_month.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                spinner_day.setMinValue(1);
                spinner_day.setMaxValue(days_number[newVal-1]);
//                spinner_day.setDisplayedValues(days);
                spinner_day.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            }
        });

        this.setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                settings.commit();
            }
        });
        this.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String key_word = champ_keyword.getText().toString();
                String cond;
                int selected = search_radio_group.getCheckedRadioButtonId();
                if (selected == R.id.radio_after) {
                    cond = "after";
                } else if (selected == R.id.radio_before) {
                    cond = "before";
                } else {
                    cond = "on";
                }
                Integer month = spinner_month.getValue();
                final String date = spinner_year.getValue() + "-" +
                        month + "-" +
                        spinner_day.getValue();
                final String local_condition = cond;

                String condition = key_word + "/" + local_condition + "/" + date+"/";
                settings.putString("condition", condition);
                settings.commit();

                onOk.getCondition(condition);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, R.string.searching, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    interface OnOk{
        void getCondition (String condition);
    }
}
