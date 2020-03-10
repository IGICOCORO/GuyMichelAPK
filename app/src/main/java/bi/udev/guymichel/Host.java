package bi.udev.guymichel;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

/**
 * Created by KonstrIctor on 11/11/2019.
 */

public class Host {
    private static SharedPreferences colorPreferences;
//    public static final String url = "10.0.2.2:8000";
    public static final String url = "apisaintetrinite.so-mas.net";
    private static String dirPath;

    public Host(Context context) {
        dirPath = context.getExternalFilesDir(null)+ File.separator+"Emission";
    }

    public String getDirPath() {
        return dirPath;
    }


}
