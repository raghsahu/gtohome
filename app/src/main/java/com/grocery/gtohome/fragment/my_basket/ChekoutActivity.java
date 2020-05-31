package com.grocery.gtohome.fragment.my_basket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.OrderSuccess_Activity;
import com.grocery.gtohome.activity.PayUMoneyActivity;
import com.grocery.gtohome.activity.PaymentMethod_Activity;
import com.grocery.gtohome.adapter.HistoryItemList_Adapter;
import com.grocery.gtohome.adapter.ShippingMethodAdapter;
import com.grocery.gtohome.adapter.TotalAmount_Adapter;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.Base_Url;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentDeliveryAddressBinding;
import com.grocery.gtohome.fragment.Information_Fragment;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.model.confirm_order_model.Confirm_Order_Model;
import com.grocery.gtohome.model.country_model.CountryData;
import com.grocery.gtohome.model.country_model.CountryModel;
import com.grocery.gtohome.fragment.state_model.StateModel;
import com.grocery.gtohome.fragment.state_model.StateZoneData;
import com.grocery.gtohome.model.address_model.AddressData;
import com.grocery.gtohome.model.address_model.AddressModel;
import com.grocery.gtohome.model.address_model.SaveAddressModel;
import com.grocery.gtohome.model.create_order.CreateOrderModel;
import com.grocery.gtohome.model.shipping_method.ShippingMethod;
import com.grocery.gtohome.model.slot_model.Slot_Model;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Connectivity;
import com.grocery.gtohome.utils.GPSTracker;
import com.grocery.gtohome.utils.Utilities;
import com.razorpay.Checkout;
import com.razorpay.CheckoutActivity;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

import static com.grocery.gtohome.api_client.Base_Url.BaseUrl;

/**
 * Created by Raghvendra Sahu on 09-Apr-20.
 */
public class ChekoutActivity extends AppCompatActivity implements ShippingMethodAdapter.AdapterCallback, PaymentResultListener {
    FragmentDeliveryAddressBinding binding;
    private Utilities utilities;
    Context context;
    private SessionManager sessionManager;
    private String Customer_Id;
    ShippingMethodAdapter friendsAdapter;

    List<AddressData> AddressModelList = new ArrayList<>();
    ArrayList<String> AddressName = new ArrayList<>();
    List<CountryData> CountryModelList = new ArrayList<>();
    ArrayList<String> CountryName = new ArrayList<>();
    List<StateZoneData> stateModelList = new ArrayList<>();
    ArrayList<String> stateName = new ArrayList<>();
    String address_id, country_id, zone_id;

    List<AddressData> AddressDeliveryList = new ArrayList<>();
    ArrayList<String> AddressDeliveryName = new ArrayList<>();
    List<CountryData> CountryDeliveryList = new ArrayList<>();
    ArrayList<String> CountryDeliveryName = new ArrayList<>();
    List<StateZoneData> stateDeliveryList = new ArrayList<>();
    ArrayList<String> stateDeliveryName = new ArrayList<>();
    String address_delivery_id, country_delivery_id, zone_delivery_id;

    private String TotalPrice,SubTotal,amount ="",Et_Coupan ="",Et_gift ="",FinalTotalPrice, Finalamount="";
    private String SubTitle="",SubCode="";
    String payment_code="", payment_title="";
    boolean biling_delivery_charge=false;

    boolean billing_details=false, deliver_details=false,deliver_method=false,payment_method=false,confirm_order=false;
    String Et_Comment_Order,Et_Comment_Deliver;
    String timeslot_name;
    private String Delivery_Date;
    private GPSTracker tracker;
    private Double lat;
    private Double lng;
    String address,city,postalCode,country_loc,state_loc,knownName;
    private String Order_status_id,Order_Id;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //setContentView(R.layout.activity_pyment_method);
            binding = DataBindingUtil.setContentView(this, R.layout.fragment_delivery_address);

        context=this;
        utilities = Utilities.getInstance(context);
        sessionManager = new SessionManager(context);
        Customer_Id = sessionManager.getUser().getCustomerId();
        binding.toolbar.tvToolbar.setText("Checkout");
        tracker=new GPSTracker(ChekoutActivity.this);

        //set clickable true false
            binding.tvDeliveryDetails.setEnabled(false);
            binding.tvDeliveryMethod.setEnabled(false);
            binding.tvPaymentMethod.setEnabled(false);
            binding.tvConfirmOrder.setEnabled(false);

        binding.toolbar.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (tracker.canGetLocation()) {
            lat = tracker.getLatitude();
            lng = tracker.getLongitude();
            Log.e("current_lat ", " " + String.valueOf(lat));
            Log.e("current_Lon ", " " + String.valueOf(lng));
            getAddress(lat,lng);
        } else if (!tracker.canGetLocation()) {

            tracker.showSettingsAlert();
        }

