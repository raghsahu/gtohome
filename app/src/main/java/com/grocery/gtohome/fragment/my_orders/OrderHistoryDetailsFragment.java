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
import com.grocery.gtohome.adapter.MyOrder_Adapter;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentMyOrdersBinding;
import com.grocery.gtohome.databinding.FragmentOrdersDetailsBinding;
import com.grocery.gtohome.model.order_history.OrderHistory;
import com.grocery.gtohome.model.order_history.OrderHistoryDetails;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Connectivity;
import com.grocery.gtohome.utils.Utilities;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

import static com.grocery.gtohome.api_client.Base_Url.BaseUrl;

/**
 * Created by Raghvendra Sahu on 06-May-20.
 */
public class OrderHistoryDetailsFragment extends Fragment {
    FragmentOrdersDetailsBinding binding;
    private Utilities utilities;
    private SessionManager sessionManager;
    private String Customer_Id;
    private String Order_Id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders_details, container, false);
        View root = binding.getRoot();
        utilities = Utilities.getInstance(getActivity());
        sessionManager = new SessionManager(getActivity());
        Customer_Id = sessionManager.getUser().getCustomerId();
        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.order_details));
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
            Order_Id=getArguments().getString("Order_Id");
        }

        if (Connectivity.isConnected(getActivity())) {
            getOrderDetails();
        } else {
            //Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
            utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
        }
        return root;

    }

    @SuppressLint("CheckResult")
    private void getOrderDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("order_id", Order_Id);

        apiInterface.OrderHistoryItemDetails(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<OrderHistoryDetails>() {
                    @Override
                    public void onNext(OrderHistoryDetails response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_category_pro", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                //set item list in adapter
                                if (response.getOrder().getProducts()!=null){
                                    HistoryItemList_Adapter friendsAdapter = new HistoryItemList_Adapter(response.getOrder().getProducts(),getActivity());
                                    binding.setMyOrderAdapter(friendsAdapter);//set databinding adapter
                                    friendsAdapter.notifyDataSetChanged();
                                }

                                //*************set order details data in text***
                                binding.tvOrderId.setText(response.getOrder().getOrderId());
                                binding.tvDateAddDetails.setText(response.getOrder().getDateAdded());
                                binding.tvPaymentMethod.setText(response.getOrder().getPaymentMethod());
                                binding.tvShippingMethod.setText(response.getOrder().getShippingMethod());
                                binding.tvComments.setText(response.getOrder().getComment());
                                //*************set order details history in text***
                                if (response.getOrder().getHistories()!=null){
                                    binding.tvDateAdded.setText(response.getOrder().getHistories().get(0).getDateAdded());
                                    binding.tvStatus.setText(response.getOrder().getHistories().get(0).getStatus());
                                    binding.tvCommentsHis.setText(response.getOrder().getHistories().get(0).getComment());
                                }
                                //*************set order address in text***
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    binding.tvPaymentAddress.setText(Html.fromHtml(response.getOrder().getPaymentAddress(), Html.FROM_HTML_MODE_COMPACT));
                                    binding.tvShippingAddress.setText(Html.fromHtml(response.getOrder().getShippingAddress(), Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    binding.tvPaymentAddress.setText(Html.fromHtml(response.getOrder().getPaymentAddress()));
                                    binding.tvShippingAddress.setText(Html.fromHtml(response.getOrder().getShippingAddress()));
                                }

                                //*************set order item charges in text***
                                if (response.getOrder().getTotal()!=null){
                                    binding.tvSubtotal.setText(response.getOrder().getTotals().get(0).getText());
                                    binding.tvFlateShipping.setText(response.getOrder().getTotals().get(1).getText());
                                    binding.tvTotal.setText(response.getOrder().getTotals().get(2).getText());
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
