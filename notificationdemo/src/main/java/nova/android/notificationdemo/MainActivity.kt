package nova.android.notificationdemo

import android.app.Notification
import android.app.NotificationManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var notificationManager: NotificationManager? = null
    private var builder: NotificationCompat.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        initListener()
    }

    private fun initData() {
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        builder = NotificationCompat.Builder(this@MainActivity, "MainActivity")

        builder?.apply {
            setContentTitle("我是标题")
            setContentText("我是内容")
            setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            setSmallIcon(R.mipmap.ic_launcher_round)
            setWhen(System.currentTimeMillis())
            setTicker("我是测试内容")
            setDefaults(Notification.DEFAULT_SOUND)
        }

    }

    private fun initListener() {
        button_notify.setOnClickListener(this)
        button_cancel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        v?.apply {
            when (id) {
                R.id.button_notify -> {
                    notificationManager?.notify(10, builder?.build())
                }
                R.id.button_cancel -> {
                    notificationManager?.cancel(10)
                }
                else -> {
                }
            }
        }
    }

}
