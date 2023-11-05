package com.sujeevuthayakumar.sofe4650_assignment2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

// Used to populate the list with data from the adapter
public class CustomBaseAdapter extends BaseAdapter implements Filterable {

    Context context;
    List<Location> locationList;
    List<Location> originalLocationList;
    LayoutInflater inflater;
    DataBaseHelper dataBaseHelper;

    public CustomBaseAdapter(Context context, List<Location> locationList) {
        this.context = context;
        this.locationList = locationList;
        inflater = LayoutInflater.from(context);
        this.dataBaseHelper = new DataBaseHelper(context);
    }

    // Count the number of items in the location array
    @Override
    public int getCount() {
        return locationList.size();
    }

    // Get a specific item based on an index
    @Override
    public Object getItem(int i) {
        return locationList.get(i);
    }

    // Get an item id by an index
    @Override
    public long getItemId(int i) {
        return locationList.get(i).getId();
    }

    // Set the data on a list component based on index
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_custom_list_view, null);
        TextView address = (TextView) view.findViewById(R.id.list_address);
        address.setText(locationList.get(i).getAddress());
        TextView latitude = (TextView) view.findViewById(R.id.list_latitude);
        latitude.setText("Latitude: " + locationList.get(i).getLatitude());
        TextView longitude = (TextView) view.findViewById(R.id.list_longitude);
        longitude.setText("Longitude: " + locationList.get(i).getLongitude());
        return view;
    }


    // Used to filter the adapter base don a string sequence
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                locationList = (List<Location>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            // The logic used to filter the adapter data
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                // Holds the results of a filtering operation in values
                FilterResults results = new FilterResults();
                List<Location> FilteredArrList = new ArrayList<Location>();

                if (originalLocationList == null) {
                    // Saves the original data in originalLocationList
                    originalLocationList = new ArrayList<Location>(locationList);
                }

                if (constraint == null || constraint.length() == 0) {

                    // Set the Original result to return
                    results.count = originalLocationList.size();
                    results.values = originalLocationList;
                }
                else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < originalLocationList.size(); i++) {
                        Location data = originalLocationList.get(i);
                        if (data.getAddress().toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(data);
                        }
                    }
                    // Set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
