package nova.android.androidtester.api

import io.reactivex.Observable
import nova.android.androidtester.bean.User
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *  nova.android.androidtester.api.
 *
 * @author Created by WXG on 2018/7/20 020 17:01.
 * @version V1.0
 */
interface TestService {

    @GET("/test/api")
    fun getUser(@Query("username") username: String): Observable<User>

}