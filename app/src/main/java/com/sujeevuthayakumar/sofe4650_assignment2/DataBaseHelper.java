package com.sujeevuthayakumar.sofe4650_assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String COLUMN_LOCATION = "LOCATION";
    public static final String LOCATION_TABLE = COLUMN_LOCATION + "_TABLE";
    public static final String COLUMN_LOCATION_ADDRESS = COLUMN_LOCATION + "_ADDRESS";
    public static final String COLUMN_LOCATION_LATITUDE = COLUMN_LOCATION + "_LATITUDE";
    public static final String COLUMN_LOCATION_LONGITUDE = COLUMN_LOCATION + "_LONGITUDE";
    private Context context;

    public DataBaseHelper(@Nullable Context context) {
        super(context, "location.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + LOCATION_TABLE
                + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_LOCATION_ADDRESS + " TEXT, "
                + COLUMN_LOCATION_LATITUDE + " TEXT, "
                + COLUMN_LOCATION_LONGITUDE + " TEXT)";

        sqLiteDatabase.execSQL(createTableStatement);
        addMockData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        location.setAddress(getAddress(location.getLatitude(), location.getLongitude()));

        cv.put(COLUMN_LOCATION_ADDRESS, location.getAddress());
        cv.put(COLUMN_LOCATION_LATITUDE, location.getLatitude());
        cv.put(COLUMN_LOCATION_LONGITUDE, location.getLongitude());
        System.out.println(location.toString());

        long insert = db.insert(LOCATION_TABLE, null, cv);
        db.close();
        return insert != -1;
    }

    public boolean updateOne(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        location.setAddress(getAddress(location.getLatitude(), location.getLongitude()));

        cv.put(COLUMN_LOCATION_ADDRESS, location.getAddress());
        cv.put(COLUMN_LOCATION_LATITUDE, location.getLatitude());
        cv.put(COLUMN_LOCATION_LONGITUDE, location.getLongitude());

        int update = db.update(LOCATION_TABLE, cv, "id=?", new String[]{Integer.toString(location.getId())});
        return update != -1;
    }

    public boolean deleteOne(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int delete = db.delete(LOCATION_TABLE, "id=?", new String[]{Integer.toString(id)});
        return delete != -1;
    }

    public List<Location> getEveryone() {
        List<Location> returnList = new ArrayList<>();

        // get data from the database

        String queryString = "SELECT * FROM " + LOCATION_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int locationID = cursor.getInt(0);
                String locationAddress = cursor.getString(1);
                String locationLatitude = cursor.getString(2);
                String locationLongitude = cursor.getString(3);

                Location newLocation = new Location(locationID, locationAddress, locationLatitude, locationLongitude);
                returnList.add(newLocation);
            } while (cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();
        return returnList;
    }

    private List<Location> getMockDataFromCSV() {
        List<Location> locationList = new ArrayList<>();
        InputStream is = context.getResources().openRawResource(R.raw.locations);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        );
        String line = "";

        try{
            reader.readLine();
            int id = 1;

            while ((line = reader.readLine()) != null){
                String[] tokens = line.split(",");

                Location newLocation = new Location();

                String latitude = tokens[0];
                String longitude = tokens[1];
                String address = getAddress(latitude, longitude);

                newLocation.setLatitude(String.valueOf(latitude));
                newLocation.setLongitude(String.valueOf(longitude));
                newLocation.setAddress(address);
                System.out.println(newLocation.toString());

                locationList.add(newLocation);
            }
        } catch(IOException e){
            e.printStackTrace();
        }

        return locationList;
    }

    private void addMockData(SQLiteDatabase db) {
        List<Location> mockLocations = getMockDataFromCSV();
        for (Location location : mockLocations) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_LOCATION_ADDRESS, location.getAddress());
            cv.put(COLUMN_LOCATION_LATITUDE, location.getLatitude());
            cv.put(COLUMN_LOCATION_LONGITUDE, location.getLongitude());
            db.insert(LOCATION_TABLE, null, cv);
        }
    }

    private String getAddress(String latitude, String longitude){
        String address = "";

        if(Geocoder.isPresent()){
            Geocoder geocoder = new Geocoder(this.context, Locale.getDefault());

            try {
                List<Address> addresses;
                addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
                if(!addresses.isEmpty()){
                    address = addresses.get(0).getAddressLine(0);
                } else{
                    address = "No Address Found";
                }
            } catch(IOException e){
                //e.printStackTrace();
                address = "No Address Found";
            }
        }

        return address;
    }
}
