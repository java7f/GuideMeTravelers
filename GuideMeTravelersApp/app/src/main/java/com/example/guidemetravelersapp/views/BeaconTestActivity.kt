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
import com.example.guidemetravelersapp.helpers.ASBLeScannerWrapper
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme

class BeaconTestActivity : ComponentActivity(), ASScannerCallback, ASEDSTCallback {

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
        scannerWrapper = ASBLeScannerWrapper(this, this)
        scannerWrapper.startScan()
    }

    override fun scannedBleDevices(result: ScanResult?) {
        val advertisingString = ASResultParser.byteArrayToHex(
            result!!.scanRecord!!.bytes
        )

        val logstr = result.device.address + " / RSSI: " + result.rssi + " / Adv packet: " + advertisingString
        //Check if scanned device is already in the list by mac address
        //Check if scanned device is already in the list by mac address
        var contains = false
        for (i in scannedDeivcesList.indices) {
            if (scannedDeivcesList.get(i).contains(result.device.address)) {
                //Device already added
                contains = true
                //Replace the device with updated values in that position
                scannedDeivcesList.set(
                    i, """${result.rssi}  ${result.device.name}(${result.device.address})"""
                )
                break
            }
        }

        if (!contains) {
            //Scanned device not found in the list. NEW => add to list
            scannedDeivcesList.add(
                """${result.rssi}  ${result.device.name}(${result.device.address})"""
            )
        }

        when (ASResultParser.getAdvertisingType(result)) {
            ASUtils.TYPE_IBEACON ->
            {
                val parsingResult = ASResultParser.getDataFromAdvertising(result)
                Log.i(BeaconTestActivity::class.simpleName, result.device.name + " - iBEACON - " + parsingResult.toString())
            }
                /**** Example to get data from advertising ***
                 * advData = ASResultParser.getDataFromAdvertising(result);
                 * try {
                 * Log.i(TAG, "FrameType = " +advData.getString("FrameType")+" AdvTxPower = "+advData.getString("AdvTxPower")+" UUID = "+advData.getString("UUID")+" Major = "+advData.getString("Major")+" Minor = "+advData.getString("Minor"));
                 * }catch (Exception ex){
                 * Log.i(TAG,"Error parsing JSON");
                 * }
                 * / */
                //Log.i(BeaconTestActivity::class.simpleName, result.device.name + " - iBEACON - " + logstr)
            ASUtils.TYPE_EDDYSTONE_UID ->
                /**** Example to get data from advertising ***
                 * advData = ASResultParser.getDataFromAdvertising(result);
                 * try {
                 * Log.i(TAG, "FrameType = " +advData.getString("FrameType")+" AdvTxPower = "+advData.getString("AdvTxPower")+" Namespace = "+advData.getString("Namespace")+" Instance = "+advData.getString("Instance"));
                 * }catch (Exception ex){
                 * Log.i(TAG,"Error parsing JSON");
                 * }
                 * / */
                Log.i(BeaconTestActivity::class.simpleName, result.device.name + " - UID - " + logstr)
            ASUtils.TYPE_EDDYSTONE_URL ->
                /**** Example to get data from advertising ***
                 * advData = ASResultParser.getDataFromAdvertising(result);
                 * try {
                 * Log.i(TAG, "FrameType = " +advData.getString("FrameType")+"  AdvTxPower = "+advData.getString("AdvTxPower")+" Url = "+advData.getString("Url"));
                 * }catch (Exception ex){
                 * Log.i(TAG,"Error parsing JSON");
                 * }
                 * / */
                Log.i(BeaconTestActivity::class.simpleName, result.device.name + " - URL - " + logstr)
            ASUtils.TYPE_EDDYSTONE_TLM ->
                /**** Example to get data from advertising ***
                 * advData = ASResultParser.getDataFromAdvertising(result);
                 * try {
                 * if(advData.getString("Version").equals("0")){
                 * Log.i(TAG, "FrameType = " +advData.getString("FrameType")+" Version = "+advData.getString("Version")+" Vbatt = "+advData.getString("Vbatt")+" Temp = "+advData.getString("Temp")+" AdvCount = "+advData.getString("AdvCount")+" TimeUp = "+advData.getString("TimeUp"));
                 * }
                 * else{
                 * Log.i(TAG, "FrameType = " +advData.getString("FrameType")+" Version = "+advData.getString("Version")+" EncryptedTLMData = "+advData.getString("EncryptedTLMData")+" Salt = "+advData.getString("Salt")+" IntegrityCheck = "+advData.getString("IntegrityCheck"));
                 * }
                 * }catch (Exception ex){
                 * Log.i(TAG,"Error parsing JSON");
                 * }
                 * / */
                Log.i(BeaconTestActivity::class.simpleName, result.device.name + " - TLM - " + logstr)
            ASUtils.TYPE_EDDYSTONE_EID ->
                /**** Example to get EID in Clear by the air ***
                 * if(!readingEID) {
                 * readingEID = true;
                 * new ASEDSTService(null,this,10);
                 * ASEDSTService.setClient_ProjectId(client, getPrefs.getString("projectId", null));
                 * ASEDSTService.getEIDInClearByTheAir(result);
                 * }
                 * / */
                /**** Example to get data from advertising ***
                 * advData = ASResultParser.getDataFromAdvertising(result);
                 * try {
                 * Log.i(TAG, "FrameType = " +advData.getString("FrameType")+" AdvTxPower = "+advData.getString("AdvTxPower")+" EID = "+advData.getString("EID"));
                 * }catch (Exception ex){
                 * Log.i(TAG,"Error parsing JSON");
                 * }
                 * / */
                Log.i(BeaconTestActivity::class.simpleName, result.device.name + " - EID - " + logstr)
            ASUtils.TYPE_DEVICE_CONNECTABLE -> Log.i(
                BeaconTestActivity::class.simpleName,
                result.device.name + " - CONNECTABLE - " + logstr
            )
            ASUtils.TYPE_UNKNOWN -> Log.i(
                BeaconTestActivity::class.simpleName,
                result.device.name + " - UNKNOWN - " + logstr
            )
            else -> Log.i(BeaconTestActivity::class.simpleName, "ADVERTISING TYPE: " + "ERROR PARSING")
        }
    }

    override fun onEDSTSlotsWrite(result: Int) {
        TODO("Not yet implemented")
    }

    override fun onGetEDSTSlots(result: Int, slots: Array<out ASEDSTSlot>?) {
        if (result == ASUtils.READ_OK) {

            for (i in slots!!.indices) {
                Log.i(
                    BeaconTestActivity::class.simpleName, "onGetEDSTSlots - slot $i advint = " + Integer.toString(
                        slots!![i].adv_int
                    ) + " txpower = " + slots!![i].tx_power + " advtxpower = " + slots!![i].adv_tx_power + " frame type = 0x" + Integer.toHexString(
                        slots!![i].frame_type
                    ) + " data = " + slots!![i].data
                )
            }
        } else Log.i(BeaconTestActivity::class.simpleName, "onGetEDSTSlots - Error (" + Integer.toString(result) + ")")

    }

    override fun onReadEDSTCharacteristic(
        result: Int,
        characteristic: BluetoothGattCharacteristic?,
        readval: ByteArray?
    ) {
        Log.i(
            BeaconTestActivity::class.simpleName,
            "onReadEDSTCharacteristic - result = " + result + " characteristic = " + characteristic!!.uuid + " readval = " + ASResultParser.byteArrayToHex(
                readval
            )
        )
    }

    override fun onWriteEDSTCharacteristic(
        result: Int,
        characteristic: BluetoothGattCharacteristic?
    ) {
        TODO("Not yet implemented")
    }

    override fun onGetEIDInClear(result: Int, EID: String?, msg: String?) {
        TODO("Not yet implemented")
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