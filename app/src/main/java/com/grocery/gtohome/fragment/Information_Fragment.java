package com.grocery.gtohome.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.Base_Url;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentEditInfoBinding;
import com.grocery.gtohome.databinding.FragmentInfoBinding;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.model.company_info_model.Company_infoModel;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Connectivity;
import com.grocery.gtohome.utils.Utilities;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

/**
 * Created by Raghvendra Sahu on 06-May-20.
 */
public class Information_Fragment extends AppCompatActivity {
    FragmentInfoBinding binding;
    private Utilities utilities;
    private String Info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_info);
        utilities = Utilities.getInstance(this);

        binding.toolbar.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (getIntent() != null) {
            Info = getIntent().getStringExtra("Info");
            binding.toolbar.tvToolbar.setText(Info);
        }

        if (Connectivity.isConnected(Information_Fragment.this)) {
            CallInfoApi(Info);
        } else {
            // Toast.makeText(context, getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
            utilities.dialogOK(Information_Fragment.this, getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
        }

    }


    @SuppressLint("CheckResult")
    private void CallInfoApi(String info) {
        final ProgressDialog progressDialog = new ProgressDialog(Information_Fragment.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.InfoCompany()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Company_infoModel>() {
                    @SuppressLint("SetJavaScriptEnabled")
                    @Override
                    public void onNext(Company_infoModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_my_test", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                binding.webview.getSettings().setJavaScriptEnabled(true);
                                binding.webview.getSettings().setLoadWithOverviewMode(true);
                                binding.webview.getSettings().setUseWideViewPort(true);
                                binding.webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);

                                WebSettings webSettings =  binding.webview.getSettings();
                                webSettings.setDefaultTextEncodingName("utf-8");

                                for (int i = 0; i < response.getInformations().size(); i++) {
                                    String inform = response.getInformations().get(i).getTitle();

                                    if (info.equals("About Us")) {
                                        if (inform.equals("About Us")) {
                                            binding.webview.loadDataWithBaseURL("", response.getInformations().get(i).getDescription(), "text/html", "UTF-8", "");
                                        }
                                    }else if (info.equals("Terms")){
                                        if (inform.equals("Terms &amp; Conditions")) {
                                            binding.webview.loadDataWithBaseURL("", response.getInformations().get(i).getDescription(), "text/html", "UTF-8", "");
                                        }
                                    }else if (info.equals("DeliveryInfo")){
                                        if (inform.equals("Delivery Information")) {
                                            binding.webview.loadDataWithBaseURL("", response.getInformations().get(i).getDescription(), "text/html", "UTF-8", "");
                                        }
                                     }else if (info.equals("Privacy")){
                                        if (inform.equals("Privacy Policy")) {
                                            binding.webview.loadDataWithBaseURL("", response.getInformations().get(i).getDescription(), "text/html", "UTF-8", "");
                                        }
                                    }else if (info.equals("GH PRIME MEMBERSHIP")){
                                        if (inform.equals("GH PRIME MEMBERSHIP")) {
                                            binding.webview.loadDataWithBaseURL("", response.getInformations().get(i).getDescription(), "text/html", "UTF-8", "");
                                        }
                                    }

                                }

                            } else {
                                Toast.makeText(Information_Fragment.this, response.getMsg(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
