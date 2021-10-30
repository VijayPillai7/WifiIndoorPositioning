package com.vijay.wifiindoorpositioning.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vijay.wifiindoorpositioning.R;


public class SectionHeaderViewHolder extends RecyclerView.ViewHolder {
    final TextView tvTitle;


    public SectionHeaderViewHolder(View headerView) {
        super(headerView);
        tvTitle = (TextView) headerView.findViewById(R.id.tv_section_name);
    }
}
