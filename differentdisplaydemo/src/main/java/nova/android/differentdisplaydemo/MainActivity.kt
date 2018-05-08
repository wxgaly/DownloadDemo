package nova.android.differentdisplaydemo

import android.app.Presentation
import android.content.Context
import android.content.DialogInterface
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private var presentation: Presentation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        btn_show.setOnClickListener {
            NeutralDialogFragment().show("这是第一个屏", "这是第一个屏", "确定",
                    DialogInterface.OnClickListener { _, which ->
                        Toast.makeText(this@MainActivity, "点击了按钮 $which", Toast.LENGTH_SHORT).show()
                    }, fragmentManager)
        }


        btn_show_second.setOnClickListener {
            val displayManager: DisplayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
            val presentationDisplays = displayManager.displays

            presentationDisplays.forEach {
                Log.d("wxg", it.toString())
            }
            Log.d("wxg", "display size is : ${presentationDisplays.size}")
            if (presentationDisplays.isNotEmpty() && presentationDisplays.size > 1) {

                try {
//                    val display = presentationDisplays[presentationDisplays.size - 1]
                    val display = presentationDisplays[0]
                    presentation = DifferentDisplay(this@MainActivity, display)
                    presentation?.window?.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
                    presentation?.show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {

                Toast.makeText(this@MainActivity, "只有一个屏，无法开启扩张屏", Toast.LENGTH_SHORT).show()

            }
        }

        btn_hide_second.setOnClickListener {
            presentation?.dismiss()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
