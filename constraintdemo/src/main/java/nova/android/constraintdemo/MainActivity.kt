package nova.android.constraintdemo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_mian.*

/**
 *  nova.android.constraintdemo.
 *
 * @author Created by WXG on 2018/4/28 028 17:53.
 * @version V1.0
 */
class MainActivity : AppCompatActivity() {

    private var fragments: List<Fragment>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mian)
        initData()
        initView()
    }

    private fun initData() {
        fragments = listOf(SubFragment(), SubFragment(), SubFragment(), SubFragment())
    }

    private fun initView() {


        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment = fragments!![position]

            override fun getCount(): Int = fragments!!.size

        }

        tab_indent.setViewPager(viewPager, 0)

    }


}