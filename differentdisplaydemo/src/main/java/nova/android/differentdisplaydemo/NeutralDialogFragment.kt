package nova.android.differentdisplaydemo

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.app.FragmentManager
import android.content.DialogInterface
import android.os.Bundle

/**
 *  nova.android.differentdisplaydemo.
 *
 * @author Created by WXG on 2018/5/8 008 19:06.
 * @version V1.0
 */
class NeutralDialogFragment : DialogFragment() {

    private var neutralCallback: DialogInterface.OnClickListener? = null

    private var title: String? = null

    private var message: String? = null

    private var hint: String? = null

    fun show(title: String, message: String, hint: String, neutralCallback: DialogInterface.OnClickListener,
             fragmentManager: FragmentManager) {
        this.title = title
        this.message = message
        this.hint = hint
        this.neutralCallback = neutralCallback
        show(fragmentManager, "NeutralDialogFragment")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setNeutralButton(hint, neutralCallback)
        return builder.create()
    }

}