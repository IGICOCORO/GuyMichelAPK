package bi.udev.guymichel;


import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArchivesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView recyclerView;
    private ArrayList<Parole> paroles;
    private int page;
    private GridLayoutManager gridLayoutManager;
    private AdaptateurArchive adaptateur;
    SwipeRefreshLayout swipe_layout_emission;
    private MainActivity activity;

    public ArchivesFragment() {
    }

    public ArchivesFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_emissions, container, false);
        setHasOptionsMenu(true);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_recycler);
        swipe_layout_emission = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout_emission);

        paroles = new ArchiveModel(getContext()).getAll();

        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(gridLayoutManager);

        adaptateur = new AdaptateurArchive(getContext(), paroles);
        recyclerView.setAdapter(adaptateur);
        swipe_layout_emission.setOnRefreshListener(this);


        swipe_layout_emission.post(new Runnable() {

            @Override
            public void run() {

                if (swipe_layout_emission != null) {
                    swipe_layout_emission.setRefreshing(true);
                }
                getArchives();
            }
        });

        return rootView;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu);
        MenuItem menuItem = (MenuItem) menu.findItem(R.id.action_search);
        menu.removeItem(menuItem.getItemId());
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            new AlertSettings(getContext(), activity).create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        activity = getActivity();
//    }

    public void getArchives(){
        paroles = new ArchiveModel(getContext()).getAll();
        adaptateur.setData(paroles);
        adaptateur.notifyDataSetChanged();
        swipe_layout_emission.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        getArchives();
    }
}
