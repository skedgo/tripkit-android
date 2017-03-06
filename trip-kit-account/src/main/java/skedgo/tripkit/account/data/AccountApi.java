package skedgo.tripkit.account.data;

import com.skedgo.android.tripkit.account.LogInBody;
import com.skedgo.android.tripkit.account.LogInResponse;
import com.skedgo.android.tripkit.account.LogOutResponse;
import com.skedgo.android.tripkit.account.SignUpBody;
import com.skedgo.android.tripkit.account.SignUpResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface AccountApi {
  @POST("account/signup") Observable<SignUpResponse> signUp(@Body SignUpBody body);
  @POST("account/login") Observable<LogInResponse> logIn(@Body LogInBody body);
  @POST("account/logout") Observable<LogOutResponse> logOut();
}
