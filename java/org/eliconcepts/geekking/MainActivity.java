package org.eliconcepts.geekking;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    private BottomNavigationView nav;
    private FrameLayout frame;
    Dialog myDialog;
    MediaPlayer happy_song;
    MediaPlayer sad_song;

    private ChatFragment chatFragment;
    private MusicFragment musicFragment;

    private ViewPager viewPager;
    MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frame = (FrameLayout) findViewById(R.id.main_frame);
        nav = (BottomNavigationView) findViewById(R.id.navigation);
        nav.setSelectedItemId(R.id.chatButton);

        final String[] settings_list = {"History", "Account", "About", "Log Out"};

        //finding out what is the current emotion
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
        if (my_emo.equals("happiness")) { //play happy song
            happy_song = MediaPlayer.create(MainActivity.this, R.raw.happy);
            happy_song.start();
        }
        if (my_emo.equals("sadness")) { //play sad song
            sad_song = MediaPlayer.create(MainActivity.this, R.raw.sad);
            sad_song.start();
        }

        myDialog = new Dialog(this);

        viewPager = (ViewPager) findViewById(R.id.pager);

        chatFragment = new ChatFragment();
        musicFragment = new MusicFragment();

        nav.setItemTextColor(getColorStateList(R.color.bottom_nav_icon_color_selector_chat));
        nav.setItemIconTintList(getColorStateList(R.color.bottom_nav_icon_color_selector_chat));

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.chatButton:
                        viewPager.setCurrentItem(0);
                        nav.setItemTextColor(getColorStateList(R.color.bottom_nav_icon_color_selector_chat));
                        nav.setItemIconTintList(getColorStateList(R.color.bottom_nav_icon_color_selector_chat));
                        return true;

                    case R.id.musicButton:
                        viewPager.setCurrentItem(1);
                        nav.setItemTextColor(getColorStateList(R.color.bottom_nav_icon_color_selector));
                        nav.setItemIconTintList(getColorStateList(R.color.bottom_nav_icon_color_selector));
                        return true;

                    case R.id.action_settings:
                        nav.setItemTextColor(getColorStateList(R.color.bottom_nav_icon_color_selector));
                        nav.setItemIconTintList(getColorStateList(R.color.bottom_nav_icon_color_selector));

                        final Dialog alertDialog = new Dialog(MainActivity.this);
                        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.fragment_settings, null);

                        ListAdapter theAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, settings_list);
                        ListView theListView = (ListView) view.findViewById(R.id.simpleListView);
                        theListView.setAdapter(theAdapter);
                        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (parent.getItemAtPosition(position).equals("Log Out")){
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                                    alertDialogBuilder.setTitle("Log Out");
                                    alertDialogBuilder
                                            .setMessage("Do you want to log out?")
                                            .setCancelable(false)
                                            .setPositiveButton("Yes, I'm Sure", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                                                    SharedPreferences.Editor Ed=sp.edit();
                                                    Ed.putString("Unm", "0" );
                                                    Ed.putString("Psw", "0");
                                                    Ed.apply();
                                                    Intent intent = new Intent(MainActivity.this, tryactivity.class);
                                                    startActivity(intent);
                                                }
                                            })
                                            .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();

                                }
                            }
                        });


                        View backgroundimage = view.findViewById(R.id.background);
                        Drawable background = backgroundimage.getBackground();
                        background.setAlpha(70);

                        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.title);
                        setSupportActionBar(mToolbar);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);

                        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.cancel();
                            }
                        });

                        Gif screenShot = new Gif();
                        Bitmap map = screenShot.takeScreenShot(MainActivity.this);
                        Bitmap fast = screenShot.fastblur(map, 20);
                        final Drawable draw = new BitmapDrawable(getResources(), fast);
                        alertDialog.getWindow().setBackgroundDrawable(draw);
                        alertDialog.setContentView(view);
                        alertDialog.show();

                        return true;

                    default:
                        return false;
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    nav.getMenu().getItem(0).setChecked(false);
                }
                if (position == 1){
                    nav.setItemTextColor(getColorStateList(R.color.bottom_nav_icon_color_selector));
                    nav.setItemIconTintList(getColorStateList(R.color.bottom_nav_icon_color_selector));
                }
                else{
                    nav.setItemTextColor(getColorStateList(R.color.bottom_nav_icon_color_selector_chat));
                    nav.setItemIconTintList(getColorStateList(R.color.bottom_nav_icon_color_selector_chat));
                }
                nav.getMenu().getItem(position).setChecked(true);
                prevMenuItem = nav.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);


    }

    private void setupViewPager(ViewPager viewPager) {
        Slider adapter = new Slider(getSupportFragmentManager());
        chatFragment = new ChatFragment();
        musicFragment = new MusicFragment();
        adapter.addFragment(chatFragment);
        adapter.addFragment(musicFragment);
        viewPager.setAdapter(adapter);
    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

}
