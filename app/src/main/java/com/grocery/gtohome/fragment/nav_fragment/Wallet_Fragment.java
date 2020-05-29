package com.grocery.gtohome.fragment.nav_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.databinding.FragmentBlogBinding;
import com.grocery.gtohome.databinding.FragmentWalletBinding;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Utilities;

public class Wallet_Fragment extends Fragment {
    FragmentWalletBinding binding;
    private Utilities utilities;
    SessionManager session;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallet, container, false);
        View root = binding.getRoot();
        utilities = Utilities.getInstance(getActivity());
        session = new SessionManager(getActivity());

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

        return root;

    }
}
