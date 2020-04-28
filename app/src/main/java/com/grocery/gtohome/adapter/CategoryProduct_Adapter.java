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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.grocery.gtohome.BR;
import com.grocery.gtohome.R;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.CategoryProductListBinding;
import com.grocery.gtohome.databinding.FeatureProductListBinding;
import com.grocery.gtohome.fragment.Product_Details_Fragment;
import com.grocery.gtohome.model.FilterBy;
import com.grocery.gtohome.model.SampleModel;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.model.category_product_model.CategoryProduct_List;
import com.grocery.gtohome.model.category_product_model.ProductOptionValue;
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
 * Created by Raghvendra Sahu on 26-Apr-20.
 */
public class CategoryProduct_Adapter extends RecyclerView.Adapter<CategoryProduct_Adapter.ViewHolder> {

    private final String Customer_Id;
    private List<CategoryProduct_List> dataModelList;
    Context context;
    private String QtyOptionValueId;
    private Utilities utilities;
    SessionManager sessionManager;
    // List<ProductOptionValue> productOptionValueList = new ArrayList<>();



    public CategoryProduct_Adapter(List<CategoryProduct_List> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        context = ctx;
        utilities = Utilities.getInstance(context);
        sessionManager = new SessionManager(context);
        Customer_Id = sessionManager.getUser().getCustomerId();

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CategoryProductListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.category_product_list, parent, false);

        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CategoryProduct_List dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.setModel(dataModel);
        // holder.itemRowBinding.setItemClickListener(this);

        holder.itemRowBinding.ivImage.setOnClickListener(new View.OnClickListener() {
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

        //***********************set quantity in spinner*******************
        if (dataModel.getOptions()!=null && !dataModel.getOptions().isEmpty()){
            for (int i=0; i<dataModel.getOptions().size(); i++){
                ArrayList<String> QtyNameList = new ArrayList<>();
                for (int j=0; j<dataModel.getOptions().get(i).getProductOptionValue().size(); j++){
                    String QtyName=dataModel.getOptions().get(i).getProductOptionValue().get(j).getName();
                    String QtyPrice=dataModel.getOptions().get(i).getProductOptionValue().get(j).getPrice();
                    String QtyOptionValueId=dataModel.getOptions().get(i).getProductOptionValue().get(j).getOptionValueId();
                    String QtyProductOptionValueId=dataModel.getOptions().get(i).getProductOptionValue().get(j).getProductOptionValueId();

                    //   productOptionValueList.add(new ProductOptionValue(QtyName,QtyPrice,QtyOptionValueId,QtyProductOptionValueId));
                    QtyNameList.add(QtyName);
                }
                ArrayAdapter<String> adp = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, QtyNameList);
                holder.itemRowBinding.spinnerQty.setAdapter(adp);

            }
        }else {
            holder.itemRowBinding.llSpin.setVisibility(View.GONE);
            holder.itemRowBinding.tvPrice.setText(dataModel.getPrice());
        }


        holder.itemRowBinding.spinnerQty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
                String  selecteditem =  adapter.getItemAtPosition(i).toString();

                for (int k=0; k<dataModel.getOptions().size(); k++) {
                    for (int j = 0; j < dataModel.getOptions().get(k).getProductOptionValue().size(); j++) {
                        String QtyName = dataModel.getOptions().get(k).getProductOptionValue().get(j).getName();
                        String QtyPrice = dataModel.getOptions().get(k).getProductOptionValue().get(j).getPrice();
                        QtyOptionValueId = dataModel.getOptions().get(k).getProductOptionValue().get(j).getProductOptionValueId();
                        if (selecteditem.equals(QtyName)){
                            holder.itemRowBinding.tvPrice.setText(QtyPrice);
                        }

                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }
        });

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

        //*************************************************************

        //add cart on click
        holder.itemRowBinding.tvAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Product_Id=dataModel.getProductId();
                String tvCountNumber=holder.itemRowBinding.tvCountNumber.getText().toString();

                String product_option_id=null;
                if (dataModel.getOptions() != null && !dataModel.getOptions().isEmpty()) {
                    for (int k = 0; k < dataModel.getOptions().size(); k++) {
                        product_option_id = dataModel.getOptions().get(k).getProductOptionId();

                    }
                }

                String option_key;
                if (product_option_id!=null){
                    option_key= "option["+product_option_id+"]";
                }else {
                    option_key= "option[]";
                }

                Log.e("option_key",option_key.toString());

                if (Connectivity.isConnected(context)) {
                    AddCartProduct(Product_Id, Customer_Id, "0", tvCountNumber,QtyOptionValueId,option_key);
                } else {
                    utilities.dialogOK(context, context.getString(R.string.validation_title), context.getString(R.string.please_check_internet), context.getString(R.string.ok), false);
                }

            }
        });



    }

    @SuppressLint("CheckResult")
    private void AddCartProduct(String product_id, String customer_id, String api_id, String quantity, String qty_option, String option_key) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("product_id", product_id);
        map.put("api_id", api_id);
        map.put("quantity", quantity);
        map.put("customer_id", customer_id);
        map.put(option_key, qty_option);

        apiInterface.AddCart(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SimpleResultModel>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(SimpleResultModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_add_cart", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                utilities.dialogOK(context, "", response.getMsg(), context.getString(R.string.ok), false);

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
    public int getItemCount() {
        return dataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CategoryProductListBinding itemRowBinding;

        public ViewHolder(CategoryProductListBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.model, obj);
            itemRowBinding.executePendingBindings();
        }
    }
}
