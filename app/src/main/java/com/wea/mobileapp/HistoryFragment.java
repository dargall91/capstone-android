package com.wea.mobileapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wea.local.DBHandler;
import com.wea.local.model.CMACMessageModel;
import com.wea.models.CMACMessage;
import com.wea.mobileapp.databinding.HistoryFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    private HistoryFragmentBinding binding;
    private static TextView tv;
    private static List<String> history;
    private DBHandler dbHandler;
    LayoutInflater inflater;
    private View view;
    private List<CMACMessageModel> alertModalArrayList;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        this.inflater = inflater;
        view = inflater.inflate(R.layout.history_fragment, container, false);
        history = new ArrayList<>();
        binding = HistoryFragmentBinding.inflate(inflater, container, false);

        alertModalArrayList = new ArrayList<>();

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        recyclerView.setAdapter(new CMACRVAdapter(alertModalArrayList, view.getContext().getApplicationContext()));
    }
}