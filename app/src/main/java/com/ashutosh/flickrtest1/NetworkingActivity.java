package com.ashutosh.flickrtest1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ashutosh.flickrtest1.utils.Constants;
import com.ashutosh.flickrtest1.utils.NetworkUtil;
import com.ashutosh.flickrtest1.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class NetworkingActivity extends AppCompatActivity {

    private static final String TAG = NetworkingActivity.class.getSimpleName();
    private EditText mEditText;
    private Button mButton;
    private TextView mTxtSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networking);

        mEditText = (EditText) findViewById(R.id.et_num_of_hits);
        mButton = (Button) findViewById(R.id.btn_fetch_data);
        mTxtSuccess = (TextView) findViewById(R.id.txt_success);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = mEditText.getText().toString();

                fetchData(Utility.parseInteger(value));
            }
        });
    }

    private void fetchData(int numOfTimes) {
        Log.d(TAG, "Times: " + numOfTimes);

        for(int i = 0; i < numOfTimes; i++){
            Log.d(TAG, "ResponseMade : " + (i + 1));
            NetworkUtil.fetchData(this, i + 1, Constants.LIMIT_IMAGES_PER_PAGE, new NetworkUtil.FetchDataSuccessCallback() {
                @Override
                public void onFetchSuccess(JSONObject jsonObject) {
                    try {
                        int page = jsonObject.getJSONObject("photos").getInt("page");

                        Utility.showToast(NetworkingActivity.this, "Page Fetched: " + page);
                        mTxtSuccess.setText("Fetched Page: " + page);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String msg) {

                }
            });
        }
    }
}
