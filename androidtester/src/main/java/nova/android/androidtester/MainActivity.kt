package nova.android.androidtester

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        btn.setOnClickListener {

            Toast.makeText(this@MainActivity,  "${editText.text}---${add(1, 2)}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun add(a: Int, b: Int): Int = a + b

}
