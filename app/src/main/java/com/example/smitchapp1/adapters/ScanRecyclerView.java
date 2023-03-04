package com.example.smitchapp1.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smitchapp1.dataclasses.ScanResultModel;
import com.example.smitchapp1.databinding.ItemViewScanresultsBinding;

import java.util.List;

public class ScanRecyclerView extends RecyclerView.Adapter<ScanRecyclerView.ScanRecyclerViewHolder> {
    List<ScanResultModel> mScanResults;

    public ScanRecyclerView(List<ScanResultModel> scanResults) {
        this.mScanResults = scanResults;
    }

    @NonNull
    @Override
    public ScanRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewScanresultsBinding binding = ItemViewScanresultsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ScanRecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanRecyclerViewHolder holder, int position) {
        ScanResultModel scanResultModel = mScanResults.get(holder.getAdapterPosition());

        holder.binding.tvServiceName.setText("Service Name " + scanResultModel.getServiceName());
        holder.binding.tvServiceType.setText("Service Type " + scanResultModel.getServiceType());
        holder.binding.tvIPAddress.setText("IP Address " + scanResultModel.getIpAddress());
        holder.binding.tvPort.setText("Port " + mScanResults.get(position).getPort());
    }

    @Override
    public int getItemCount() {
        return mScanResults.size();
    }


    public static class ScanRecyclerViewHolder extends RecyclerView.ViewHolder {
        ItemViewScanresultsBinding binding;

        public ScanRecyclerViewHolder(@NonNull ItemViewScanresultsBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
