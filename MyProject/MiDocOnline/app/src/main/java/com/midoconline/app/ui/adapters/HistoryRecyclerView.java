package com.midoconline.app.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.midoconline.app.R;
import com.midoconline.app.ui.activities.HistoryChatActivity;

/**
 * Created by Prashant on 18/10/15.
 */
public class HistoryRecyclerView extends RecyclerView.Adapter<HistoryRecyclerView.CustomViewHolder> {
    private Context mContext;

    public HistoryRecyclerView(Context context) {
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_custom_history_row, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {

        customViewHolder.mBtnChatHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HistoryChatActivity.class);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDate;
        public TextView txtDoctorName;
        public TextView txtDoctorOccupation;
        public TextView txtDoctorCharge;
        public Button mBtnChatHistory;
        public Button mBtnCall;

        public CustomViewHolder(View view) {
            super(view);
            txtDate = (TextView) view.findViewById(R.id.text_date);
            txtDoctorName = (TextView) view.findViewById(R.id.text_doctor_name);
            txtDoctorOccupation = (TextView) view.findViewById(R.id.text_doctor_speciality);
            txtDoctorCharge = (TextView) view.findViewById(R.id.text_prize);
            mBtnChatHistory = (Button) view.findViewById(R.id.btn_chatHistory);
            mBtnCall = (Button) view.findViewById(R.id.btn_call);

        }
    }
}
