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

public class HistoryFragment extends AppCompatActivity {

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

        dbHandler = new DBHandler(HistoryFragment.this);

        System.out.println("INSIDE ONCREATE HISTORY");
        super.onCreate(savedInstanceState);
        System.out.println(savedInstanceState);
        if (savedInstanceState != null){
            history = savedInstanceState.getCharSequenceArrayList("historyMessages");
            System.out.println(history.get(0));
        }


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
*/
    public static void setText(ArrayList messages) {
        String text = "";
        history = messages;
        System.out.println("PRINTING OUT CMAC MESSAGE 2");
        for (int i = 0; i <= messages.size() - 1; i++){
            text += messages.get(i).toString();
            text += "\n\n";
        }

        System.out.println("INSIDE SET TEXT");
        System.out.println(text);
        if (tv != null){
            System.out.println("TV Not Null");
            tv.setText(text);
        }
    }

}