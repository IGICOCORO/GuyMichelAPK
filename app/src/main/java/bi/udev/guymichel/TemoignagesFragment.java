package bi.udev.guymichel;


import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class TemoignagesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private MainActivity activity;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipe_layout_emission;
    private ArrayList<Temoignage> temoignages;
    private int page;
    private GridLayoutManager gridLayoutManager;
    private AdaptateurTemoignage adaptateur;
    private String condition="";

    public TemoignagesFragment() {
        // Required empty public constructor
    }


    public TemoignagesFragment(MainActivity activity) {
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_emissions, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_recycler);
        swipe_layout_emission = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout_emission);

        swipe_layout_emission.setOnRefreshListener(this);

        temoignages = new ArrayList<>();
        page = 1;

        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adaptateur = new AdaptateurTemoignage(getContext(), temoignages);

        recyclerView.setAdapter(adaptateur);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                page++;
                chargerParole(page);
            }
        });
        swipe_layout_emission.post(new Runnable() {

            @Override
            public void run() {

                if(swipe_layout_emission != null) {
                    swipe_layout_emission.setRefreshing(true);
                }
                temoignages = new ArrayList<>();
                page = 1;
                chargerParole(page);
            }
        });

        return rootView;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        activity = getActivity();
//    }
    private void chargerParole(int no_page) {
        if(temoignages.isEmpty()) temoignages.clear();
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://"+ Host.url+"/api/messages/"+condition+no_page).newBuilder();
        String url = urlBuilder.build().toString();
        setHasOptionsMenu(true);

        Request request = new Request.Builder()
                .url(url)
//                .header("Authorization", "Token "+token)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                final String mMessage = e.getMessage();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, R.string.no_internet, Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= 14) {
                            swipe_layout_emission.setRefreshing(false);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    for( int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Temoignage message = new Temoignage(
                                jsonObject.getString("id"),
                                jsonObject.getString("titre"),
                                jsonObject.getString("categorie"),
                                jsonObject.getString("author"),
                                jsonObject.getString("photo"),
                                jsonObject.getString("date"),
                                jsonObject.getString("slug")
                        );
                        temoignages.add(message);
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipe_layout_emission.setRefreshing(false);
                            adaptateur.setData(temoignages);
                            adaptateur.notifyDataSetChanged();
                        }
                    });

                } catch (Exception e) {
                    final String message = e.getMessage();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(activity, R.string.no_more_data, Toast.LENGTH_SHORT).show();
                            if (Build.VERSION.SDK_INT >= 14) {
                                swipe_layout_emission.setRefreshing(false);
                            }

                        }
                    });
                }
            }
        });

    }

    @Override
    public void onRefresh() {
        temoignages = new ArrayList<>();
        page = 1;
        chargerParole(page);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            new AlertSettings(getContext(), activity).create().show();
            return true;
        }else if (id == R.id.action_search) {
//            rechercher();
            new AlertSearch(getContext(), activity, new AlertSearch.OnOk() {
                @Override
                public void getCondition(String cond) {
                    condition = cond;
                    chargerParole(1);
                }
            }).create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//    public void rechercher(){
//        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
//
//        View vueAlert = activity.getLayoutInflater().inflate(R.layout.search_layout, null);
//        alertBuilder.setView(vueAlert);
//        alertBuilder.setTitle(R.string.Recherche);
//
//        final EditText champ_keyword = (EditText) vueAlert.findViewById(R.id.champ_keyword);
//        final RadioGroup search_radio_group = (RadioGroup) vueAlert.findViewById(R.id.search_radio_group);
//        final Spinner spinner_year = (Spinner) vueAlert.findViewById(R.id.spinner_year);
//        final Spinner spinner_month = (Spinner) vueAlert.findViewById(R.id.spinner_month);
//        final Spinner spinner_day = (Spinner) vueAlert.findViewById(R.id.spinner_day);
//
//        ArrayList<String> years = new ArrayList<>();
//        ArrayList<String> months = new ArrayList<>();
//        final ArrayList<String> days = new ArrayList<>();
//
//        for (int i = 2018; i<= new Date().getYear()+1900; i++){
//            years.add(Integer.toString(i));
//        }
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, years);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        ArrayAdapter<String> combo = adapter;
//        spinner_year.setAdapter(combo);
//
//        months.add(getString(R.string.janvier));months.add(getString(R.string.février));months.add(getString(R.string.mars))
//        ;months.add(getString(R.string.avril));months.add(getString(R.string.mai));months.add(getString(R.string.juin));
//        months.add(getString(R.string.juillet));months.add(getString(R.string.août));months.add(getString(R.string.septembre));
//        months.add(getString(R.string.octobre));months.add(getString(R.string.novembre));months.add(getString(R.string.décembre));
//
//        adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, months);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        combo = adapter;
//        spinner_month.setAdapter(combo);
//
//        final int[] days_number = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
//        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                for (int i=1; i<=days_number[position]; i++){
//                    days.add(Integer.toString(i));
//
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, days);
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    ArrayAdapter<String> combo = adapter;
//                    spinner_day.setAdapter(combo);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        alertBuilder.setPositiveButton(getContext().getString(R.string.recherche), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                final String key_word = champ_keyword.getText().toString();
//                String cond;
//                int selected = search_radio_group.getCheckedRadioButtonId();
//                if (selected == R.id.radio_after) {
//                    cond = "after";
//                } else if (selected == R.id.radio_before) {
//                    cond = "before";
//                } else {
//                    cond = "on";
//                }
//                Integer month = spinner_month.getSelectedItemPosition() + 1;
//                final String date = spinner_year.getSelectedItem() + "-" +
//                        month + "-" +
//                        spinner_day.getSelectedItem();
//                final String local_condition = cond;
//
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        condition = key_word + "/" + local_condition + "/" + date+"/";
//                        chargerParole(1);
//                    }
//                });
//            }
//        });
//        alertBuilder.setNegativeButton(getContext().getString(R.string.annuler), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getContext(), getContext().getString(R.string.annulé), Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });
//
//        final AlertDialog dialog = alertBuilder.create();
//        dialog.show();
//    }
}
