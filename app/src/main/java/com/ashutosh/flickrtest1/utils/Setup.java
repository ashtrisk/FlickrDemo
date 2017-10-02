package com.ashutosh.flickrtest1.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.squareup.okhttp.OkHttpClient;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/* Using custom timeout with glide to avoid SocketTimeoutException (default glide timeout is 2500 ms) */
public class Setup implements GlideModule {
    @Override public void applyOptions(Context context, GlideBuilder builder) {
        // Note : If not specified glide calculates memory cache size based on device memory class & the screen resolution
        // But since we may need to assign still lower memory cache, so we assign it here.
//        int memoryCacheSizeBytes = 1024 * 1024 * 10; // Set memory cache max size 10 MB

        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int memoryCacheSizeBytes = calculator.getMemoryCacheSize() / 2;
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));

        int memoryBitmapPoolSize = calculator.getBitmapPoolSize() / 2;
        builder.setBitmapPool( new LruBitmapPool(memoryBitmapPoolSize));

        // Note : Glide by default uses RGB_%^% decode format
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
    }
    @Override public void registerComponents(Context context, Glide glide) {
        OkHttpClient client = new OkHttpClient();
        /* set 20 + 20 seconds timeout for loading images */
        client.setConnectTimeout(20, TimeUnit.SECONDS);
        client.setReadTimeout(20, TimeUnit.SECONDS);
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
    }
}