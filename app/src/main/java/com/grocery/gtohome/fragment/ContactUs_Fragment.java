package com.grocery.gtohome.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.activity.login_signup.Login_Activity;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.Base_Url;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.FragmentContactUsBinding;
import com.grocery.gtohome.databinding.FragmentHomeBinding;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.model.login_model.LoginModel;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Utilities;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

/**
 * Created by Raghvendra Sahu on 08-Apr-20.
 */
public class ContactUs_Fragment extends Fragment implements OnMapReadyCallback ,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    FragmentContactUsBinding binding;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Utilities utilities;
    SessionManager session;
    private Context context;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_us, container, false);
        View root = binding.getRoot();
        utilities = Utilities.getInstance(getActivity());
        context = getActivity();
        session = new SessionManager(getActivity());
        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.contact_us));
            ((MainActivity) getActivity()).CheckBottom(4);
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

        binding.tvViewGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 11.245909, 75.780386);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getActivity().startActivity(intent);
            }
        });

        binding.tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name= binding.etName.getText().toString();
                String email= binding.etEmail.getText().toString();
                String et_enquiry= binding.etEnquiry.getText().toString();

                if (!name.isEmpty() && !email.isEmpty() && !et_enquiry.isEmpty()){
                    SendEnquiry(name,email,et_enquiry);
                }else {
                    Toast.makeText(getActivity(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;


    }

    @SuppressLint("CheckResult")
    private void SendEnquiry(String name, String email, String et_enquiry) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.SendEnquiry(name,email,et_enquiry)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SimpleResultModel>() {
                    @Override
                    public void onNext(SimpleResultModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_my_test", "" + response.getStatus());
                           // Toast.makeText(Login_Activity.this, "" + response.getMsg(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                utilities.dialogOK(context, getString(R.string.validation_title), response.getMsg(), getString(R.string.ok), false);
                                binding.etEnquiry.getText().clear();
                                binding.etEmail.getText().clear();
                                binding.etName.getText().clear();

                            }else {

                                if (response.getError().getEnquiry()!=null){
                                    utilities.dialogOK(context, getString(R.string.validation_title), response.getError().getEnquiry(), getString(R.string.ok), false);
                                }
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
                        // Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        if (e instanceof HttpException) {
                            int code = ((HttpException) e).code();
                            switch (code) {
                                case 403:
                                    break;
                                case 404:
                                    //  Toast.makeText(Login_Activity.this, R.string.invalid_login, Toast.LENGTH_SHORT).show();
                                    break;
                                case 409:
                                    break;
                                default:
                                    // Toast.makeText(Login_Activity.this, R.string.cb_snooze_network_error, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            if (TextUtils.isEmpty(e.getMessage())) {
                                // Toast.makeText(Login_Activity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        e.printStackTrace();

                    }


                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                buildGoogleApiClient();
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.setMyLocationEnabled(true);

            }
        } else {
            buildGoogleApiClient();
            //       mMap.setMyLocationEnabled(true);
        }


        LatLng marker = new LatLng(11.245909, 75.780386);
//        mMap.addMarker(new MarkerOptions().position(marker).title("GtoHome"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));

       Marker current_marker = mMap.addMarker(new MarkerOptions()
                .position(marker)
                .title("GtoHome")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .snippet(" ")
        );

        final CameraPosition cameraPosition = new CameraPosition.Builder().target(marker).zoom(13)
                .bearing(90)
                .tilt(30)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                                                    @Override
                                                    public boolean onMyLocationButtonClick() {
                                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                                        return true;
                                                    }
                                                }
        );
    }


    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
