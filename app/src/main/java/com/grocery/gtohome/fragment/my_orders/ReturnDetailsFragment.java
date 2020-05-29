package com.grocery.gtohome.fragment.my_orders;

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
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.adapter.HistoryItemList_Adapter;
import com.grocery.gtohome.adapter.TotalAmount_Adapter;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentOrdersDetailsBinding;
import com.grocery.gtohome.databinding.FragmentReturnDetailsBinding;
import com.grocery.gtohome.model.order_history.OrderHistoryDetails;
import com.grocery.gtohome.model.return_model.ReturnDetailsModel;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Connectivity;
import com.grocery.gtohome.utils.Utilities;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

import static com.grocery.gtohome.api_client.Base_Url.BaseUrl;

public class ReturnDetailsFragment extends Fragment {
    FragmentReturnDetailsBinding binding;
    private Utilities utilities;
    private SessionManager sessionManager;
    private String Customer_Id;
    private String Return_Id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_return_details, container, false);
        View root = binding.getRoot();
        utilities = Utilities.getInstance(getActivity());
        sessionManager = new SessionManager(getActivity());
        Customer_Id = sessionManager.getUser().getCustomerId();
        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.return_details));
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

        if (getArguments()!=null){
            Return_Id=getArguments().getString("Return_Id");
        }

        if (Connectivity.isConnected(getActivity())) {
            getReturnDetails();
        } else {
            //Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
            utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
        }
        return root;

    }

    @SuppressLint("CheckResult")
    private void getReturnDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("return_id", Return_Id);
        map.put("customer_id", Customer_Id);

        apiInterface.ReturnItemDetails(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ReturnDetailsModel>() {
                    @Override
                    public void onNext(ReturnDetailsModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_category_pro", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                //set item list in adapter

                                //*************set order details data in text***
                                binding.tvOrderId.setText(response.getResponse().getOrderId());
                                binding.tvReturnId.setText(response.getResponse().getReturnId());
                                binding.tvDateAddDetails.setText(response.getResponse().getDateAdded());
                                binding.tvOrderDate.setText(response.getResponse().getDateOrdered());

                                binding.tvProdName.setText(response.getResponse().getProduct());
                                binding.tvModel.setText(response.getResponse().getModel());
                                binding.tvQty.setText(response.getResponse().getQuantity());

                                binding.tvReason.setText(response.getResponse().getReason());
                                binding.tvOpen.setText(response.getResponse().getOpened());
                                binding.tvAction.setText(response.getResponse().getAction());

                                binding.tvComments.setText(response.getResponse().getComment());

                                //*************set return details history in text***
                                if (response.getResponse().getHistories()!=null){
                                    binding.tvDateAdded.setText(response.getResponse().getHistories().get(0).getDateAdded());
                                    binding.tvStatus.setText(response.getResponse().getHistories().get(0).getStatus());
                                    binding.tvCommentsHis.setText(response.getResponse().getHistories().get(0).getComment());
                                }


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
