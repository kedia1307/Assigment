package com.example.smitchapp1.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.smitchapp1.viewmodels.MainViewModel;
import com.example.smitchapp1.adapters.ScanRecyclerView;
import com.example.smitchapp1.dataclasses.ScanResultModel;
import com.example.smitchapp1.databinding.ActivityMainBinding;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    List<ScanResultModel> scanResultModels = new ArrayList<>();
    MainViewModel viewModel;
    int localPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        initializeServerSocket();
        binding.btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.registerService(localPort, MainActivity.this);
            }
        });
        binding.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.discoverService(MainActivity.this);
                viewModel.getScanResults().observe(MainActivity.this, new Observer<List<ScanResultModel>>() {
                    @Override
                    public void onChanged(List<ScanResultModel> mScanResultModels) {
                        scanResultModels = mScanResultModels;

                        setRecyclerAdapter(scanResultModels);
                    }
                });

            }
        });

    }

    private void setRecyclerAdapter(List<ScanResultModel> scanResultModels) {
        ScanRecyclerView recyclerViewAdapter = new ScanRecyclerView(scanResultModels);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvResults.setLayoutManager(linearLayoutManager);
        binding.rvResults.setAdapter(recyclerViewAdapter);

    }

    public void initializeServerSocket() {
        // Initialize a server socket on the next available port.
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(0);
            // Store the chosen port.
            localPort = serverSocket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}