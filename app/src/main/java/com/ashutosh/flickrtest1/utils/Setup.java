package com.ashutosh.flickrtest1.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.squareup.okhttp.OkHttpClient;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/* Using custom timeout with glide to avoid SocketTimeoutException (default glide timeout is 2500 ms) */
public class Setup implements GlideModule {
    @Override public void applyOptions(Context context, GlideBuilder builder) {
    }
    @Override public void registerComponents(Context context, Glide glide) {
        OkHttpClient client = new OkHttpClient();
        /* set 20 + 20 seconds timeout for loading images */
        client.setConnectTimeout(20, TimeUnit.SECONDS);
        client.setReadTimeout(20, TimeUnit.SECONDS);
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
    }
}