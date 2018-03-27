package org.eliconcepts.geekking;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.felipecsl.gifimageview.library.GifImageView;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class loadingChat extends AppCompatActivity {

    static GifImageView chatLoad;
    static GifImageView loading;
    static TextView lookFor;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_for_chat);
        loading = (GifImageView) findViewById(R.id.loading);
        lookFor = (TextView) findViewById(R.id.looking);
        //get the emotion of the user from client class
        String my_emo;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                my_emo = null;
            } else {
                my_emo = extras.getString("MY_EMO");
            }
        } else {
            my_emo = (String) savedInstanceState.getSerializable("MY_EMO");
        }

        //get the username
        SharedPreferences sp1 = getApplicationContext().getSharedPreferences("Login", MODE_PRIVATE);
        String unm = sp1.getString("Unm", null);
        Pattern p = Pattern.compile("true: (.*)");
        Matcher m = p.matcher(unm);
        String username ="";
        if(m.find()){
            MatchResult mr = m.toMatchResult();
            username = mr.group(1);
        }

        Gif gif2 = new Gif(loading);
        gif2.execute("https://thumbs.gfycat.com/VigilantWellwornBoa-size_restricted.gif");
        loading.startAnimation();
        loading.setVisibility(View.VISIBLE);

        chatLoad = (GifImageView) findViewById(R.id.loadForChat);
        Gif gif = new Gif(chatLoad);
        gif.execute("https://thumbs.gfycat.com/AnxiousElegantCarp-max-1mb.gif");
        chatLoad.startAnimation();
        chatLoad.setVisibility(View.VISIBLE);
        String msg;
        MyTaskParams.Client my_client = new MyTaskParams.Client(loadingChat.this);
        msg = my_client.Go(username, my_emo);
        MyTaskParams params = new MyTaskParams(msg, null, null);
        my_client.execute(params);
    }
}
