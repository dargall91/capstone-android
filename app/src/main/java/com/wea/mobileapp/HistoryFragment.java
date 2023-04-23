package com.wea.mobileapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wea.local.DBHandler;
import com.wea.mobileapp.databinding.HistoryFragmentBinding;

public class HistoryFragment extends Fragment {
    private HistoryFragmentBinding binding;
    private DBHandler dbHandler;
    LayoutInflater inflater;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        this.inflater = inflater;
        binding = HistoryFragmentBinding.inflate(inflater, container, false);
        dbHandler = new DBHandler(this.getContext());

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        recyclerView.setAdapter(new CMACRVAdapter(inflater, dbHandler, view.getContext().getApplicationContext()));
    }
}