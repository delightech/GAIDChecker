package com.shinjukujohnny.gaidchecker;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity  {
    public static EditText edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int i = 0;
        while(i < 20) {
            // level up
            AdjustEvent event = new AdjustEvent("pqb3cq");
            Adjust.trackEvent(event);

            // purchase
            event = new AdjustEvent("cho60f");
            event.setRevenue(100, "JPY");
            Adjust.trackEvent(event);
            i++;
            System.out.println(i);
        }

        // AdvertisingIdClient cannot be called in the main thread.
        AsyncTask<Void, Void, String> task = new AdIdTask(this);
        task.execute();

        edit = new EditText(this);
        edit.setWidth(280);
        setContentView(edit);
    }
}

/**
 * Android ADIDは別スレッドで取得する必要があるため下記実装となる。
 */
class AdIdTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = "GPS";
    private Activity mActivity;

    AdIdTask(Activity activity) {
        mActivity = activity;
    }

    @Override
    protected String doInBackground(Void... params) {
        String advertisingId = "";
        try {
            AdvertisingIdClient.Info info =
                    AdvertisingIdClient.getAdvertisingIdInfo(mActivity.getApplicationContext());
            advertisingId = info.getId();
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, "GooglePlayServices not available.");
        } catch (GooglePlayServicesRepairableException | IOException e) {
        }
        return advertisingId;
    }

    @Override
    protected void onPostExecute(String id) {
        Log.d("DEBUG", "###GAID###" + id);
        MainActivity.edit.setText(id, TextView.BufferType.NORMAL);
    }
}