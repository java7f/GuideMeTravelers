package com.example.guidemetravelersapp.helpers

import android.bluetooth.le.ScanResult
import android.util.Log
import com.accent_systems.ibks_sdk.scanner.ASResultParser
import com.accent_systems.ibks_sdk.scanner.ASScannerCallback
import com.accent_systems.ibks_sdk.utils.ASUtils
import com.example.guidemetravelersapp.BuildConfig
import com.example.guidemetravelersapp.views.BeaconTestActivity

class ScannerCallback  {
    companion object : ASScannerCallback {
        override fun scannedBleDevices(result: ScanResult?) {

            when (ASResultParser.getAdvertisingType(result)) {
                ASUtils.TYPE_IBEACON ->
                {
                    val parsingResult = ASResultParser.getDataFromAdvertising(result)
                    if(parsingResult["UUID"] == BuildConfig.BEACON_UUID && !ASBLeScannerWrapper.scannedDeivcesList.contains(result!!.device.address)) {
                        ASBLeScannerWrapper.scannedDeivcesList.add(result.device.address)
                    }
                    Log.i(BeaconTestActivity::class.simpleName, result!!.device.name + " - iBEACON - " + parsingResult.toString())
                    Log.i(BeaconTestActivity::class.simpleName, " - iBEACON - ${ASBLeScannerWrapper.scannedDeivcesList} - ${parsingResult["UUID"]}")

                }
                ASUtils.TYPE_DEVICE_CONNECTABLE -> Log.i(
                    BeaconTestActivity::class.simpleName,
                    result!!.device.name + " - CONNECTABLE - "
                )
                ASUtils.TYPE_UNKNOWN -> Log.i(
                    BeaconTestActivity::class.simpleName,
                    result!!.device.name + " - UNKNOWN - "
                )
                else -> Log.i(BeaconTestActivity::class.simpleName, "ADVERTISING TYPE: " + "ERROR PARSING")
            }
        }
    }
}