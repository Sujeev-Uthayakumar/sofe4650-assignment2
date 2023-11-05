package com.sujeevuthayakumar.sofe4650_assignment2;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.sujeevuthayakumar.sofe4650_assignment2.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private SharedLocationViewModel viewModel;
    private Location location;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(getActivity()).get(SharedLocationViewModel.class);
        viewModel.getData().observe(getViewLifecycleOwner(), data -> {
            if (data != null) {
                this.location = data;
                binding.editTitle.setText(data.getLatitude());
                binding.editSubtitle.setText(data.getLongitude());
            }
        });
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!areInputsValid(view)) {
                    String title = binding.editTitle.getText().toString();
                    String subTitle = binding.editSubtitle.getText().toString();
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
                    if (location != null) {
                        Location updateNoteModel;
                        updateNoteModel = new Location(location.getId(), title, subTitle);
                        dataBaseHelper.updateOne(updateNoteModel);
                        Toast.makeText(getContext(), "Location Successfully Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Location location;
                        try {
                            location = new Location(title, subTitle);
                        } catch (Exception e) {
                            location = new Location(-1, "error", "error", "error");
                        }

                        boolean success = dataBaseHelper.addOne(location);
                        System.out.printf(location.toString());
                        Toast.makeText(getContext(), "Location Successfully Added", Toast.LENGTH_SHORT).show();
                    }

                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);

                }
                styleErrorInputs(view);
            }
        });

        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
                if (location != null) {
                    dataBaseHelper.deleteOne(location.getId());
                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                    Toast.makeText(getContext(), "Location Successfully Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Note Cannot be Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean areInputsValid(View view) {
        String titleText = binding.editTitle.getText().toString();
        String subTitleText = binding.editSubtitle.getText().toString();
        boolean validLatitude = Double.parseDouble(titleText) > 90 || Double.parseDouble(titleText) < -90;
        boolean validLongitude = Double.parseDouble(subTitleText) > 180 || Double.parseDouble(subTitleText) < -180;
        return titleText.isEmpty() || subTitleText.isEmpty() || validLatitude || validLongitude;
    }

    private void styleErrorInputs(View view) {
        // Get the values of inputs
        String titleText = binding.editTitle.getText().toString();
        String subTitleText = binding.editSubtitle.getText().toString();

        // Color for input error
        int color = Color.rgb(255,0,0);
        int color2 = Color.parseColor("#FF11AA");

        // The states for an input when clicked, and hovered
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_focused},
                new int[] { android.R.attr.state_hovered},
                new int[] { android.R.attr.state_enabled},
                new int[] { }
        };

        // Set all possible colors
        int[] colors = new int[] {
                color,
                color,
                color,
                color2
        };

        // The list of colors used for the state
        ColorStateList myColorList = new ColorStateList(states, colors);

        // Check for principal amount input
        if (titleText.isEmpty()) {
            binding.editTitleLayout.setError("Required");
        } else if (subTitleText.isEmpty()) {
            binding.editSubtitleLayout.setError("Required");
        } else {
            binding.editTitleLayout.setError(null);
        }

        if (Double.parseDouble(titleText) > 90 || Double.parseDouble(titleText) < -90) {
            binding.editTitleLayout.setError("The value needs to be between -90 degrees and 90 degrees");
        } else {
            binding.editTitleLayout.setError(null);
        }

        if (Double.parseDouble(subTitleText) > 180 || Double.parseDouble(subTitleText) < -180) {
            binding.editSubtitleLayout.setError("The value needs to be between -180 degrees and 180 degrees");
        } else {
            binding.editSubtitleLayout.setError(null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}