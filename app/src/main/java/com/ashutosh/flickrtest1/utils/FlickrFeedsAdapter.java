package com.ashutosh.flickrtest1.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ashutosh.flickrtest1.NetworkingActivity;
import com.ashutosh.flickrtest1.R;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

/**
 * Created by Vostro-Daily on 9/26/2017.
 */

public class FlickrFeedsAdapter extends RecyclerView.Adapter<FlickrFeedsAdapter.FlickrFeedsViewHolder> {

    private static final String TAG = FlickrFeedsAdapter.class.getSimpleName();
    private final Context context;
    private List<String> imgUrlsList;

    private int itemWidth;
    private int itemHeight;

    public FlickrFeedsAdapter(Context context, List<String> imgUrlsList) {
        this.context = context;
        this.imgUrlsList = imgUrlsList;
        /* items are as wide as screen width */
        this.itemWidth = Utility.getScreenWidth(context);
        this.itemHeight = context.getResources().getDimensionPixelSize(R.dimen.feed_item_height);

        Log.d(TAG, "Width:Height = " + itemWidth + ":" + itemHeight);
    }

    @Override
    public FlickrFeedsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FlickrFeedsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flickr_feed, parent, false));
    }

    @Override
    public void onBindViewHolder(final FlickrFeedsViewHolder holder, int position) {
        /* Giving specific width and height to to transform images to this size & save images for only this size in memory */
        NetworkUtil.loadImage(context, imgUrlsList.get(position), holder.imgvFlickrFeed,
                itemWidth, itemHeight,
                new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        Log.d(TAG, "" + e.getMessage());
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progress.setVisibility(View.GONE);

                        /* we can decrease the resource width here, if required */
//                        float width = resource.getIntrinsicWidth();
//                        float height = resource.getIntrinsicHeight();
                        return false;
                    }
                });

        /* clear memory periodically to free up memory */
        if(position % 10 == 0){
            NetworkUtil.clearMemory(context);
        } else if(position % 5 == 0){
            NetworkUtil.trimMemory(context);
        }

        if(position == 0){
            holder.imgvFlickrFeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startNetworkingActivity();
                }
            });
        }
    }

    @Override
    public void onViewRecycled(FlickrFeedsViewHolder holder) {
        super.onViewRecycled(holder);
        NetworkUtil.clearViewMemory(holder.imgvFlickrFeed);
    }

    private void startNetworkingActivity() {
        Intent intent = new Intent(context, NetworkingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return imgUrlsList.size();
    }

    class FlickrFeedsViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgvFlickrFeed;
        private View progress;

        public FlickrFeedsViewHolder(View itemView) {
            super(itemView);

            imgvFlickrFeed = (ImageView) itemView.findViewById(R.id.img_item_flickr_feed);
            progress = itemView.findViewById(R.id.progress);
        }
    }
}
