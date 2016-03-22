package com.event.joe.datastorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Joe on 23/02/2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "eventDB";
    private static final String TABLE_NAME = "table_event";
    private static final String KEY_ID = "id";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String DATE = "date";
    private static final String TITLE = "title";
    private static final String LOCATION = "location";
    private static final String TICKET_URL = "ticketURL";
    private static final String IMAGE_URL = "imageURL";
    private static final String POSTCODE = "postcode";
    private static final String ID = "id";

    //  private static final String[] COLUMNS = {KEY_ID,KEY_PC1,KEY_PC2};

    private SQLiteDatabase db;
    private String idChosenEvent;
    private String longitude;
    private String latitude;
    private String date;
    private String title;
    private String location;
    private String imageURL;
    private String postcode;
    private String ticketURL;
    private JSONObject event;
    private String id;

    //Login Table
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final String FIRSTNAME = "firstName";
    private static final String LASTNAME = "lastName";
    private static final String TABLE_LOGIN = "table_login";

   private static final String CREATE_LOGIN_TABLE = "CREATE TABLE table_login ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "username TEXT," + "firstName TEXT, " + "lastName TEXT," + "password TEXT )";
   private static final String CREATE_EVENTS_TABLE = "CREATE TABLE table_event ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "latitude TEXT," + "longitude TEXT, " + "date TEXT," + "title TEXT," + "location TEXT," + "imageURL TEXT," + "postcode TEXT," + "ticketURL TEXT )";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXSISTS table_event");
        db.execSQL("DROP TABLE IF EXSISTS table_login");
        onCreate(db);
    }

    public boolean CheckIsDataAlreadyInDBorNot(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        String Query = "Select * from " + TABLE_LOGIN + " where " + USERNAME + " = '" + username + "'";

        try{
            cursor = db.rawQuery(Query, null);
            if(cursor.getCount() <= 0){
                cursor.close();

            }
        }catch (Exception e){
            return false;
        }

        cursor.close();
        return true;
    }

    public String addUser(String username, String password, String firstName, String lastName) {

                    this.username = username;
                    this.password = password;
                    this.firstName = firstName;
                    this.lastName = lastName;

                    db = this.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(USERNAME, username);
                    values.put(PASSWORD, password);
                    values.put(FIRSTNAME, firstName);
                    values.put(LASTNAME, lastName);
                    db.insert(TABLE_LOGIN, null, values);
                    db.close();
                    return "Account Added";
                }

    public String getPassword(String currentUsername){
        String currentPassword = null;

        int x = 1;
        List<String> List = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM " + TABLE_LOGIN + " WHERE username = '" + currentUsername + "'", null);

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                currentPassword = cursor.getString(cursor.getColumnIndex(PASSWORD));
                x = x + 1;
                cursor.moveToNext();
            }
        }

        return currentPassword;
    }

    public List<String> getAllUsernames() {
        int x = 1;
        List<String> List = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT username FROM " + TABLE_LOGIN, null);

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                String usernameTemp = cursor.getString(cursor.getColumnIndex(USERNAME));
            List.add(usernameTemp);
                x = x + 1;
                cursor.moveToNext();
            }
        }
        return List;
    }


    public void addInitialUser(){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME, "joews9");
        values.put(FIRSTNAME, "Joe");
        values.put(PASSWORD, "password123");
        values.put(LASTNAME, "Millership");
        db.insert(TABLE_LOGIN, null, values);
        db.close();
    }

    public void saveEvent(JSONObject event) {
        this.event = event;

        //set the variables from the JSONObject
        try {
            latitude = event.getString("latitude");
            longitude = event.getString("longitude");
            date = event.getString("start_time");
            location = event.getString("venue_name");
            title = event.getString("title");
            ticketURL = event.getString("url");
            imageURL = event.getJSONObject("image").getJSONObject("medium").getString("url");
            postcode = event.getString("postal_code");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LATITUDE, latitude);
        values.put(LONGITUDE, longitude);
        values.put(DATE, date);
        values.put(TITLE, title);
        values.put(LOCATION, location);
        values.put(IMAGE_URL, imageURL);
        values.put(POSTCODE, postcode);
        values.put(TICKET_URL, ticketURL);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void deleteEvent(String eventID) {
        String deleteQuery = "DELETE FROM " + TABLE_NAME + " WHERE id= " + eventID;
        db = this.getWritableDatabase();
        db.execSQL(deleteQuery);

    }

    public List<String> getAllEvents() {

        int x = 1;
        List<String> List = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT title, date FROM " + TABLE_NAME + " ORDER BY id", null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String title = cursor.getString(cursor.getColumnIndex(TITLE));

                String titleListAdd = title;

                List.add(titleListAdd);
                x = x + 1;
                cursor.moveToNext();
            }
        }
        return List;
    }

    public List<String> getAllEventDates() {

        int x = 1;
        List<String> List = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT title, date FROM " + TABLE_NAME + " ORDER BY id", null);

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                String date = cursor.getString(cursor.getColumnIndex(DATE));

                String dateListAdd = date;

                List.add(dateListAdd);
                x = x + 1;
                cursor.moveToNext();
            }
        }
        return List;
    }

    public List<String> getAllEventIDs() {
        int x = 1;
        List<String> list = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_NAME + " ORDER BY id", null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String date = cursor.getString(cursor.getColumnIndex(ID));

                String idListAdd = date;

                list.add(idListAdd);
                x = x + 1;
                cursor.moveToNext();
            }
        }
        return list;
    }

    public HashMap<String, String> getChosenEvent(String idChosenEvent) {
        HashMap<String, String> chosenEvent = new HashMap<String, String>();
        this.idChosenEvent = idChosenEvent;
        int x = 1;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + "title, date, location, latitude, longitude, imageURL, ticketURL, postcode FROM " + TABLE_NAME + " WHERE id = '" + idChosenEvent + "'", null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                String titleEvent = cursor.getString(cursor.getColumnIndex(TITLE));
                String dateEvent = cursor.getString(cursor.getColumnIndex(DATE));
                String locationEvent = cursor.getString(cursor.getColumnIndex(LOCATION));
                String latitudeEvent = cursor.getString(cursor.getColumnIndex(LATITUDE));
                String longitudeEvent = cursor.getString(cursor.getColumnIndex(LONGITUDE));
                String imageURLEvent = cursor.getString(cursor.getColumnIndex(IMAGE_URL));
                String ticketURLEvent = cursor.getString(cursor.getColumnIndex(TICKET_URL));
                String postcodeEvent = cursor.getString(cursor.getColumnIndex(POSTCODE));

                chosenEvent.put("title", titleEvent);
                chosenEvent.put("date", dateEvent);
                chosenEvent.put("location", locationEvent);
                chosenEvent.put("latitude", latitudeEvent);
                chosenEvent.put("longitude", longitudeEvent);
                chosenEvent.put("imageURL", imageURLEvent);
                chosenEvent.put("ticketURL", ticketURLEvent);
                chosenEvent.put("postcode", postcodeEvent);

                x = x + 1;
                cursor.moveToNext();
            }
        }

        return chosenEvent;
    }


}
