package com.ashutosh.flickrtest1.utils;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

/**
 * Created by Vostro-Daily on 9/27/2017.
 *
 * This class create a singleton requestQueue for making network networkin
 */

public class VolleySingleton {

    private static VolleySingleton instance;
    private RequestQueue requestQueue;

    private VolleySingleton(Context context) {
        // Instantiate the cache with 1 MB limit which may be good enough
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);

        // Set up the network to use HttpURLConnection as the HTTP client.e
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue.
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
//        requestQueue = Volley.newRequestQueue(context);
    }

    public static VolleySingleton getInstance(Context context){
        if(instance == null){
            instance = new VolleySingleton(context.getApplicationContext());
        }
        return instance;
    }

    public void addRequest(Request<? extends Object> stringRequest){
        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }

    public void cancelAllRequests(String tag){
        requestQueue.cancelAll(tag);
    }

    public void clean(){
        requestQueue = null;
        instance = null;
    }
}
