package com.example.smitchapp1.viewmodels;

import android.app.Application;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.smitchapp1.dataclasses.ScanResultModel;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    MutableLiveData<List<ScanResultModel>> scanResults = new MutableLiveData<>();
    List<ScanResultModel> scanResultModels = new ArrayList<>();
    private static final String TAG = "MainViewModel";
    private static final String SERVICE_TYPE = "_http._tcp.";

    NsdManager.RegistrationListener registrationListener;
    NsdManager nsdManager;

    String serviceName;
    NsdManager.DiscoveryListener discoveryListener;
    NsdManager.ResolveListener resolveListener;

    NsdServiceInfo mService;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void registerService(int port, Context context) {
        // Create the NsdServiceInfo object, and populate it.
        NsdServiceInfo serviceInfo = new NsdServiceInfo();

        // The name is subject to change based on conflicts
        // with other services advertised on the same network.
        serviceInfo.setServiceName("MyNSD");
        serviceInfo.setServiceType(SERVICE_TYPE);
        serviceInfo.setPort(port);
        initializeRegistrationListener();
        nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);

        nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener);
    }

    public void initializeRegistrationListener() {
        registrationListener = new NsdManager.RegistrationListener() {

            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                // Save the service name. Android may have changed it in order to
                // resolve a conflict, so update the name you initially requested
                // with the name Android actually used.
                serviceName = NsdServiceInfo.getServiceName();
                Log.d(TAG, "Service registered" + "" + serviceName);
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Registration failed! Put debugging code here to determine why.
                Log.d(TAG, "Service registered11" + errorCode);
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
                // Service has been unregistered. This only happens when you call
                // NsdManager.unregisterService() and pass in this listener.
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Unregistration failed. Put debugging code here to determine why.
            }
        };
    }

    public void discoverService(Context context) {
        initializeDiscoveryListener();
        if (nsdManager == null)
            nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);

        nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
    }

    public void initializeDiscoveryListener() {

        // Instantiate a new DiscoveryListener
        discoveryListener = new NsdManager.DiscoveryListener() {

            // Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                // A service was found! Do something with it.
                Log.d(TAG, "Service discovery success" + service);
                if (service.getServiceType().equals(SERVICE_TYPE)) {
                    // Service type is the string containing the protocol and
                    // transport layer for this service.
                    initializeResolveListener();
                    nsdManager.resolveService(service, resolveListener);
                    Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
                Log.e(TAG, "service lost: " + service);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                nsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                nsdManager.stopServiceDiscovery(this);
            }
        };
    }

    public void initializeResolveListener() {
        resolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Called when the resolve fails. Use the error code to debug.
                Log.e(TAG, "Resolve failed: " + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "Resolve Succeeded. " + serviceInfo);
                mService = serviceInfo;

                ScanResultModel scanResultModel = new ScanResultModel(mService.getServiceName(), mService.getServiceType(), mService.getHost().getHostAddress(), mService.getPort());
                scanResultModels.add(scanResultModel);
                scanResults.postValue(scanResultModels);

            }
        };

    }

    public MutableLiveData<List<ScanResultModel>> getScanResults() {
        return scanResults;
    }
}
