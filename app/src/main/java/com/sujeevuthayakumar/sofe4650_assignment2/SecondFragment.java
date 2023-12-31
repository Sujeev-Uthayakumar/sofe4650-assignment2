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

// The page that focuses on being able to add and edit coordinates
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

        // Used to transfer data between fragments
        viewModel = new ViewModelProvider(getActivity()).get(SharedLocationViewModel.class);
        viewModel.getData().observe(getViewLifecycleOwner(), data -> {
            // If the data is not null, set the properties on the editText components
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

        // The onClickListener for when the submit button is pressed
        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If the inputs are valid then begin making sure which database method to use
                if (!areInputsValid(view)) {
                    // Get the input values for the EditText fields
                    String latitude = binding.editLatitude.getText().toString();
                    String longitude = binding.editLongitude.getText().toString();
                    // Setup the Database to make operations
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
                    // If the location model already is set, then it is an update operation
                    if (location != null) {
                        // Update the database with the updated data
                        Location updatedLocationModel;
                        updatedLocationModel = new Location(location.getId(), latitude, longitude);
                        dataBaseHelper.updateOne(updatedLocationModel);
                        Toast.makeText(getContext(), "Location Successfully Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        // Add to the database with the new location model
                        Location location;
                        try {
                            location = new Location(latitude, longitude);
                        } catch (Exception e) {
                            location = new Location(-1, "error", "error", "error");
                        }
                        // Insert with in the database with the new data
                        boolean success = dataBaseHelper.addOne(location);
                        System.out.printf(location.toString());
                        Toast.makeText(getContext(), "Location Successfully Added", Toast.LENGTH_SHORT).show();
                    }
                    // After completing the operation, return back to the First Fragment
                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);

                }
                // Style the inputs with the necessary errors
                styleErrorInputs(view);
            }
        });

        // The onClickListener for the delete button when pressed
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Setup the Database to make operations
                DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
                // If the location model already is set, then it is an update operation
                if (location != null) {
                    // Delete operation for only when there is an already existent location model
                    dataBaseHelper.deleteOne(location.getId());
                    // After completing the operation, return back to the First Fragment
                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                    Toast.makeText(getContext(), "Location Successfully Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Location Cannot be Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Check whether the input fields are valid
    private boolean areInputsValid(View view) {
        String latitude = binding.editLatitude.getText().toString();
        String longitude = binding.editLongitude.getText().toString();
        boolean validLongitude = true;
        boolean validLatitude = true;
        // If the latitude is within bounds
        if (latitude != null && !latitude.isEmpty()) {
            validLatitude = Double.parseDouble(latitude) > 90 || Double.parseDouble(latitude) < -90;
        }
        // If the longitude is within bounds
        if (longitude != null && !longitude.isEmpty()) {
            validLongitude = Double.parseDouble(longitude) > 180 || Double.parseDouble(longitude) < -180;
        }
        // Boolean expression to determine if fields are valid
        return latitude.isEmpty() || longitude.isEmpty() || validLatitude || validLongitude;
    }

    // Style the input fields with errors if inputs are invalid
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

        // Check for inputs
        if (latitude.isEmpty()) {
            binding.editLatitudeLayout.setError("Required");
        } else if (longitude.isEmpty()) {
            binding.editLongitudeLayout.setError("Required");
        } else {
            binding.editLatitudeLayout.setError(null);
        }

        // If the latitude is within bounds
        if (latitude != null && !latitude.isEmpty()) {
            if (Double.parseDouble(latitude) > 90 || Double.parseDouble(latitude) < -90) {
                binding.editLatitudeLayout.setError("The value needs to be between -90 degrees and 90 degrees");
            } else {
                binding.editLatitudeLayout.setError(null);
            }
        }

        // If the longitude is within bounds
        if (longitude != null && !longitude.isEmpty()) {
            if (Double.parseDouble(longitude) > 180 || Double.parseDouble(longitude) < -180) {
                binding.editLongitudeLayout.setError("The value needs to be between -180 degrees and 180 degrees");
            } else {
                binding.editLongitudeLayout.setError(null);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}