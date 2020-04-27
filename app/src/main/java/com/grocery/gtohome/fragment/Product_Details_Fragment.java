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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.adapter.CategoryProduct_Adapter;
import com.grocery.gtohome.adapter.FeatureProduct_Adapter;
import com.grocery.gtohome.adapter.TestimonialSliderAdapter;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.Base_Url;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentAllProductsBinding;
import com.grocery.gtohome.databinding.FragmentProductDetailsBinding;
import com.grocery.gtohome.model.SampleModel;
import com.grocery.gtohome.model.SliderModel;
import com.grocery.gtohome.model.category_product_model.CategoryProductModel;
import com.grocery.gtohome.model.product_details.Product_Details_Model;
import com.grocery.gtohome.utils.Connectivity;
import com.grocery.gtohome.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
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
        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.product_details));
        } catch (Exception e) {
        }

        if (getArguments() != null) {
            Product_Id = getArguments().getString("Product_Id");
        }


        if (Connectivity.isConnected(getActivity())) {
            getProductDetails();
           // getProductDetailsVolly();
        } else {
            Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
        }

        getFeaturedProductList();//fruit veg list
        SetTestimonials();

        return root;

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
                    @Override
                    public void onNext(Product_Details_Model response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_details_pro", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {

                                for (int i=0; i<response.getProducts().size(); i++){
                                    binding.tvProName.setText(response.getProducts().get(i).getName());
                                    binding.tvPrice.setText(response.getProducts().get(i).getPrice());
                                    binding.tvProDesc.setText(response.getProducts().get(i).getDescription());


                                        Glide    //image loading
                                            .with(getActivity())
                                            .load(response.getProducts().get(i).getThumb())
                                            //.centerCrop()
                                            .placeholder(R.drawable.placeholder_image)
                                            .into(binding.ivProductImg);
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

    private void getFeaturedProductList() {
        ArrayList<SampleModel> sampleModels=new ArrayList<>();
        sampleModels.add(new SampleModel("Apple", "20", R.drawable.apple));
        sampleModels.add(new SampleModel("Apple Gala", "100", R.drawable.gala_apple));

        FeatureProduct_Adapter friendsAdapter = new FeatureProduct_Adapter(sampleModels,getActivity());
        binding.setFeatureAdapter(friendsAdapter);//set databinding adapter
        friendsAdapter.notifyDataSetChanged();
    }



    private void SetTestimonials() {
        ArrayList<SliderModel> listarray1 = new ArrayList<>();

        listarray1.add(new SliderModel(getString(R.string.equipro_test1),R.drawable.user_img));
        listarray1.add(new SliderModel(getString(R.string.equipro_test1),R.drawable.user_img));
        listarray1.add(new SliderModel(getString(R.string.equipro_test1),R.drawable.user_img));

        //set slider
        //dotesIndicater(0);
        sliderAdapter = new TestimonialSliderAdapter(getActivity(),listarray1);
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
