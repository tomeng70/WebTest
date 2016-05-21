package com.example.tom.webtest;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by tom on 5/20/16.
 */
public class WebServer extends NanoHTTPD {

    // constant values.

    // use port 8080 because port 80 on Android requires SU access.
    private final static int PORT = 8080;
    private final static String INDEX_PATH="/sdcard/FIRST/blocks/index.htm";

    // construction.
    public WebServer() throws IOException {
        super(PORT);
        start();
        Log.d("WebServer", "NanoHTTPD-based server is running on port 8080.");
    }

    // respond to request from client.
    @Override
    public Response serve(IHTTPSession session)  {
        String msg = "";

        // open file.
        msg = readIndexFile();

        return newFixedLengthResponse(msg);
    }

    // open the default index HTML file.
    private String readIndexFile() {
        String path;
        String msg = "";
        File root = Environment.getExternalStorageDirectory();
        //path = root.getAbsolutePath() + INDEX_PATH;
        path = INDEX_PATH;
        try {
            Log.d("WebServer", "path = " + path);
            FileReader index = new FileReader(path);
            BufferedReader reader = new BufferedReader(index);
            String line="";
            while (((line = reader.readLine()) != null))  {
                msg += line;
                Log.d("WebServer", "line = " + line);
            }
            reader.close();

        } catch (IOException e) {
            Log.e("WebServer",  e.getLocalizedMessage());
        }

        return msg;
    }


}
