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
                    if(parsingResult["UUID"] == BuildConfig.BEACON_UUID) {
                        if(!ASBLeScannerWrapper.scannedDevicesList.contains(result!!.device.address)) {
                            ASBLeScannerWrapper.scannedDevicesList.set(result.device.address, mutableListOf(result.rssi))
                        }
                        else {
                            ASBLeScannerWrapper.scannedDevicesList[result.device.address]?.add(result.rssi)
                        }
                        ASBLeScannerWrapper.measuredPower = parsingResult["AdvTxPower"] as Int
                    }
                    Log.i(BeaconTestActivity::class.simpleName, result!!.device.name + " - iBEACON - " + parsingResult)
                    Log.i(BeaconTestActivity::class.simpleName, " - iBEACON - ${ASBLeScannerWrapper.scannedDevicesList} - ${parsingResult["UUID"]} - ${ASBLeScannerWrapper.measuredPower}")

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