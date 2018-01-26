package com.example.varun.schedulerapp3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class ItineraryEditActivity extends AppCompatActivity implements View.OnClickListener {
    public Button save_button, discard_button, del_button;
    public EditText eventname;
    private String EventName;
    public int h , m,id ;
    public DBHelper mydb;
    TimePicker tp;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_edit);
        mydb = new DBHelper(this);
        eventname = (EditText) findViewById(R.id.eventname_editText);
        tp = (TimePicker)findViewById(R.id.timePicker);
        tp.setIs24HourView(false);
        save_button = (Button) findViewById(R.id.save_button);
        discard_button = (Button) findViewById(R.id.discard_button);
        del_button = (Button) findViewById(R.id.delete_button);
        save_button.setOnClickListener(this);
        discard_button.setOnClickListener(this);
        del_button.setOnClickListener(this);
        editEvent();

    }

    public void editEvent(){
       try {
           Intent edit = getIntent();
           id = Integer.parseInt(edit.getStringExtra("id"));
           eventname.setText(edit.getStringExtra("Event"));
           String h = edit.getStringExtra("Hour");
           String m = edit.getStringExtra("Min");
           Log.d("id", String.valueOf(id));
           Log.d("h",h);
           Log.d("m",m);
           tp.setHour(Integer.valueOf(h));
           tp.setMinute(Integer.valueOf(m));
           flag = true;
       }catch (Exception E){}


    }

    public void itineraryIntent(){
        Intent i = new Intent(this, ItineraryActivity.class);
        startActivity(i);
    }
    @Override
    public void onClick(View view) {    
        switch (view.getId()) {
            case R.id.save_button:
                EventName = String.valueOf(eventname.getText());
                if (EventName.isEmpty()){
                    Toast.makeText(this,"Type EventName",Toast.LENGTH_SHORT).show();
                }else{

                    h = tp.getHour();
                    m = tp.getMinute();
                    StringBuilder hm = new StringBuilder();
                    hm.append(h);
                    hm.append(":");
                    hm.append(m);
                    if (flag == false){
                        mydb.insertEvent(EventName, String.valueOf(hm));
                        Log.d("Done", "Done");
                        itineraryIntent();
                    }else {
                        mydb.updateEvent(id,EventName,String.valueOf(hm));
                        itineraryIntent();
                        Log.d("Update","event");
                    }
                }
                break;

            case R.id.discard_button:
                itineraryIntent();
                break;

            case R.id.delete_button:
                mydb.deleteEvent(id);
                itineraryIntent();
                Toast.makeText(this,"Event Deleted",Toast.LENGTH_SHORT).show();
                break;


        }
    }


}

