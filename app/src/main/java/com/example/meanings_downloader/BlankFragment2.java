package com.example.meanings_downloader;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class BlankFragment2 extends Fragment {

    int count_fav_meaning;
    TextView textView_count;


  public  BlankFragment2(int count){
      this.count_fav_meaning=count;
  }
public BlankFragment2(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_blank_fragment2, container, false);
       textView_count=view.findViewById(R.id.favmeaning_count);
        return view;
    }

    @Override
    public void onResume() {
textView_count.setText(String.valueOf(count_fav_meaning));
        super.onResume();
    }
}
