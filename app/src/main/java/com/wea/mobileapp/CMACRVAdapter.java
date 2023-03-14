package com.wea.mobileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wea.local.model.CMACMessageModel;

import java.util.List;

public class CMACRVAdapter extends RecyclerView.Adapter<CMACRVAdapter.ViewHolder> {

    private List<CMACMessageModel> cmacMessageModelArrayList;
    private Context context;

    public CMACRVAdapter(List<CMACMessageModel> cmacMessageModelArrayList, Context context) {
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
        CMACMessageModel modal = cmacMessageModelArrayList.get(position);
        holder.cmacAlertID.setText(modal.getMessageNumber());
    }

    @Override
    public int getItemCount() {
        return cmacMessageModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView cmacAlertID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cmacAlertID = itemView.findViewById(R.id.idCMACAlertID);
        }
    }
}
