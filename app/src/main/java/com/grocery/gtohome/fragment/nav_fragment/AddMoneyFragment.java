package com.grocery.gtohome.fragment.nav_fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.activity.PayUMoneyActivity;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentAddMoneyBinding;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Utilities;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

import static com.grocery.gtohome.api_client.Base_Url.BaseUrl;

public class AddMoneyFragment extends AppCompatActivity implements PaymentResultListener {
    FragmentAddMoneyBinding binding;
    private Utilities utilities;
    SessionManager session;
    private String CustomerId;
    private String amount;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_pyment_method);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_add_money);
        context=AddMoneyFragment.this;

        utilities = Utilities.getInstance(context);
        session = new SessionManager(context);
        CustomerId=session.getUser().getCustomerId();

        binding.toolbar.tvToolbar.setText(getString(R.string.addmoney));

        binding.toolbar.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Checkout.preload(context);

//        try {
//            ((MainActivity) getActivity()).Update_header(getString(R.string.addmoney));
//
//        } catch (Exception e) {
//        }
        //onback press call
//        View view = getActivity().findViewById(R.id.img_back);
//        if (view instanceof ImageView) {
//            ImageView imageView = (ImageView) view;
//            //Do your stuff
//            imageView.setVisibility(View.GONE);
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ((MainActivity) getActivity()).onBackPressed();
//                }
//            });
//        }

        binding.tvAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               amount= binding.etAmount.getText().toString();

               if (!amount.isEmpty()){
                   if (!binding.radioPayu.isChecked() && !binding.radioRozarpay.isChecked()){
                       utilities.dialogOK(context, getString(R.string.validation_title),
                               "Please select payment method", getString(R.string.ok), false);
                   }else {
                       if (binding.radioPayu.isChecked()){
                           Intent intent = new Intent(context, PayUMoneyActivity.class);
                           intent.putExtra("OrderId","");
                           intent.putExtra("amount",amount);
                           intent.putExtra("Order_status_id","");
                           intent.putExtra("PaymentType","Wallet");
                           startActivity(intent);

                       }else {
                           startPayment(amount);
                       }
                   }

               }else {
                   utilities.dialogOK(context, getString(R.string.validation_title),
                           "Please enter amount", getString(R.string.ok), false);
               }

            }
        });


       // return root;

    }

    private void startPayment(String finalamount) {
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "GtoHome Products");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");

            // String payment = editTextPayment.getText().toString();

            double total = Double.parseDouble(finalamount);
            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
            preFill.put("email", session.getUser().getEmail());
            preFill.put("contact", session.getUser().getTelephone());

            options.put("prefill", preFill);

            co.open(AddMoneyFragment.this, options);
        } catch (Exception e) {
            Toast.makeText(context, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        // Toast.makeText(this, "Payment successfully done! " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        try {
            Toast.makeText(context,"sucess",Toast.LENGTH_LONG).show();
            UpdatePaymentStatus("RazorPay: "+razorpayPaymentID);
        }catch (Exception e){
            Log.e("OnPaymentSuccess_error", "Exception in onPaymentSuccess", e);
        }

    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(context, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }

    @SuppressLint("CheckResult")
    private void UpdatePaymentStatus(String razorpayPaymentID) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        apiInterface.UpdateWalletStatus(CustomerId,amount,razorpayPaymentID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SimpleResultModel>() {
                    @Override
                    public void onNext(SimpleResultModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                utilities.dialogOKOnBack(context, getString(R.string.validation_title),
                                        response.getMsg(), getString(R.string.ok), true);

                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                utilities.dialogOK(context, getString(R.string.validation_title),
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
