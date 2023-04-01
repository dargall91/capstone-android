package com.wea.mobileapp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wea.interfaces.WeaApiInterface;
import com.wea.local.DBHandler;
import com.wea.local.SavedDataModel;
import com.wea.models.CollectedDeviceData;

import java.util.List;

public class CMACRVAdapter extends RecyclerView.Adapter<CMACRVAdapter.ViewHolder> {
    private List<SavedDataModel> savedData;
    private DBHandler dbHandler;
    private Context context;
    private LayoutInflater inflater;

    public CMACRVAdapter(LayoutInflater inflater, DBHandler dbHandler, Context context) {
        this.inflater = inflater;
        this.dbHandler = dbHandler;
        savedData = dbHandler.getAllRows();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        savedData = dbHandler.getAllRows();
        View rvView = LayoutInflater.from(context).inflate(R.layout.alert_rv_item, parent, false);

        rvView.setOnClickListener(view -> {
            LinearLayout cardLayout = (LinearLayout) ((ViewGroup) view).getChildAt(0);
            TextView uri = (TextView) cardLayout.getChildAt(2);
            CollectedDeviceData collectedDeviceData = WeaApiInterface.getSingleJsonResult(CollectedDeviceData.class,
                    (String) uri.getText());

            View dataLayout = inflater.inflate(R.layout.collected_data_layout, parent, false);
            TextView messageNumber = dataLayout.findViewById(R.id.message_number);
            messageNumber.setText(String.format("%08X", collectedDeviceData.getMessageNumber()));
            TextView timeReceived = dataLayout.findViewById(R.id.time_received);
            timeReceived.setText(collectedDeviceData.getTimeReceived());
            TextView timePresented = dataLayout.findViewById(R.id.time_presented);
            TextView timePresentedText = dataLayout.findViewById(R.id.time_presented_text);
            TextView insideArea = dataLayout.findViewById(R.id.received_inside);
            insideArea.setText(String.valueOf(collectedDeviceData.isReceivedInside()));
            TextView presented = dataLayout.findViewById(R.id.message_presented);
            presented.setText(String.valueOf(collectedDeviceData.isMessagePresented()));
            TextView locationAvailable = dataLayout.findViewById(R.id.location_available);
            locationAvailable.setText(String.valueOf(collectedDeviceData.isLocationAvailable()));
            TextView distance = dataLayout.findViewById(R.id.distance);
            distance.setText(String.valueOf(collectedDeviceData.getDistanceFromPolygon()));
            TextView optedOut = dataLayout.findViewById(R.id.opted_out);
            optedOut.setText(String.valueOf(collectedDeviceData.isOptedOut()));

            if (collectedDeviceData.isMessagePresented()) {
                timePresentedText.setText(collectedDeviceData.getTimeDisplayed());
            } else {
                timePresented.setVisibility(View.GONE);
                timePresentedText.setVisibility(View.GONE);
            }

            new AlertDialog.Builder(parent.getContext())
                    .setTitle("Collected Data")
                    .setView(dataLayout)
                    .show();
        });

        return new ViewHolder(rvView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SavedDataModel model = savedData.get(position);
        holder.cmacAlertID.setText("Message Number: " + model.getMessageNumber());
        holder.date.setText("Time Received: " + model.getDateTime());
        holder.uri.setText(model.getUri());
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
