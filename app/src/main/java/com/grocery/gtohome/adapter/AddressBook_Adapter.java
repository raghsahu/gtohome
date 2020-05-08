package com.grocery.gtohome.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.grocery.gtohome.BR;
import com.grocery.gtohome.R;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.AddressListBinding;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.model.address_model.AddressData;
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
public class AddressBook_Adapter extends RecyclerView.Adapter<AddressBook_Adapter.ViewHolder> {

    private List<AddressData> dataModelList;
    Context context;
    private Utilities utilities;
    private SessionManager sessionManager;
    private String Customer_Id;


    public AddressBook_Adapter(List<AddressData> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        context = ctx;
        utilities = Utilities.getInstance(context);
        sessionManager = new SessionManager(context);
        Customer_Id = sessionManager.getUser().getCustomerId();

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AddressListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.address_list, parent, false);

        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AddressData dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.setModel(dataModel);
        // holder.itemRowBinding.setItemClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.itemRowBinding.tvAddress.setText(Html.fromHtml(dataModel.getAddress(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.itemRowBinding.tvAddress.setText(Html.fromHtml(dataModel.getAddress()));
        }

        holder.itemRowBinding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cart_id=dataModel.getAddressId();
                if (Connectivity.isConnected(context)) {
                    RemoveAddress(Customer_Id,cart_id,dataModel,position);
                } else {
                    //Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
                    utilities.dialogOK(context, context.getString(R.string.validation_title), context.getString(R.string.please_check_internet), context.getString(R.string.ok), false);
                }
            }
        });
    }

    @SuppressLint("CheckResult")
    private void RemoveAddress(String customer_id, String cart_id, AddressData dataModel, int position) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("customer_id", customer_id);
        map.put("address_id", cart_id);

        apiInterface.RemoveAddressApi(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SimpleResultModel>() {
                    @Override
                    public void onNext(SimpleResultModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_category_pro", "" + response.getMsg());
                            Toast.makeText(context, "" + response.getMsg(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {

                                dataModelList.remove(dataModel);
                                notifyDataSetChanged();
                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
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
        public AddressListBinding itemRowBinding;

        public ViewHolder(AddressListBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.model, obj);
            itemRowBinding.executePendingBindings();
        }
    }
}
