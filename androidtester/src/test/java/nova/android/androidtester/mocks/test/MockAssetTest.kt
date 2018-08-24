package nova.android.androidtester.mocks.test

import io.reactivex.Observable
import nova.android.androidtester.bean.MockAssest
import org.junit.Test

/**
 *  nova.android.androidtester.mocks.test.
 *
 * @author Created by WXG on 2018/7/20 020 16:41.
 * @version V1.0
 */
class MockAssetTest {

    @Test
    fun assetTest() {
        //MockAssest读取文件，该函数所得结果将来会作为模拟的网络数据返回，我们这个单元测试的意义
        //就是保证模拟的网络数据能够正确的返回
        val content = MockAssest.readFile(MockAssest.USER_DATA)
        Observable.just(content).subscribe(Co)
                .test()
                .assertValue("{\r\n" +
                        "    \"username\": \"wxg\",\r\n" +
                        "    \"password\": \"123456\"\r\n" +
                        "}")
    }

}