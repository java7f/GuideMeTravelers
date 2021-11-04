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
import java.util.List;

public class ASBLeScannerWrapper {

    static Activity _act;
    static ASScannerCallback _cb;
    public static List<String> scannedDeivcesList = new ArrayList<>();

    public ASBLeScannerWrapper(Activity activity, ASScannerCallback scannerCallback) {
        _act = activity;
        _cb = scannerCallback;
    }

    public void startScan() {
        int err;
        new ASBleScanner(_act, _cb).setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
        err = ASBleScanner.startScan();
        if(err != ASUtils.TASK_OK) {
            Log.i("ScannerWrapper", "startScan - Error (" + Integer.toString(err) + ")");
        }
    }
}
