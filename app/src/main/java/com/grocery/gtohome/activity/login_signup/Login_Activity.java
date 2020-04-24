package com.grocery.gtohome.activity.login_signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.Base_Url;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.ActivityLoginBinding;
import com.grocery.gtohome.model.register_model.RegistrationModel;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Connectivity;
import com.grocery.gtohome.utils.Utilities;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

import static com.grocery.gtohome.api_client.Base_Url.Register;
import static com.grocery.gtohome.api_client.Base_Url.loginapi;

public class Login_Activity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private Utilities utilities;
    private Context context;
    String Et_Pw,Et_Email;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login_);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_);
        utilities = Utilities.getInstance(this);
        context = this;
        session = new SessionManager(Login_Activity.this);

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
                    if (Connectivity.isConnected(context)){
                        CallLoginApi();
                    }else {
                        // Toast.makeText(context, getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
                        utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
                    }

                }
            }
        });

    }

    @SuppressLint("CheckResult")
    private void CallLoginApi() {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.LoginUser(loginapi, Et_Email,Et_Pw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RegistrationModel>() {
                    @Override
                    public void onNext(RegistrationModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_my_test", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getResult().equalsIgnoreCase("true")) {
                                Log.e("result_my_test", "" + response.getData().getCustomerId());
                                session.createSession(response.getData());
                                Toast.makeText(context, response.getMsg(), Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(Login_Activity.this, MainActivity.class);
                                startActivity(intent);


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
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_email), getString(R.string.ok), false);
            binding.etEmail.requestFocus();
            return false;
        }else if (Et_Email !=null && !Et_Email.equals("") && !Et_Email.isEmpty()) {
            if (!utilities.checkEmail(Et_Email)) {
                utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_valid_email), getString(R.string.ok), false);
                binding.etEmail.requestFocus();
                return false;
            }
        }else if (Et_Pw.trim().length() == 0) {
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
}
