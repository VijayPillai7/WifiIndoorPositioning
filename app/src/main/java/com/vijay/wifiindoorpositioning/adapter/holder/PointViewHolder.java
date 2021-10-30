package com.vijay.wifiindoorpositioning.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vijay.wifiindoorpositioning.R;


public class PointViewHolder extends RecyclerView.ViewHolder {
    final TextView tvIdentifier, tvIdentifier2, tvPointX;

    public PointViewHolder(View itemView) {
        super(itemView);
        tvIdentifier = itemView.findViewById(R.id.point_identifier);
        tvPointX = itemView.findViewById(R.id.point_x);
        tvIdentifier2 = itemView.findViewById(R.id.point_identifier2);
//        tvPointY = itemView.findViewById(R.id.point_y);

    }
}
