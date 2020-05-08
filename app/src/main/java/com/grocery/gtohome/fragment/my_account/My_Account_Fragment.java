package com.grocery.gtohome.fragment.my_account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.activity.Splash_Activity;
import com.grocery.gtohome.databinding.FragmentHomeBinding;
import com.grocery.gtohome.databinding.FragmentMyAccountBinding;
import com.grocery.gtohome.fragment.Information_Fragment;
import com.grocery.gtohome.fragment.my_orders.OrderHistory_Fragment;
import com.grocery.gtohome.fragment.my_orders.RecuringPayment_Fragment;
import com.grocery.gtohome.fragment.my_orders.ReturnProduct_Fragment;
import com.grocery.gtohome.fragment.my_orders.ReturnRequest_Fragment;
import com.grocery.gtohome.fragment.my_orders.RewardPoint_Fragment;
import com.grocery.gtohome.fragment.my_orders.Transaction_Fragment;
import com.grocery.gtohome.session.SessionManager;

/**
 * Created by Raghvendra Sahu on 08-Apr-20.
 */
public class My_Account_Fragment extends Fragment {
    FragmentMyAccountBinding binding;
    SessionManager sessionManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_account, container, false);
        View root = binding.getRoot();
        sessionManager =new SessionManager(getActivity());
        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.my_account));
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
                Information_Fragment fragment2 = new Information_Fragment();
                Bundle bundle = new Bundle();
                // bundle.putSerializable("MyPhotoModelResponse", dataModelList.get(position));
                  bundle.putString("Info","About Us");
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                fragment2.setArguments(bundle);
            }
        });


        binding.llTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Information_Fragment fragment2 = new Information_Fragment();
                Bundle bundle = new Bundle();
                // bundle.putSerializable("MyPhotoModelResponse", dataModelList.get(position));
                bundle.putString("Info","Terms");
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                fragment2.setArguments(bundle);
            }
        });

        binding.llDeliveryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Information_Fragment fragment2 = new Information_Fragment();
                Bundle bundle = new Bundle();
                // bundle.putSerializable("MyPhotoModelResponse", dataModelList.get(position));
                bundle.putString("Info","DeliveryInfo");
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                fragment2.setArguments(bundle);
            }
        });

        binding.llPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Information_Fragment fragment2 = new Information_Fragment();
                Bundle bundle = new Bundle();
                // bundle.putSerializable("MyPhotoModelResponse", dataModelList.get(position));
                bundle.putString("Info","Privacy");
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

                sessionManager.logout();

            }

        });
        final AlertDialog alert = dialog.create();
        alert.show();
    }

}
