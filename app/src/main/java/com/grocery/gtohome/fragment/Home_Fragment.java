package com.grocery.gtohome.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.adapter.FeatureProduct_Adapter;
import com.grocery.gtohome.adapter.FruitVeg_Adapter;
import com.grocery.gtohome.adapter.SliderAdapter_range;
import com.grocery.gtohome.databinding.FragmentHomeBinding;
import com.grocery.gtohome.model.SampleModel;
import com.grocery.gtohome.model.SliderModel;

import java.util.ArrayList;

/**
 * Created by Raghvendra Sahu on 08-Apr-20.
 */
public class Home_Fragment extends Fragment {
    FragmentHomeBinding binding;
    SliderAdapter_range sliderAdapter_range;
    private int dotsCount;
    private ImageView[] dotes;
    FruitVeg_Adapter friendsAdapter;

    ArrayList<SampleModel>sampleModels;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View root = binding.getRoot();
        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.home));
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

        getFruitVegList();//fruit veg list
        getFeaturedProductList();//fruit veg list
        getPopularBrandList();//popular brand list


        return root;

    }

    private void getPopularBrandList() {
        sampleModels=new ArrayList<>();
        sampleModels.add(new SampleModel("Apple", "20", R.drawable.ashirwad_logo));
        sampleModels.add(new SampleModel("Apple Gala", "100", R.drawable.brand2));
        sampleModels.add(new SampleModel("Banana Robest", "50", R.drawable.brand3));
        sampleModels.add(new SampleModel("Beans", "20", R.drawable.brand4));
        sampleModels.add(new SampleModel("Beetroot", "20", R.drawable.brand5));
        sampleModels.add(new SampleModel("Broad Beans", "20", R.drawable.brand6));

        friendsAdapter = new FruitVeg_Adapter(sampleModels,getActivity());
        binding.setFruitvegAdapter(friendsAdapter);//set databinding adapter
        friendsAdapter.notifyDataSetChanged();

    }


    private void getFeaturedProductList() {
        ArrayList<SampleModel> sampleModels=new ArrayList<>();
        sampleModels.add(new SampleModel("Apple", "20", R.drawable.apple));
        sampleModels.add(new SampleModel("Apple Gala", "100", R.drawable.gala_apple));
        sampleModels.add(new SampleModel("Banana Robest", "50", R.drawable.banana_robust));
        sampleModels.add(new SampleModel("Beans", "20", R.drawable.beans_img));
        sampleModels.add(new SampleModel("Beetroot", "20", R.drawable.beetroot));
        sampleModels.add(new SampleModel("Broad Beans", "20", R.drawable.broad_beans));

        FeatureProduct_Adapter friendsAdapter = new FeatureProduct_Adapter(sampleModels,getActivity());
        binding.setFeatureAdapter(friendsAdapter);//set databinding adapter
        friendsAdapter.notifyDataSetChanged();
    }

    private void getFruitVegList() {
        ArrayList<SampleModel> sampleModels=new ArrayList<>();
        sampleModels.add(new SampleModel("Apple", "20", R.drawable.apple));
        sampleModels.add(new SampleModel("Apple Gala", "100", R.drawable.gala_apple));
        sampleModels.add(new SampleModel("Banana Robest", "50", R.drawable.banana_robust));
        sampleModels.add(new SampleModel("Beans", "20", R.drawable.beans_img));
        sampleModels.add(new SampleModel("Beetroot", "20", R.drawable.beetroot));
        sampleModels.add(new SampleModel("Broad Beans", "20", R.drawable.broad_beans));

        FruitVeg_Adapter friendsAdapter = new FruitVeg_Adapter(sampleModels,getActivity());
        binding.setFruitvegAdapter(friendsAdapter);//set databinding adapter
        friendsAdapter.notifyDataSetChanged();
    }


    private void SliderListArray() {
        // arraylist list variable for store data;
        ArrayList<SliderModel> listarray = new ArrayList<>();

        listarray.add(new SliderModel(" ",R.drawable.banner1));
        listarray.add(new SliderModel(" ",R.drawable.banar2));
        listarray.add(new SliderModel(" ",R.drawable.banar3));

        for (int i=0; i<listarray.size(); i++) {
            DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .image(listarray.get(i).getSlide_image())
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            binding.homeImgSlider.addSlider(textSliderView);

        }

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



}
