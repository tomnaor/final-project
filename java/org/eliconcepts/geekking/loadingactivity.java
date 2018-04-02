package org.eliconcepts.geekking;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import java.io.IOException;

import static org.eliconcepts.geekking.R.id.loading;

public class loadingactivity extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private ImageView imgview;
    private ImageView img2;
    private Activity loadActivity;
    long animationDuration = 2500;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.loading);


        imgview = (ImageView) findViewById(R.id.logo);
        img2 = (ImageView) findViewById(R.id.faceit);
        /* New Handler to start the Menu-Activity 
         * and close this org.eliconcepts.geekking.Splash-Screen after some seconds.*/
        handleAnimation(imgview);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                //check if user is real - verify of username and password.
                SharedPreferences sp1 = getApplicationContext().getSharedPreferences("Login", MODE_PRIVATE);
                String unm = sp1.getString("Unm", "0");
                String pass = sp1.getString("Psw", "0");
                if (unm.contains("true") && pass.equals("true")){
                    Intent intent = new Intent(loadingactivity.this, WelcomeActivity.class);
                    loadingactivity.this.startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    loadingactivity.this.finish();
                }
                else {
                    Intent mainIntent = new Intent(loadingactivity.this, tryactivity.class);
                    loadingactivity.this.startActivity(mainIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    loadingactivity.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
    public void handleAnimation(View view){
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(imgview, view.ALPHA, 0.0f, 1.0f);
        alphaAnimation.setDuration(animationDuration);
        ObjectAnimator alphaAnimation2 = ObjectAnimator.ofFloat(img2, view.ALPHA, 0.0f, 1.0f);
        alphaAnimation2.setDuration(animationDuration);
        ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(imgview, "rotation", 0f, 720f);
        rotateAnimation.setDuration(animationDuration);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotateAnimation, alphaAnimation, alphaAnimation2);
        animatorSet.start();
    }
}