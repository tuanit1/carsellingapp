package com.example.autosellingapp.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.autosellingapp.R;
import com.example.autosellingapp.items.ManufacturerItem;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FragmentHome extends Fragment {

    private View rootView;
    private ArrayList<ManufacturerItem> arrayList_manu;
    private RecyclerView rv_manu;
    private CardView cv_search;
    private NestedScrollView scrollView;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        Hook();


        return rootView;
    }

    private void Hook(){
        rv_manu = rootView.findViewById(R.id.rv_manu);
        cv_search= rootView.findViewById(R.id.cv_search);
        scrollView = rootView.findViewById(R.id.home_scroll_view);
        progressBar = rootView.findViewById(R.id.progressBar_home);
    }
}