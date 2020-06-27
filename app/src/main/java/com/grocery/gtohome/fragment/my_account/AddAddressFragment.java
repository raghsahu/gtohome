package com.grocery.gtohome.fragment.my_account;

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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.Base_Url;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentAddAddressBinding;
import com.grocery.gtohome.model.state_model.StateModel;
import com.grocery.gtohome.model.state_model.StateZoneData;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.model.address_model.AddressData;
import com.grocery.gtohome.model.country_model.CountryData;
import com.grocery.gtohome.model.country_model.CountryModel;
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
public class AddAddressFragment extends Fragment {
    FragmentAddAddressBinding binding;
    String address_id, country_id, zone_id;
    List<CountryData> CountryModelList = new ArrayList<>();
    ArrayList<String> CountryName = new ArrayList<>();
    List<StateZoneData> stateModelList = new ArrayList<>();
    ArrayList<String> stateName = new ArrayList<>();
    private Utilities utilities;
    Context context;
    private SessionManager sessionManager;
    private String Customer_Id;
    private String default_address;
    AddressData addressData;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_address, container, false);
        View root = binding.getRoot();
        context = getActivity();
        utilities = Utilities.getInstance(context);
        sessionManager = new SessionManager(context);
        Customer_Id = sessionManager.getUser().getCustomerId();
        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.my_account));
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
        //*******************************************
        if (getArguments() != null) {
            addressData = (AddressData) getArguments().getSerializable("MyAddressEdit");
            address_id = getArguments().getString("addressId");
            binding.setModel(addressData);
        }
        //*************************************************
        //get existing address
        if (Connectivity.isConnected(context)) {
            getCountryName();
        } else {
            //Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
        }

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

        binding.tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save address api
                String first_name = binding.etFirstname.getText().toString();
                String last_name = binding.etLastname.getText().toString();
                String company_name = binding.etCompany.getText().toString();
                String address1 = binding.etAddressOne.getText().toString();
                String address2 = binding.etAddressTwo.getText().toString();
                String city = binding.etCity.getText().toString();
                String post_code = binding.etPostcode.getText().toString();
                if (binding.radioNo.isChecked()) {
                    default_address = "0";
                } else {
                    default_address = "1";
                }

                if (!first_name.isEmpty() && !last_name.isEmpty() && !address1.isEmpty()
                        && !city.isEmpty() && !post_code.isEmpty()) {

                    if (country_id != null && !country_id.isEmpty() && zone_id != null && !zone_id.isEmpty()) {
                        if (Connectivity.isConnected(context)) {
                            SaveAddressApi(first_name, last_name, company_name, address1, address2, city, post_code, default_address);
                        } else {
                            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
                        }

                    } else {
                        utilities.dialogOK(context, getString(R.string.validation_title), "Please select country & state", getString(R.string.ok), false);
                    }


                } else {
                    utilities.dialogOK(context, getString(R.string.validation_title), "Please enter all fields!", getString(R.string.ok), false);
                }

            }
        });


        return root;

    }

    @SuppressLint("CheckResult")
    private void SaveAddressApi(String first_name, String last_name, String company_name, String address1, String address2,
                                String city, String post_code, String default_address) {

        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.SaveNewAddress(address_id,Customer_Id, first_name, last_name, company_name, address1, address2, city,
                post_code, country_id, zone_id, default_address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SimpleResultModel>() {
                    @Override
                    public void onNext(SimpleResultModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_my_test", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {

                                utilities.dialogOKOnBack(context, getString(R.string.validation_title), response.getMsg(), getString(R.string.ok), true);

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
}
