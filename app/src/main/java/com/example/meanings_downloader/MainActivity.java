package com.example.meanings_downloader;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements Adapter.Clicklistener {
    FrameLayout container;
    FragmentManager fragmentManager;
    BlankFragment blankFragment;
    int fav_meaning_count=0;
    Database database;
    Bundle bundle;
    FragmentManager.OnBackStackChangedListener onBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            Log.d("printing", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bundle=new Bundle();
        container = findViewById(R.id.container);
        fragmentManager = getSupportFragmentManager();
        blankFragment = new BlankFragment(this);
        fragmentManager.beginTransaction().add(R.id.container, blankFragment, "first").addToBackStack("first").commit();
        getSupportFragmentManager().addOnBackStackChangedListener(onBackStackChangedListener);
        database=Database.Database_create(this);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(String.valueOf(fav_meaning_count),fav_meaning_count);
        bundle=outState;
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {

        onRestoreInstanceState(bundle);
        super.onResume();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fav_meaning_count=savedInstanceState.getInt(String.valueOf(fav_meaning_count));

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onclick(int adapterposition, final Entity entity, View view, ProgressBar progressBar) {

        if (view.getId() == R.id.add_fav_meaning) {
            if (entity.getFav_meaning()==0) {
                ((ImageView) view).setImageResource(R.drawable.likesfill);
                entity.setFav_meaning(1);
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        database.dao().update(entity);
                    }
                });

                fav_meaning_count++;
            }

            else {
                ((ImageView) view).setImageResource(R.drawable.likes);
                fav_meaning_count--;
                entity.setFav_meaning(0);
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        database.dao().update(entity);
                    }
                });

            }

        }

        else {
            getSupportFragmentManager().beginTransaction().hide((Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag("first")))).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.container, inner_meanings_fragment.newInstance((entity.getMeaning_of_word()), entity.getExample()), "second").addToBackStack("second").commit();
        }
    }

    @Override
    public void onclick() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new BlankFragment2(fav_meaning_count)).addToBackStack(null).commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {
        getSupportFragmentManager().beginTransaction().show((Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag("first")))).commit();

        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            if(blankFragment.drawerLayout.isDrawerOpen(GravityCompat.START))
                blankFragment.drawerLayout.closeDrawer(GravityCompat.START);
            else
            finish();
        } else
            // getSupportFragmentManager().popBackStackImmediate();
            super.onBackPressed();
    }
}
