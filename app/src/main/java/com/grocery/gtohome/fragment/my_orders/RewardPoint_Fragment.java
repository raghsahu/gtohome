package com.grocery.gtohome.fragment.my_orders;

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
import com.grocery.gtohome.databinding.FragmentMyAccountBinding;
import com.grocery.gtohome.databinding.FragmentRewardPointBinding;

/**
 * Created by Raghvendra Sahu on 09-Apr-20.
 */
public class RewardPoint_Fragment extends Fragment {
    FragmentRewardPointBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reward_point, container, false);
        View root = binding.getRoot();
        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.reward_point));
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

        return root;

    }
}
