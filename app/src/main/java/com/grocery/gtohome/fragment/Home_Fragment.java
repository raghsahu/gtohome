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

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.adapter.CategoryList_Adapter;
import com.grocery.gtohome.adapter.CategoryProduct_Adapter;
import com.grocery.gtohome.adapter.PopularBrand_Adapter;
import com.grocery.gtohome.adapter.SliderAdapter_range;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.Base_Url;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentHomeBinding;
import com.grocery.gtohome.model.popular_brand.PopularBrandModel;
import com.grocery.gtohome.model.SampleModel;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.model.SliderModel;
import com.grocery.gtohome.model.category_model.CategoryModel;
import com.grocery.gtohome.model.category_product_model.CategoryProductModel;
import com.grocery.gtohome.model.home_slider.HomeSliderBanner;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Connectivity;
import com.grocery.gtohome.utils.Utilities;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

/**
 * Created by Raghvendra Sahu on 08-Apr-20.
 */
public class Home_Fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    FragmentHomeBinding binding;
    SliderAdapter_range sliderAdapter_range;
    private int dotsCount;
    private ImageView[] dotes;
    PopularBrand_Adapter friendsAdapter;
    private Utilities utilities;
    ArrayList<SampleModel>sampleModels;
    SessionManager session;


    public static Home_Fragment newInstance(String movieTitle) {
        Home_Fragment fragmentAction = new Home_Fragment();
        Bundle args = new Bundle();
        //args.putString(KEY_MOVIE_TITLE, movieTitle);
        fragmentAction.setArguments(args);

        return fragmentAction;
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//
//        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
//        binding.swipeToRefresh.setOnRefreshListener(this);
//    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View root = binding.getRoot();
        utilities = Utilities.getInstance(getActivity());
        session = new SessionManager(getActivity());
       // binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
       // binding.swipeToRefresh.setOnRefreshListener(this);

        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.home));
            ((MainActivity) getActivity()).CheckBottom(0);
          //  ((MainActivity) getActivity()).CountCart("2");
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

        // initialize a SliderLayout
        binding.homeImgSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        //binding.homeImgSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        binding.homeImgSlider.setCustomAnimation(new DescriptionAnimation());
        binding.homeImgSlider.setDuration(3000);
        //slider image list
        SliderListArray();

        //side slide bar
        setHorizontalSliderItem();

        getPopularBrandList();//popular brand list
        getFeatureProduct();

        if (session.getCategoryData()!=null && !session.getCategoryData().isEmpty()){
            CategoryList_Adapter friendsAdapter = new CategoryList_Adapter(session.getCategoryData(),getActivity());
            binding.setCategoryAdapter(friendsAdapter);//set databinding adapter
            friendsAdapter.notifyDataSetChanged();

        }else {
        if (Connectivity.isConnected(getActivity())){
            getAllCategory();
        }else {
            utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
        }
        }

//        binding.swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                binding.swipeToRefresh.setRefreshing(false);
//
//                if (Connectivity.isConnected(getActivity())){
//                    getAllCategory();
//                }else {
//                    utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
//                }
//            }
//        });


        binding.tvAllGrocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                All_Product_Fragment fragment2 = new All_Product_Fragment();
                Bundle bundle = new Bundle();
                // bundle.putSerializable("MyPhotoModelResponse", dataModelList.get(position));
                //   bundle.putString("Title",dataModel.getCategory_name());
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                fragment2.setArguments(bundle);
            }
        });

