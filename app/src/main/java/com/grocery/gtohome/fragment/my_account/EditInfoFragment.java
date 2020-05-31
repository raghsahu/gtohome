package com.grocery.gtohome.fragment.my_account;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.activity.login_signup.Register_Activity;
import com.grocery.gtohome.activity.login_signup.Register_Success_Activity;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.Base_Url;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentEditInfoBinding;
import com.grocery.gtohome.databinding.FragmentMyAccountBinding;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.model.login_model.LoginModel;
import com.grocery.gtohome.model.register_model.RegistrationModel;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Connectivity;
import com.grocery.gtohome.utils.Utilities;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

import static com.grocery.gtohome.api_client.Base_Url.Register;

/**
 * Created by Raghvendra Sahu on 09-Apr-20.
 */
public class EditInfoFragment extends Fragment {
    FragmentEditInfoBinding binding;
    private Utilities utilities;
    private Context context;
    SessionManager sessionManager;
    private String mobile, email, First_name, Last_name, Customer_id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_info, container, false);
        View root = binding.getRoot();
        utilities = Utilities.getInstance(getActivity());
        sessionManager = new SessionManager(getActivity());
        Customer_id = sessionManager.getUser().getCustomerId();
        context = getActivity();

        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.edit_info));
        } catch (Exception e) {
        }
        //onback press call
        View view = getActivity().findViewById(R.id.img_back);
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            //Do your stuff
            imageView.setVisibility(View.VISIBLE);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).onBackPressed();
                }
            });
        }

        //set data in edit field.
        binding.etFirstname.setText(sessionManager.getUser().getFirstname());
        binding.etLastname.setText(sessionManager.getUser().getLastname());
        binding.etMobile.setText(sessionManager.getUser().getTelephone());
        binding.etEmail.setText(sessionManager.getUser().getEmail());

        binding.tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                First_name = binding.etFirstname.getText().toString();
                Last_name = binding.etLastname.getText().toString();
                mobile = binding.etMobile.getText().toString();
                email = binding.etEmail.getText().toString();


                if (isValid()) {
                    if (Connectivity.isConnected(context)) {
                        CallUpdateApi(First_name, Last_name, mobile, email);
                    } else {
                        // Toast.makeText(context, getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
                        utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
                    }

                }

            }
        });

        return root;
    }

    @SuppressLint("CheckResult")
    private void CallUpdateApi(String first_name, String lastname, String mobile, String email) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.UpdateUser(first_name, lastname, mobile, email, Customer_id)
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
                                // Log.e("result_my_test", "" + response.getData().getCustomerId());
                                utilities.dialogOKOnBack(context, getString(R.string.validation_title), response.getMsg(), getString(R.string.ok), true);
                                sessionManager.createSession(response.getCustomerInfo());

                            } else {
                                Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
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
        if (First_name.isEmpty()) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_first_name), getString(R.string.ok), false);
            binding.etFirstname.requestFocus();
            return false;
        } else if (Last_name.isEmpty()) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_last_name), getString(R.string.ok), false);
            binding.etLastname.requestFocus();
            return false;
        } else if (email.isEmpty()) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_email), getString(R.string.ok), false);
            binding.etEmail.requestFocus();
            return false;
        } else if (!utilities.checkEmail(email)) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_valid_email), getString(R.string.ok), false);
            binding.etEmail.requestFocus();
            return false;
        } else if (mobile.isEmpty()) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_mobile_number), getString(R.string.ok), false);
            binding.etMobile.requestFocus();
            return false;
        } else if (!utilities.checkMobile(mobile)) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_valid_mobile_number), getString(R.string.ok), false);
            binding.etMobile.requestFocus();
            return false;
        }

        return true;
    }
}
