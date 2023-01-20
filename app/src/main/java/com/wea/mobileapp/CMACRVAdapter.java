package com.wea.mobileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wea.local.model.CMACMessageModel;

import java.util.ArrayList;

public class CMACRVAdapter extends RecyclerView.Adapter<CMACRVAdapter.ViewHolder> {

    private ArrayList<CMACMessageModel> cmacMessageModelArrayList;
    private Context context;

    public CMACRVAdapter(ArrayList<CMACMessageModel> cmacMessageModelArrayList, Context context) {
        this.cmacMessageModelArrayList = cmacMessageModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return cmacMessageModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView cmacAlertID, cmacSender, cmacMessageType, capIdentifier;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cmacAlertID = itemView.findViewById(R.id.idCMACAlertID);
            cmacSender = itemView.findViewById(R.id.idCMACSender);
            cmacMessageType = itemView.findViewById(R.id.idCMACMessageType);
            capIdentifier = itemView.findViewById(R.id.idCMACCapIdentifier);
        }
    }
}
