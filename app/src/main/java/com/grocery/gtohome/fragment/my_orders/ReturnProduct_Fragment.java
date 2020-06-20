package com.grocery.gtohome.fragment.my_orders;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.adapter.MyOrder_Adapter;
import com.grocery.gtohome.adapter.ReturnReasonAdapter;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentMyAccountBinding;
import com.grocery.gtohome.databinding.FragmentReturnProductBinding;
import com.grocery.gtohome.fragment.my_basket.ChekoutActivity;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.model.order_history.OrderHistory;
import com.grocery.gtohome.model.return_reason_model.ReturnReasonModel;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Connectivity;
import com.grocery.gtohome.utils.Utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

import static com.grocery.gtohome.api_client.Base_Url.BaseUrl;

/**
 * Created by Raghvendra Sahu on 09-Apr-20.
 */
public class ReturnProduct_Fragment extends Fragment {
    FragmentReturnProductBinding binding;
    private Utilities utilities;
    private SessionManager sessionManager;
     String Customer_Id;
    String radio_open="0";
    ReturnReasonAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_return_product, container, false);
        View root = binding.getRoot();
        utilities = Utilities.getInstance(getActivity());
        sessionManager = new SessionManager(getActivity());
        Customer_Id = sessionManager.getUser().getCustomerId();

        //set basic data from session
        binding.etFirstname.setText(sessionManager.getUser().getFirstname());
        binding.etLastname.setText(sessionManager.getUser().getLastname());
        binding.etEmail.setText(sessionManager.getUser().getEmail());
        binding.etTelefone.setText(sessionManager.getUser().getTelephone());

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


        if (Connectivity.isConnected(getActivity())) {
            getReturnReason();
        } else {
            //Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
            utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
        }


        //*********************radio group product open or not*********************
        binding.radioGroupOpen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_yes:
                      radio_open="1";
                        break;
                    case R.id.radio_no:
                        radio_open="0";
                        break;

                }
            }
        });

        //*************select calendar order date
        binding.etOrderdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Select_date();
            }
        });

        //********************click continue return
        binding.tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname= binding.etFirstname.getText().toString();
                String lastname= binding.etLastname.getText().toString();
                String email= binding.etEmail.getText().toString();
                String telephone= binding.etTelefone.getText().toString();
                String orderid= binding.etOrderid.getText().toString();
                String orderdate= binding.etOrderdate.getText().toString();
                String product_name= binding.etProductName.getText().toString();
                String product_code= binding.etProductCode.getText().toString();
                String qty= binding.etQty.getText().toString();
                String other_faulty= binding.etFaultyDetails.getText().toString();

                if (!firstname.isEmpty() && !lastname.isEmpty() && !email.isEmpty() && !telephone.isEmpty() && !orderid.isEmpty() &&
                    !product_name.isEmpty() && !product_code.isEmpty()){
                    if (adapter!=null && adapter.ReturnReasonId()!=null){
                        String return_id=adapter.ReturnReasonId();
                        if (binding.checkTerms.isChecked()){
                            CalReturnApi(firstname,lastname,email,telephone,orderid,orderdate,product_name,product_code,qty,other_faulty,return_id);
                        }else {
                            utilities.dialogOK(getActivity(), getString(R.string.validation_title), "Please accept  Terms & Conditions", getString(R.string.ok), false);
                        }

                    }else {
                        utilities.dialogOK(getActivity(), getString(R.string.validation_title), "Please select return reason", getString(R.string.ok), false);
                    }
                }else {
                    utilities.dialogOK(getActivity(), getString(R.string.validation_title), "Please enter all fields", getString(R.string.ok), false);

                }
            }
        });

        return root;
    }

        private void Select_date() {
            Calendar mcurrentDate = Calendar.getInstance();
            final int[] mYear = {mcurrentDate.get(Calendar.YEAR)};
            final int[] mMonth = {mcurrentDate.get(Calendar.MONTH)};
            final int[] mDay = {mcurrentDate.get(Calendar.DAY_OF_MONTH)};
            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    Calendar myCalendar = Calendar.getInstance();
                    myCalendar.set(Calendar.YEAR, selectedyear);
                    myCalendar.set(Calendar.MONTH, selectedmonth);
                    myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                    String myFormat = "dd-MM-yyyy"; //Change as you need
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                    binding.etOrderdate.setText(sdf.format(myCalendar.getTime()));
                   // Delivery_Date = sdf.format(myCalendar.getTime());

                    mDay[0] = selectedday;
                    mMonth[0] = selectedmonth;
                    mYear[0] = selectedyear;
                }
            }, mYear[0], mMonth[0], mDay[0]);
            //mDatePicker.setTitle("Select date");
            mDatePicker.show();
        }


    @SuppressLint("CheckResult")
    private void CalReturnApi(String firstname, String lastname, String email, String telephone, String orderid, String orderdate,
                              String product_name, String product_code, String qty, String other_faulty, String return_id) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("customer_id", Customer_Id);
        map.put("order_id", orderid);
        map.put("date_ordered", orderdate);
        map.put("firstname", firstname);
        map.put("lastname", lastname);
        map.put("email", email);
        map.put("telephone", telephone);
        map.put("product", product_name);
        map.put("model", product_code);
        map.put("opened", radio_open);
        map.put("return_reason_id", return_id);
        map.put("agree", "1");
        map.put("quantity", qty);
        map.put("comment", other_faulty);

        apiInterface.ReturnRequestApi(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SimpleResultModel>() {
                    @Override
                    public void onNext(SimpleResultModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("return_add", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                utilities.dialogOKOnBack(getActivity(), getString(R.string.validation_title),
                                        response.getMsg(), getString(R.string.ok), true);

                            } else {

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

    @SuppressLint("CheckResult")
    private void getReturnReason() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        apiInterface.ReturnReasonApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ReturnReasonModel>() {
                    @Override
                    public void onNext(ReturnReasonModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_capro", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                 adapter = new ReturnReasonAdapter(getActivity(), response.getReturnReasons());
                                LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(getActivity());
                                binding.listReason .setLayoutManager(recyclerLayoutManager);
                                binding.listReason.setAdapter(adapter);
                            } else {

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
