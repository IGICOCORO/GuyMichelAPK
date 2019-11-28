package bi.udev.guymichel;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static MediaPlayer mediaPlayer;
    TabLayout main_tabs;
    private TabsAdapter tabsAdapter;
    static TextView lbl_playing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] tabs_names = new String[]{MainActivity.this.getString(R.string.Emission),
                                            MainActivity.this.getString(R.string.Archives),
                                            MainActivity.this.getString(R.string.Messages),};

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        main_tabs = (TabLayout) findViewById(R.id.main_tabs);
        ViewPager fragments_container = (ViewPager) findViewById(R.id.fragments_container);
        tabsAdapter = new TabsAdapter(getSupportFragmentManager(), tabs_names);
        lbl_playing = (TextView) findViewById(R.id.lbl_playing);

//        miniMediaPlayer.setVisibility(View.INVISIBLE);

//        fragments_container.setOffscreenPageLimit(6);
        fragments_container.setAdapter(tabsAdapter);
        main_tabs.setupWithViewPager(fragments_container);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Enabling database for resume support even after the application is killed:
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .build();
        PRDownloader.initialize(this, config);

        lbl_playing.setVisibility(View.INVISIBLE);
        lbl_playing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_emission) {
            main_tabs.getTabAt(0).select();
        } else if (id == R.id.nav_gallery) {
            main_tabs.getTabAt(2).select();
        } else if (id == R.id.nav_archives) {
            main_tabs.getTabAt(1).select();
        } else if (id == R.id.nav_temoigner) {
            String url = "https://api.whatsapp.com/send?phone=+250780746855";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_about) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);

            View vueAlert = MainActivity.this.getLayoutInflater().inflate(R.layout.about_layout, null);
            alertBuilder.setView(vueAlert);
            TextView txt_about = (TextView) vueAlert.findViewById(R.id.txt_about);

            String about = "Yezu aje gusohoza amasezerano. Buri wese abere maso amakamba ye kuko satani ari kurwanira kuyamwambura. Ni igihe cyo gushishoza cyane. Ni igihe cyo gusenga mu kuri. Ni igihe cyo kwisubiraho no guhinduka. Ni igihe cyo gukomera mu kwemera no kutajarajara mu madini. \n" +
                    "\n" +
                    "Umuburo Yezu yampaye guha abantu bose ukubiye muri ayo magambo! \n" +
                    "\n" +
                    "Ku itariki ya 15/08/1998 Yezu yambonekeye ndi gusenga hari nka 20:30 ndi iwanjye mu rugo n'abana banjye turi gusenga isengesho rusange ry'abakristu twitegura kujya kuryama. \n" +
                    "\n" +
                    "Habanje kuza umuyaga mwinshi unyuzuzamo imbaraga nyinshi n'ibyishimo kandi nkumva nshize amanga! Ubwo natangiye kuvuga amagambo numvaga ko atari ayanjye ahubwo ari ayo ntewemo n'uwo n'izo mbaraga. Nahise numva ubwenge bwanjye buhumutse ntangira kumva nsobanukiwe byinshi.\n" +
                    "\n" +
                    "Muri ako kanya nahise mbona Yezu aje imbere yanjye rwose muri distance itari nini ariko sinabashaga kuba namukozaho intoki kuko yari hejuru yanjye. Arambwira ngo \" nje kugutuma ku bana banjye bose ariko ku buryo bw'umwihariko abakristu Gatolika\". Abakristu Gatolika bakomeje kuba indangare bari gupfa nk'abakene kandi muri Gatolika hari ubukungu bwose. \n" +
                    "\n" +
                    "Byabanje kungora kwemera kumukorera ariko nyuma ndemera. \n" +
                    "\n" +
                    "Mu butumwa mfite rero harimo ubwo kuburira abagenzi bagana mu Ijuru ngo bajye bahora bisuzuma kuko Yezu yatubwiye ati \"murabe intungane nk'uko So wo mu Ijuru ari intungane.\n" +
                    "\n" +
                    "Izi nyigisho ntanga nyuze muri audio ni Yezu wazintegetse kandi  ambwira ko ari we uzajya ambwira icyo nigishaho. Zatangiye ku itariki ya 19/03/2017. \n" +
                    "\n" +
                    "Zikaba zigamije kudukebura twebwe abemeye gukurikira Yezu, zikatwereka ishusho nyayo yacu twirebeye mu ndorerwamo y'Ibyanditswe Bitagatifu muri Bibiliya. Tukareba aho dukora neza tugakomeza aho dutsindwa tukisubiraho tugahinduka.\n" +
                    "\n" +
                    "Yezu akomeje kumbwira ngo abantu bitondere ibihe biriho kandi bamenye ko iki gisekuruza aricyo cya nyuma. Niwe Ntangiriro n'Iherezo kandi araje bidatinze!";

//            if(Build.VERSION.SDK_INT >= 24) {
//                txt_about.setText(Html.fromHtml(about, Html.FROM_HTML_MODE_LEGACY));
//            }else{
//                txt_about.setText(Html.fromHtml(about));
//            }
            txt_about.setText(about);
            alertBuilder.setNegativeButton(R.string.ok, null);

            AlertDialog dialog = alertBuilder.create();
            dialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
