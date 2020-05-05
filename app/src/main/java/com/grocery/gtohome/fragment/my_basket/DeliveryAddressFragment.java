package com.grocery.gtohome.fragment.my_basket;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.activity.PaymentMethod_Activity;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.Base_Url;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentDeliveryAddressBinding;
import com.grocery.gtohome.fragment.country_model.CountryData;
import com.grocery.gtohome.fragment.country_model.CountryModel;
import com.grocery.gtohome.fragment.state_model.StateModel;
import com.grocery.gtohome.fragment.state_model.StateZoneData;
import com.grocery.gtohome.model.address_model.AddressData;
import com.grocery.gtohome.model.address_model.AddressModel;
import com.grocery.gtohome.model.address_model.SaveAddressModel;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Connectivity;
import com.grocery.gtohome.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

import static com.grocery.gtohome.api_client.Base_Url.BaseUrl;

/**
 * Created by Raghvendra Sahu on 09-Apr-20.
 */
public class DeliveryAddressFragment extends Fragment {
    FragmentDeliveryAddressBinding binding;
    private Utilities utilities;
    private SessionManager sessionManager;
    private String Customer_Id;
    List<AddressData> AddressModelList = new ArrayList<>();
    ArrayList<String> AddressName = new ArrayList<>();
    List<CountryData> CountryModelList = new ArrayList<>();
    ArrayList<String> CountryName = new ArrayList<>();
    List<StateZoneData> stateModelList = new ArrayList<>();
    ArrayList<String> stateName = new ArrayList<>();
    String address_id, country_id, zone_id;
    private String TotalPrice;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_delivery_address, container, false);
        View root = binding.getRoot();
        utilities = Utilities.getInstance(getActivity());
        sessionManager = new SessionManager(getActivity());
        Customer_Id = sessionManager.getUser().getCustomerId();

        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.my_basket));
        } catch (Exception e) {
        }
        //onback press call
        View view = getActivity().findViewById(R.id.img_back);
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            //Do your stuff
            imageView.setVisibility(View.VISIBLE);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).onBackPressed();
                }
            });
        }

        //********************************
        if (getArguments()!=null){
            TotalPrice=getArguments().getString("TotalPrice");
        }

        //get existing address
        if (Connectivity.isConnected(getActivity())) {
            getExistingAddress(Customer_Id);
            getCountryName();
        } else {
            //Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
            utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
        }

        binding.tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!binding.radioExistAddress.isChecked() && !binding.radioOtherAddress.isChecked()) {
                    utilities.dialogOK(getActivity(), getString(R.string.validation_title), "Please select shipping address", getString(R.string.ok), false);
                } else {
                    if (binding.radioExistAddress.isChecked()) {
                        if (address_id != null && !address_id.isEmpty()) {
                            Intent intent = new Intent(getActivity(), PaymentMethod_Activity.class);
                            intent.putExtra("TotalPrice",TotalPrice);
                            intent.putExtra("CountryId",country_id);
                            intent.putExtra("ZoneId",zone_id);
                            intent.putExtra("AddressId",address_id);
                            startActivity(intent);
                        } else {
                            utilities.dialogOK(getActivity(), getString(R.string.validation_title), "Address not found, Please select address!", getString(R.string.ok), false);
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

                            if (Connectivity.isConnected(getActivity())) {
                                SaveAddressApi(first_name, last_name, company_name, address1, address2, city, post_code);
                            } else {
                                utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
                            }


                        } else {
                            utilities.dialogOK(getActivity(), getString(R.string.validation_title), "Please enter all fields!", getString(R.string.ok), false);
                        }
                    }
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
                    country_id = AddressModelList.get(i).getCountryId();
                    zone_id = AddressModelList.get(i).getZoneId();
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

        //******************************************
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


        return root;

    }

    @SuppressLint("CheckResult")
    private void SaveAddressApi(String first_name, String last_name, String company_name, String address1, String address2,
                                String city, String post_code) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
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
                                OpenDialog(getActivity(), getString(R.string.validation_title), response.getAddressId(),
                                        "Save Delivery Address Successful", getString(R.string.ok));

                            } else {
                                Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
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

    private void OpenDialog(FragmentActivity activity, String string, final Integer addressId, String save_delivery_address_successful, String ok) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage(Html.fromHtml("Save Delivery Address Successful"));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(getActivity(), PaymentMethod_Activity.class);
                intent.putExtra("TotalPrice",TotalPrice);
                intent.putExtra("CountryId",country_id);
                intent.putExtra("ZoneId",zone_id);
                intent.putExtra("AddressId",addressId.toString());
                startActivity(intent);

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @SuppressLint("CheckResult")
    private void getCountryWiseState(String country_id) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
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
                                ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stateName);
                                binding.spinState.setAdapter(adp);

                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                utilities.dialogOK(getActivity(), getString(R.string.validation_title),
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
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
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
                                ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, CountryName);
                                binding.spinCountry.setAdapter(adp);

                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                utilities.dialogOK(getActivity(), getString(R.string.validation_title),
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
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
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
                                ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, AddressName);
                                binding.spinExistAddress.setAdapter(adp);

                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                utilities.dialogOK(getActivity(), getString(R.string.validation_title),
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
}
