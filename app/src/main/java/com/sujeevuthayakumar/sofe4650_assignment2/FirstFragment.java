package com.sujeevuthayakumar.sofe4650_assignment2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.sujeevuthayakumar.sofe4650_assignment2.databinding.FragmentFirstBinding;

import java.util.List;

// The main page to see all the addresses
public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private SharedLocationViewModel viewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Setup the Database to make operations
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
        // Get all the locations in the database
        List<Location> locationList = dataBaseHelper.getEveryone();

        // Set the adapter data on the list item
        ArrayAdapter<Location> locationArrayAdapter = new ArrayAdapter<Location>(getContext(), android.R.layout.simple_list_item_1, locationList);
        // Setup the communication between the First Fragment and the Second Fragment
        viewModel = new ViewModelProvider(getActivity()).get(SharedLocationViewModel.class);
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        // Setup the adapter to be able to set the list data
        CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getContext(), locationList);
        binding.listview.setAdapter(customBaseAdapter);
        // The textListener to be able work on text changes for searches
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Filter the adapter based on a text submit
            @Override
            public boolean onQueryTextSubmit(String query) {
                customBaseAdapter.getFilter().filter(query);
                return false;
            }

            // Filter the adapter based on a text change
            @Override
            public boolean onQueryTextChange(String newText) {
                customBaseAdapter.getFilter().filter(newText);
                return false;
            }
        });

        // The onClickListener when an item in the list is clicked
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Navigate to the Second Fragment when clicked
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);

                // Send the selected item data to the Second Fragment
                Location selectedLocation = locationArrayAdapter.getItem(position);
                viewModel.setData(selectedLocation);
            }
        });
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // The onClickListener for the floating action button to add locations
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the Second Fragment when clicked
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
                // Send data to the Second Fragment
                viewModel.setData(null);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}