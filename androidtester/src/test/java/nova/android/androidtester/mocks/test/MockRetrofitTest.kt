package nova.android.androidtester.mocks.test

import nova.android.androidtester.api.TestService
import nova.android.androidtester.bean.MockAssest
import nova.android.androidtester.mocks.util.MockRetrofit
import org.junit.Test

/**
 *  nova.android.androidtester.mocks.test.
 *
 * @author Created by WXG on 2018/7/20 020 16:54.
 * @version V1.0
 */
class MockRetrofitTest {

    @Test
    fun mockRetrofitTest() {
        // 这个测试是保证Retrofit能够成功拦截API请求，并返回本地的Mock数据
        val retrofit = MockRetrofit()
        val service = retrofit.create(TestService::class.java)
        retrofit.path = MockAssest.USER_DATA  //设置Path，设置后，retrofit会拦截API,并返回对应Path下Json文件的数据

        service.getUser("wxg")
                .test()
                .assertValue { it ->
                    it.username.equals("wxg")
                    it.password.equals("111")
                }
    }

}