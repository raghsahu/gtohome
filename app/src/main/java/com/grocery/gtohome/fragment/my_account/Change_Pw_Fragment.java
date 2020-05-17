package com.grocery.gtohome.fragment.my_account;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentChangePwBinding;
import com.grocery.gtohome.databinding.FragmentEditInfoBinding;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Connectivity;
import com.grocery.gtohome.utils.Utilities;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

import static com.grocery.gtohome.api_client.Base_Url.BaseUrl;

/**
 * Created by Raghvendra Sahu on 09-Apr-20.
 */
public class Change_Pw_Fragment extends Fragment {
    FragmentChangePwBinding binding;
    private Utilities utilities;
    private SessionManager sessionManager;
    private String Customer_Id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_pw, container, false);
        View root = binding.getRoot();
        utilities = Utilities.getInstance(getActivity());
        sessionManager = new SessionManager(getActivity());
        Customer_Id = sessionManager.getUser().getCustomerId();

        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.change_pw));
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



        binding.tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String Et_pw= binding.etPw.getText().toString();
               String Et_conf_pw= binding.etConfirmPw.getText().toString();
                if (Et_pw.isEmpty()){
                    utilities.dialogOK(getActivity(), getString(R.string.validation_title), "Please Enter Password", getString(R.string.ok), false);
                }else if (Et_pw.length() < 5 || Et_pw.length() > 20) {
                    utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_enter_valid_password), getString(R.string.ok), false);
                    binding.etPw.requestFocus();
                }else if (Et_conf_pw.isEmpty()){
                    utilities.dialogOK(getActivity(), getString(R.string.validation_title), "Please Enter Confirm Password", getString(R.string.ok), false);
                }else if (Et_conf_pw.length() < 5 || Et_conf_pw.length() > 20) {
                    utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_enter_valid_confirm_password), getString(R.string.ok), false);
                    binding.etPw.requestFocus();
                }else if (!Et_pw.equals(Et_conf_pw)) {
                    utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.your_confirm_pass_doesnot), getString(R.string.ok), false);
                    binding.etConfirmPw.requestFocus();
                }else {
                    if (Connectivity.isConnected(getActivity())){
                        ChangedPwApi(Et_pw,Et_conf_pw);
                    }else {
                        utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
                    }
                }


            }
        });

        return root;
    }


    @SuppressLint("CheckResult")
    private void ChangedPwApi(String et_pw, String et_conf_pw) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("customer_id", Customer_Id);
        map.put("password", et_pw);
        map.put("confirm_password", et_conf_pw);

        apiInterface.ChanePwApi(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SimpleResultModel>() {
                    @Override
                    public void onNext(SimpleResultModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_category_pro", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                utilities.dialogOK(getActivity(), getString(R.string.validation_title),
                                        response.getMsg(), getString(R.string.ok), false);
                                binding.etConfirmPw.getText().clear();
                                binding.etPw.getText().clear();

                            } else {
                                utilities.dialogOK(getActivity(), getString(R.string.validation_title),
                                        response.getMsg(), getString(R.string.ok), false);
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Handle error
                        progressDialog.dismiss();
                        Log.e("Categ_product_error", e.toString());

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
}
