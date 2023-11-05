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

    @Override
    public int getCount() {
        return locationList.size();
    }

    @Override
    public Object getItem(int i) {
        return locationList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return locationList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_custom_list_view, null);
        TextView titleView = (TextView) view.findViewById(R.id.list_title);
        titleView.setText(locationList.get(i).getAddress());
        TextView subtitleView = (TextView) view.findViewById(R.id.list_subtitle);
        subtitleView.setText(locationList.get(i).getLongitude());
        TextView noteView = (TextView) view.findViewById(R.id.list_note);
        noteView.setText(locationList.get(i).getLatitude());
        return view;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                locationList = (List<Location>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<Location> FilteredArrList = new ArrayList<Location>();

                if (originalLocationList == null) {
                    originalLocationList = new ArrayList<Location>(locationList); // saves the original data in mOriginalValues
                }

                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
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
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
