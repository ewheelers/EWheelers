package com.ewheelers.ewheelers;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ewheelers.ewheelers.Activities.UserRegistrationActivity;


public class SlideFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    @StringRes
    private static final int[] PAGE_TITLES =
            new int[] { R.string.page_text_1, R.string.page_text_2, R.string.page_text_3,R.string.page_text_3,R.string.page_text_3 };
    @StringRes
    private static final int[] PAGE_IMAGE =
            new int[] {
                    R.drawable.mb1, R.drawable.mb2, R.drawable.mb3,R.drawable.mb4,R.drawable.mb5
            };
    private SliderViewModel sliderViewModel;

    public static SlideFragment newInstance(int index) {
        SlideFragment fragment = new SlideFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sliderViewModel = ViewModelProviders.of(this).get(SliderViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        sliderViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slide, container, false);
        //final TextView textView = root.findViewById(R.id.section_label);
        final ImageView imageView = root.findViewById(R.id.imageView);
        sliderViewModel.getText().observe(this, new Observer<Integer>() {
            @Override public void onChanged(Integer index) {
                //textView.setText(PAGE_TITLES[index]);
                imageView.setImageResource(PAGE_IMAGE[index]);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserRegistrationActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}
