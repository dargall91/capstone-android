package com.wea.mobileapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.wea.local.DBHandler;
import com.wea.mobileapp.databinding.HistoryFragmentBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

public class HistoryFragment extends Fragment {

    private HistoryFragmentBinding binding;
    private static TextView tv;
    private static ArrayList  history = new ArrayList<>();
    private DBHandler dbHandler;

/*    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = HistoryFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }*/

//    @Override
////    public void onCreate(@Nullable Bundle savedInstanceState){
////        super.onCreate(savedInstanceState);
////        if (savedInstanceState != null){
////            tv.setText(savedInstanceState.getCharSequence("ourKey"));
////        }
////    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        System.out.println("Testing");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_fragment);

        dbHandler = new DBHandler(this.getActivity());

        if (savedInstanceState != null){
            history = savedInstanceState.getCharSequenceArrayList("historyMessages");
            System.out.println(history.get(0));
        }

    }

    private void setContentView(int history_fragment) {
    }

/*    public void onStart(){
        super.onStart();
        tv = (TextView)getView().findViewById(R.id.textview_second);
    }*/

 /*   @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("INSIDE ONSAVEINSTANCE");
        outState.putStringArrayList("historyMessages", history);
        System.out.println(outState);
    }
*/
/*
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
*/
    public static void setText(ArrayList messages) {
        String text = "";
        history = messages;

        for (int i = 0; i <= messages.size() - 1; i++){
            text += messages.get(i).toString();
            text += "\n\n";
        }

        if (tv != null){
            System.out.println("TV Not Null");
            tv.setText(text);
        }
    }

}