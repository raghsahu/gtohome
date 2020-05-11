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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.adapter.AddressBook_Adapter;
import com.grocery.gtohome.adapter.WishList_Adapter;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentMyAccountBinding;
import com.grocery.gtohome.databinding.FragmentWishlistBinding;
import com.grocery.gtohome.model.SampleModel;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.model.wishlist_model.Wishlist_Model;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Connectivity;
import com.grocery.gtohome.utils.Utilities;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

import static com.grocery.gtohome.api_client.Base_Url.BaseUrl;

/**
 * Created by Raghvendra Sahu on 09-Apr-20.
 */
public class WishList_Fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    FragmentWishlistBinding binding;
    private Utilities utilities;
    private SessionManager sessionManager;
    private String Customer_Id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wishlist, container, false);
        View root = binding.getRoot();
        utilities = Utilities.getInstance(getActivity());
        sessionManager = new SessionManager(getActivity());
        Customer_Id = sessionManager.getUser().getCustomerId();
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
        binding.swipeToRefresh.setOnRefreshListener(this);

        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.my_wishlist));
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

        //get wish product
        if (Connectivity.isConnected(getActivity())) {
            getWishItem();
        } else {
            //Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
            utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
        }


        return root;
    }

    @SuppressLint("CheckResult")
    private void getWishItem() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        HashMap<String, String> map = new HashMap<String, String>();
      //  map.put("product_id", product_id);
        map.put("customer_id", Customer_Id);
       // map.put("remove", "1");

        apiInterface.GetWishlist(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Wishlist_Model>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(Wishlist_Model response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_add_cart", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                               // utilities.dialogOK(getActivity(), "","Success: You have added to your wish list", getString(R.string.ok), false);

                                WishList_Adapter friendsAdapter = new WishList_Adapter(response.getWishlist(),getActivity());
                                binding.setWishlistAdapter(friendsAdapter);//set databinding adapter
                                friendsAdapter.notifyDataSetChanged();
                                binding.swipeToRefresh.setVisibility(View.VISIBLE);
                            } else {
                                binding.swipeToRefresh.setVisibility(View.GONE);
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                utilities.dialogOK(getActivity(), "","No WishList item found", getString(R.string.ok), false);
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Handle error
                        progressDialog.dismiss();
                        Log.e("product_details_error", e.toString());

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
            getWishItem();
        }else {
            utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
        }
    }
}
