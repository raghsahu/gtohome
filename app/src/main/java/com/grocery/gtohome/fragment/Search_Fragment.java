package com.grocery.gtohome.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
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
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.adapter.CategoryProduct_Adapter;
import com.grocery.gtohome.adapter.CustomExpandableListAdapter;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.Base_Url;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentHomeBinding;
import com.grocery.gtohome.databinding.FragmentSearchBinding;
import com.grocery.gtohome.model.CategoryNameId;
import com.grocery.gtohome.model.category_model.CategoryChild;
import com.grocery.gtohome.model.category_model.CategoryModel;
import com.grocery.gtohome.model.category_model.CategoryName;
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

/**
 * Created by Raghvendra Sahu on 08-Apr-20.
 */
public class Search_Fragment extends Fragment {
    FragmentSearchBinding binding;
    SessionManager session;
    private String Customer_Id;
    private Utilities utilities;
    private ArrayList<String> catList= new ArrayList<>();
    private List<CategoryNameId> categoryChildList= new ArrayList<>();
    private List<CategoryNameId> AllcategoryList= new ArrayList<>();
    private String category_id;
    private String sub_category="0",description="0";

    public static Search_Fragment newInstance(String movieTitle) {
        Search_Fragment fragmentAction = new Search_Fragment();
        Bundle args = new Bundle();
        //args.putString(KEY_MOVIE_TITLE, movieTitle);
        fragmentAction.setArguments(args);

        return fragmentAction;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        View root = binding.getRoot();
        utilities = Utilities.getInstance(getActivity());
        session = new SessionManager(getActivity());
        Customer_Id = session.getUser().getCustomerId();

        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.search));
            ((MainActivity) getActivity()).CheckBottom(1);
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
        //***************set spinner category and sub category********************
        if (session.getCategoryData() != null && !session.getCategoryData().isEmpty()) {
            for (int i=0; i<session.getCategoryData().size(); i++){
                for (int j=0; j<session.getCategoryData().get(i).getChildren().size(); j++){
                    categoryChildList.add(new CategoryNameId(session.getCategoryData().get(i).getChildren().get(j).getName(),
                                session.getCategoryData().get(i).getChildren().get(j).getCategory_id() ));

                }
            }

            //new list for spinner subcategory item
            AllcategoryList.add(0,new CategoryNameId("All Categories",""));
            for (int i=0; i<categoryChildList.size(); i++){
                AllcategoryList.add(new CategoryNameId(categoryChildList.get(i).getName(),
                        categoryChildList.get(i).getCategory_id()));
            }

            for (int i=0; i<AllcategoryList.size(); i++){
                String cat_name;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                   cat_name= String.valueOf(Html.fromHtml(AllcategoryList.get(i).getName(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                   cat_name= String.valueOf(Html.fromHtml(AllcategoryList.get(i).getName()));
                }

                catList.add(cat_name);
            }

            ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, catList) {
                @NonNull
                @Override
                //set hint text by default in center
                public View getView(int position, @Nullable View cView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, cView, parent);
                    view.setPadding(view.getLeft(), view.getTop(), 0, view.getBottom());
                    return view;
                }
            };
            aa.setDropDownViewResource(R.layout.list_item_spinner);
            //Setting the ArrayAdapter data on the Spinner
            binding.spinCategory.setAdapter(aa);

        } else {
            if (Connectivity.isConnected(getActivity())) {
               // Category_SubCategoryData();
            } else {
                utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
            }
        }

        //*****************select spinner category*********************************
        binding.spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
                String selecteditem = adapter.getItemAtPosition(i).toString();

                if (AllcategoryList != null && !AllcategoryList.isEmpty()) {
                    if (selecteditem.equalsIgnoreCase(AllcategoryList.get(i).getName())){
                        if (AllcategoryList.get(i).getName().equalsIgnoreCase("All Categories")){
                            category_id = "";
                        }else {
                            category_id = AllcategoryList.get(i).getCategory_id();
                        }

                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        binding.tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String search_text=binding.etSearch.getText().toString();

                if (binding.checkSubCategory.isChecked()){
                    sub_category="1";
                }else {
                    sub_category="0";
                }
                if (binding.checkProDescription.isChecked()){
                    description="0";
                }else {
                    description="0";
                }

                if (binding.etSearch.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter text", Toast.LENGTH_SHORT).show();
                }else {

                   // if (search_text.length()<3){
                     //   utilities.dialogOK(getActivity(), getString(R.string.validation_title), "Please enter minimum 3 character",
                     //           getString(R.string.ok), false);
                  //  }else {
                        if (Connectivity.isConnected(getActivity())){
                            getSearchProduct(Customer_Id,category_id,sub_category,description,binding.etSearch.getText().toString());
                        }else {
                            utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_check_internet),
                                    getString(R.string.ok), false);
                        }
                  //  }



                }

            }
        });


        return root;

    }


    @SuppressLint("CheckResult")
    private void getSearchProduct(String customer_Id, String category_id, String sub_category, String description, String et_search) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.SearchApi(customer_Id, category_id, sub_category,description,et_search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CategoryProductModel>() {
                    @Override
                    public void onNext(CategoryProductModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("search_test", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {

                                if (response.getProducts()!=null && !response.getProducts().isEmpty()){
                                    CategoryProduct_Adapter friendsAdapter = new CategoryProduct_Adapter(response.getProducts(), getActivity(),"");
                                    binding.setFeatureAdapter(friendsAdapter);//set databinding adapter
                                    friendsAdapter.notifyDataSetChanged();
                                }else {
                                    utilities.dialogOKOnBack(getActivity(), getString(R.string.validation_title),
                                            "Product not match this criteria", getString(R.string.ok), false);
                                }

                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                utilities.dialogOKOnBack(getActivity(), getString(R.string.validation_title),
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
                        Log.e("mr_product_error", e.toString());
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
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

    private void Category_SubCategoryData() {

    }
}
