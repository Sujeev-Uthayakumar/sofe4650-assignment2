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
                binding.editLatitude.setText(data.getLatitude());
                binding.editLongitude.setText(data.getLongitude());
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
                    String latitude = binding.editLatitude.getText().toString();
                    String longitude = binding.editLongitude.getText().toString();
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
                    if (location != null) {
                        Location updatedLocationModel;
                        updatedLocationModel = new Location(location.getId(), latitude, longitude);
                        dataBaseHelper.updateOne(updatedLocationModel);
                        Toast.makeText(getContext(), "Location Successfully Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Location location;
                        try {
                            location = new Location(latitude, longitude);
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
                    Toast.makeText(getContext(), "Location Cannot be Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean areInputsValid(View view) {
        String latitude = binding.editLatitude.getText().toString();
        String longitude = binding.editLongitude.getText().toString();
        boolean validLatitude = Double.parseDouble(latitude) > 90 || Double.parseDouble(latitude) < -90;
        boolean validLongitude = Double.parseDouble(longitude) > 180 || Double.parseDouble(longitude) < -180;
        return latitude.isEmpty() || longitude.isEmpty() || validLatitude || validLongitude;
    }

    private void styleErrorInputs(View view) {
        // Get the values of inputs
        String latitude = binding.editLatitude.getText().toString();
        String longitude = binding.editLongitude.getText().toString();

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
        if (latitude.isEmpty()) {
            binding.editLatitudeLayout.setError("Required");
        } else if (longitude.isEmpty()) {
            binding.editLongitudeLayout.setError("Required");
        } else {
            binding.editLatitudeLayout.setError(null);
        }

        if (Double.parseDouble(latitude) > 90 || Double.parseDouble(latitude) < -90) {
            binding.editLatitudeLayout.setError("The value needs to be between -90 degrees and 90 degrees");
        } else {
            binding.editLatitudeLayout.setError(null);
        }

        if (Double.parseDouble(longitude) > 180 || Double.parseDouble(longitude) < -180) {
            binding.editLongitudeLayout.setError("The value needs to be between -180 degrees and 180 degrees");
        } else {
            binding.editLongitudeLayout.setError(null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}