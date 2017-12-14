package com.jjanzic.puull;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.TextHttpResponseHandler;

import java.io.FileNotFoundException;
import java.io.InputStream;

import cz.msebera.android.httpclient.Header;

public class UploadActivity extends Activity {
    private static final String TAG = "PuullStart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendImage(intent);
            }
        }
        finish();
    }


    void handleSendImage(Intent intent) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            Log.d(TAG, "handleSendImage: sending image from intent");
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                uploadImage(inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    private void uploadImage(InputStream picturePath) {
        ImageUplodader.upload(picturePath, uploadHandler());
    }


    private TextHttpResponseHandler uploadHandler() {
        Toast.makeText(
                getApplicationContext(),
                "Uploading image to pull.pw",
                Toast.LENGTH_LONG
        ).show();
        return new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String imageUrl) {
                Toast.makeText(
                        getApplicationContext(),
                        "Upload successful, share the link",
                        Toast.LENGTH_SHORT
                ).show();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, imageUrl);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, imageUrl));
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                String progressMsg = "Uploaded: " + String.valueOf(bytesWritten / totalSize);
                Log.d(TAG, progressMsg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
                Toast.makeText(
                    getApplicationContext(),
                    "Uploading image failed!",
                    Toast.LENGTH_SHORT
                ).show();
            }
        };
    }
}
