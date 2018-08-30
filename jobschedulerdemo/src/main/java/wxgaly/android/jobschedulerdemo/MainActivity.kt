package wxgaly.android.jobschedulerdemo

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*


/**
 * 参考文档
 * https://www.jianshu.com/p/9fb882cae239
 */
class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private var jobId = 1
    private val delayTime = 3000L
    private var mJobScheduler: JobScheduler? = null
    private var mServiceComponent: ComponentName? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initView()
        initData()
    }

    /**
     * init data.
     */
    private fun initData() {
        // 可以不用启动自己继承的service
        startService(Intent(this@MainActivity, JobSchedulerService::class.java))
        mJobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        mServiceComponent = ComponentName(packageName, JobSchedulerService::class.java.name)
    }

    /**
     * init view.
     */
    private fun initView() {
        fab.setOnClickListener { view ->
            Snackbar.make(view, "One job is added, it will be execute after 3 second.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            addJob()
        }
    }

    /**
     * add one job.
     */
    private fun addJob() {
        val builder = JobInfo.Builder(jobId++, mServiceComponent)
                .setMinimumLatency(delayTime)
                .setOverrideDeadline(delayTime)
                .build()
        mJobScheduler?.schedule(builder)
        Log.d(TAG, "add one job")
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
