package com.example.tom.webtest;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private SimpleWebServer server;
    private String ip;
    private WifiManager wm;
    File rootFile;

    private final static int port = 8080;
    private final static String INDEX_PATH="/sdcard/FIRST/blocks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // verify that we have permission to access file system.
        // if we don't have permission, ask for permission.
        verifyStoragePermissions(this);

        wm = (WifiManager) getSystemService(WIFI_SERVICE);
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        Log.d("WebServer", "ip = " + ip);
        rootFile = new File(INDEX_PATH);
    }

    @Override
    public void onResume()  {
        super.onResume();

        // try to create a new webserver.
        try {
            server = new SimpleWebServer(ip,port, rootFile,false);
            server.start();
        } catch (Exception e)  {
            Log.e("WebServer", "Exception caught... " + e.getLocalizedMessage());
        }
    }

    @Override
    public void onPause()  {
        super.onPause();

        // stop the web server.
        if (server != null) {
            server.stop();
            Log.d("WebServer", "Server stopped.");
        }
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
