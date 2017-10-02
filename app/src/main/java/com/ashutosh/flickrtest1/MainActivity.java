package com.ashutosh.flickrtest1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ashutosh.flickrtest1.utils.Constants;
import com.ashutosh.flickrtest1.utils.FlickrFeedsAdapter;
import com.ashutosh.flickrtest1.utils.NetworkUtil;
import com.ashutosh.flickrtest1.utils.RecyclerOnScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRvFlickrFeeds;
    private FlickrFeedsAdapter mFlickrAdapter;
    /* List of image urls to load images to show */
    private List<String> imgUrlList;

    /* page number to fetch more images */
    private int currPageNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* For this task set low memory cache, decreases max. memory usage by half */
        NetworkUtil.setMemoryCategoryLow(this);

        imgUrlList = new ArrayList<>();
        mFlickrAdapter = new FlickrFeedsAdapter(this, imgUrlList);

        mRvFlickrFeeds = (RecyclerView) findViewById(R.id.rv_flickr_feeds);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvFlickrFeeds.setLayoutManager(linearLayoutManager);
        mRvFlickrFeeds.setAdapter(mFlickrAdapter);
        /* Number of items recycler view caches for current visible screen. Default is 2 */
        mRvFlickrFeeds.setItemViewCacheSize(0);

        mRvFlickrFeeds.addOnScrollListener(new RecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                Log.d(TAG, "onLoadMore:" + imgUrlList.size());
                fetchData();
            }
        });

        fetchData();
    }

    /* Fetch more images data using Flickr API */
    private void fetchData() {
        NetworkUtil.fetchData(this, currPageNumber, Constants.LIMIT_IMAGES_PER_PAGE, new NetworkUtil.FetchDataSuccessCallback(){
            @Override
            public void onFetchSuccess(JSONObject responseJson) {
                List<String> imgUrlList = parseResponse(responseJson);

                updateImages(imgUrlList);
//                printUrls();
            }
            @Override
            public void onFailure(String msg) {
//                Log.d(TAG, msg);
            }
        });
    }

    /* return the list of image urls by parsing server response */
    public static List<String> parseResponse(JSONObject jsonObject) {
        List<String> imgUrlList = new ArrayList<>();
        try{
            JSONArray photoArr = jsonObject.getJSONObject("photos").getJSONArray("photo");

            JSONObject photoItem;

            for(int i = 0; i < photoArr.length(); i++){
                photoItem = photoArr.getJSONObject(i);
                if(photoItem.has("url_o")){
                    imgUrlList.add(photoItem.getString("url_o"));
                }
            }

        } catch (JSONException e){
            e.printStackTrace();
        }

        return imgUrlList;
    }

    private void updateImages(List<String> imgUrls) {
        /* increase page number by one so that next request fetches images for next page */
        currPageNumber++;

        int posNewItem = imgUrlList.size();
        imgUrlList.addAll(imgUrls);
//        mFlickrAdapter.notifyDataSetChanged();
        mFlickrAdapter.notifyItemInserted(posNewItem);
    }

    private void printUrls() {
        for(String imgUrl : imgUrlList){
            Log.d(TAG, "IMG = " + imgUrl);
        }
    }
}
