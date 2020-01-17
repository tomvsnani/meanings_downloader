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

public class MainActivity extends AppCompatActivity implements Adapter.Clicklistener {
    FrameLayout container;
    FragmentManager fragmentManager;
    BlankFragment blankFragment;
    Toolbar toolbar;
    FragmentManager.OnBackStackChangedListener onBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            Log.d("printing", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
        }
    };


   /* protected void onResume() {
        String tag="first";
        //Log.d("camehere","previous2");
        if (getSupportFragmentManager().findFragmentByTag( tag ) == null) {
            // No fragment in backStack with same tag..
            Log.d("camehere","previous");
            getSupportFragmentManager().beginTransaction().add(R.id.container,blankFragment,tag)
            .addToBackStack(null)
            .commit ();

        }
        else {
            Log.d("camehere","previous1");
            getSupportFragmentManager().beginTransaction().show(blankFragment).commit();
        }
        super.onResume();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = findViewById(R.id.container);
        fragmentManager = getSupportFragmentManager();
        blankFragment = new BlankFragment();
        fragmentManager.beginTransaction().add(R.id.container, blankFragment, "first").addToBackStack("first").commit();
        getSupportFragmentManager().addOnBackStackChangedListener(onBackStackChangedListener);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onclick(int adapterposition, Entity entity, View view, ProgressBar progressBar) {

        if (view.getId() == R.id.add_fav_meaning) {
            Log.d("getting", view.getTag().toString());
            if (((ImageView) view).getTag().equals(getResources().getString(R.string.like))) {
                ((ImageView) view).setImageResource(R.drawable.likesfill);
                ((ImageView) view).setTag(getResources().getString(R.string.likenot));
            } else {
                ((ImageView) view).setImageResource(R.drawable.likes);
                view.setTag(getResources().getString(R.string.like));
            }
        } else {
            getSupportFragmentManager().beginTransaction().hide((Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag("first")))).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.container, inner_meanings_fragment.newInstance((entity.getMeaning_of_word()), entity.getExample()), "second").addToBackStack("second").commit();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {
        getSupportFragmentManager().beginTransaction().show((Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag("first")))).commit();

        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else
            // getSupportFragmentManager().popBackStackImmediate();
            super.onBackPressed();
    }
}
