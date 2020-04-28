package com.grocery.gtohome.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.bumptech.glide.Glide;
import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.adapter.CategoryProduct_Adapter;
import com.grocery.gtohome.adapter.TestimonialSliderAdapter;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentProductDetailsBinding;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.model.SliderModel;
import com.grocery.gtohome.model.category_product_model.ProductOptionValue;
import com.grocery.gtohome.model.product_details.Product_Details_Model;
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
public class Product_Details_Fragment extends Fragment {
    FragmentProductDetailsBinding binding;
    String Product_Id;
    private TestimonialSliderAdapter sliderAdapter;
    private int dotsCount;
    private ImageView[] dotes;
    private List<ProductOptionValue> productOptionValueList = new ArrayList<>();
    private Utilities utilities;
    private SessionManager sessionManager;
    private String Customer_Id;
    private String QtyName;
    private String QtyId="";
    private String product_option_id;


    public static Product_Details_Fragment newInstance(String movieTitle) {
        Product_Details_Fragment fragmentAction = new Product_Details_Fragment();
        Bundle args = new Bundle();
        //args.putString(KEY_MOVIE_TITLE, movieTitle);
        fragmentAction.setArguments(args);

        return fragmentAction;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_details, container, false);
        View root = binding.getRoot();
        utilities = Utilities.getInstance(getActivity());
        sessionManager = new SessionManager(getActivity());
        Customer_Id = sessionManager.getUser().getCustomerId();
        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.product_details));
        } catch (Exception e) {
        }

        if (getArguments() != null) {
            Product_Id = getArguments().getString("Product_Id");
        }


        if (Connectivity.isConnected(getActivity())) {
            getProductDetails();
        } else {
            Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
        }

        SetTestimonials();

        //click spinner

        binding.spinnerQty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
                String selecteditem = adapter.getItemAtPosition(i).toString();
                QtyName = productOptionValueList.get(i).getName();
                String QtyPrice = productOptionValueList.get(i).getPrice();
                QtyId = productOptionValueList.get(i).getProductOptionValueId();

                if (selecteditem.equals(QtyName)) {
                    binding.tvPrice.setText(QtyPrice);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        //click add cart
        binding.tvAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String option_key;
                if (product_option_id!=null){
                   option_key= "option["+product_option_id+"]";
                }else {
                    option_key= "option[]";
                }

                Log.e("option_key",option_key.toString());

                if (Connectivity.isConnected(getActivity())) {
                    AddCartProduct(Product_Id, Customer_Id, "0", "1",QtyId,option_key);
                } else {
                    utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
                }

            }
        });


        return root;

    }

    @SuppressLint("CheckResult")
    private void AddCartProduct(String product_id, String customer_id, String api_id, String quantity, String qty_option, String option_key) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
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
                                utilities.dialogOK(getActivity(), "", response.getMsg(), getString(R.string.ok), false);

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


    @SuppressLint("CheckResult")
    private void getProductDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("product_id", Product_Id);

        apiInterface.ProductDetails(Product_Id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Product_Details_Model>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(Product_Details_Model response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_details_pro", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {

                                for (int i = 0; i < response.getProducts().size(); i++) {
                                    binding.tvProName.setText(response.getProducts().get(i).getName());
                                    binding.tvPrice.setText(response.getProducts().get(i).getPrice());
                                    binding.tvProDesc.setText(response.getProducts().get(i).getDescription());
                                    binding.tvReward.setText(response.getProducts().get(i).getReward().toString());
                                    binding.tvStockStatus.setText(response.getProducts().get(i).getStockStatus());
                                    binding.tvProcode.setText(response.getProducts().get(i).getModel());

                                    Glide    //image loading
                                            .with(getActivity())
                                            .load(response.getProducts().get(i).getThumb())
                                            //.centerCrop()
                                            .placeholder(R.drawable.placeholder_image)
                                            .into(binding.ivProductImg);

                                    //set spinner
                                    if (response.getProducts().get(i).getOptions() != null && !response.getProducts().get(i).getOptions().isEmpty()) {
                                        for (int k = 0; k < response.getProducts().get(i).getOptions().size(); k++) {
                                            product_option_id=response.getProducts().get(i).getOptions().get(k).getProductOptionId();
                                            ArrayList<String> QtyNameList = new ArrayList<>();
                                            for (int j = 0; j < response.getProducts().get(i).getOptions().get(i).getProductOptionValue().size(); j++) {
                                                String QtyName = response.getProducts().get(i).getOptions().get(k).getProductOptionValue().get(j).getName();
                                                String QtyPrice = response.getProducts().get(i).getOptions().get(k).getProductOptionValue().get(j).getPrice();
                                                String QtyOptionValueId = response.getProducts().get(i).getOptions().get(k).getProductOptionValue().get(j).getOptionValueId();
                                                String QtyProductOptionValueId = response.getProducts().get(i).getOptions().get(k).getProductOptionValue().get(j).getProductOptionValueId();

                                                productOptionValueList.add(new ProductOptionValue(QtyName, QtyPrice, QtyOptionValueId, QtyProductOptionValueId));
                                                QtyNameList.add(QtyName);
                                            }
                                            ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, QtyNameList);
                                            binding.spinnerQty.setAdapter(adp);

                                        }
                                    } else {
                                        binding.llSpin.setVisibility(View.GONE);
                                        binding.tvPrice.setText(response.getProducts().get(i).getPrice());
                                    }

                                    //****set related product
                                    if (response.getProducts().get(i).getRelatedProducts() != null &&
                                            !response.getProducts().get(i).getRelatedProducts().isEmpty()) {

                                        CategoryProduct_Adapter friendsAdapter = new CategoryProduct_Adapter(response.getProducts().get(i).getRelatedProducts(), getActivity());
                                        binding.setFeatureAdapter(friendsAdapter);//set databinding adapter
                                        friendsAdapter.notifyDataSetChanged();
                                    }

                                }


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


    private void SetTestimonials() {
        ArrayList<SliderModel> listarray1 = new ArrayList<>();

        listarray1.add(new SliderModel(getString(R.string.equipro_test1), R.drawable.user_img));
        listarray1.add(new SliderModel(getString(R.string.equipro_test1), R.drawable.user_img));
        listarray1.add(new SliderModel(getString(R.string.equipro_test1), R.drawable.user_img));

        //set slider
        //dotesIndicater(0);
        sliderAdapter = new TestimonialSliderAdapter(getActivity(), listarray1);
        binding.sliderPager.setAdapter(sliderAdapter);
        binding.sliderPager.setPageTransformer(true, new ZoomOutSlideTransformer());
        binding.sliderPager.setCurrentItem(0);
        binding.sliderPager.addOnPageChangeListener(pageChangeListener);
        dotesIndicater();

    }

    @SuppressLint("ClickableViewAccessibility")
    public void dotesIndicater() {
        dotsCount = sliderAdapter.getCount();
        dotes = new ImageView[dotsCount];
        binding.linearLayout.removeAllViews();
        for (int i = 0; i < dotsCount; i++) {
            dotes[i] = new ImageView(getActivity());
            dotes[i].setImageResource(R.drawable.circle_inactive);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    20,
                    20
            );

            params.setMargins(4, 0, 4, 0);

            final int presentPosition = i;
            dotes[presentPosition].setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    binding.sliderPager.setCurrentItem(presentPosition);
                    return true;
                }

            });


            binding.linearLayout.addView(dotes[i], params);

            //linearLayout.addView(dotes[i]);
            //linearLayout.bringToFront();
        }
        dotes[0].setImageResource(R.drawable.circle_active);
        /*if (dotes.length > 0) {
            dotes[i].setImageResource(R.drawable.circle_active);
        }*/
    }


    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            //dotesIndicater(position);

            //for (int i = 0; i < dotes.length; i++) {
            /*for (ImageView dote : dotsCount) {
                dotes[position].setImageResource(R.drawable.circle_inactive);
            }*/

            for (int i = 0; i < dotsCount; i++) {
                dotes[i].setImageDrawable(getResources().getDrawable(R.drawable.circle_inactive));
            }

            dotes[position].setImageResource(R.drawable.circle_active);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
