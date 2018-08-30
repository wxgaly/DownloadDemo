package wxgaly.android.jobschedulerdemo

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast

/**
 *  wxgaly.android.jobschedulerdemo.
 *
 * @author Created by WXG on 2018/8/31 0:39.
 * @version V1.0
 */
class JobSchedulerService : JobService() {

    private val TAG = "JobSchedulerService"

    override fun onStartJob(params: JobParameters?): Boolean {
        // 有且仅有onStartJob返回值为true时，才会调用onStopJob来销毁job
        // 返回false来销毁这个工作
        Log.d(TAG, "onStartJob")
        mJobHandler.sendMessage(Message.obtain(mJobHandler, 1, params))
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "onStopJob")
        // 返回true，表示该工作耗时，同时工作处理完成后需要调用onStopJob销毁（jobFinished）
        // 返回false，任务运行不需要很长时间，到return时已完成任务处理
        mJobHandler.removeMessages(1)
        return false
    }

    private val mJobHandler = Handler(Handler.Callback { msg ->

        Toast.makeText(applicationContext, "JobService task running", Toast.LENGTH_SHORT).show()
        // 调用jobFinished
        jobFinished(msg.obj as JobParameters, false)

        true
    })

}