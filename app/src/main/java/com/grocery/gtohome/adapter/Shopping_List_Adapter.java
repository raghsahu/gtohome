package com.grocery.gtohome.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.grocery.gtohome.BR;
import com.grocery.gtohome.R;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.ShoppingItemListBinding;
import com.grocery.gtohome.databinding.WishItemListBinding;
import com.grocery.gtohome.model.SampleModel;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.model.cart_model.CartModel;
import com.grocery.gtohome.model.cart_model.CartProduct;
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
public class Shopping_List_Adapter extends RecyclerView.Adapter<Shopping_List_Adapter.ViewHolder> {

    private List<CartProduct> dataModelList;
    Context context;
    private Utilities utilities;
    private SessionManager sessionManager;
    private String Customer_Id;

    public Shopping_List_Adapter(List<CartProduct> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        context = ctx;
        utilities = Utilities.getInstance(context);
        sessionManager = new SessionManager(context);
        Customer_Id = sessionManager.getUser().getCustomerId();

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ShoppingItemListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.shopping_item_list, parent, false);

        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CartProduct dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.setModel(dataModel);
        // holder.itemRowBinding.setItemClickListener(this);
        if (dataModel.getStock()){
            holder.itemRowBinding.tvStockStatus.setText("In stock");
            holder.itemRowBinding.tvStockStatus.setTextColor(Color.parseColor("#84C225"));
        }else {
            holder.itemRowBinding.tvStockStatus.setText("Out of stock");
            holder.itemRowBinding.tvStockStatus.setTextColor(Color.parseColor("#D9534F"));
        }


        if (dataModel.getOption() != null && !dataModel.getOption().isEmpty()) {
            for (int i = 0; i < dataModel.getOption().size(); i++) {
                holder.itemRowBinding.tvGmQty.setText("Quantity: " + dataModel.getOption().get(i).getValue());
            }
        } else {
            holder.itemRowBinding.tvGmQty.setVisibility(View.GONE);
        }

        ///**********************plus minus cart item
        holder.itemRowBinding.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String item_id=getProductCategorywises.get(position).getId();
                //  Log.e("item_id_cart", item_id);
                int qty = Integer.parseInt(holder.itemRowBinding.tvCountNumber.getText().toString());
                if (qty > 1) {
                    qty = qty - 1;
                    holder.itemRowBinding.tvCountNumber.setText(String.valueOf(qty));
                }
                Log.e("item_qty", String.valueOf(qty));


            }
        });

        holder.itemRowBinding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String item_id=getProductCategorywises.get(position).getId();
                // Log.e("item_id_cart", item_id);
                int qty = Integer.parseInt(holder.itemRowBinding.tvCountNumber.getText().toString());
                qty = qty + 1;
                Log.e("item_qty", String.valueOf(qty));

                holder.itemRowBinding.tvCountNumber.setText(String.valueOf(qty));
            }
        });

        holder.itemRowBinding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cart_id=dataModel.getCartId();
                if (Connectivity.isConnected(context)) {
                    RemoveCartItem(Customer_Id,cart_id,dataModel,position);
                } else {
                    //Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
                    utilities.dialogOK(context, context.getString(R.string.validation_title), context.getString(R.string.please_check_internet), context.getString(R.string.ok), false);
                }

            }
        });

        holder.itemRowBinding.tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cart_id=dataModel.getCartId();
                String cart_qty=holder.itemRowBinding.tvCountNumber.getText().toString();
                if (Connectivity.isConnected(context)) {
                    UpdateCartItem(Customer_Id,cart_id,dataModel,position,cart_qty,holder.itemRowBinding.tvCountNumber,dataModel.getPrice(),holder.itemRowBinding.tvTotalPrice);
                } else {
                    //Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
                    utilities.dialogOK(context, context.getString(R.string.validation_title), context.getString(R.string.please_check_internet), context.getString(R.string.ok), false);
                }

            }
        });
    }

    @SuppressLint("CheckResult")
    private void UpdateCartItem(String customer_id, String cart_id, final CartProduct dataModel, int position,
                                final String cart_qty, final TextView tvCountNumber, final String price, final TextView tvTotalPrice) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("customer_id", customer_id);
        map.put("cart_id", cart_id);
        map.put("quantity", cart_qty);

        apiInterface.UpdateCartItemApi(map)
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
                                dataModel.setQuantity(cart_qty);
                                tvCountNumber.setText(dataModel.getQuantity());

                                String BasePrice=price.substring(1);
                                double totalPrice=Double.parseDouble(BasePrice)*Double.parseDouble(tvCountNumber.getText().toString());

                                dataModel.setTotal("₹"+totalPrice);
                                tvTotalPrice.setText(dataModel.getTotal());

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

    @SuppressLint("CheckResult")
    private void RemoveCartItem(String customer_id, String cart_id, final CartProduct dataModel, int position) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("customer_id", customer_id);
        map.put("cart_id", cart_id);

        apiInterface.RemoveCartItemApi(map)
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
        public ShoppingItemListBinding itemRowBinding;

        public ViewHolder(ShoppingItemListBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.model, obj);
            itemRowBinding.executePendingBindings();
        }
    }
}
