package com.example.smarthomegesturetraining;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DemoActivity extends AppCompatActivity {

    private static final int REQUEST_ID = 1;
    private static final int VIDEO_TIME = 5;
    public static final String MYPREF = "practice_number_pref";
    private static final String USER_LNAME = "Sharma";

    String currGesture;
    String demoFileName;
    String practiceFile_GestureName;
    String practiceFile_fullName;

    VideoView vvDemo;
    Button practiceBtn;
    Button uploadBtn;
    private Uri vvURI_Demo;
    private Uri vvURI_Practice;
    VideoView vvPractice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_demo);
        vvDemo = findViewById(R.id.videoview_demo);
        vvPractice = findViewById(R.id.videoview_practice);

        Intent intent = getIntent();
        currGesture = intent.getStringExtra("Gesture Name");
        setDemoAndPracticeFileName(currGesture);

        setVideoView(demoFileName);

        practiceBtn = (Button) findViewById(R.id.demo_practice_btn);
        practiceBtn.setOnClickListener(v -> recordPracticeView(currGesture));

        uploadBtn = findViewById(R.id.demo_upload_btn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToServer();
                finish();
            }
        });
        uploadBtn.setVisibility(View.GONE);
        vvPractice.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        vvDemo.setVideoURI(vvURI_Demo);
        vvDemo.start();
        vvDemo.setOnCompletionListener (mediaPlayer -> vvDemo.start());

        if(vvURI_Practice != null) {
            vvPractice.setVideoURI(vvURI_Practice);
            vvPractice.start();
            vvPractice.setOnCompletionListener (mediaPlayer -> vvPractice.start());
        }
    }

    private void setDemoAndPracticeFileName(String currGesture) {
        if(currGesture.equals("Turn On Light")) {
            demoFileName = "lighton";
            practiceFile_GestureName = "LightOn";
        }
        else if(currGesture.equals("Turn Off Light")) {
            demoFileName = "lightoff";
            practiceFile_GestureName = "LightOff";
        }
        else if(currGesture.equals("Turn On Fan")) {
            demoFileName = "fanon";
            practiceFile_GestureName = "FanOn";
        }
        else if(currGesture.equals("Turn Off Fan")) {
            demoFileName = "fanoff";
            practiceFile_GestureName = "FanOff";
        }
        else if(currGesture.equals("Increase Fan Speed")) {
            demoFileName = "increasefanspeed";
            practiceFile_GestureName = "FanUp";
        }
        else if(currGesture.equals("Decrease Fan Speed")) {
            demoFileName = "decreasefanspeed";
            practiceFile_GestureName = "FanDown";
        }
        else if(currGesture.equals("Set Thermostat")) {
            demoFileName = "setthermo";
            practiceFile_GestureName = "SetThermo";
        }
        else if(currGesture.equals("0")) {
            demoFileName = "h0";
            practiceFile_GestureName = "Num0";
        }
        else if(currGesture.equals("1")) {
            demoFileName = "h1";
            practiceFile_GestureName = "Num1";
        }
        else if(currGesture.equals("2")) {
            demoFileName = "h2";
            practiceFile_GestureName = "Num2";
        }
        else if(currGesture.equals("3")) {
            demoFileName = "h3";
            practiceFile_GestureName = "Num3";
        }
        else if(currGesture.equals("4")) {
            demoFileName = "h4";
            practiceFile_GestureName = "Num4";
        }
        else if(currGesture.equals("5")) {
            demoFileName = "h5";
            practiceFile_GestureName = "Num5";
        }
        else if(currGesture.equals("6")) {
            demoFileName = "h6";
            practiceFile_GestureName = "Num6";
        }
        else if(currGesture.equals("7")) {
            demoFileName = "h7";
            practiceFile_GestureName = "Num7";
        }
        else if(currGesture.equals("8")) {
            demoFileName = "h8";
            practiceFile_GestureName = "Num8";
        }
        else if(currGesture.equals("9")) {
            demoFileName = "h9";
            practiceFile_GestureName = "Num9";
        }
    }

    private void recordPracticeView(String fileName) {

        practiceFile_fullName = practiceFile_GestureName + "_PRACTICE_" + getPracticeNumber(practiceFile_GestureName) + "_" + USER_LNAME + ".mp4";
        File fileVideo = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PracticeVideos/" + practiceFile_fullName);

        Intent record = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        record.putExtra(MediaStore.EXTRA_DURATION_LIMIT, VIDEO_TIME);

        vvURI_Practice = FileProvider.getUriForFile(getApplicationContext(),
                "com.example.smarthomegesturetraining.provider", fileVideo);
        record.putExtra(MediaStore.EXTRA_OUTPUT, vvURI_Practice);

        startActivityForResult(record, REQUEST_ID);
    }

    private int getPracticeNumber(String practiceFile_gestureName) {
        SharedPreferences pref = getSharedPreferences(MYPREF,MODE_PRIVATE);

        int pracNum=0;

        if(!pref.contains(practiceFile_gestureName)) {
            pracNum = 1;
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(practiceFile_gestureName, pracNum);
            editor.apply();
        }
        else if(pref.contains(practiceFile_gestureName)) {
            pracNum = pref.getInt(practiceFile_gestureName,0)+1;
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(practiceFile_gestureName, pracNum);
            editor.apply();
        }

        return pracNum;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_ID && resultCode == RESULT_OK) {
            uploadBtn.setVisibility(View.VISIBLE);
            vvPractice.setVisibility(View.VISIBLE);

            vvPractice.setVideoURI(vvURI_Practice);
            vvPractice.start();
            vvPractice.setOnCompletionListener (mediaPlayer -> vvPractice.start());

            Toast.makeText(this, "video loc: /Internal Storage/PracticeVideos/" + practiceFile_fullName,Toast.LENGTH_LONG).show();
        }
    }

    private void setVideoView(String videoName) {
        int resId = this.getResources().getIdentifier(videoName, "raw", this.getPackageName());
        vvURI_Demo = Uri.parse("android.resource://" + getPackageName() + "/" + resId);
        vvDemo.setVideoURI(vvURI_Demo);

        vvDemo.start();
        vvDemo.setOnCompletionListener (mediaPlayer -> vvDemo.start());
    }

    public void uploadToServer() {
        File videoPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PracticeVideos/");
        File sourceFile = new File(videoPath, practiceFile_fullName);

        MultipartBody.Builder builder=new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("myfile",sourceFile.getName().toString(), RequestBody.create(MediaType.parse("/"),sourceFile));
        MultipartBody multipartBody = builder.build();

        String urlStr = "http://" + "192.168.0.104" + ":" + 5000 + "/api/upload";
        Request request = new Request
                .Builder()
                .post(multipartBody)
                .url(urlStr)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DemoActivity.this, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Toast.makeText(DemoActivity.this,response.body().string(),Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