        //********************************
        if (getIntent()!=null){
            TotalPrice=getIntent().getStringExtra("TotalPrice");
            SubTotal=getIntent().getStringExtra("SubTotal");
            Et_Coupan=getIntent().getStringExtra("Et_Coupan");
            Et_gift=getIntent().getStringExtra("Et_gift");
        }

        //get existing address
        if (Connectivity.isConnected(context)) {
            getExistingAddress(Customer_Id);
            getCountryName();

        } else {
            //Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
        }

        billing_details=true;
        binding.llBilingDetails.setVisibility(View.VISIBLE);

        //***********************
            binding.tvTerms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ChekoutActivity.this, Information_Fragment.class);
                    intent.putExtra("Info","Terms");
                    startActivity(intent);
                }
            });

        binding.tvBillingContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biling_delivery_charge=false;
                if (!binding.radioExistAddress.isChecked() && !binding.radioOtherAddress.isChecked() && !binding.radioCurrentLoc.isChecked()) {
                    utilities.dialogOK(context, getString(R.string.validation_title), "Please select shipping address", getString(R.string.ok), false);
                } else {
                    if (binding.radioExistAddress.isChecked()) {
                        if (address_id != null && !address_id.isEmpty()) {

                            binding.llBilingDetails.setVisibility(View.GONE);
                            billing_details=false;

                            binding.tvDeliveryDetails.setEnabled(true);
                            binding.llDeliveryDetails.setVisibility(View.VISIBLE);
                            deliver_details=true;

                        } else {
                            utilities.dialogOK(context, getString(R.string.validation_title), "Address not found, Please select address!", getString(R.string.ok), false);
                        }
                    } else if (binding.radioOtherAddress.isChecked()) {
                        // save address api
                        String first_name = binding.etFirstname.getText().toString();
                        String last_name = binding.etLastname.getText().toString();
                        String company_name = binding.etCompany.getText().toString();
                        String address1 = binding.etAddressOne.getText().toString();
                        String address2 = binding.etAddressTwo.getText().toString();
                        String city = binding.etCity.getText().toString();
                        String post_code = binding.etPostcode.getText().toString();

                        if (!first_name.isEmpty() && !last_name.isEmpty() && !address1.isEmpty()
                                && !city.isEmpty() && !post_code.isEmpty()) {

                            if (country_id != null && !country_id.isEmpty() && zone_id != null && !zone_id.isEmpty()){
                                if (Connectivity.isConnected(context)) {
                                    SaveAddressApi(first_name, last_name, company_name, address1, address2, city, post_code,false);
                                } else {
                                    utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
                                }

                            }else {
                                utilities.dialogOK(context, getString(R.string.validation_title), "Please select country & state", getString(R.string.ok), false);
                            }

                        } else {
                            utilities.dialogOK(context, getString(R.string.validation_title), "Please enter all fields!", getString(R.string.ok), false);
                        }
                    }else {

                        if (lat!=0.0 && lng!=0.0){
                            if (stateModelList!=null){
                                for (int i=0; i<stateModelList.size(); i++){
                                    if (state_loc.equalsIgnoreCase(stateModelList.get(i).getName())){
                                        zone_id=stateModelList.get(i).getZoneId();
                                    }
                                }
                            }

                            Log.e("curre_loc",""+country_id+" "+zone_id);

                            String first_name = sessionManager.getUser().getFirstname();
                            String last_name =  sessionManager.getUser().getLastname();
                            String company_name = " ";
                            String address1 = knownName;
                            String address2 = " ";
                            String City = city;
                            String post_code = postalCode;

                            if (Connectivity.isConnected(context)) {
                                SaveAddressApi(first_name, last_name, company_name, address1, address2, City, post_code,false);
                            } else {
                                utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
                            }

                        }else {
                            utilities.dialogOK(context, getString(R.string.validation_title), "Current address not found", getString(R.string.ok), false);
                        }

                    }
                }
            }
        });

        //******************************************************************

            binding.tvContinueDelivery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!binding.radioExistDelivery.isChecked() && !binding.radioOtherDelivery.isChecked() && !binding.radioCurrentLoc1.isChecked()) {
                        utilities.dialogOK(context, getString(R.string.validation_title), "Please select delivery address", getString(R.string.ok), false);
                    } else {
                        if (binding.radioExistDelivery.isChecked()) {
                            if (address_delivery_id != null && !address_delivery_id.isEmpty()) {
                                getShippingMethod();

                                binding.llDeliveryDetails.setVisibility(View.GONE);
                                deliver_details=false;

                                binding.tvDeliveryMethod.setEnabled(true);
                                binding.llDeliveryMethod.setVisibility(View.VISIBLE);
                                deliver_method=true;

                            } else {
                                utilities.dialogOK(context, getString(R.string.validation_title), "Address not found, Please select address!", getString(R.string.ok), false);
                            }
                        } else if (binding.radioOtherDelivery.isChecked()){
                            // save address api
                            String first_name = binding.etFirstnameDelivery.getText().toString();
                            String last_name = binding.etLastnameDelivery.getText().toString();
                            String company_name = binding.etCompanyDelivery.getText().toString();
                            String address1 = binding.etAddressOneDelivery.getText().toString();
                            String address2 = binding.etAddressTwoDelivery.getText().toString();
                            String city = binding.etCityDelivery.getText().toString();
                            String post_code = binding.etPostcodeDelivery.getText().toString();

                            if (!first_name.isEmpty() && !last_name.isEmpty() && !address1.isEmpty()
                                    && !city.isEmpty() && !post_code.isEmpty()) {
                                if (country_delivery_id != null && !country_delivery_id.isEmpty() && zone_delivery_id != null && !zone_delivery_id.isEmpty()){

                                    if (Connectivity.isConnected(context)) {
                                        biling_delivery_charge=true;
                                        SaveAddressApi(first_name, last_name, company_name, address1, address2, city, post_code,true);
                                    } else {
                                        utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
                                    }
                                }else {
                                    utilities.dialogOK(context, getString(R.string.validation_title), "Please select country & state", getString(R.string.ok), false);
                                }



                            } else {
                                utilities.dialogOK(context, getString(R.string.validation_title), "Please enter all fields!", getString(R.string.ok), false);
                            }
                        }else {
                            if (lat!=0.0 && lng!=0.0){
                                if (stateModelList!=null){
                                    for (int i=0; i<stateModelList.size(); i++){
                                        if (state_loc.equalsIgnoreCase(stateModelList.get(i).getName())){
                                            zone_id=stateModelList.get(i).getZoneId();
                                        }
                                    }
                                }

                                Log.e("curre_loc",""+country_id+" "+zone_id);

                                String first_name = sessionManager.getUser().getFirstname();
                                String last_name =  sessionManager.getUser().getLastname();
                                String company_name = " ";
                                String address1 = knownName;
                                String address2 = " ";
                                String City = city;
                                String post_code = postalCode;

                                if (Connectivity.isConnected(context)) {
                                    biling_delivery_charge=true;
                                    SaveAddressApi(first_name, last_name, company_name, address1, address2, City, post_code,true);
                                } else {
                                    utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
                                }
                            }else {
                                utilities.dialogOK(context, getString(R.string.validation_title), "Current address not found", getString(R.string.ok), false);
                            }
                        }


                    }
                }
            });

            //***************************************************
            binding.tvContinueDeliveryMethod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (SubTitle.isEmpty() && SubTitle.equals("")) {
                        utilities.dialogOK(ChekoutActivity.this, getString(R.string.validation_title),
                                "Please select shipping method", getString(R.string.ok), false);
                    } else if (Delivery_Date!=null && !Delivery_Date.isEmpty()){
                        if(timeslot_name!=null && !timeslot_name.isEmpty()) {
                            Et_Comment_Deliver=binding.etComments.getText().toString();

                            binding.llDeliveryMethod.setVisibility(View.GONE);
                            deliver_method=false;

                            binding.tvPaymentMethod.setEnabled(true);
                            binding.llPaymentMethod.setVisibility(View.VISIBLE);
                            payment_method=true;
                        }else {
                            utilities.dialogOK(ChekoutActivity.this, getString(R.string.validation_title),
                                    "Please select timeslot", getString(R.string.ok), false);

                        }

                    }else {
                        utilities.dialogOK(ChekoutActivity.this, getString(R.string.validation_title),
                                "Please select delivery date", getString(R.string.ok), false);
                    }

                }
            });

            //***************************************************
            binding.tvContinueCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (payment_code.isEmpty() && payment_code.equals("")) {
                        utilities.dialogOK(ChekoutActivity.this, getString(R.string.validation_title),
                                "Please select payment method", getString(R.string.ok), false);
                    } else {
                        if (!binding.checkTerms.isChecked()) {
                            utilities.dialogOK(ChekoutActivity.this, getString(R.string.validation_title),
                                    "Please accept Terms & Conditions", getString(R.string.ok), false);
                        } else {
                           Et_Comment_Order=binding.etCommentsOrder.getText().toString();

                            binding.llPaymentMethod.setVisibility(View.GONE);
                            payment_method=false;

                            binding.tvConfirmOrder.setEnabled(true);
                            binding.llDeliveryConfirm.setVisibility(View.VISIBLE);
                            confirm_order=true;

                            if (Connectivity.isConnected(ChekoutActivity.this)){
                                GetTotalProduct_Amount(Customer_Id,SubTotal,address_id,SubCode,SubTitle,Et_Coupan,Et_gift);
                            }else {
                                utilities.dialogOK(ChekoutActivity.this, getString(R.string.validation_title), getString(R.string.please_check_internet),
                                        getString(R.string.ok), false);
                            }
                        }
                    }


                }
            });

            //********place final order
        binding.tvConfirmOrderDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connectivity.isConnected(ChekoutActivity.this)){
                    CreateOrder(Customer_Id,Et_Comment_Order,address_id,address_delivery_id,payment_code,payment_title,SubCode,SubTitle,TotalPrice);
                }else {
                    utilities.dialogOK(ChekoutActivity.this, getString(R.string.validation_title), getString(R.string.please_check_internet),
                            getString(R.string.ok), false);
                }

            }
        });

        //**************************************************
        binding.spinExistAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
                String selecteditem = adapter.getItemAtPosition(i).toString();
                //or this can be also right: selecteditem = level[i];
                if (AddressModelList != null && !AddressModelList.isEmpty()) {
                    address_id = AddressModelList.get(i).getAddressId();
                    //country_id = AddressModelList.get(i).getCountryId();
                   // zone_id = AddressModelList.get(i).getZoneId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        //**************************************************
        binding.spinExistDelivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
                String selecteditem = adapter.getItemAtPosition(i).toString();
                //or this can be also right: selecteditem = level[i];
                if (AddressDeliveryList != null && !AddressDeliveryList.isEmpty()) {
                    address_delivery_id = AddressDeliveryList.get(i).getAddressId();
                    country_delivery_id = AddressDeliveryList.get(i).getCountryId();
                    zone_delivery_id = AddressDeliveryList.get(i).getZoneId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        //***************************************************************************************
        binding.spinCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {

                String selecteditem = adapter.getItemAtPosition(i).toString();
                //or this can be also right: selecteditem = level[i];
                if (CountryModelList != null && !CountryModelList.isEmpty()) {
                    country_id = CountryModelList.get(i).getCountryId();
                    getCountryWiseState(country_id);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
        //*spin delivery country
        binding.spinCountryDelivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {

                String selecteditem = adapter.getItemAtPosition(i).toString();
                //or this can be also right: selecteditem = level[i];
                if (CountryDeliveryList != null && !CountryDeliveryList.isEmpty()) {
                    country_delivery_id = CountryDeliveryList.get(i).getCountryId();
                    getCountryWiseState(country_delivery_id);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        //**************************************
        binding.spinState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {

                String selecteditem = adapter.getItemAtPosition(i).toString();
                //or this can be also right: selecteditem = level[i];
                if (stateModelList != null && !stateModelList.isEmpty()) {
                    zone_id = stateModelList.get(i).getZoneId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        //*******************blling address***********************
        binding.radioGroupAddress.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_exist_address:
                        binding.llOtherAddress.setVisibility(View.GONE);
                        break;
                    case R.id.radio_other_address:
                        binding.llOtherAddress.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radio_current_loc:
                        for (int i=0; i<CountryModelList.size(); i++){
                            if (country_loc.equalsIgnoreCase(CountryModelList.get(i).getName())){
                                country_id=CountryModelList.get(i).getCountryId();
                                getCountryWiseState(country_id);
                            }
                        }
                        break;
                }
            }
        });

        //*********************delivery address*********************
        binding.radioGroupDelivery.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_exist_delivery:
                        binding.llOtherDelivery.setVisibility(View.GONE);
                        break;
                    case R.id.radio_other_delivery:
                        binding.llOtherDelivery.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radio_current_loc1:
                        for (int i=0; i<CountryModelList.size(); i++){
                            if (country_loc.equalsIgnoreCase(CountryModelList.get(i).getName())){
                                country_id=CountryModelList.get(i).getCountryId();
                                getCountryWiseState(country_id);
                            }
                        }
                        break;

                }
            }
        });
        //*******************************************
        binding.tvBillingDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!billing_details){
                    binding.llBilingDetails.setVisibility(View.VISIBLE);
                    billing_details=true;
                }else {
                    binding.llBilingDetails.setVisibility(View.GONE);
                    billing_details=false;
                }

                deliver_details=false;
                deliver_method=false;
                payment_method=false;
                confirm_order=false;

                binding.llDeliveryDetails.setVisibility(View.GONE);
                binding.llDeliveryMethod.setVisibility(View.GONE);
                binding.llPaymentMethod.setVisibility(View.GONE);
                binding.llDeliveryConfirm.setVisibility(View.GONE);

            }
        });
        //*******************************************
        binding.tvDeliveryDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!deliver_details){
                    binding.llDeliveryDetails.setVisibility(View.VISIBLE);
                    deliver_details=true;
                }else {
                    binding.llDeliveryDetails.setVisibility(View.GONE);
                    deliver_details=false;
                }

                billing_details=false;
                deliver_method=false;
                payment_method=false;
                confirm_order=false;

                binding.llBilingDetails.setVisibility(View.GONE);
                binding.llDeliveryMethod.setVisibility(View.GONE);
                binding.llPaymentMethod.setVisibility(View.GONE);
                binding.llDeliveryConfirm.setVisibility(View.GONE);

            }
        });

        //*********************tv delivery method expand**********************
        binding.tvDeliveryMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!deliver_method) {
                    binding.llDeliveryMethod.setVisibility(View.VISIBLE);
                    deliver_method = true;
                } else {
                    binding.llDeliveryMethod.setVisibility(View.GONE);
                    deliver_method = false;
                }

                billing_details=false;
                deliver_details=false;
                payment_method=false;
                confirm_order=false;

                binding.llBilingDetails.setVisibility(View.GONE);
                binding.llDeliveryDetails.setVisibility(View.GONE);
                binding.llPaymentMethod.setVisibility(View.GONE);
                binding.llDeliveryConfirm.setVisibility(View.GONE);

            }
        });

        //*********************tv delivery method expand**********************
        binding.tvPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!payment_method) {
                    binding.llPaymentMethod.setVisibility(View.VISIBLE);
                    payment_method = true;
                } else {
                    binding.llPaymentMethod.setVisibility(View.GONE);
                    payment_method = false;
                }

                billing_details=false;
                deliver_details=false;
                deliver_method=false;
                confirm_order=false;

                binding.llBilingDetails.setVisibility(View.GONE);
                binding.llDeliveryDetails.setVisibility(View.GONE);
                binding.llDeliveryMethod.setVisibility(View.GONE);
                binding.llDeliveryConfirm.setVisibility(View.GONE);


            }
        });
        //*********************tv confirm order expand**********************
        binding.tvConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!confirm_order) {
                    binding.llDeliveryConfirm.setVisibility(View.VISIBLE);
                    confirm_order = true;
                } else {
                    binding.llDeliveryConfirm.setVisibility(View.GONE);
                    confirm_order = false;
                }

                billing_details=false;
                deliver_details=false;
                deliver_method=false;
                payment_method=false;

                binding.llBilingDetails.setVisibility(View.GONE);
                binding.llDeliveryDetails.setVisibility(View.GONE);
                binding.llDeliveryMethod.setVisibility(View.GONE);
                binding.llPaymentMethod.setVisibility(View.GONE);


            }
        });

        //******************************************
            binding.rgPaymentMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.radio_cod:
                            payment_code="cod";
                            payment_title="Cash On Delivery";
                            break;
                        case R.id.radio_payu:
                            payment_code="payu";
                            payment_title="PayUMoney";
                            break;
                        case R.id.radio_rozarpay:
                            payment_code="razorpay";
                            payment_title="Credit Card / Debit Card / Net Banking (Razorpay)";
                            break;

                    }
                }
            });
            //*********************tv delivery method expand**********************
            binding.tvDeliveryDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Select_date();

                }
            });


        //*******************bradio button time slot***********************
        binding.radioGroupSlot.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_slot_1:
                        timeslot_name="1";
                        break;
                    case R.id.radio_slot_2:
                        timeslot_name="2";
                        break;
                }
            }
        });

    }

    private void getAddress(Double lat, Double lng) {

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state_loc = addresses.get(0).getAdminArea();
            country_loc = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            knownName = addresses.get(0).getFeatureName();

           binding.tvCurrentLoc.setText(address);
           binding.tvCurrentLoc1.setText(address);
           // tv_find_city.setText(city);
            Log.e("address_latlng", ""+address);
//            Log.e("address_city", ""+city);
//            Log.e("address_state", ""+state_loc);
//            Log.e("address_country", ""+country_loc);
//            Log.e("address_knownName", ""+knownName);

        }catch (Exception e){

        }



    }

    @SuppressLint("CheckResult")
    private void GetTotalProduct_Amount(String customer_id, String subTotal, String address_id, String subCode, String subTitle,
                                        String et_coupan, String et_gift) {

        final ProgressDialog progressDialog = new ProgressDialog(ChekoutActivity.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("customer_id", customer_id);
        map.put("address_id", address_id);
        map.put("shipping_method[code]", subCode);
        map.put("shipping_method[title]", subTitle);
        map.put("subtotal", subTotal);
        map.put("coupon", et_coupan);
        map.put("voucher", et_gift);

        apiInterface.ConfirmOrder(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Confirm_Order_Model>() {
                    @Override
                    public void onNext(Confirm_Order_Model response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_order", "" + response.getStatus());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {

                                Order_status_id=response.getOrder_status_id();
                                if (response.getOrders().getProducts()!=null){
                                    HistoryItemList_Adapter friendsAdapter = new HistoryItemList_Adapter(response.getOrders().getProducts(),ChekoutActivity.this);
                                    binding.setMyOrderAdapter(friendsAdapter);//set databinding adapter
                                    friendsAdapter.notifyDataSetChanged();
                                }

                                for (int i=0; i<response.getOrders().getTotals().size(); i++){
                                    if (response.getOrders().getTotals().get(i).getTitle().equals("Total")){
                                        FinalTotalPrice= response.getOrders().getTotals().get(i).getText();
                                    }

                                }

                                TotalAmount_Adapter friendsAdapter = new TotalAmount_Adapter(response.getOrders().getTotals(),ChekoutActivity.this);
                                binding.setTotalAdapter(friendsAdapter);//set databinding adapter
                                friendsAdapter.notifyDataSetChanged();

                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                utilities.dialogOK(ChekoutActivity.this, getString(R.string.validation_title),
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
    private void CreateOrder(String customer_id, String et_comment, String addressId,String address_delivery_id, final String payment_code, String payment_title,
                             String subCode, String subTitle, String totalPrice) {
        final ProgressDialog progressDialog = new ProgressDialog(ChekoutActivity.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();
        // amount=TotalPrice.substring(1);
        if (FinalTotalPrice!=null && !FinalTotalPrice.isEmpty()){
            Finalamount = FinalTotalPrice.replace("₹", "").replace(",", "");
        }

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("customer_id", customer_id);
        map.put("comment", et_comment);
        map.put("address_id", addressId);
        map.put("shipping_address_id", address_delivery_id);
        map.put("payment_method[code]", payment_code);
        map.put("payment_method[title]", payment_title);
        map.put("shipping_method[code]", subCode);
        map.put("shipping_method[title]", subTitle);
        map.put("subtotal", totalPrice);
        map.put("deliverydate", Delivery_Date);
        map.put("extrafield", "");
        map.put("timeslotid", timeslot_name);
        map.put("coupon", Et_Coupan);
        map.put("voucher", Et_gift);

        apiInterface.CreateOrder(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CreateOrderModel>() {
                    @Override
                    public void onNext(CreateOrderModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_order", "" + response.getStatus());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                Order_Id=response.getOrderId().toString();

                                if (payment_code.equals("cod")){
                                    Intent intent = new Intent(ChekoutActivity.this, OrderSuccess_Activity.class);
                                    intent.putExtra("OrderId",response.getOrderId().toString());
                                    startActivity(intent);
                                }else  if (payment_code.equals("payu")){

                                    Intent intent = new Intent(ChekoutActivity.this, PayUMoneyActivity.class);
                                    intent.putExtra("OrderId",response.getOrderId().toString());
                                    intent.putExtra("amount",Finalamount);
                                    intent.putExtra("Order_status_id",Order_status_id);
                                    startActivity(intent);

                                    // CallPaymentgateway();
                                }else  if (payment_code.equals("razorpay")){

                                    startPayment(Finalamount);
                                }


                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                utilities.dialogOK(ChekoutActivity.this, getString(R.string.validation_title),
                                        "Order not submit, error", getString(R.string.ok), true);
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

    private void startPayment(String finalamount) {
        final Activity activity = this;
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Product Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");

           // String payment = editTextPayment.getText().toString();

            double total = Double.parseDouble(finalamount);
            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
            preFill.put("email", sessionManager.getUser().getEmail());
            preFill.put("contact", sessionManager.getUser().getTelephone());

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void Select_date() {
        Calendar mcurrentDate = Calendar.getInstance();
        final int[] mYear = {mcurrentDate.get(Calendar.YEAR)};
        final int[] mMonth = {mcurrentDate.get(Calendar.MONTH)};
        final int[] mDay = {mcurrentDate.get(Calendar.DAY_OF_MONTH)};
        DatePickerDialog mDatePicker = new DatePickerDialog(ChekoutActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, selectedyear);
                myCalendar.set(Calendar.MONTH, selectedmonth);
                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                String myFormat = "dd-MM-yyyy"; //Change as you need
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                binding.tvDeliveryDate.setText(sdf.format(myCalendar.getTime()));
                Delivery_Date=sdf.format(myCalendar.getTime());

                if (Connectivity.isConnected(ChekoutActivity.this)){
                    GetSlotBooking(sdf.format(myCalendar.getTime()));
                }else {
                    utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
                }


                mDay[0] = selectedday;
                mMonth[0] = selectedmonth;
                mYear[0] = selectedyear;
            }
        }, mYear[0], mMonth[0], mDay[0]);
        //mDatePicker.setTitle("Select date");
        mDatePicker.show();
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
       mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis() + 9 * 24 * 60 * 60 * 1000);
    }

    @SuppressLint("CheckResult")
    private void GetSlotBooking(String date) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        apiInterface.GetSlot(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Slot_Model>() {
                    @Override
                    public void onNext(Slot_Model response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_adderss", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {

                                //set time slot in radio button
                                binding.radioGroupSlot.setVisibility(View.VISIBLE);
                                if (response.getModulePincodedays().getPincodedaysTimeslot()==0){
                                    binding.radioSlot1.setText(Html.fromHtml(response.getModulePincodedays().getTimeslots().get_0().get1()+" "+
                                            response.getModulePincodedays().getIndividualslots().get1()));
                                    binding.radioSlot2.setText(Html.fromHtml(response.getModulePincodedays().getTimeslots().get_0().get2()+" "+
                                            response.getModulePincodedays().getIndividualslots().get2()));

                                }//else if ()


                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                utilities.dialogOK(context, getString(R.string.validation_title),
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
                        Log.e("Categ_product_error", e.toString());
                        utilities.dialogOK(context, getString(R.string.validation_title),
                                "We are sorry but we don't delivery on this date at your pincode", getString(R.string.ok), false);

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
    private void getShippingMethod() {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

       // amount=TotalPrice.substring(1);
        if (TotalPrice!=null && !TotalPrice.isEmpty()){
            amount = TotalPrice.replace("₹", "").replace(",", "");
        }


        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        apiInterface.GetShippingMethod(amount,address_delivery_id,zone_delivery_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ShippingMethod>() {
                    @Override
                    public void onNext(ShippingMethod response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_adderss", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                friendsAdapter = new ShippingMethodAdapter(response.getShippingMethods(),context);
                                binding.setShipingRateAdapter(friendsAdapter);//set databinding adapter
                                friendsAdapter.notifyDataSetChanged();

                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                utilities.dialogOK(context, getString(R.string.validation_title),
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
    private void SaveAddressApi(String first_name, String last_name, String company_name, String address1, String address2,
                                String city, String post_code, boolean delivery_continue) {

        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.SaveDeliveryAddress(Customer_Id, first_name, last_name, company_name, address1, address2, city, post_code, country_id, zone_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SaveAddressModel>() {
                    @Override
                    public void onNext(SaveAddressModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_my_test", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                // Log.e("result_my_test", "" + response.getData().getCustomerId());
                                if (delivery_continue){
                                    OpenDialogDelivery(context, getString(R.string.validation_title), response.getAddressId(),
                                            "Save Delivery Address Successful", getString(R.string.ok));
                                    if (biling_delivery_charge){
                                        getShippingMethod();
                                    }

                                }else {
                                    OpenDialog(context, getString(R.string.validation_title), response.getAddressId(),
                                            "Save Shipping Address Successful", getString(R.string.ok));
                                }


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

    private void OpenDialog(Context activity, String string, final Integer addrId, String save_delivery_address_successful, String ok) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage(Html.fromHtml("Save Billing Address Successful"));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                address_id=addrId.toString();
                binding.llBilingDetails.setVisibility(View.GONE);
                billing_details=false;

                binding.tvDeliveryDetails.setEnabled(true);
                binding.llDeliveryDetails.setVisibility(View.VISIBLE);
                deliver_details=true;

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void OpenDialogDelivery(Context activity, String string, final Integer addrId, String save_delivery_address_successful, String ok) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage(Html.fromHtml("Save Billing Address Successful"));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//                Intent intent = new Intent(context, PaymentMethod_Activity.class);
//                intent.putExtra("TotalPrice",TotalPrice);
//                intent.putExtra("CountryId",country_id);
//                intent.putExtra("ZoneId",zone_id);
//                intent.putExtra("AddressId",addressId.toString());
//                startActivity(intent);

                address_delivery_id=addrId.toString();
                binding.llDeliveryDetails.setVisibility(View.GONE);
                deliver_details=false;

                binding.tvDeliveryMethod.setEnabled(true);
                binding.llDeliveryMethod.setVisibility(View.VISIBLE);
                deliver_method=true;

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @SuppressLint("CheckResult")
    private void getCountryWiseState(String country_id) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        apiInterface.GetState(country_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<StateModel>() {
                    @Override
                    public void onNext(StateModel response) {
                        //Handle logic
                        try {
                            stateModelList.clear();
                            stateName.clear();
                            progressDialog.dismiss();
                            Log.e("result_adderss", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {

                                stateModelList = response.getZone();
                                for (int i = 0; i < response.getZone().size(); i++) {
                                    stateName.add(response.getZone().get(i).getName());
                                }
                                ArrayAdapter<String> adp = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, stateName);
                                binding.spinState.setAdapter(adp);
                                //******spin state***********
                                stateDeliveryList = response.getZone();
                                for (int i = 0; i < response.getZone().size(); i++) {
                                    stateDeliveryName.add(response.getZone().get(i).getName());
                                }
                                ArrayAdapter<String> adp1 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, stateDeliveryName);
                                binding.spinStateDelivery.setAdapter(adp1);

                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                utilities.dialogOK(context, getString(R.string.validation_title),
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
    private void getCountryName() {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        apiInterface.GetCountry()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CountryModel>() {
                    @Override
                    public void onNext(CountryModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_adderss", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {

                                CountryModelList = response.getCountries();
                                for (int i = 0; i < response.getCountries().size(); i++) {
                                    CountryName.add(response.getCountries().get(i).getName());
                                }
                                ArrayAdapter<String> adp = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, CountryName);
                                binding.spinCountry.setAdapter(adp);

                                //spin delivery country
                                CountryDeliveryList = response.getCountries();
                                for (int i = 0; i < response.getCountries().size(); i++) {
                                    CountryDeliveryName.add(response.getCountries().get(i).getName());
                                }
                                ArrayAdapter<String> adp1 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, CountryDeliveryName);
                                binding.spinCountryDelivery.setAdapter(adp1);

                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                utilities.dialogOK(context, getString(R.string.validation_title),
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
    private void getExistingAddress(String customer_id) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        apiInterface.GetExistingAddress(customer_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<AddressModel>() {
                    @Override
                    public void onNext(AddressModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_adderss", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {

                                AddressModelList = response.getAddresses();
                                for (int i = 0; i < response.getAddresses().size(); i++) {
                                    AddressName.add(response.getAddresses().get(i).getAddress1() + " " + response.getAddresses().get(i).getAddress2()
                                            + " " + response.getAddresses().get(i).getCity() + " " + response.getAddresses().get(i).getZoneCode()
                                            + " " + response.getAddresses().get(i).getCountry() + " " + response.getAddresses().get(i).getPostcode());
                                }
                                ArrayAdapter<String> adp = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, AddressName);
                                binding.spinExistAddress.setAdapter(adp);

                                //delivery spin
                                AddressDeliveryList = response.getAddresses();
                                for (int i = 0; i < response.getAddresses().size(); i++) {
                                    AddressDeliveryName.add(response.getAddresses().get(i).getAddress1() + " " + response.getAddresses().get(i).getAddress2()
                                            + " " + response.getAddresses().get(i).getCity() + " " + response.getAddresses().get(i).getZoneCode()
                                            + " " + response.getAddresses().get(i).getCountry() + " " + response.getAddresses().get(i).getPostcode());
                                }
                                ArrayAdapter<String> adp1 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, AddressDeliveryName);
                                binding.spinExistDelivery.setAdapter(adp1);

                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                utilities.dialogOK(context, getString(R.string.validation_title),
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
    public void onMethodCallback(String subTitle, String subCode) {
        SubTitle=subTitle;
        SubCode=subCode;

        Log.e("shipping_method",SubCode+" "+SubTitle);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
       // Toast.makeText(this, "Payment successfully done! " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        UpdateOrderStatus("RazorPay: "+razorpayPaymentID);

    }

    @SuppressLint("CheckResult")
    private void UpdateOrderStatus(String razorpayPaymentID) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        apiInterface.UpdateOrderStatus(Customer_Id,Order_status_id,Order_Id,razorpayPaymentID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SimpleResultModel>() {
                    @Override
                    public void onNext(SimpleResultModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                Toast.makeText(ChekoutActivity.this, "Payment successfully done! " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ChekoutActivity.this, OrderSuccess_Activity.class);
                                intent.putExtra("OrderId",Order_Id);
                                startActivity(intent);
                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                utilities.dialogOK(context, getString(R.string.validation_title),
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
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }
}
