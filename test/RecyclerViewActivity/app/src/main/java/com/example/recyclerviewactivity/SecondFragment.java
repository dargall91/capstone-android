package com.example.recyclerviewactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerviewactivity.databinding.FragmentSecondBinding;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Item> items = new ArrayList<Item>();
        items.add(new Item("John", "john@email.com", R.drawable.a));
        items.add(new Item("Bob", "Bob@email.com", R.drawable.a));
        items.add(new Item("Becky", "Becky@email.com", R.drawable.a));
        items.add(new Item("Miller", "Miller@email.com", R.drawable.a));
        items.add(new Item("Sara", "Sara@email.com", R.drawable.a));
        items.add(new Item("Seline", "Seline@email.com", R.drawable.a));
        items.add(new Item("John", "john@email.com", R.drawable.a));
        items.add(new Item("Bob", "Bob@email.com", R.drawable.a));
        items.add(new Item("Becky", "Becky@email.com", R.drawable.a));
        items.add(new Item("Miller", "Miller@email.com", R.drawable.a));
        items.add(new Item("Sara", "Sara@email.com", R.drawable.a));
        items.add(new Item("Seline", "Seline@email.com", R.drawable.a));
        items.add(new Item("John", "john@email.com", R.drawable.a));
        items.add(new Item("Bob", "Bob@email.com", R.drawable.a));
        items.add(new Item("Becky", "Becky@email.com", R.drawable.a));
        items.add(new Item("Miller", "Miller@email.com", R.drawable.a));
        items.add(new Item("Sara", "Sara@email.com", R.drawable.a));
        items.add(new Item("Seline", "Seline@email.com", R.drawable.a));

//        View view = inflater.inflate(R.layout.fragment_second, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new MyAdapter(view.getContext().getApplicationContext(), items));

//        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(SecondFragment.this)
//                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}