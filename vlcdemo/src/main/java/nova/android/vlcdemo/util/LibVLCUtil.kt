package nova.android.vlcdemo.util

import org.videolan.libvlc.LibVLC


/**
 *  nova.android.vlcdemo.util.
 *
 * @author Created by WXG on 2018/11/27 027 11:57.
 * @version V1.0
 */
object LibVLCUtil {


    private var libVLC: LibVLC? = null

    @Synchronized
    @Throws(IllegalStateException::class)
    fun getLibVLC(options: ArrayList<String>?): LibVLC? {
        if (libVLC == null) {
            libVLC = if (options == null) {
                LibVLC()
            } else {
                LibVLC(options)
            }
        }
        return libVLC
    }

}