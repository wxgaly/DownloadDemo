package wxgaly.android.wavedemo2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import wxgaly.android.wavedemo2.view.WaveTestView

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(WaveTestView(this))
        initData()
    }

    private fun initData() {
//        val arr = arrayOfNulls<Float>(2)
//        for (i in 0 until arr.size) {
//            Log.d(TAG, "arr[$i] = ${arr[i]}")
//        }
//        for (i in 0 until arr.size) {
//            arr[i] = i.toFloat()
//            Log.d(TAG, "arr[$i] = ${arr[i]}")
//        }
//        val array = Array(9) { _ -> Array(4) { 1 } }

//        Log.d(TAG, "${2 / 2}   ---  ${2.div(3)}")
//        Log.d(TAG, "${array.size} --- ${array[0].size}")
//        for (i in 0 until 9) {
//            for (j in 0 until 4) {
//                array[i][j] = (j + 1) * (i + 1)
//                Log.d(TAG, "${array[i][j]} ")
//            }
//            Log.d(TAG, "------------ ")
//        }

    }
}
