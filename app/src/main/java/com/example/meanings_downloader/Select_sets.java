package com.example.meanings_downloader;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class Select_sets extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
   static  List<Integer> num_of_sets_to_recycler_view=new ArrayList<>();
    Adapter_for_sets_selection adapter_for_sets_selection;
    static Adapter.Clicklistener clicklistener;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private int num_of_sets;


    public Select_sets() {
        // Required empty public constructor
    }


    public static Select_sets newInstance(String param1, String param2, Adapter.Clicklistener clicklistener) {
        Select_sets fragment = new Select_sets();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
       Select_sets.clicklistener=clicklistener;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
           num_of_sets =Integer.parseInt( getArguments().getString(ARG_PARAM2));

           for(int i=1;i<=num_of_sets;i++)
           {
               num_of_sets_to_recycler_view.add(i);
           }
           adapter_for_sets_selection=new Adapter_for_sets_selection(num_of_sets,num_of_sets_to_recycler_view,clicklistener);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View v=  inflater.inflate(R.layout.fragment_select_sets, container, false);
        recyclerView=v.findViewById(R.id.sets_recycler_view);

        linearLayoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter_for_sets_selection);
        adapter_for_sets_selection.notifyDataSetChanged();
        return v;
    }





}
