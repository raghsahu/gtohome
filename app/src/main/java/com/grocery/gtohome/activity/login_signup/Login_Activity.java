package com.grocery.gtohome.activity.login_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.DeliveryAreaActivity;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.Base_Url;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.ActivityLoginBinding;
import com.grocery.gtohome.model.login_model.LoginModel;
import com.grocery.gtohome.model.register_model.RegistrationModel;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Connectivity;
import com.grocery.gtohome.utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

public class Login_Activity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    ActivityLoginBinding binding;
    private Utilities utilities;
    private Context context;
    String Et_Pw, Et_Email;
    SessionManager session;
    private GoogleApiClient googleApiClient;
    CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 1;
    private String social_name = "", social_id = "", social_email = "", social_img = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login_);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_);
        utilities = Utilities.getInstance(this);
        context = this;
        session = new SessionManager(Login_Activity.this);

        //******google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //*****fb login
        callbackManager = CallbackManager.Factory.create();
        binding.btnFb.setPermissions("email", "public_profile");

        binding.ivGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });


        binding.btnFb.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("onSuccess");
                       // Toast.makeText(Login_Activity.this, "success", Toast.LENGTH_SHORT).show();

                        String accessToken = loginResult.getAccessToken()
                                .getToken();
                        Log.i("accessToken", accessToken);

                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {

                                        Log.i("Login_FB", response.toString());
                                        try {
                                            social_id = object.getString("id");
                                            try {
                                                URL profile_pic = new URL(
                                                        "http://graph.facebook.com/" + social_id + "/picture?type=large");
                                                Log.i("profile_pic", profile_pic + "");

                                            } catch (MalformedURLException e) {
                                                e.printStackTrace();
                                            }
                                            social_name = object.getString("name");
                                            String first_name = object.getString("first_name");
                                            String last_name = object.getString("last_name");

                                            if (object.has("email"))
                                            {
                                               social_email = object.getString("email");
                                            }
                                            else
                                            {
                                                social_email = "";
                                            }

                                            Log.e("fb_name",first_name + " "+last_name);
                                            //  Log.e("fb_email",social_email + "");

                                              if (social_email!=null && !social_email.isEmpty()){
                                                  if (session.getTokenId()!=null && !session.getTokenId().equalsIgnoreCase("")){
                                                      if (Connectivity.isConnected(Login_Activity.this)) {
                                                          SocialLoginApi(social_email, social_id, first_name,last_name, "facebook");
                                                      } else {
                                                          Toast.makeText(Login_Activity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
                                                      }
                                                  }else {
                                                      Toast.makeText(Login_Activity.this, "Firebase token not get yet, Please restart the app.", Toast.LENGTH_SHORT).show();
                                                  }

                                              }else {
                                                  utilities.dialogOK(context, getString(R.string.validation_title), "Email id not found, Please try other Facebook login", getString(R.string.ok), false);
                                                  LoginManager.getInstance().logOut();
                                              }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                       // parameters.putString("fields","id,name,email,gender, birthday");
                        parameters.putString("fields", "id,name,first_name, last_name, email,link");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("onCancel");
                        Toast.makeText(Login_Activity.this, getString(R.string.cancel), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("onError");
                        Toast.makeText(Login_Activity.this, getString(R.string.fail), Toast.LENGTH_SHORT).show();
                        //  Log.e("LoginActivity", exception.toString());
                        // Log.e("LoginActivity", exception.getMessage());
                    }
                });

        binding.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login_Activity.this, Register_Activity.class);
                startActivity(intent);
            }
        });

        binding.tvForgotPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login_Activity.this, Forgot_Pw_Activity.class);
                startActivity(intent);
            }
        });

        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Et_Email = binding.etEmail.getText().toString();
                Et_Pw = binding.etPw.getText().toString();

                if (isValid()) {

                    if (session.getTokenId()!=null && !session.getTokenId().equalsIgnoreCase("")){
                        if (Connectivity.isConnected(context)) {
                            CallLoginApi();
                        } else {
                            // Toast.makeText(context, getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
                            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
                        }
                    }else {
                        Toast.makeText(Login_Activity.this, "Firebase token not get yet, Please restart the app.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    public void onClick(View v) {
        if (v == binding.ivFb) {
            binding.btnFb.performClick();
        }
    }

    @SuppressLint("CheckResult")
    private void CallLoginApi() {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.LoginUser(Et_Email, Et_Pw, session.getTokenId(),session.getDeviceId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoginModel>() {
                    @Override
                    public void onNext(LoginModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_my_test", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                Log.e("result_my_test", "" + response.getCustomerInfo().getCustomerId());
                                session.createSession(response.getCustomerInfo());
                                Toast.makeText(context, response.getMsg(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(context, response.getMsg(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Handle error
                        progressDialog.dismiss();
                        Log.e("mr_product_error", e.toString());

                        if (e instanceof HttpException) {
                            int code = ((HttpException) e).code();
                            switch (code) {
                                case 403:
                                    break;
                                case 404:
                                    //Toast.makeText(EmailSignupActivity.this, R.string.email_already_use, Toast.LENGTH_SHORT).show();
                                    break;
                                case 409:
                                    break;
                                default:
                                    // Toast.makeText(EmailSignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            if (TextUtils.isEmpty(e.getMessage())) {
                                // Toast.makeText(EmailSignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(EmailSignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });
    }

    private boolean isValid() {

        if (Et_Email == null || Et_Email.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), "Please enter your email or mobile no.", getString(R.string.ok), false);
            binding.etEmail.requestFocus();
            return false;
        }
//        else if (Et_Email != null && !Et_Email.equals("") && !Et_Email.isEmpty()) {
//            if (!utilities.checkEmail(Et_Email)) {
//                utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_valid_email), getString(R.string.ok), false);
//                binding.etEmail.requestFocus();
//                return false;
//            }
//        }
        else if (Et_Pw.trim().length() == 0) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_password), getString(R.string.ok), false);
            binding.etPw.requestFocus();
            return false;
        } else if (Et_Pw.length() < 6 || Et_Pw.length() > 20) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_valid_password), getString(R.string.ok), false);
            binding.etPw.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();
            assert acct != null;
            social_name = acct.getDisplayName();
            social_email = acct.getEmail();

            String lastname=acct.getFamilyName();
            String firstname=acct.getGivenName();

            if (acct.getPhotoUrl() != null) {
                social_img = acct.getPhotoUrl().toString();
            } else {
                social_img = "";
            }
            Log.e("social_img ", " " + social_img);
            social_id = acct.getId();

            acct.getId();
            Log.e("GoogleResult", social_id + "------" + social_name + "------" + social_email);
            Log.e("GoogleResult1", firstname + "-" + lastname);

            if (Connectivity.isConnected(Login_Activity.this)) {
                gotoHome(firstname,lastname);
            } else {
                Toast.makeText(this, "Please check Internet", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Sign in cancel", Toast.LENGTH_LONG).show();
        }
    }

    private void gotoHome(String firstname, String lastname) {
        if (!social_email.isEmpty() && !social_id.isEmpty()) {

            if (!Patterns.EMAIL_ADDRESS.matcher(social_email).matches()) {
                Toast.makeText(Login_Activity.this, "Email not valid", Toast.LENGTH_SHORT).show();
            } else {
                if (session.getTokenId()!=null && !session.getTokenId().equalsIgnoreCase("")){
                    if (Connectivity.isConnected(Login_Activity.this)) {
                        SocialLoginApi(social_email, social_id, firstname,lastname, "gplus");

                    } else {
                        Toast.makeText(Login_Activity.this, "Please check internet", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Login_Activity.this, "Firebase token not get yet, Please restart the app.", Toast.LENGTH_SHORT).show();
                }

            }
        } else {
            Toast.makeText(Login_Activity.this, "Please try again", Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("CheckResult")
    private void SocialLoginApi(String social_email, String social_id, String firstname, String lastname, String social_type) {
        final ProgressDialog progressDialog = new ProgressDialog(Login_Activity.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.LoginSocialUser(firstname,lastname,social_email,social_id,social_type,session.getTokenId(),session.getDeviceId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoginModel>() {
                    @Override
                    public void onNext(LoginModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_my_test", "" + response.getStatus());
                            Toast.makeText(Login_Activity.this, "" + response.getMsg(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                Toast.makeText(context, response.getMsg(), Toast.LENGTH_SHORT).show();

                                session.createSession(response.getCustomerInfo());
                                Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else {

                                if (response.getError().getApproved()!=null){
                                    utilities.dialogOK(context, getString(R.string.validation_title),
                                            response.getError().getApproved(), getString(R.string.ok), false);
                                }
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Handle error
                        progressDialog.dismiss();
                        Log.e("mr_product_error", e.toString());
                        // Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        if (e instanceof HttpException) {
                            int code = ((HttpException) e).code();
                            switch (code) {
                                case 403:
                                    break;
                                case 404:
                                    //  Toast.makeText(Login_Activity.this, R.string.invalid_login, Toast.LENGTH_SHORT).show();
                                    break;
                                case 409:
                                    break;
                                default:
                                    // Toast.makeText(Login_Activity.this, R.string.cb_snooze_network_error, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            if (TextUtils.isEmpty(e.getMessage())) {
                                // Toast.makeText(Login_Activity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Login_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        e.printStackTrace();

                    }


                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });

    }
}