//        binding.tvViewallFruit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                All_Product_Fragment fragment2 = new All_Product_Fragment();
//                Bundle bundle = new Bundle();
//                // bundle.putSerializable("MyPhotoModelResponse", dataModelList.get(position));
//                //   bundle.putString("Title",dataModel.getCategory_name());
//                FragmentManager manager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = manager.beginTransaction();
//                fragmentTransaction.replace(R.id.frame, fragment2);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                fragment2.setArguments(bundle);
//            }
//        });

        binding.tvViewAllFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                All_Product_Fragment fragment2 = new All_Product_Fragment();
                Bundle bundle = new Bundle();
                // bundle.putSerializable("MyPhotoModelResponse", dataModelList.get(position));
                //   bundle.putString("Title",dataModel.getCategory_name());
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                fragment2.setArguments(bundle);
            }
        });

        //subscribe news
        binding.tvSubscribeNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String news_address=binding.etEmail.getText().toString();
                if (news_address.isEmpty()){
                    utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_enter_email), getString(R.string.ok), false);
                }else {
                    if (Connectivity.isConnected(getActivity())){
                        AddNewsLetter(news_address);
                    }else {
                        utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
                    }
                }
            }
        });


        return root;

    }

    @SuppressLint("CheckResult")
    private void AddNewsLetter(String news_address) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.AddNews(news_address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SimpleResultModel>() {
                    @Override
                    public void onNext(SimpleResultModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("blog_details", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                binding.etEmail.getText().clear();
                                utilities.dialogOKOnBack(getActivity(), "", response.getMsg(), getActivity().getString(R.string.ok), false);

                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                utilities.dialogOKOnBack(getActivity(), "", response.getMsg(), getActivity().getString(R.string.ok), false);

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
    private void getFeatureProduct() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(true);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.FeatureProductApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CategoryProductModel>() {
                    @Override
                    public void onNext(CategoryProductModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_my_test", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                               // session.setCategoryData(response.getCategories());

                                CategoryProduct_Adapter friendsAdapter = new CategoryProduct_Adapter(response.getProducts(), getActivity(), "");
                                binding.setFeatureAdapter(friendsAdapter);//set databinding adapter
                                friendsAdapter.notifyDataSetChanged();

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
                        Log.e("mr_product_error", e.toString());

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
    private void getAllCategory() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.CategoryApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CategoryModel>() {
                    @Override
                    public void onNext(CategoryModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_my_test", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getResult().equalsIgnoreCase("true")) {
                                session.setCategoryData(response.getCategories());

                                CategoryList_Adapter friendsAdapter = new CategoryList_Adapter(response.getCategories(),getActivity());
                                binding.setCategoryAdapter(friendsAdapter);//set databinding adapter
                                friendsAdapter.notifyDataSetChanged();

                             //   binding.swipeToRefresh.setVisibility(View.VISIBLE);
                            } else {
                              //  binding.swipeToRefresh.setVisibility(View.GONE);
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
                        Log.e("mr_product_error", e.toString());

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
    private void getPopularBrandList() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.PopularBrandApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<PopularBrandModel>() {
                    @Override
                    public void onNext(PopularBrandModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_my_test", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {

                                friendsAdapter = new PopularBrand_Adapter(response.getResponse().getBanners(),getActivity());
                                binding.setPopularBrandAdapter(friendsAdapter);//set databinding adapter
                                friendsAdapter.notifyDataSetChanged();

                            } else {


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
    private void SliderListArray() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.HomeHorizontalSlideApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<HomeSliderBanner>() {
                    @Override
                    public void onNext(HomeSliderBanner response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_my_test", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {

                                for (int i=0; i<response.getResponse().getBanners().size(); i++) {
                                    DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
                                    // initialize a SliderLayout
                                    textSliderView
                                            .image(response.getResponse().getBanners().get(i).getImage())
                                            .setScaleType(BaseSliderView.ScaleType.Fit);

                                    binding.homeImgSlider.addSlider(textSliderView);

                                }

                            } else {


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

    private void setHorizontalSliderItem() {
        ArrayList<SliderModel> listarray1 = new ArrayList<>();

        listarray1.add(new SliderModel("Wide Collection \nRefreshments",R.drawable.side_baner1));
        listarray1.add(new SliderModel("Break The Chain",R.drawable.side_baner2));
        listarray1.add(new SliderModel("Get California RED \n @ 12$ \n Shop for $200",R.drawable.sub_banner3));

        //**********
        sliderAdapter_range = new SliderAdapter_range(getActivity(),listarray1);
        binding.sliderPager.setAdapter(sliderAdapter_range);
        binding.sliderPager.setPageTransformer(true, new ZoomOutSlideTransformer());
        binding.sliderPager.setCurrentItem(0);
        binding.sliderPager.addOnPageChangeListener(pageChangeListener);
        dotesIndicater();
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            for (int i = 0; i < dotsCount; i++) {
                dotes[i].setImageDrawable(getResources().getDrawable(R.drawable.circle_inactive));
            }

            dotes[position].setImageResource(R.drawable.circle_active);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @SuppressLint("ClickableViewAccessibility")
    public void dotesIndicater() {
        dotsCount = sliderAdapter_range.getCount();
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

        }
        dotes[0].setImageResource(R.drawable.circle_active);

    }


    @Override
    public void onRefresh() {
              //  binding.swipeToRefresh.setRefreshing(false);

        if (Connectivity.isConnected(getActivity())){
            getAllCategory();
        }else {
            utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
        }
    }
}
