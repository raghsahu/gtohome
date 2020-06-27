package com.grocery.gtohome.fragment.my_basket;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.adapter.Shopping_List_Adapter;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentMyBasketBinding;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.model.cart_model.CartModel;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Connectivity;
import com.grocery.gtohome.utils.Utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

import static com.grocery.gtohome.activity.MainActivity.tv_budge;
import static com.grocery.gtohome.api_client.Base_Url.BaseUrl;

/**
 * Created by Raghvendra Sahu on 08-Apr-20.
 */
public class MyBasket_Fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    FragmentMyBasketBinding binding;
    private Utilities utilities;
    private SessionManager sessionManager;
    private String Customer_Id;
    Shopping_List_Adapter friendsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_basket, container, false);
        View root = binding.getRoot();
        utilities = Utilities.getInstance(getActivity());
        sessionManager = new SessionManager(getActivity());
        Customer_Id = sessionManager.getUser().getCustomerId();
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
        binding.swipeToRefresh.setOnRefreshListener(this);

        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.my_basket));
            ((MainActivity) getActivity()).CheckBottom(2);
        } catch (Exception e) {
        }
        //onback press call
        View view = getActivity().findViewById(R.id.img_back);
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            //Do your stuff
            imageView.setVisibility(View.GONE);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).onBackPressed();
                }
            });
        }

        //get cart product
        if (Connectivity.isConnected(getActivity())) {
            getCartItem(false);
        } else {
            //Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
            utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
        }

        binding.tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get cart total price
                if (Connectivity.isConnected(getActivity())) {
                    getCartItem(true);
                } else {
                    //Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
                    utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
                }

            }
        });

        binding.tvClearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get cart total price
                if (Connectivity.isConnected(getActivity())) {
                    DeleteCartItemDialog();
                } else {
                    //Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
                    utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
                }

            }
        });

        return root;

    }

    private void DeleteCartItemDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage(Html.fromHtml("Are you sure delete all item!"));
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DeleteCartItem();
            }
        });

        alertDialogBuilder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @SuppressLint("CheckResult")
    private void DeleteCartItem() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("customer_id", Customer_Id);

        apiInterface.CartRemoveApi(map)
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
                              //  ((MainActivity) getActivity()).CountCart("0");
                                tv_budge.setText("0");
                                binding.setCartlistAdapter(null);//set databinding adapter
                                friendsAdapter.notifyDataSetChanged();

                                utilities.dialogOK(getActivity(), getString(R.string.validation_title),
                                        response.getMsg(), getString(R.string.ok), false);

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
    private void getCartItem(final boolean total) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("customer_id", Customer_Id);
        map.put("coupon", "");
        map.put("voucher", "");

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
                                if (total){
                                  //  ((MainActivity) getActivity()).CountCart(response.getCartCount().toString());
                                    tv_budge.setText(response.getCartCount().toString());
                                    String total_amount = "",sub_total="";
                                    for (int j=0; j<response.getProducts().size(); j++){
                                        if (!response.getProducts().get(j).getStock()){
                                            utilities.dialogOK(getActivity(), getString(R.string.validation_title),
                                                    "Please remove Out of Stock product", getString(R.string.ok), false);
                                            break;
                                        }else {

                                            if(j == response.getProducts().size() - 1){
                                                CartCoopanFragment fragment2 = new CartCoopanFragment();
                                                //  DeliveryAddressFragment fragment2 = new DeliveryAddressFragment();
                                                Bundle bundle = new Bundle();
                                                // bundle.putSerializable("Cart_Total", (Serializable) response.getTotals());
                                                bundle.putSerializable("Cart_Total", (Serializable) response.getTotals());
                                                //  bundle.putString("TotalPrice",total_amount);
                                                // bundle.putString("SubTotal",sub_total);
                                                FragmentManager manager = getActivity().getSupportFragmentManager();
                                                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                                                fragmentTransaction.replace(R.id.frame, fragment2);
                                                fragmentTransaction.addToBackStack(null);
                                                fragmentTransaction.commit();
                                                fragment2.setArguments(bundle);
                                            }


                                        }
                                    }

                                }else {
                                    friendsAdapter = new Shopping_List_Adapter(response.getProducts(),getActivity());
                                    binding.setCartlistAdapter(friendsAdapter);//set databinding adapter
                                    friendsAdapter.notifyDataSetChanged();
                                }

                                binding.swipeToRefresh.setVisibility(View.VISIBLE);
                            } else {
                                tv_budge.setText("0");
                                binding.swipeToRefresh.setVisibility(View.GONE);
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
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


    @Override
    public void onRefresh() {
        binding.swipeToRefresh.setRefreshing(false);

        if (Connectivity.isConnected(getActivity())){
            getCartItem(false);
        }else {
            utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
        }
    }
}
