package com.grocery.gtohome.fragment.my_account;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.activity.Splash_Activity;
import com.grocery.gtohome.activity.login_signup.Login_Activity;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.Base_Url;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentHomeBinding;
import com.grocery.gtohome.databinding.FragmentMyAccountBinding;
import com.grocery.gtohome.fragment.Information_Fragment;
import com.grocery.gtohome.fragment.my_orders.OrderHistory_Fragment;
import com.grocery.gtohome.fragment.my_orders.RecuringPayment_Fragment;
import com.grocery.gtohome.fragment.my_orders.ReturnProduct_Fragment;
import com.grocery.gtohome.fragment.my_orders.ReturnRequest_Fragment;
import com.grocery.gtohome.fragment.my_orders.RewardPoint_Fragment;
import com.grocery.gtohome.fragment.my_orders.Transaction_Fragment;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.model.login_model.LoginModel;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Connectivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

/**
 * Created by Raghvendra Sahu on 08-Apr-20.
 */
public class My_Account_Fragment extends Fragment  implements GoogleApiClient.OnConnectionFailedListener{
    FragmentMyAccountBinding binding;
    SessionManager sessionManager;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_account, container, false);
        View root = binding.getRoot();
        sessionManager =new SessionManager(getActivity());
        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.my_account));
            ((MainActivity) getActivity()).CheckBottom(3);
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

        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(),  new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        // connection failed, should be handled
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        binding.llEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditInfoFragment fragment2 = new EditInfoFragment();
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


        binding.llChangePw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Change_Pw_Fragment fragment2 = new Change_Pw_Fragment();
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

        binding.llModifyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Change_Address_Fragment fragment2 = new Change_Address_Fragment();
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

        binding.llModifyWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WishList_Fragment fragment2 = new WishList_Fragment();
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

        binding.llOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderHistory_Fragment fragment2 = new OrderHistory_Fragment();
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


        binding.llRewardPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RewardPoint_Fragment fragment2 = new RewardPoint_Fragment();
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

        binding.llViewReturnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReturnRequest_Fragment fragment2 = new ReturnRequest_Fragment();
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

        binding.llTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transaction_Fragment fragment2 = new Transaction_Fragment();
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

        binding.llRecurringPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecuringPayment_Fragment fragment2 = new RecuringPayment_Fragment();
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

        binding.llReturnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReturnProduct_Fragment fragment2 = new ReturnProduct_Fragment();
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

        binding.llAboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Information_Fragment.class);
                intent.putExtra("Info","About Us");
                startActivity(intent);

            }
        });


        binding.llTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Information_Fragment.class);
                intent.putExtra("Info","Terms");
                startActivity(intent);

            }
        });

        binding.llDeliveryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Information_Fragment.class);
                intent.putExtra("Info","DeliveryInfo");
                startActivity(intent);

            }
        });

        binding.llPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Information_Fragment.class);
                intent.putExtra("Info","Privacy");
                startActivity(intent);

            }
        });

        binding.llNewsletter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NewsLetter_Fragment fragment2 = new NewsLetter_Fragment();
                Bundle bundle = new Bundle();
                // bundle.putSerializable("MyPhotoModelResponse", dataModelList.get(position));
              //  bundle.putString("Info","Privacy");
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                fragment2.setArguments(bundle);
            }
        });

        binding.llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout_User();

            }
        });

        return root;

    }

    private void Logout_User() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity()).setTitle(R.string.app_name)
                .setMessage("Are you sure, you want to logout this app");

        dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                
                if (Connectivity.isConnected(getActivity())){
                    LogoutUser();
                }else {
                    Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
                }

            }

        });
        final AlertDialog alert = dialog.create();
        alert.show();
    }


    @SuppressLint("CheckResult")
    private void LogoutUser() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.LogoutUser(sessionManager.getUser().getCustomerId())
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

                                if (googleApiClient != null && googleApiClient.isConnected()) {
                                    // signed in. Show the "sign out" button and explanation.
                                    google_logout();
                                    disconnectFromFacebook();
                                } else{
                                    Log.e("logout_app", "local login logout");
                                    // not signed in. Show the "sign in" button and explanation.
                                    disconnectFromFacebook();
                                    sessionManager.logout();

                                }

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

    private void disconnectFromFacebook() {

        Log.e("logout_app_fb", "fb login logout");
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/",
                null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

                sessionManager.logout();

            }
        }).executeAsync();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void google_logout() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()){
                            Log.e("logout_app_gmail", "google login logout");
                            sessionManager.logout();

                        }else{
                            Toast.makeText(getActivity(),"Session not close",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
    }
}
