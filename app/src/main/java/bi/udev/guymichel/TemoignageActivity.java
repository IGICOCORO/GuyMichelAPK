package bi.udev.guymichel;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TemoignageActivity extends AppCompatActivity {

    TextView txt_temoignage;
    String slug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temoignage);

        txt_temoignage = (TextView) findViewById(R.id.txt_temoignage);
        slug = getIntent().getStringExtra("slug");

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://"+ Host.url+"/api/message/"+slug).newBuilder();
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
//                .header("Authorization", "Token "+token)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
//                final String mMessage = e.getMessage().toString();
                TemoignageActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TemoignageActivity.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    final String message = jsonObject.getString("message");
                    TemoignageActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(Build.VERSION.SDK_INT >= 24) {
                                txt_temoignage.setText(Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY));
                            }else{
                                txt_temoignage.setText(Html.fromHtml(message));
                            }
                        }
                    });

                } catch (Exception e) {
                }
            }
        });

    }
}
