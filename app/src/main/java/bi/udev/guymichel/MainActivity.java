package bi.udev.guymichel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static MediaPlayer mediaPlayer;
    TabLayout main_tabs;
    private TabsAdapter tabsAdapter;
    static TextView lbl_playing;
    static SharedPreferences colorPreferences;
    private static Window window;
    static Locale myLocale;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        colorPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        loadLocale();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        main_tabs = (TabLayout) findViewById(R.id.main_tabs);
        ViewPager fragments_container = (ViewPager) findViewById(R.id.fragments_container);
        tabsAdapter = new TabsAdapter(getSupportFragmentManager(), MainActivity.this);
        lbl_playing = (TextView) findViewById(R.id.lbl_playing);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        String color = colorPreferences.getString("color", "sans");
        window = getWindow();
        changeColor();

//        fragments_container.setOffscreenPageLimit(6);
        fragments_container.setAdapter(tabsAdapter);
        main_tabs.setupWithViewPager(fragments_container);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

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
            new AlertSettings(MainActivity.this, MainActivity.this).create().show();
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
    public int getButtonColor(){
        String color_string = colorPreferences.getString("color", "sans");
        int color;
        if(color_string.equalsIgnoreCase("BLEU")){
            color = ResourcesCompat.getColor(getResources(),R.color.colorPrimaryBlue, null);
        }else if(color_string.equalsIgnoreCase("VERT")){
            color = ResourcesCompat.getColor(getResources(),R.color.colorPrimaryGreen, null);
        }else {
            color = ResourcesCompat.getColor(getResources(),R.color.colorPrimary, null);
        }
        return color;
    }
    public void changeColor(){
        String color = colorPreferences.getString("color", "sans");
        if(color.equalsIgnoreCase("BLEU")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimaryDarkBlue, null));
            }
            lbl_playing.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimaryBlue, null));
            main_tabs.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryBlue, null));

            getSupportActionBar().setBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryBlue))
            );
            navigationView.setItemTextColor(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(),R.color.colorPrimaryDarkBlue, null)));
        }else if(color.equalsIgnoreCase("VERT")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimaryDarkGreen, null));
            }
            MainActivity.lbl_playing.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimaryGreen, null));
            main_tabs.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimaryGreen, null));
            getSupportActionBar().setBackgroundDrawable(
                    new ColorDrawable(ResourcesCompat.getColor(getResources(),R.color.colorPrimaryGreen, null))
            );
            navigationView.setItemTextColor(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(),R.color.colorPrimaryDarkGreen, null)));

        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimaryDark, null));
            }
            MainActivity.lbl_playing.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimary, null));
            main_tabs.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimary, null));
            getSupportActionBar().setBackgroundDrawable(
                    new ColorDrawable(ResourcesCompat.getColor(getResources(),R.color.colorPrimary, null))
            );
            navigationView.setItemTextColor(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(),R.color.colorPrimaryDark, null)));
        }
    }
    public void loadLocale() {
        String langPref = "Language";
        String language = colorPreferences.getString("language", "sans");
        changeLang(language);
    }

    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

    }

    public void saveLocale(String lang) {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }
}
