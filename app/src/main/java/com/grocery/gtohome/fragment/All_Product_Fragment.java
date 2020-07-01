package com.grocery.gtohome.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.adapter.CategoryProduct_Adapter;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentAllProductsBinding;
import com.grocery.gtohome.model.FilterBy;
import com.grocery.gtohome.model.FilterByModel;
import com.grocery.gtohome.model.category_product_model.CategoryProductModel;
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
 * Created by Raghvendra Sahu on 10-Apr-20.
 */
public class All_Product_Fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    FragmentAllProductsBinding binding;
    String SubCategory_Id,GH_Offer="",manufacturer_id="";
    private Utilities utilities;
    List<FilterBy> filterByModelList = new ArrayList<>();
    ArrayList<String> filterName = new ArrayList<>();
    String sort, order;
    private int mCurrentIndex;
    SessionManager sessionManager;

    public static All_Product_Fragment newInstance(String SubCategory_Id, int curent_pos) {
        All_Product_Fragment fragmentAction = new All_Product_Fragment();
        Bundle args = new Bundle();
        args.putString("SubCategory_Id", SubCategory_Id);
        args.putString("manufacturer_id", "");
        args.putInt("curent_pos", curent_pos);
        fragmentAction.setArguments(args);

        return fragmentAction;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_products, container, false);
        View root = binding.getRoot();
        utilities = Utilities.getInstance(getActivity());
         binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
         binding.swipeToRefresh.setOnRefreshListener(this);
         sessionManager=new SessionManager(getActivity());
        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.all_products));
        } catch (Exception e) {
        }

        if (getArguments() != null) {
            SubCategory_Id = getArguments().getString("SubCategory_Id");
            manufacturer_id = getArguments().getString("manufacturer_id");
            GH_Offer = getArguments().getString("GH_Offer");
            mCurrentIndex = getArguments().getInt("curent_pos");
            Log.e("selectedID",SubCategory_Id);
        }

        //get product
        if (Connectivity.isConnected(getActivity())) {
           // getCategoryWiseProduct("p.sort_order","ASC");
            if (GH_Offer!=null && !GH_Offer.equalsIgnoreCase("") && GH_Offer.equalsIgnoreCase("Special")){
                binding.llFilter.setVisibility(View.GONE);
                ((MainActivity) getActivity()).Update_header("Special product");
                getSpecialProduct();
            }else {
                getFilterBy();
            }
        } else {
            Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
        }


        binding.spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {

             String  selecteditem =  adapter.getItemAtPosition(i).toString();
                //or this can be also right: selecteditem = level[i];
                sort=filterByModelList.get(i).getValue();
                order=filterByModelList.get(i).getOrder();

                Log.e("selected_item",""+sort+" "+order);
                getCategoryWiseProduct(sort,order);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }
        });


        return root;
    }

    @SuppressLint("CheckResult")
    private void getSpecialProduct() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        apiInterface.SpecialProductApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CategoryProductModel>() {
                    @Override
                    public void onNext(CategoryProductModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("special_pro", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                CategoryProduct_Adapter friendsAdapter = new CategoryProduct_Adapter(response.getProducts(), getActivity(),GH_Offer);
                                binding.setFeatureAdapter(friendsAdapter);//set databinding adapter
                                friendsAdapter.notifyDataSetChanged();

                                binding.swipeToRefresh.setVisibility(View.VISIBLE);
                            } else {
                                binding.swipeToRefresh.setVisibility(View.GONE);
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                utilities.dialogOKOnBack(getActivity(), getString(R.string.validation_title),
                                        response.getMsg(), getString(R.string.ok), true);
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
    private void getFilterBy() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
         progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        apiInterface.FilterByApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<FilterByModel>() {
                    @Override
                    public void onNext(FilterByModel response) {
                        //Handle logic
                        try {
                            filterByModelList.clear();
                            filterName.clear();
                           progressDialog.dismiss();
                            Log.e("result_category_pro", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                filterByModelList=response.getSortby();
                                for (int i=0; i<response.getSortby().size(); i++){
                                    filterName.add(response.getSortby().get(i).getText());
                                }
                                ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, filterName);
                                binding.spinnerFilter.setAdapter(adp);
                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
//                                utilities.dialogOKOnBack(getActivity(), getString(R.string.validation_title),
//                                        response.getMsg(), getString(R.string.ok), true);
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
    private void getCategoryWiseProduct(String sort, String order) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("category_id", SubCategory_Id);
        map.put("sort", sort);
        map.put("order", order);
        map.put("manufacturer_id", manufacturer_id);

        apiInterface.CategoryProductApi(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CategoryProductModel>() {
                    @Override
                    public void onNext(CategoryProductModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_category_pro", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                CategoryProduct_Adapter friendsAdapter = new CategoryProduct_Adapter(response.getProducts(), getActivity(),"");
                                binding.setFeatureAdapter(friendsAdapter);//set databinding adapter
                                friendsAdapter.notifyDataSetChanged();

                                binding.swipeToRefresh.setVisibility(View.VISIBLE);
                            } else {
                                binding.swipeToRefresh.setVisibility(View.GONE);
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                utilities.dialogOKOnBack(getActivity(), getString(R.string.validation_title),
                                        response.getMsg(), getString(R.string.ok), true);
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

        if (Connectivity.isConnected(getActivity())) {
            // getCategoryWiseProduct("p.sort_order","ASC");
            if (GH_Offer!=null && !GH_Offer.equalsIgnoreCase("") && GH_Offer.equalsIgnoreCase("Special")){
                binding.llFilter.setVisibility(View.GONE);
                getSpecialProduct();
            }else {
                getCategoryWiseProduct(sort,order);
            }
        } else {
            Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        mCurrentIndex = ((LinearLayoutManager) binding.recyclerAllPro.getLayoutManager()).findFirstVisibleItemPosition();
        sessionManager.setCurrent_Position(mCurrentIndex);
    }

    @Override
    public void onResume(){
        super.onResume();
        mCurrentIndex = sessionManager.getCurrent_Position();
        binding.recyclerAllPro.getLayoutManager().scrollToPosition(mCurrentIndex);
    }
}
