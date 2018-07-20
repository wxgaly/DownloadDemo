package nova.android.androidtester.bean

import java.io.File

/**
 *  nova.android.androidtester.bean.
 *
 * @author Created by WXG on 2018/7/20 020 16:27.
 * @version V1.0
 */

data class User(val username: String, val password: String)


object MockAssest {

    private const val BASE_PATH = "../androidtester/src/test/java/nova/android/androidtester/mocks/data"

    //User API对应的模拟json数据的文件路径
    val USER_DATA = "$BASE_PATH/userJson_test"

    //通过文件路径，读取Json数据
    fun readFile(path: String): String {
        val content = file2String(File(path))
        return content
    }
    //kotlin丰富的I/O API,我们可以通过file.readText（charset）直接获取结果
    fun file2String(f: File, charset: String = "UTF-8"): String {
        return f.readText(Charsets.UTF_8)
    }
}