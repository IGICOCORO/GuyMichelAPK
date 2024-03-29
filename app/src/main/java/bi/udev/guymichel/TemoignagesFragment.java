package bi.udev.guymichel;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

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
                        Toast.makeText(activity, R.string.no_internet, Toast.LENGTH_LONG).show();
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
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(activity, R.string.no_more_data, Toast.LENGTH_SHORT).show();
//                            if (Build.VERSION.SDK_INT >= 14) {
                                swipe_layout_emission.setRefreshing(false);
//                            }
//
//                        }
//                    });
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

}
