package com.grocery.gtohome.fragment.nav_fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.adapter.CategoryList_Adapter;
import com.grocery.gtohome.adapter.CreditList_Adapter;
import com.grocery.gtohome.adapter.DebitList_Adapter;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.Base_Url;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentBlogBinding;
import com.grocery.gtohome.databinding.FragmentWalletBinding;
import com.grocery.gtohome.model.category_model.CategoryModel;
import com.grocery.gtohome.model.wallet_model.WalletModelList;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Connectivity;
import com.grocery.gtohome.utils.Utilities;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

public class Wallet_Fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    FragmentWalletBinding binding;
    private Utilities utilities;
    SessionManager session;
    private String CustomerId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallet, container, false);
        View root = binding.getRoot();
        utilities = Utilities.getInstance(getActivity());
        session = new SessionManager(getActivity());
        CustomerId = session.getUser().getCustomerId();
        // binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
        // binding.swipeToRefresh.setOnRefreshListener(this);

        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.wallet));

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

        if (Connectivity.isConnected(getActivity())) {
            getWalletHistory();
            binding.tvCredit.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
        }

        binding.tvAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMoneyFragment fragment2 = new AddMoneyFragment();
                Bundle bundle = new Bundle();
                // bundle.putSerializable("MyPhotoModelResponse", dataModelList.get(position));
                // bundle.putString("SubCategory_Id","");
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                fragment2.setArguments(bundle);
            }
        });

        binding.tvCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvCredit.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.tvDebit.setTextColor(getResources().getColor(R.color.black));
                binding.recyclerCredit.setVisibility(View.VISIBLE);
                binding.recyclerDebit.setVisibility(View.GONE);
            }
        });

        binding.tvDebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvDebit.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.tvCredit.setTextColor(getResources().getColor(R.color.black));
                binding.recyclerCredit.setVisibility(View.GONE);
                binding.recyclerDebit.setVisibility(View.VISIBLE);
            }
        });

        return root;

    }

    @SuppressLint("CheckResult")
    private void getWalletHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.GetWalletApi(CustomerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<WalletModelList>() {
                    @Override
                    public void onNext(WalletModelList response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_my_test", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {

                                binding.tvWalletAmount.setText(response.getBalance());
                                CreditList_Adapter friendsAdapter = new CreditList_Adapter(response.getCredits(), getActivity());
                                binding.setWalletCreditAdapter(friendsAdapter);//set databinding adapter
                                friendsAdapter.notifyDataSetChanged();

                                DebitList_Adapter debitAdapter = new DebitList_Adapter(response.getDebits(), getActivity());
                                binding.setWalletDebitAdapter(debitAdapter);//set databinding adapter
                                debitAdapter.notifyDataSetChanged();

                                // binding.swipeToRefresh.setVisibility(View.VISIBLE);
                            } else {
                                //   binding.swipeToRefresh.setVisibility(View.GONE);
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

    @Override
    public void onRefresh() {
        // binding.swipeToRefresh.setRefreshing(false);

        if (Connectivity.isConnected(getActivity())) {
            getWalletHistory();
        } else {
            utilities.dialogOK(getActivity(), getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
        }
    }
}
