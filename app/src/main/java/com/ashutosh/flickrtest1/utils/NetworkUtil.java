package com.ashutosh.flickrtest1.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ashutosh.flickrtest1.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;

import org.json.JSONObject;

/**
 * Created by Vostro-Daily on 9/29/2017.
 */

public class NetworkUtil {

    private static final String TAG = NetworkUtil.class.getSimpleName();

    /* Load image using glide (with placeholder) center crop */
    public static void loadImage(Context context, String imgUrl, ImageView imgView, RequestListener requestListener){
        Glide.with(context).load(imgUrl).placeholder(R.drawable.placeholder).centerCrop()
                .listener(requestListener).into(imgView);
    }

    /* Fetch flickr images data from server using Flick API */
    public static void fetchData(Context context, int pageNumber, int perPageLimit, final FetchDataSuccessCallback successCallback) {

        Uri uri = Uri.parse(Constants.BASE_URL_FLICKR)
                .buildUpon()
                .appendQueryParameter("per_page", String.valueOf(perPageLimit))
                .appendQueryParameter("page", String.valueOf(pageNumber))
                .build();

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET,
                uri.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responseJson) {
                        Log.d(TAG, responseJson.toString());

                        if(successCallback != null){
                            successCallback.onFetchSuccess(responseJson);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(successCallback != null){
                            successCallback.onFailure(error.getMessage());
                        }
                    }
                });

        VolleySingleton.getInstance(context).addRequest(jsonObjRequest);
    }

    public interface FetchDataSuccessCallback {
        void onFetchSuccess(JSONObject jsonObject);
        void onFailure(String msg);
    }
}
