package com.event.joe.savedevent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.event.joe.com.event.joe.eventlistadapter.EventListAdapter;
import com.event.joe.com.event.joe.eventlistadapter.EventListDetailProvider;
import com.event.joe.datastorage.MySQLiteHelper;
import com.event.joe.eventplanner.EventDetailsActivity;
import com.event.joe.eventplanner.R;
import com.event.joe.savedlistadapter.SavedListAdapter;
import com.event.joe.savedlistadapter.SavedListDetailProvider;
import com.nispok.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MySavedEventsFragment extends Fragment {
    ListView lv;
    MySQLiteHelper sqLiteHelper;
    private String currentTitle;
    private List<String> arrayList;
    private List<String>arrayListDates;
    private List<String>arrayListId;
    private OnEventSaveSetListener onEventSaveSetListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.saved_events_layout, container, false);

        sqLiteHelper = new MySQLiteHelper(getActivity());
        arrayList = sqLiteHelper.getAllEvents();
        arrayListDates = sqLiteHelper.getAllEventDates();
        arrayListId = sqLiteHelper.getAllEventIDs();

        lv = (ListView)view.findViewById(R.id.lvMyEvents);
        //set list adapter **************************************************************************
         SetList();
        //set list adapter **************************************************************************

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String currentID =  arrayListId.get(position);

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    Toast.makeText(getActivity(), currentID , Toast.LENGTH_SHORT).show();
                    onEventSaveSetListener.setEvent(currentID);
                }else{
                    Intent intent = new Intent(getActivity(), SavedEventDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("currentID", currentID);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }



            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                currentTitle = arrayList.get(position);
                AlertDialog diaBox = AskOption();
                diaBox.show();
                return true;
            }
        });

        return view;
    }

    public void SetList(){
        int i = 0;
        SavedListAdapter eventAdapter = new SavedListAdapter(getActivity().getApplicationContext(),R.layout.event_list_cell);
        lv.setAdapter(eventAdapter);
        for(String event_titles: arrayList){
            SavedListDetailProvider dataProvider = new SavedListDetailProvider(arrayList.get(i), arrayListDates.get(i));
            eventAdapter.add(dataProvider);
            i++;
        }
    }

    private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getActivity())
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete this Event?")
                .setIcon(R.mipmap.trashcan)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        sqLiteHelper = new MySQLiteHelper(getActivity());
                        sqLiteHelper.deleteEvent(currentTitle);
                        System.out.println("********" + currentTitle);
                        arrayList.clear();
                        arrayListDates.clear();
                        arrayListDates = sqLiteHelper.getAllEventDates();
                        arrayList = sqLiteHelper.getAllEvents();
                        dialog.dismiss();
                        //set list adapter **************************************************************************
                       SetList();
                        //set list adapter **************************************************************************
                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }

    public interface OnEventSaveSetListener{

        public void setEvent(String currentID);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{

            onEventSaveSetListener = (OnEventSaveSetListener)activity;

        }catch(Exception ex){}

    }
}
