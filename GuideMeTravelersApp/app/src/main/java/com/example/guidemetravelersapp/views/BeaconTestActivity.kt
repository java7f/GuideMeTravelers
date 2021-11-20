package com.example.guidemetravelersapp.views

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.accent_systems.ibks_sdk.EDSTService.ASEDSTCallback
import com.accent_systems.ibks_sdk.EDSTService.ASEDSTSlot
import com.accent_systems.ibks_sdk.scanner.ASBleScanner
import com.accent_systems.ibks_sdk.scanner.ASResultParser
import com.accent_systems.ibks_sdk.scanner.ASScannerCallback
import com.accent_systems.ibks_sdk.utils.ASUtils
import com.example.guidemetravelersapp.BuildConfig
import com.example.guidemetravelersapp.helpers.ASBLeScannerWrapper
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme

class BeaconTestActivity : ComponentActivity(), ASScannerCallback {

    private val scannedDeivcesList: MutableList<String> = mutableListOf()
    private lateinit var scannerWrapper: ASBLeScannerWrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            startScan()
            GuideMeTravelersAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting2("Android")
                }
            }
        }
    }

    fun startScan() {
        scannerWrapper = ASBLeScannerWrapper.getInstance()
        scannerWrapper.startScan()
    }

    override fun scannedBleDevices(result: ScanResult?) {

        when (ASResultParser.getAdvertisingType(result)) {
            ASUtils.TYPE_IBEACON ->
            {
                val parsingResult = ASResultParser.getDataFromAdvertising(result)
                if(parsingResult["UUID"] == BuildConfig.BEACON_UUID && !scannedDeivcesList.contains(result!!.device.address)) {
                    scannedDeivcesList.add(result.device.address)
                }
                Log.i(BeaconTestActivity::class.simpleName, result!!.device.name + " - iBEACON - " + parsingResult.toString())
                Log.i(BeaconTestActivity::class.simpleName, " - iBEACON - $scannedDeivcesList - ${parsingResult["UUID"]}")

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

@Composable
fun Greeting2(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
    GuideMeTravelersAppTheme {
        Greeting2("Android")
    }
}