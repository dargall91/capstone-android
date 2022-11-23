package com.wea.mobileapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.wea.mobileapp.databinding.HistoryFragmentBinding;

import org.w3c.dom.Text;

public class HistoryFragment extends Fragment {

    private HistoryFragmentBinding binding;
    private static TextView tv;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = HistoryFragmentBinding.inflate(inflater, container, false);
        if (savedInstanceState != null){
            tv.setText(savedInstanceState.getCharSequence("ourKey"));
        }
        return binding.getRoot();

    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        if (savedInstanceState != null){
//            tv.setText(savedInstanceState.getCharSequence("ourKey"));
//        }
//    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            tv.setText(savedInstanceState.getCharSequence("ourKey"));
        }
    }

    public void onStart(){
        super.onStart();
        tv = (TextView)getView().findViewById(R.id.textview_second);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence("ourKey", tv.getText());
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HistoryFragment.this)
                        .navigate(R.id.action_HistoryFragment_to_HomeFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static void setText(String text) {
        System.out.println("INSIDE SET TEXT");
        System.out.println(text);
        if (tv != null){
            System.out.println("TV Not Null");
            tv.setText(text);
        }
    }

}