package net.nyamikan.blereceivesample

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView


// ref. https://qiita.com/miyatay/items/3f43bc8348b0e1914214
// ref. http://mslgt.hatenablog.com/entry/2015/11/15/005547

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onClick(p0: View?) {
        onClickScan(p0!!);
    }

    /** BLE機器のスキャンを行うクラス  */
    private val mBluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    /** BLE機器のスキャンを別スレッドで実行するためのHandler  */
    private val mHandler: Handler? = Handler()
    /** BLE機器をスキャンした際のコールバック  */
    private val mLeScanCallback = BluetoothAdapter.LeScanCallback { device, rssi, scanRecord ->
        // スキャンできた端末の情報をログ出力
        val uuids = device.uuids
        var uuid = ""
        if (uuids != null) {
            for (puuid in uuids) {
                uuid += puuid.toString() + " "
            }
        }
        val msg = ("name=" + device.name + ", bondStatus="
                + device.bondState + ", address="
                + device.address + ", type" + device.type
                + ", uuids=" + uuid)
        Log.d("BLEActivity", msg)

        var textViewData = findViewById<TextView>(R.id.textView4)
        textViewData.append(msg + "\n")
    }

    /**
     * スキャン開始ボタンタップ時のコールバックメソッド
     * @param v
     */
    fun onClickScan(v: View) {
        // 10秒後にBLE機器のスキャンを開始します
        mHandler!!.postDelayed({ mBluetoothAdapter!!.startLeScan(mLeScanCallback) }, SCAN_PERIOD)
    }

    /**
     * スキャン停止ボタンタップ時のコールバックメソッド
     * @param v
     */
    fun onClickStop(v: View) {
        // BLE機器のスキャンを停止します
        mBluetoothAdapter!!.stopLeScan(mLeScanCallback)
    }

    companion object {
        /** スキャン時間  */
        private val SCAN_PERIOD: Long = 10000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button).setOnClickListener(this);
    }
}
