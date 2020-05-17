package com.grocery.gtohome.fragment.my_basket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.adapter.Shopping_List_Adapter;
import com.grocery.gtohome.adapter.TotalAmount_Adapter;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentCartCoopanBinding;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.model.cart_model.CartModel;
import com.grocery.gtohome.model.cart_model.CartTotal;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

import static com.grocery.gtohome.activity.MainActivity.tv_budge;
import static com.grocery.gtohome.api_client.Base_Url.BaseUrl;

/**
 * Created by Raghvendra Sahu on 09-Apr-20.
 */
public class CartCoopanFragment extends Fragment {
    FragmentCartCoopanBinding binding;
    private String TotalPrice,SubTotal;
    private Utilities utilities;
    private SessionManager sessionManager;
    private String Customer_Id;
    private String Et_gift="",Et_Coupan="";
    List<CartTotal>cartTotals=new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart_coopan, container, false);
        View root = binding.getRoot();
        utilities = Utilities.getInstance(getActivity());
        sessionManager = new SessionManager(getActivity());
        Customer_Id = sessionManager.getUser().getCustomerId();
        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.my_basket));
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

        if (getArguments()!=null){
            cartTotals= (List<CartTotal>) getArguments().getSerializable("Cart_Total");
            for (int i=0; i<cartTotals.size(); i++){
                if (cartTotals.get(i).getTitle().equals("Total")){
                    TotalPrice= cartTotals.get(i).getText();
                }

            }

            TotalAmount_Adapter friendsAdapter = new TotalAmount_Adapter(cartTotals,getActivity());
            binding.setSubtotalAdapter(friendsAdapter);//set databinding adapter
            friendsAdapter.notifyDataSetChanged();
        }

        binding.tvCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Et_Coupan= binding.etCoupon.getText().toString();
                if (Et_Coupan.isEmpty()){
                    utilities.dialogOK(getActivity(), getString(R.string.validation_title), "Please Enter Coupon code", getString(R.string.ok), false);
                }else {
                    ApplyCoopan(Et_Coupan);
                }

            }
        });

        binding.tvApplyCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Et_gift= binding.etGift.getText().toString();
                if (Et_gift.isEmpty()){
                    utilities.dialogOK(getActivity(), getString(R.string.validation_title), "Please Enter Gift code", getString(R.string.ok), false);
                }else {
                    ApplyGift(Et_gift);
                }

            }
        });


        binding.tvCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ChekoutActivity.class);
                intent.putExtra("TotalPrice",TotalPrice);
               // intent.putExtra("SubTotal",SubTotal);
                startActivity(intent);
            }
        });





        return root;

    }

    @SuppressLint("CheckResult")
    private void ApplyGift(String et_gift) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("customer_id", Customer_Id);
        map.put("voucher", et_gift);

        apiInterface.ApplyGiftApi(map)
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

                                OpenOkDialog(response.getMsg());
//                                utilities.dialogOK(getActivity(), getString(R.string.validation_title),
//                                        response.getMsg(), getString(R.string.ok), false);

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

    @SuppressLint("CheckResult")
    private void ApplyCoopan(String et_coupan) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("customer_id", Customer_Id);
        map.put("coupon", et_coupan);

        apiInterface.ApplyCoopanApi(map)
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

                                OpenOkDialog(response.getMsg());
//                                utilities.dialogOK(getActivity(), getString(R.string.validation_title),
//                                        response.getMsg(), getString(R.string.ok), false);

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

    private void OpenOkDialog(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage(Html.fromHtml(msg));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            CallViewCartApi();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @SuppressLint("CheckResult")
    private void CallViewCartApi() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("customer_id", Customer_Id);
        map.put("coupon", Et_Coupan);
        map.put("voucher", Et_gift);

        apiInterface.CartListApi(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CartModel>() {
                    @Override
                    public void onNext(CartModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_category_pro", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {

                                for (int i=0; i<response.getTotals().size(); i++){
                                    if (response.getTotals().get(i).getTitle().equals("Total")){
                                        TotalPrice= response.getTotals().get(i).getText();
                                    }
//                                    if (response.getTotals().get(i).getTitle().equals("Sub-Total")){
//                                        sub_total= response.getTotals().get(i).getText();
//                                    }
                                }

                                TotalAmount_Adapter friendsAdapter = new TotalAmount_Adapter(response.getTotals(),getActivity());
                                binding.setSubtotalAdapter(friendsAdapter);//set databinding adapter
                                friendsAdapter.notifyDataSetChanged();

                            } else {
                                utilities.dialogOK(getActivity(), getString(R.string.validation_title), response.getMsg(), getString(R.string.ok), false);
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
