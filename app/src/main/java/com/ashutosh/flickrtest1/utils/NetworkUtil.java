package com.ashutosh.flickrtest1.utils;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ashutosh.flickrtest1.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;

import org.json.JSONObject;

/**
 * Created by Vostro-Daily on 9/29/2017.
 */

public class NetworkUtil {

    private static final String TAG = NetworkUtil.class.getSimpleName();

    /* Load image using glide (with placeholder) center crop */
    public static void loadImage(Context context, String imgUrl, ImageView imgView, RequestListener requestListener){
        if(imgView != null){
            Glide.with(context)
                    .load(imgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT) // only cache result image
                    .skipMemoryCache(true)  // don't cache this image in memory, though disk cache will still work
                    .placeholder(R.drawable.placeholder)
                    .centerCrop()   // maintain the aspect ratio of image but show image fully in the view.
                    .listener(requestListener)
                    .into(imgView);
        }
    }

    /* Load image using glide (with placeholder) center crop */
    public static void loadImage(Context context, String imgUrl, ImageView imgView, int width, int height, RequestListener requestListener){
        if(imgView != null){
            Glide.with(context)
                    .load(imgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT) // only cache result image
                    .skipMemoryCache(true)  // don't cache this image in memory, though disk cache will still work
                    .placeholder(R.drawable.placeholder)
                    .override(width, height)
                    .centerCrop()   // maintain the aspect ratio of image but show image fully in the view.
                    .listener(requestListener)
                    .into(imgView);
        }
    }

    public static void clearViewMemory(View view){
        if(view != null){
            Glide.clear(view);
        }
    }

    /* should be called periodically & not continuously */
    public static void clearMemory(Context context){
        Log.d(TAG, "Clear Glide Memory");
        Glide.get(context).clearMemory();
    }

    public static void setMemoryCategoryNormal(Context context){
        Glide.get(context).setMemoryCategory(MemoryCategory.NORMAL);
    }

    public static void setMemoryCategoryLow(Context context){
        Glide.get(context).setMemoryCategory(MemoryCategory.LOW);
    }

    public static void trimMemory(Context context){
        Glide.get(context).trimMemory(ComponentCallbacks2.TRIM_MEMORY_MODERATE);
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

        jsonObjRequest.setTag(Constants.COMMON_REQUEST_TAG);
        VolleySingleton.getInstance(context).addRequest(jsonObjRequest);
    }

    public interface FetchDataSuccessCallback {
        void onFetchSuccess(JSONObject jsonObject);
        void onFailure(String msg);
    }
}
