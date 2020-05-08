package com.grocery.gtohome.fragment.my_basket;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.grocery.gtohome.adapter.ShippingMethodAdapter;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.Base_Url;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentDeliveryAddressBinding;
import com.grocery.gtohome.model.country_model.CountryData;
import com.grocery.gtohome.model.country_model.CountryModel;
import com.grocery.gtohome.fragment.state_model.StateModel;
import com.grocery.gtohome.fragment.state_model.StateZoneData;
import com.grocery.gtohome.model.address_model.AddressData;
import com.grocery.gtohome.model.address_model.AddressModel;
import com.grocery.gtohome.model.address_model.SaveAddressModel;
import com.grocery.gtohome.model.shipping_method.ShippingMethod;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Connectivity;
import com.grocery.gtohome.utils.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
public class ChekoutActivity extends AppCompatActivity implements ShippingMethodAdapter.AdapterCallback {
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

    private String TotalPrice,amount ="";
    private String SubTitle="",SubCode="";
    String payment_code="", payment_title="";

    boolean billing_details=false, deliver_details=false,deliver_method=false,payment_method=false;

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

        //set clickable true false
            binding.tvDeliveryDetails.setEnabled(false);
            binding.tvDeliveryMethod.setEnabled(false);
            binding.tvPaymentMethod.setEnabled(false);

        binding.toolbar.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //********************************
        if (getIntent()!=null){
            TotalPrice=getIntent().getStringExtra("TotalPrice");
        }

        //get existing address
        if (Connectivity.isConnected(context)) {
            getExistingAddress(Customer_Id);
            getCountryName();
            getShippingMethod();
        } else {
            //Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
        }

        billing_details=true;
        binding.llBilingDetails.setVisibility(View.VISIBLE);

        //***********************
        binding.tvBillingContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!binding.radioExistAddress.isChecked() && !binding.radioOtherAddress.isChecked()) {
                    utilities.dialogOK(context, getString(R.string.validation_title), "Please select shipping address", getString(R.string.ok), false);
                } else {
                    if (binding.radioExistAddress.isChecked()) {
                        if (address_id != null && !address_id.isEmpty()) {
//                            Intent intent = new Intent(context, PaymentMethod_Activity.class);
//                            intent.putExtra("TotalPrice",TotalPrice);
//                            intent.putExtra("CountryId",country_id);
//                            intent.putExtra("ZoneId",zone_id);
//                            intent.putExtra("AddressId",address_id);
//                            startActivity(intent);

                            binding.llBilingDetails.setVisibility(View.GONE);
                            billing_details=false;

                            binding.tvDeliveryDetails.setEnabled(true);
                            binding.llDeliveryDetails.setVisibility(View.VISIBLE);
                            deliver_details=true;

                        } else {
                            utilities.dialogOK(context, getString(R.string.validation_title), "Address not found, Please select address!", getString(R.string.ok), false);
                        }
                    } else {
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

                            if (Connectivity.isConnected(context)) {
                                SaveAddressApi(first_name, last_name, company_name, address1, address2, city, post_code,false);
                            } else {
                                utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
                            }


                        } else {
                            utilities.dialogOK(context, getString(R.string.validation_title), "Please enter all fields!", getString(R.string.ok), false);
                        }
                    }
                }
            }
        });

        //******************************************************************

            binding.tvContinueDelivery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!binding.radioExistDelivery.isChecked() && !binding.radioOtherDelivery.isChecked()) {
                        utilities.dialogOK(context, getString(R.string.validation_title), "Please select delivery address", getString(R.string.ok), false);
                    } else {
                        if (binding.radioExistDelivery.isChecked()) {
                            if (address_delivery_id != null && !address_delivery_id.isEmpty()) {
//                            Intent intent = new Intent(context, PaymentMethod_Activity.class);
//                            intent.putExtra("TotalPrice",TotalPrice);
//                            intent.putExtra("CountryId",country_id);
//                            intent.putExtra("ZoneId",zone_id);
//                            intent.putExtra("AddressId",address_id);
//                            startActivity(intent);

                                binding.llDeliveryDetails.setVisibility(View.GONE);
                                deliver_details=false;

                                binding.tvDeliveryMethod.setEnabled(true);
                                binding.llDeliveryMethod.setVisibility(View.VISIBLE);
                                deliver_method=true;

                            } else {
                                utilities.dialogOK(context, getString(R.string.validation_title), "Address not found, Please select address!", getString(R.string.ok), false);
                            }
                        } else {
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

                                if (Connectivity.isConnected(context)) {
                                    SaveAddressApi(first_name, last_name, company_name, address1, address2, city, post_code,true);
                                } else {
                                    utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
                                }


                            } else {
                                utilities.dialogOK(context, getString(R.string.validation_title), "Please enter all fields!", getString(R.string.ok), false);
                            }
                        }
                    }
                }
            });

            //***************************************************
            binding.tvContinueDeliveryMethod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    binding.llDeliveryMethod.setVisibility(View.GONE);
                    deliver_method=false;

                    binding.tvPaymentMethod.setEnabled(true);
                    binding.llPaymentMethod.setVisibility(View.VISIBLE);
                    payment_method=true;

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


        //****************************************************
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
    private void getShippingMethod() {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        amount=TotalPrice.substring(1);

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
                                }else {
                                    OpenDialog(context, getString(R.string.validation_title), response.getAddressId(),
                                            "Save Delivery Address Successful", getString(R.string.ok));
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

//                Intent intent = new Intent(context, PaymentMethod_Activity.class);
//                intent.putExtra("TotalPrice",TotalPrice);
//                intent.putExtra("CountryId",country_id);
//                intent.putExtra("ZoneId",zone_id);
//                intent.putExtra("AddressId",addressId.toString());
//                startActivity(intent);

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

}
