package nova.android.constraintdemo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *  nova.android.constraintdemo.
 *
 * @author Created by WXG on 2018/4/28 028 18:00.
 * @version V1.0
 */
class SubFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_sub, container, false)
    }

}