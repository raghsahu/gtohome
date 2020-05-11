package com.grocery.gtohome.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.grocery.gtohome.BR;
import com.grocery.gtohome.R;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.AddressListBinding;
import com.grocery.gtohome.databinding.WishItemListBinding;
import com.grocery.gtohome.fragment.Product_Details_Fragment;
import com.grocery.gtohome.model.SampleModel;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.model.wishlist_model.Wishlist_Data;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Connectivity;
import com.grocery.gtohome.utils.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

import static com.grocery.gtohome.api_client.Base_Url.BaseUrl;

/**
 * Created by Raghvendra Sahu on 09-Apr-20.
 */
public class WishList_Adapter extends RecyclerView.Adapter<WishList_Adapter.ViewHolder> {

    private List<Wishlist_Data> dataModelList;
    Context context;
    private Utilities utilities;
    private SessionManager sessionManager;
    private String Customer_Id;


    public WishList_Adapter(List<Wishlist_Data> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        context = ctx;
        utilities = Utilities.getInstance(context);
        sessionManager = new SessionManager(context);
        Customer_Id = sessionManager.getUser().getCustomerId();

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        WishItemListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.wish_item_list, parent, false);

        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Wishlist_Data dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.setModel(dataModel);
        // holder.itemRowBinding.setItemClickListener(this);

         holder.itemRowBinding.ivRemove .setOnClickListener(new View.OnClickListener() {
         @Override
          public void onClick(View v) {

             String product_id=dataModel.getProductId();
             if (Connectivity.isConnected(context)) {
                 RemoveWishItem(Customer_Id,product_id,dataModel,position);
             } else {
                 //Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
                 utilities.dialogOK(context, context.getString(R.string.validation_title), context.getString(R.string.please_check_internet), context.getString(R.string.ok), false);
             }
         }
         });


        holder.itemRowBinding.cardWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product_Details_Fragment fragment2 = new Product_Details_Fragment();
                Bundle bundle = new Bundle();
                // bundle.putSerializable("MyPhotoModelResponse", dataModelList.get(position));
                bundle.putString("Product_Id",dataModel.getProductId());
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                fragment2.setArguments(bundle);
            }
        });
    }

    @SuppressLint("CheckResult")
    private void RemoveWishItem(String customer_id, String product_id, Wishlist_Data dataModel, int position) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("product_id", product_id);
        map.put("customer_id", customer_id);
        map.put("remove", "0");

        apiInterface.AddWishlist(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SimpleResultModel>() {
                    @Override
                    public void onNext(SimpleResultModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_category_pro", "" + response.getMsg());

                            if (response.getStatus()) {
                                Toast.makeText(context, "" + response.getMsg(), Toast.LENGTH_SHORT).show();
                                dataModelList.remove(dataModel);
                                notifyDataSetChanged();
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
    public int getItemCount() {
        return dataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public WishItemListBinding itemRowBinding;

        public ViewHolder(WishItemListBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.model, obj);
            itemRowBinding.executePendingBindings();
        }
    }
}
