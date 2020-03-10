package com.example.meanings_downloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {
    FrameLayout container;
    FragmentManager fragmentManager;

    BlankFragment blankFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container=findViewById(R.id.container);
        fragmentManager=getSupportFragmentManager();
        blankFragment=new BlankFragment() ;
        fragmentManager.beginTransaction().replace(R.id.container,blankFragment).commit() ;
    }


}
