package alexdissertation.lpt;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddDetails extends AppCompatActivity {
    private String title;
    private String startDate;
    //private String endDate; probs not needed
    private String time;

    private File detailsFile;
    private ArrayList <String> details;

    private static TextView titleTextView;
    private static TextView startDateTextView;
    private static TextView endDateTextView;
    private static TextView timeTextView;
    private static String dateSelected;
    private static String buttonUsed;

    public static void setDateDelected(String t){
        AddDetails.dateSelected = t;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CalendarView calendarView = new CalendarView(this);
        AlertDialog.Builder startCalendarbuilder = new AlertDialog.Builder(this);
        startCalendarbuilder.setTitle("Add Date");
        startCalendarbuilder.setMessage("Select a Start Date");
        startCalendarbuilder.setView(calendarView);

        Calendar c = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        startDate = dateFormat.format(c.getTime());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        time = timeFormat.format(c.getTime());

        Log.d("start Date", startDate);


        titleTextView = (TextView)findViewById(R.id.Title);
        title = new detailsBundle().bundlePrevLayerTitle();
        if (titleTextView != null) {
            titleTextView.setText(title);
        }

        startDateTextView = (TextView)findViewById(R.id.startDate);
        if (startDateTextView !=null){
            startDateTextView.setText(startDate);
        }
        endDateTextView = (TextView)findViewById(R.id.endDate);
        if (endDateTextView !=null){
            endDateTextView.setText(startDate);
        }
        timeTextView = (TextView)findViewById(R.id.timeText);
        if (timeTextView !=null){
            timeTextView.setText(time);
        }

        Button setTime = (Button)findViewById(R.id.timeButton);
        if (setTime != null) {
            setTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimePickerDialog(v);

                }
            });
        }

        Button setStartDate = (Button)findViewById(R.id.startDateButton);
        if (setStartDate != null){
            setStartDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(v);
                }
            });
        }
        Button setEndDate = (Button)findViewById(R.id.endDateButton);
        if (setEndDate != null){
            setEndDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(v);
                }
            });
        }


        //All hardcoded aspects so removing the FAB.... for now...until the FAB can be utilised
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        Log.d("View", String.valueOf(v));
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
        if (String.valueOf(v) != null){
            if (String.valueOf(v).contains("startDateButton")){
                buttonUsed = "startDateButton";
            }
            if (String.valueOf(v).contains("endDateButton")){
                buttonUsed = "endDateButton";
            }
        }
        Log.d("buttonUsed",buttonUsed);
    }

    // sets up the time selector to be called when the button is pressed
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            //use current time as default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            //create new instance of timePicker dialog
            return new TimePickerDialog(getActivity(),this,hour,minute,DateFormat.is24HourFormat(getActivity()));
        }
        public void onTimeSet (TimePicker view, int hourOfDay,int minute){
            //do something with the time chosen by the user
            timeTextView.setText(String.format("%02d:%02d", hourOfDay,minute));
        }
    }

    //Date picker setup
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            //needs to be able to tell which date picker was selected....
            if (day <10){
                day = Integer.parseInt("0")+day;
            }
            if (month<10){
                month = Integer.parseInt("0")+month;
            }
            StringBuilder someDate = new StringBuilder().append(day).append("/").append(month+1).append("/").append(year);
            String aDate = String.valueOf(someDate);
            Log.d ("anotherDate", aDate);
            AddDetails.setDateDelected(aDate);
            Log.d("buttonusedfinal", buttonUsed);
            if (buttonUsed != null) {
                if (buttonUsed.equals("startDateButton")) {
                    startDateTextView.setText(dateSelected);
                }
                if (buttonUsed.equals("endDateButton")) {
                    endDateTextView.setText(dateSelected);
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbarsavebutton, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_save:
                //user chose the save button...
                try {
                    detailsSaveFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
                //intent for an activity incase the flow needs to go to the newly created details...
                //Intent intent = new Intent(getApplicationContext(), Details.class);
                //startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    // need a bundle handler to get the title Details
    public class detailsBundle{
        private String bundlePrevLayerTitle;
        public void getBundle(){
            //prevLayerTitle = null;
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                bundlePrevLayerTitle = extras.getString("detailsTitle");
                Log.d("detailsTitlebundle", bundlePrevLayerTitle);
            }
        }
        public String bundlePrevLayerTitle(){
            getBundle();

            return bundlePrevLayerTitle;
        }

    }

    public String getFileName(){
        LayerTitles fileName = new LayerTitles();
        String fileNameString;
        String Tester;
        fileName.setTitleFullConcat();//runs LT class to set the filename
        Tester = LayerTitles.getFinalFileConcat(); // 1st run of hometconc1... correctly works..
        Log.d ("filenameTester", Tester);
        fileNameString = LayerTitles.getFinalFileConcat()+"details"; //gets the file name and adds details (to differentiate to titles/layer system)
        Log.d("addDet- FileTester", fileNameString);
        return fileNameString;

    }
    public void getDetails(){// gets all of the details and adds it to an array list to go into a file
        details = new ArrayList<String>();
        String title = String.valueOf(titleTextView.getText());
        details.add(title);
        String sDate = String.valueOf(startDateTextView.getText());
        details.add(sDate);
        String eDate = String.valueOf(endDateTextView.getText());
        details.add(eDate);
        String time = String.valueOf(timeTextView.getText());
        details.add(time);
    }
    public void detailsSaveFile() throws IOException {
        getFileName();
        getDetails();
        String saveFileName = getFileName()+"Details"+"D1Q0jyf6fJ";
        Log.d("detailsSaveFN", saveFileName);
        detailsFile = new File(this.getFilesDir(), saveFileName);   //Creates file with the previous plan name and layer
        FileWriter writer = new FileWriter(detailsFile, true);
        int size = details.size(); // calling an array....
        //Log.d("Save file arraySize", String.valueOf(size));
        for (int i = 0; i < size; i++) { // adding the array to the file
            String str = details.get(i);
            //Log.d("saveFile Output", str);
            writer.write(str + "\n");
        }
        writer.close();

    }

}
