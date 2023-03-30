package com.wea.mobileapp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.wea.interfaces.WeaApiInterface;
import com.wea.local.DBHandler;
import com.wea.local.SavedDataModel;
import com.wea.models.CMACMessage;
import com.wea.models.CollectedDeviceData;

import org.w3c.dom.Text;

import java.util.List;

public class CMACRVAdapter extends RecyclerView.Adapter<CMACRVAdapter.ViewHolder> {
    private List<SavedDataModel> savedData;
    private DBHandler dbHandler;
    private Context context;

    public CMACRVAdapter(DBHandler dbHandler, Context context) {
        this.dbHandler = dbHandler;
        savedData = dbHandler.getAllRows();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        savedData = dbHandler.getAllRows();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_rv_item, parent, false);

        view.setOnClickListener(view1 -> {
            LinearLayout cardLayout = (LinearLayout) ((ViewGroup) view1).getChildAt(0);
            TextView uri = (TextView) cardLayout.getChildAt(2);
            CollectedDeviceData collectedDeviceData = WeaApiInterface.getSingleResult(CollectedDeviceData.class,
                    (String) uri.getText());

            final AlertDialog.Builder dataDialog = new AlertDialog.Builder(context);

        });

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SavedDataModel model = savedData.get(position);
        holder.cmacAlertID.setText("Message Number: " + model.getMessageNumber());
        holder.date.setText("Time Received: " + model.getDateTime());
        holder.uri.setText("Time Received: " + model.getUri());
    }

    @Override
    public int getItemCount() {
        return savedData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView cmacAlertID;
        private TextView date;
        private TextView uri;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cmacAlertID = itemView.findViewById(R.id.idCMACAlertID);
            date = itemView.findViewById(R.id.date);
            uri = itemView.findViewById(R.id.uri);
        }
    }
}
