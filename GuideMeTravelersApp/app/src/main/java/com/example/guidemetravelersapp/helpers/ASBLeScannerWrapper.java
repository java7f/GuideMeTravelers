package com.example.guidemetravelersapp.helpers;

import android.app.Activity;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.util.Log;

import com.accent_systems.ibks_sdk.scanner.ASBleScanner;
import com.accent_systems.ibks_sdk.scanner.ASResultParser;
import com.accent_systems.ibks_sdk.scanner.ASScannerCallback;
import com.accent_systems.ibks_sdk.utils.ASUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ASBLeScannerWrapper {

    static ASBleScanner scanner;
    public static Map<String, List<Integer>> scannedDevicesList = new HashMap<>();
    public static Map<String, Integer> measuredPower = new HashMap<>();
    private static ASBLeScannerWrapper single_instance = null;

    private ASBLeScannerWrapper(Activity activity, ASScannerCallback scannerCallback) {
        scanner = new ASBleScanner(activity, scannerCallback);
    }

    public static void initializeInstance(Activity activity, ASScannerCallback scannerCallback)
    {
        if (single_instance == null)
            single_instance = new ASBLeScannerWrapper(activity, scannerCallback);
    }

    public static ASBLeScannerWrapper getInstance() {
        return single_instance;
    }

    public void startScan() {
        int err;
        scanner.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
        err = ASBleScanner.startScan();
        if(err != ASUtils.TASK_OK) {
            return;
        }
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopScan();
    }

    public void stopScan() {
        ASBleScanner.stopScan();
    }
}
