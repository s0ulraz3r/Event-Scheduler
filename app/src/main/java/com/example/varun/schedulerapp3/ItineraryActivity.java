package com.example.varun.schedulerapp3;

import android.content.Intent;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ItineraryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private DBHelper mydb;
    ArrayAdapter<String> adapter;
    ListView listView;
    ArrayList<String> eventList;
    ArrayList<HashMap<String,String>> timeList;
    Calendar cal;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);
        mydb = new DBHelper(this);
        listView = (ListView) findViewById(R.id.itinerary_listview);
        eventList = mydb.getAllEvent();
        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, eventList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu_itinerary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_event:    //Creates New Event
                Toast.makeText(this,"Create New Event",Toast.LENGTH_SHORT).show();
                Intent itineraryIntent = new Intent(this, ItineraryEditActivity.class);
                startActivity(itineraryIntent);
                return true;

            case R.id.set_alarms:  //Sets alarm
                Toast.makeText(this,"set_alarms",Toast.LENGTH_SHORT).show();
                cal = Calendar.getInstance();
                timeList = mydb.getAllEvents();
                for(HashMap<String,String> event : timeList){
                    int hour=Integer.valueOf(event.get("met").split(":")[0]);
                    int minute=Integer.valueOf(event.get("met").split(":")[1]);
                    String eventName=event.get("eventName");
                    cal.add(Calendar.HOUR,hour);
                    cal.add(Calendar.MINUTE,minute);
                    Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
                    alarmIntent.putExtra(AlarmClock.EXTRA_HOUR,cal.get(Calendar.HOUR_OF_DAY));
                    alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES,cal.get(Calendar.MINUTE));
                    alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE,eventName);
                    alarmIntent.putExtra(AlarmClock.EXTRA_SKIP_UI,true);
                    startActivityForResult(alarmIntent,Integer.valueOf(event.get("id")));
                }
                return true;

            case R.id.cancel_alarms:  //Cancel alarms
                Toast.makeText(this,"cancel_alarms",Toast.LENGTH_SHORT).show();
                Intent cancelalarm = new Intent(AlarmClock.ACTION_DISMISS_ALARM);
                cancelalarm.putExtra(AlarmClock.EXTRA_ALARM_SEARCH_MODE,AlarmClock.ALARM_SEARCH_MODE_LABEL);
                String[] eventSplit,eventName;
                eventSplit = eventList.get(0).split(",");
                eventName = eventSplit[0].split("\\.");
                Log.d("dismiss", eventName[1]);
                cancelalarm.putExtra(AlarmClock.EXTRA_MESSAGE,eventName[1]);
                startActivity(cancelalarm);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            HashMap<String, String> event = timeList.get(requestCode + 1);
            int hour = Integer.valueOf(event.get("met").split(":")[0]);
            int minute = Integer.valueOf(event.get("met").split(":")[1]);
            String eventName = event.get("eventName");
            cal.add(Calendar.HOUR, hour);
            cal.add(Calendar.MINUTE, minute);
            Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
            alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, cal.get(Calendar.HOUR_OF_DAY));
            alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, cal.get(Calendar.MINUTE));
            alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, eventName);
            alarmIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
            startActivityForResult(alarmIntent, Integer.valueOf(event.get("id")));
        }
        catch (Exception ex){

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String values = adapterView.getItemAtPosition(i).toString();
        List<String> t = Arrays.asList(values.split(","));
        Intent edit = new Intent(this,ItineraryEditActivity.class);
        String[] split_event = t.get(0).split("\\.");
        edit.putExtra("id",split_event[0]);
        edit.putExtra("Event",split_event[1]);
        String[] split_time = t.get(1).split(":");
        edit.putExtra("Hour",split_time[0]);
        edit.putExtra("Min",split_time[1]);
        startActivity(edit);
    }
}
