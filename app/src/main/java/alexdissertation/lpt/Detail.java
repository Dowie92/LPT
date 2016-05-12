package alexdissertation.lpt;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
//import android.app.TaskStackBuilder;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.BoolRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.TaskStackBuilder;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.StreamHandler;

public class Detail extends AppCompatActivity {
    private String loadFileName;
    private static ArrayList <String> detailContent = new ArrayList<String>();
    private static ArrayList<String> metricContent = new ArrayList<String>();

    private static TextView titleTextView;
    private static TextView startDateTextView;
    private static TextView endDateTextView;
    private static TextView timeTextView;
    private static EditText detailsEditText;

    private static EditText checkBoxUserInput;
    private static CheckBox firstCheckbox;
    private static int checkListsize = 0;
    private static boolean notificationSettings;
    private boolean loadDetailsFileCorrect;
    private boolean loadMetricsFileCorrect;

    // for the date selectors
    private static String buttonUsed;
    private static String dateSelected;

    private String metricTypeSelected;

    private static String notificationSelectorDate;
    private static TextView dateTextView;
    private static TextView timeAD;
    private static boolean notificationTimeBool;

    public static void setDateSelected(String t){
        Detail.dateSelected = t;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        notificationTimeBool = false;

        detailContent.clear();
        int i = detailContent.size();
        metricContent.clear();
        int m = metricContent.size();

        String prevTitle = new detailsBundle().bundlePrevLayerTitle();
        Log.d("prevTitle", prevTitle);
        String fileName = getFileName();
        Log.d("getFileName", fileName);

        notificationSettingsCheck();
        loadDetailsFile();
        loadDetailMetricsFile();

        arrayListChecker();
        populateDetails();
        populateMetrics();

        firstCheckbox = (CheckBox)findViewById(R.id.firstCheckbox);
        checkBoxUserInput = (EditText)findViewById(R.id.firstCheckboxEditText);// the first edit text...
        checkBoxUserInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String userInput = String.valueOf(checkBoxUserInput.getText()); //gets the text from the edit Text
                    Log.d("checkbox userInput",userInput);
                    // need to do something with the String inputs (to save)... add to an array
                    checkListsize = checkListsize +1;
                    updateCheckbox();
                }
                return false;
            }
        });
        firstCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxUserInput.setPaintFlags(checkBoxUserInput.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );
                }
                else{
                    checkBoxUserInput.setPaintFlags(0);
                    //do nothing
                }
            }
        });

        Button setTime = (Button)findViewById(R.id.timeButton);
        if (setTime != null) {
            setTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimePickerDialog(v);

                }
            });
        }
        titleTextView = (TextView)findViewById(R.id.Title);
        startDateTextView = (TextView)findViewById(R.id.startDate);
        endDateTextView = (TextView)findViewById(R.id.endDate);
        timeTextView = (TextView)findViewById(R.id.timeText);
        detailsEditText = (EditText)findViewById(R.id.detailsEditText);

        ImageButton setStartDate = (ImageButton)findViewById(R.id.startDateButton);
        if (setStartDate != null){
            setStartDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(v);
                }
            });
        }
        ImageButton setEndDate = (ImageButton)findViewById(R.id.endDateButton);
        if (setEndDate != null){
            setEndDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(v);
                }
            });
        }
        ImageButton setStartDateNotification = (ImageButton)findViewById(R.id.startDateNotificationButton);
        if (setStartDateNotification != null){
            setStartDateNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notificationAlertDialog();
                }
            });
        }
        ImageButton setEndDateNotification = (ImageButton)findViewById(R.id.endDateNotificationButton);
        if (setEndDateNotification != null){
            setEndDateNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notificationAlertDialog();
                }
            });
        }

        final Button metricsButton = (Button)findViewById(R.id.metricsAdd);
        if (metricsButton != null) {
            metricsButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    addMetricAlertDialog(); // add in a new metric

                }
            });
        }


        //Removing the FAB for now as it gets in the way...
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    } //onCreate method end

    public void addNotification(String date, String time) {
        final LinearLayout metricLayout = (LinearLayout) findViewById(R.id.notificationsLinear); //gets the metric LL

        final LinearLayout overLinearLayout = new LinearLayout(Detail.this);
        overLinearLayout.setId(R.id.notificationsLinearLayout); // might be able to use this to loop through all items to get their values...
        overLinearLayout.setOrientation(LinearLayout.VERTICAL);
        overLinearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.metricsborder));
        overLinearLayout.setPadding(0, 0, 0, 50);
        overLinearLayout.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        if (metricLayout != null) { // as long as ML exists add a new layout
            metricLayout.addView(overLinearLayout);
        }

        final LinearLayout linearLayout1 = new LinearLayout(Detail.this); // Creates the new LL - for title
        linearLayout1.setId(R.id.notificationsLinearLayout); // might be able to use this to loop through all items to get their values...
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


        if (metricLayout != null) { // as long as ML exists add a new layout
            overLinearLayout.addView(linearLayout1);
        }

        String nameTextText = "Date:";
        final TextView notificationDateText = new TextView(Detail.this);
        notificationDateText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        notificationDateText.setGravity(Gravity.BOTTOM);
        notificationDateText.setPadding(30, 0, 30, 0);
        notificationDateText.setText(nameTextText);


        final TextView notificationDate = new TextView(Detail.this);
        notificationDate.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        notificationDate.setGravity(Gravity.BOTTOM);
        notificationDate.setText(date);

        final Button deleteButton = new Button(Detail.this);
        deleteButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete_black_18dp, 0);
        deleteButton.getBackground().setAlpha(0);

        linearLayout1.addView(notificationDateText);
        linearLayout1.addView(notificationDate);
        linearLayout1.addView(deleteButton);

        final LinearLayout linearLayout2 = new LinearLayout(Detail.this); // Creates the new LL - for title
        linearLayout2.setId(R.id.notificationsLinearLayout); // might be able to use this to loop through all items to get their values...
        linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout2.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


        if (metricLayout != null) { // as long as ML exists add a new layout
            overLinearLayout.addView(linearLayout2);
        }

        String timeText = "Time:";
        final TextView notificationTimeText = new TextView(Detail.this);
        notificationTimeText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 85f));
        notificationTimeText.setGravity(Gravity.BOTTOM);
        notificationTimeText.setPadding(30, 0, 0, 0);
        notificationTimeText.setText(timeText);

        final TextView notificationTime = new TextView(Detail.this);
        notificationTime.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 15f));
        notificationTime.setGravity(Gravity.BOTTOM);
        notificationTime.setText(time);

        linearLayout2.addView(notificationTimeText);
        linearLayout2.addView(notificationTime);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overLinearLayout.setVisibility(View.GONE);
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.GONE);
            }
        });

    }

    public void notificationAlertDialog (){
        LayoutInflater layoutInflater = LayoutInflater.from(Detail.this);
        View addNotificationView = layoutInflater.inflate(R.layout.notificationcreatorlayout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);
        builder.setTitle("Create Alert");
        builder.setView(addNotificationView);
        notificationTimeBool = true;

        dateTextView = (TextView)addNotificationView.findViewById(R.id.dateTextView);
        // need a check to see which button was used to create the alert dialog...
        Log.d("startdateTV", String.valueOf(startDateTextView.getText()));
        if (dateTextView != null) {
            dateTextView.setText(startDateTextView.getText());
        }
        timeAD = (TextView)addNotificationView.findViewById(R.id.timeTextView);
        if (timeAD != null) {
            timeAD.setText(timeTextView.getText());
        }

        final ImageButton dateSelector = (ImageButton)addNotificationView.findViewById(R.id.dateSelector);
        if (dateSelector != null){
            dateSelector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(v);
                }
            });
        }
        ImageButton timeSelector =(ImageButton)addNotificationView.findViewById(R.id.timeSelector);
        if (timeSelector != null){
            timeSelector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimePickerDialog(v);
                }
            });
        }

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createDateNotification();
                /* will also need to set the notifications area to visisble... pass the data that the alert dialog holds onto
                * the new area....
                * create the notification with time restrictions to go off when it is set...
                */
                addNotification(String.valueOf(dateTextView.getText()),String.valueOf(timeAD.getText()));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //does nothing... alerts user to enter another number... want to edit the text val to remove a number
                Toast.makeText(Detail.this, "The creation of an alert has been canceled.", Toast.LENGTH_LONG).show();
                notificationTimeBool = false;
            }
        });
        builder.show();
    }


    public void createDateNotification (){
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        // set for 30 seconds later
        alarmMgr.set(AlarmManager.RTC, Calendar.getInstance().getTimeInMillis() + 10000, alarmIntent);
        /*
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_date_range_white_18dp);
        builder.setContentTitle("Times up");
        builder.setContentText("This is the end date for the plan/subtitle");
        builder.setVibrate(new long[]{ 1000, 1000, 1000 });


        Intent resultIntent = new Intent(this, Detail.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        int notificationId = 1;
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId, builder.build());
        */
        //TaskStackBuilder stackBuilder = new TaskStackBuilder(this);
        //stackBuilder.addParentStack(Detail.class);
        //adds the intent that starts the activity to the top of the stack
        //stackBuilder.addNextIntent(resultIntent);
        //PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        //NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        Log.d("View", String.valueOf(v));
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
        if (String.valueOf(v) != null){ // check on which button was used in selection
            if (String.valueOf(v).contains("startDateButton")){
                buttonUsed = "startDateButton";
            }
            if (String.valueOf(v).contains("endDateButton")){
                buttonUsed = "endDateButton";
            }
        }
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
            return new TimePickerDialog(getActivity(),this,hour,minute, DateFormat.is24HourFormat(getActivity()));
        }
        public void onTimeSet (TimePicker view, int hourOfDay, int minute){
            //do something with the time chosen by the user
            if (!notificationTimeBool ){
                timeTextView.setText(String.format("%02d:%02d", hourOfDay,minute));
            }

            else{
                timeAD.setText(String.format("%02d:%02d", hourOfDay,minute));
                notificationTimeBool = false;
            }

            // need a way to differentiate between the alert dialog and the normal use...
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
        //
        public void onDateSet(DatePicker view, int year, int month, int day) {// sets the date selected
            boolean dateCheck = false;
            // Do something with the date chosen by the user needs changing to work
            if (day <10){ // adds a 0 infront of the day if it is <10
                day = Integer.parseInt("0")+day;
            }
            if (month<10){ //adds a 0 infront of the month is it is ,10
                month = Integer.parseInt("0")+month;
            }
            StringBuilder someDate = new StringBuilder().append(day).append("/").append(month+1).append("/").append(year);
            String aDate = String.valueOf(someDate);
            Log.d ("anotherDate", aDate);
            Detail.setDateSelected(aDate); //sets the value of dateSelected

            if (buttonUsed != null) {
                if (buttonUsed.equals("startDateButton")) {
                    startDateTextView.setText(dateSelected);
                    endDateTextView.setText(dateSelected);
                    buttonUsed = null;
                }
                else if (buttonUsed.equals("endDateButton")) {
                    // might need to parse it into a simpeDate format...
                    final String startDate = String.valueOf(startDateTextView.getText());
                    Log.d("start Date", startDate);
                    Log.d ("aDate", aDate);

                    if (startDate != null){
                        try {
                            dateCheck = isDateAfter(startDate, aDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Log.d ("dateCheck", String.valueOf(dateCheck));
                    }
                    if (!dateCheck){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Warning:");
                        builder.setCancelable(false);
                        builder.setMessage("The end date that you have selected is before the start date\n\nPlease select a new end date");
                        builder.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogue, int id) {
                                //Closes the Alert Dialog
                                //need to get current date/time
                                endDateTextView.setText(startDate);
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                    else{
                        endDateTextView.setText(dateSelected);
                    }
                    buttonUsed = null;
                }

            }
            else if (buttonUsed == null){
                notificationSelectorDate = dateSelected;
                dateTextView.setText(notificationSelectorDate);
                Toast.makeText(getActivity(), "the null thing!", Toast.LENGTH_LONG).show();
            }

        }
    }
    public static boolean isDateAfter(String startDate,String endDate) throws ParseException {
        String sDF = "dd/MM/yyyy"; //
        SimpleDateFormat df = new SimpleDateFormat(sDF);
        Date date1 = df.parse(endDate);
        Date startingDate = df.parse(startDate);
        Log.d ("date 1", String.valueOf(date1));
        Log.d ("date 2", String.valueOf(startingDate));

        if (date1.after(startingDate)) {
            return true;
        }
        else {
            return false;
        }
    }

    public class detailsBundle{
        private String bundlePrevLayerTitle;
        public void getBundle(){
            //prevLayerTitle = null;
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                bundlePrevLayerTitle = extras.getString("lastTitle");
                Log.d("detailsTitlebundle", bundlePrevLayerTitle);
            }
        }
        public String bundlePrevLayerTitle(){
            getBundle();
            if (bundlePrevLayerTitle !=null)
            bundlePrevLayerTitle = bundlePrevLayerTitle.replace("D1Q0jyf6fJ","");
            return bundlePrevLayerTitle;
        }

    }
    public boolean notificationSettingsCheck(){
       notificationSettings = new Home().getNotificationSetting();
        return notificationSettings;
    }

    public void percentageCheck(String percentageString){
        double percentageDouble = Double.parseDouble(percentageString);
        double twentyFive = 25;
        double fifty = 50;
        double seventyFive = 75;
        double hundred = 100;
        String standardMessage =  "Congratulations you have completed ";
        String messageEqual25 =  standardMessage+twentyFive+"% of your metric at "+percentageDouble+"% complete so far.\n\nYou are a quarter of the way there!";
        String messageOver25 =  standardMessage+"over "+twentyFive+" of your metric at "+percentageDouble+"% complete so far.\n\nKeep it up!";
        String messageEqual50 =  standardMessage+fifty+"% of your metric at "+percentageDouble+"% complete so far.\n\nYou are half way there!";
        String messageOver50 =  standardMessage+"over "+fifty+"% of your metric at "+percentageDouble+"% complete so far.\n\nKeep it up!";
        String messageEqual75 =  standardMessage+seventyFive+"% of your metric at "+percentageDouble+"% complete so far.\n\nYou are three quarters the way there. Keep it up";
        String messageOver75 =  standardMessage+"over "+seventyFive+" of your metric at "+percentageDouble+"% complete so far.\n\nAlmost Complete";
        String messageEqual100 =  standardMessage+percentageDouble+"% of your metric!\n\nIts Done! Now onto the next one!";
        String messageOver100 =  standardMessage+"more than your metric needed!! With "+percentageDouble+"% done!";
        if (percentageDouble == twentyFive){ // for the 25% complete
            congratsAlertBuilder(messageEqual25);
        }
        else if (percentageDouble >twentyFive && percentageDouble< fifty){  // for between 25 and 50
            congratsAlertBuilder(messageOver25);
        }
        if (percentageDouble == fifty ){   //for 50%
            congratsAlertBuilder(messageEqual50);
        }
        if (percentageDouble > fifty && percentageDouble < seventyFive){   // for between 50 and 75
            congratsAlertBuilder(messageOver50);
        }
        if (percentageDouble == seventyFive){
            congratsAlertBuilder(messageEqual75);
        }
        if (percentageDouble > seventyFive && percentageDouble < hundred){
            congratsAlertBuilder(messageOver75);
        }
        if (percentageDouble == hundred){
            congratsAlertBuilder(messageEqual100);
        }
        if (percentageDouble > hundred){
            num2AboveNum1(messageOver100);
        }

    }
    public void overHundred(String messageOver100){
        congratsAlertBuilder(messageOver100);
    }
    public String percentageCalculation(double firstNumber, double secondNumber){
        double percentageBool = (secondNumber/firstNumber)*100;
        String percentageFormat = String.format("%.2f", percentageBool);
        return percentageFormat;
    }
    public void congratsAlertBuilder(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);
        builder.setTitle("Congrats");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Great!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //closes the alert Dialog
            }
        });
        builder.show();
    }
    public void num2AboveNum1(final String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);
        builder.setTitle("Number Larger");
        builder.setMessage("The number you have given for Complete is larger than the value Needed.");
        builder.setPositiveButton("Confirm & Keep Value", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //does nothing
                overHundred(message);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //does nothing... alerts user to enter another number... want to edit the text val to remove a number
                Toast.makeText(Detail.this, "Please enter another number!", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();

    }
    public void missingValAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);
        builder.setTitle("Number missing");
        builder.setMessage("One or both of the metric numbers are missing\n\nPlease enter a value(s)");
        builder.setCancelable(false);
        builder.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //closes the alert Dialog
            }
        });
        builder.show();

    }
    public void addMetricAlertDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(Detail.this);
        View addDetailView = layoutInflater.inflate(R.layout.metricsinputdialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);
        builder.setView(addDetailView);
        final EditText metricNameEditText = (EditText)addDetailView.findViewById(R.id.nameEditText);
        final EditText metricAmountEditText = (EditText)addDetailView.findViewById(R.id.amountEditText);
        final EditText metricCompleteEditText = (EditText)addDetailView.findViewById(R.id.completeEditText);

        final EditText metricAmountEditTextDouble = (EditText)addDetailView.findViewById(R.id.amountEditTextDouble);
        final EditText metricCompleteEditTextDouble = (EditText)addDetailView.findViewById(R.id.completeEditTextDouble);
        // setting up the spinner
        final Spinner metricTypeSelector = (Spinner)addDetailView.findViewById(R.id.metricTypeSpinner);
        List<String> metricSpinner = new ArrayList<String>();
        metricSpinner.add("Whole numbers");
        metricSpinner.add("Decimal numbers");
        final ArrayAdapter<String> metricsDataAdapter = new ArrayAdapter<String>(Detail.this, android.R.layout.simple_spinner_item, metricSpinner);
        metricTypeSelector.setAdapter(metricsDataAdapter);
        metricTypeSelector.setSelection(Adapter.NO_SELECTION, false);
        metricTypeSelected = "Whole numbers";

        // need to set the base level value
        metricTypeSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                metricTypeSelected = metricTypeSelector.getItemAtPosition(arg2).toString();
                // set the values to null when they dissapear
                addMetricTypeCheck();
                if (addMetricTypeCheck()){
                    metricAmountEditTextDouble.setVisibility(View.INVISIBLE);
                    metricCompleteEditTextDouble.setVisibility(View.INVISIBLE);
                    metricAmountEditTextDouble.setText(null);
                    metricCompleteEditTextDouble.setText(null);

                    metricAmountEditText.setVisibility(View.VISIBLE);
                    metricCompleteEditText.setVisibility(View.VISIBLE);
                }
                else{
                    metricAmountEditText.setVisibility(View.INVISIBLE);
                    metricCompleteEditText.setVisibility(View.INVISIBLE);
                    metricAmountEditText.setText(null);
                    metricCompleteEditText.setText(null);

                    metricAmountEditTextDouble.setVisibility(View.VISIBLE);
                    metricCompleteEditTextDouble.setVisibility(View.VISIBLE);
                }//if (metricTypeSelected.equals("Decimal numbers"))
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // no need to d anything...
            }
        });

        builder.setTitle("Add Metric");
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //closes the alert Dialog
                String metricNameVal = null;
                String metricAmountVal = null;
                String metricCompleteVal = null;

                if (metricNameEditText != null) {
                    metricNameVal = metricNameEditText.getText().toString();
                    Log.d("metricNameVal", metricNameVal);
                }
                boolean addMCheck = addMetricTypeCheck();
                if (addMCheck){
                    if (metricAmountEditText != null) {
                        metricAmountVal = metricAmountEditText.getText().toString();
                    }
                    if (metricCompleteEditText != null) {
                        metricCompleteVal = metricCompleteEditText.getText().toString();
                    }
                }
                else {
                    if (metricAmountEditTextDouble != null) {
                        metricAmountVal = metricAmountEditTextDouble.getText().toString();
                    }
                    if (metricCompleteEditTextDouble != null) {
                        metricCompleteVal = metricCompleteEditTextDouble.getText().toString();
                    }
                }
                addMetric(metricNameVal, metricAmountVal, metricCompleteVal);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogue, int id) {
                //cancels the add
            }
        });
        builder.show();
    }
    public boolean addMetricTypeCheck(){
        boolean wholeNumberSelected;
        if (metricTypeSelected == null) {
            wholeNumberSelected = Boolean.parseBoolean(null);
            Log.d("WNSNull", String.valueOf(wholeNumberSelected));
            return wholeNumberSelected;
        }
        else {
            if (metricTypeSelected.equals("Whole numbers")) {
                wholeNumberSelected = true;
            } else {
                wholeNumberSelected = false;
            }
            Log.d("wholeNumberSelected", String.valueOf(wholeNumberSelected));
        }
        return wholeNumberSelected;
    }

    public void addMetric(String name, String amount, String complete){
        final LinearLayout metricLayout = (LinearLayout)findViewById(R.id.metricsLinear); //gets the metric LL
        boolean addMetricCheck = addMetricTypeCheck();

        final LinearLayout overLinearLayout = new  LinearLayout(Detail.this);
        overLinearLayout.setId(R.id.metricLinearLayout); // might be able to use this to loop through all items to get their values...
        overLinearLayout.setOrientation(LinearLayout.VERTICAL);
        overLinearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.metricsborder));
        overLinearLayout.setPadding(0,0,0,50);
        overLinearLayout.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        if (metricLayout != null) { // as long as ML exists add a new layout
            metricLayout.addView(overLinearLayout);
        }

        final LinearLayout linearLayout1 = new LinearLayout(Detail.this); // Creates the new LL - for title
        linearLayout1.setId(R.id.metricLinearLayout); // might be able to use this to loop through all items to get their values...
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


        if (metricLayout != null) { // as long as ML exists add a new layout
            overLinearLayout.addView(linearLayout1);
        }

        String nameTextText = "Metric Name";
        final TextView metricNameText = new TextView(Detail.this);
        metricNameText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        metricNameText.setGravity(Gravity.BOTTOM);
        metricNameText.setPadding(30,0,30,0);
        metricNameText.setText(nameTextText);

        String metricNameHint = "word count";
        final EditText metricName = new EditText(Detail.this);
        metricName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        metricName.setGravity(Gravity.BOTTOM);
        metricName.setHint(metricNameHint);
        metricName.setImeOptions(EditorInfo.IME_ACTION_DONE);
        metricName.setInputType(InputType.TYPE_CLASS_TEXT);
        metricName.setText(name);

        final Button deleteButton = new Button(Detail.this);
        deleteButton.setCompoundDrawablesWithIntrinsicBounds( 0,0,R.drawable.ic_delete_black_18dp,0);
        deleteButton.getBackground().setAlpha(0);


        linearLayout1.addView(metricNameText);
        linearLayout1.addView(metricName);
        linearLayout1.addView(deleteButton);

        //Layer 1 sorted...

        //Layer2
        final LinearLayout linearLayout2 = new LinearLayout(Detail.this); // creates the LL for to be added
        linearLayout2.setId(R.id.metricLinearLayout); // might be able to use this to loop through all items to get their values...
        linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout2.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        if (metricLayout != null) { // as long as ML exists add a new layout
            overLinearLayout.addView(linearLayout2);
        }

        String amountTextViewText = "Amount";
        final TextView amountTextView= new TextView(Detail.this);
        amountTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        amountTextView.setGravity(Gravity.BOTTOM);
        amountTextView.setPadding(30,0,200,0);
        amountTextView.setText(amountTextViewText);

        final EditText metricsFirstNumber = new EditText(Detail.this);
        metricsFirstNumber.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        metricsFirstNumber.setGravity(Gravity.CENTER_HORIZONTAL);
        if (addMetricCheck){
            metricsFirstNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        else{
            metricsFirstNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        if (amount.contains(".") | complete.contains(".")){
            metricsFirstNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        else{
            metricsFirstNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        metricsFirstNumber.setImeOptions(EditorInfo.IME_ACTION_DONE);
        metricsFirstNumber.setText(amount);

        String placeHolder = "";
        final TextView placeHolderTextV = new TextView(Detail.this);
        placeHolderTextV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        placeHolderTextV.setGravity(Gravity.BOTTOM);
        placeHolderTextV.setText(placeHolder);
        placeHolderTextV.setPadding(400,0,0,0);

        linearLayout2.addView(amountTextView);
        linearLayout2.addView(metricsFirstNumber);
        linearLayout2.addView(placeHolderTextV);


        //Linear Layout 2 done

        final LinearLayout linearLayout3 = new LinearLayout(Detail.this); // creates the LL for the complete
        linearLayout3.setId(R.id.metricLinearLayout); // might be able to use this to loop through all items to get their values...
        linearLayout3.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout3.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        if (metricLayout != null) { // as long as ML exists add a new layout
            overLinearLayout.addView(linearLayout3);
        }

        String completeTextText = "Complete";
        TextView completeText = new TextView(Detail.this);
        completeText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        completeText.setGravity(Gravity.BOTTOM);
        completeText.setPadding(30,0,200,0);
        completeText.setText(completeTextText);


        final EditText metricsCompleteNumber = new EditText(Detail.this);
        metricsCompleteNumber.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        metricsCompleteNumber.setGravity(Gravity.CENTER_HORIZONTAL);
        if (addMetricCheck){ // when adding a new metric frim in the details area
            metricsCompleteNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        else{
            metricsCompleteNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        if (amount.contains(".") | complete.contains(".")){ // for checking the details from the savefile coming from addDetails Class
            metricsFirstNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        else{
            metricsCompleteNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        metricsCompleteNumber.setImeOptions(EditorInfo.IME_ACTION_DONE);
        metricsCompleteNumber.setText(complete);

        // need an onclick done for this to perform the stat analysis to get the % done....
        //will need checks to make sure that it is correct... no lower than 1st val numbers... no too large numbers

        String placeHolder2 = "";
        final TextView placeHolderTextV2 = new TextView(Detail.this);
        placeHolderTextV2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        placeHolderTextV2.setGravity(Gravity.BOTTOM);
        placeHolderTextV2.setText(placeHolder2);
        placeHolderTextV2.setPadding(400,0,0,0);

        linearLayout3.addView(completeText);
        linearLayout3.addView(metricsCompleteNumber);
        linearLayout3.addView(placeHolderTextV2);


        final LinearLayout linearLayout4 = new LinearLayout(Detail.this); //creates the LL completeness
        linearLayout4.setId(R.id.metricLinearLayout); // might be able to use this to loop through all items to get their values...
        linearLayout4.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout4.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        if (metricLayout != null) { // as long as ML exists add a new layout
            overLinearLayout.addView(linearLayout4);
        }


        final TextView finalStatText = new TextView(Detail.this);
        finalStatText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        finalStatText.setGravity(Gravity.BOTTOM);
        finalStatText.setPadding(30,80,50,100);
        finalStatText.setVisibility(View.GONE);
        linearLayout4.addView(finalStatText);
        //Stat analysis... get the % complete
        //calculation on the done click when the user has input from the second number
        // performs the stats and checks on the second value when the user moves away
        metricsCompleteNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if ((String.valueOf(metricsFirstNumber.getText())).equals("") || (String.valueOf(metricsCompleteNumber.getText())).equals("")) {
                        missingValAlertDialog();
                    }
                    else{
                        final double number1 = Double.parseDouble(String.valueOf(metricsFirstNumber.getText()));
                        final double number2 = Double.parseDouble(String.valueOf(metricsCompleteNumber.getText()));
                        String percentageString = percentageCalculation(number1, number2);
                        boolean notificationSettingsCheck = notificationSettingsCheck();
                        if (!notificationSettingsCheck){
                            percentageCheck(percentageString);
                        }
                        if (number1 ==0 && number2 ==0){
                            percentageString = "0";
                        }
                        String finalStatTextText = "You have completed " + percentageString + "% of your Plan/Subtask";
                        finalStatText.setText(finalStatTextText);
                        finalStatText.setVisibility(View.VISIBLE);

                    }

                }

            }
        });
        // performs the checks and stat analysis on the first number value when the user moves away
        metricsFirstNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if ((String.valueOf(metricsFirstNumber.getText())).equals("") || (String.valueOf(metricsCompleteNumber.getText())).equals("")) {// alert to let the user know they have left this blank
                        missingValAlertDialog();
                    }
                    else{
                        final double number1 = Double.parseDouble(String.valueOf(metricsFirstNumber.getText()));
                        final double number2 = Double.parseDouble(String.valueOf(metricsCompleteNumber.getText()));
                        String percentageString = percentageCalculation(number1, number2);
                        boolean notificationSettingsCheck = notificationSettingsCheck();
                        if (!notificationSettingsCheck){
                            percentageCheck(percentageString);
                        }
                        //percentageCheck(percentageString);
                        if (number1 ==0 && number2 ==0){
                            percentageString = "0";
                        }
                        String finalStatTextText = "You have completed " + percentageString + "% of your Plan/Subtask";
                        finalStatText.setText(finalStatTextText);
                        finalStatText.setVisibility(View.VISIBLE);

                    }

                }

            }
        });
        metricsCompleteNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //check the number values are not null or
                    if ((String.valueOf(metricsFirstNumber.getText())).equals("") || (String.valueOf(metricsCompleteNumber.getText())).equals("")) {
                        //Alert Dialog to let the user know...
                        AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);
                        builder.setTitle("Number missing");
                        builder.setMessage("One or both of the metric numbers are missing\n\nPlease enter a value(s)");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //closes the alert Dialog
                            }
                        });
                        builder.show();

                    }
                    else{
                        final double number1 = Integer.parseInt(String.valueOf(metricsFirstNumber.getText()));
                        final double number2 = Integer.parseInt(String.valueOf(metricsCompleteNumber.getText()));
                        String percentageString = percentageCalculation(number1, number2);
                        boolean notificationSettingsCheck = notificationSettingsCheck();
                        if (!notificationSettingsCheck){
                            percentageCheck(percentageString);
                        }
                        //percentageCheck(percentageString);
                        if (number1 ==0 && number2 ==0){
                            percentageString = "0";
                        }
                        String finalStatTextText = "You have completed " + percentageString + "% of your Plan/Subtask";
                        finalStatText.setText(finalStatTextText);
                        finalStatText.setVisibility(View.VISIBLE);
                    }

                }
                return false;
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overLinearLayout.setVisibility(View.GONE);
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.GONE);
                linearLayout3.setVisibility(View.GONE);
                linearLayout4.setVisibility(View.GONE);

            }
        });

    }

    public void updateCheckbox(){ // adds a new Linear Layout containin the Checkbox and edit text
        final LinearLayout firstLinearLayout = (LinearLayout)findViewById(R.id.checkBoxLinear);
        final LinearLayout linearLayout = new LinearLayout(Detail.this);
        linearLayout.setId(checkListsize);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        if (firstLinearLayout != null) {
            firstLinearLayout.addView(linearLayout);
        }

        CheckBox checkBox = new CheckBox(Detail.this);
        checkBox.setGravity(Gravity.BOTTOM);


        final EditText editText = new EditText(Detail.this);
        editText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        editText.setGravity(Gravity.BOTTOM);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        final Button deleteButton = new Button(Detail.this);
        deleteButton.setCompoundDrawablesWithIntrinsicBounds( 0,0,R.drawable.ic_delete_black_18dp,0);
        deleteButton.getBackground().setAlpha(0);
        deleteButton.setVisibility(View.GONE);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.GONE);
            }
        });

        linearLayout.addView(checkBox);
        linearLayout.addView(editText);
        linearLayout.addView(deleteButton);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editText.setPaintFlags(editText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );
                }
                else{
                    checkBoxUserInput.setPaintFlags(0);
                }
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String userInput = String.valueOf(checkBoxUserInput.getText()); //gets the text from the edit Text
                    Log.d("checkbox userInput",userInput);
                    checkListsize = checkListsize +1;
                    updateCheckbox();
                }
                return false;
            }


        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    deleteButton.setVisibility(View.VISIBLE);
                }
                else {
                    deleteButton.setVisibility(View.GONE);
                }
            }
        });

    }
    public void updateCheckbox(String loadFileInput){ // adds a new Linear Layout containin the Checkbox and edit text
        final LinearLayout firstLinearLayout = (LinearLayout)findViewById(R.id.checkBoxLinear);
        final LinearLayout linearLayout = new LinearLayout(Detail.this);
        linearLayout.setId(checkListsize);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        if (firstLinearLayout != null) {
            firstLinearLayout.addView(linearLayout);
        }

        CheckBox checkBox = new CheckBox(Detail.this);
        checkBox.setGravity(Gravity.BOTTOM);

        final EditText editText = new EditText(Detail.this);
        editText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        editText.setGravity(Gravity.BOTTOM);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setText(loadFileInput);

        final Button deleteButton = new Button(Detail.this);
        deleteButton.setCompoundDrawablesWithIntrinsicBounds( 0,0,R.drawable.ic_delete_black_18dp,0);
        deleteButton.getBackground().setAlpha(0);
        deleteButton.setVisibility(View.GONE);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linearLayout.setVisibility(View.GONE);
            }
        });

        linearLayout.addView(checkBox);
        linearLayout.addView(editText);
        linearLayout.addView(deleteButton);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editText.setPaintFlags(editText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );
                }
                else{
                    editText.setPaintFlags(0);
                }
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String userInput = String.valueOf(checkBoxUserInput.getText()); //gets the text from the edit Text
                    Log.d("checkbox userInput",userInput);
                    // need to do something with the String inputs (to save)... add to an array
                    checkListsize = checkListsize +1;
                    updateCheckbox();
                }
                return false;
            }


        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    deleteButton.setVisibility(View.VISIBLE);
                }
                else {
                    deleteButton.setVisibility(View.GONE);
                }
            }
        });

    }


    public void arrayListChecker(){
        int size = detailContent.size();
        for(int i=0; i<size; i++){
            String arrayVal = detailContent.get(i);
            Log.d("DetailArrayVal", arrayVal);
        }

    }
    public void populateMetrics(){
        int metricSize = metricContent.size();
        Log.d ("metricContSize", String.valueOf(metricSize));
        int loopTimes = metricSize/3; //Calculation for the number of times that there will be a loop
        Log.d ("metricContSize", String.valueOf(metricSize));
        int nameInt = 0;
        int amountInt = 1;
        int completeInt = 2;

        for (int i = 0; i<loopTimes; i++){ // should loop 2 times
            String name = metricContent.get(nameInt);// gets the 0 array value....
            String amount = metricContent.get(amountInt); // gets the 1 array value
            String complete = metricContent.get(completeInt); // gets the 2 array value
            addMetric(name,amount,complete);// going to need to pass the values into the metrics

            //adds 3 to each int to increase their value to get the next details...
            nameInt = nameInt+3;
            amountInt = amountInt+3;
            completeInt = completeInt +3;
        }

    }

    public void populateDetails(){
        int i = detailContent.size();

        titleTextView = (TextView)findViewById(R.id.Title);
        /*if (titleTextView != null) {
            titleTextView.setText(detailContent.get(0));
        }*/
        if (1 <= i) { // gets the first value and adds it to its position
            titleTextView.setText(detailContent.get(0));
        }
        startDateTextView = (TextView)findViewById(R.id.startDate);
        /*if (startDateTextView !=null){
            startDateTextView.setText(detailContent.get(1));
        }*/
        if (2<=i){
            startDateTextView.setText(detailContent.get(1));
        }
        endDateTextView = (TextView)findViewById(R.id.endDate);
        /*if (endDateTextView !=null){
            endDateTextView.setText(detailContent.get(2));
        }*/
        if (3 <= i){
            endDateTextView.setText(detailContent.get(2));
        }
        timeTextView = (TextView)findViewById(R.id.timeText);
        /*if (timeTextView !=null){
            timeTextView.setText(detailContent.get(3));
        }*/
        if (4<=i){
            timeTextView.setText(detailContent.get(3));
        }
        detailsEditText = (EditText)findViewById(R.id.detailsEditText);
        /*if (detailsEditText != null){
            detailsEditText.setText(detailContent.get(4));
        }*/
        if (5 == i){ // adds the text that goes into the notes section
            if (detailContent.get(4)!= null){
                detailsEditText.setText(detailContent.get(4));
            }
        }

        if (6 <= i) { // 6 to check for the first edit text which can be set...
            checkBoxUserInput = (EditText)findViewById(R.id.firstCheckboxEditText);
            if (detailContent.get(5)!= null) {
                checkBoxUserInput.setText(detailContent.get(5));
            }

        }
        if (7 <= i) { // checks for there being more to create the edit Texts Needed
            for (int editTextI = 7; editTextI < i; editTextI++) { // iterates through the values to create the checkboxes and Edit texts
                if (detailContent.get(editTextI-1)!=null){ // check that the value exists in the arrayList...Might not be needed...
                    updateCheckbox(String.valueOf(detailContent.get(editTextI-1)));
                }
                // need to feed in the value needed to be added to the edit Text
                // updateCheckbox();
            }
        }


    }
    public String getFileName(){
        LayerTitles fileName = new LayerTitles();
        String fileNameString;
        String Tester;
        fileName.setTitleFullConcat();//runs LT class to set the filename
        Tester = LayerTitles.getFinalFileConcat(); // 1st run of hometconc1... correctly works..
        Log.d ("filenameTester", Tester);
        fileNameString = LayerTitles.getFinalFileConcat()+"detailsD1Q0jyf6fJ";
        return fileNameString;
    }
    public String getMetricFileName(){
        LayerTitles fileName = new LayerTitles();
        String fileNameString;
        String Tester;
        fileName.setTitleFullConcat();//runs LT class to set the filename
        Tester = LayerTitles.getFinalFileConcat(); // 1st run of hometconc1... correctly works..
        Log.d ("filenameTester", Tester);
        fileNameString = LayerTitles.getFinalFileConcat()+"detailsMetricsD1Q0jyf6fJ";
        return fileNameString;
    }


    //Load file code
    public void loadDetailsFile() {
        loadFileName = getFileName();
        Log.d("Detail-LoadFileName", loadFileName);
        File loadFile = Detail.this.getFileStreamPath(loadFileName); //loadFileName
        if (loadFile.exists()){
            Log.d("System Out:LoadFile", "loadfile does exist!");
            try {
                FileInputStream fileInputStream = new FileInputStream(loadFile);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                String message;
                // this adds it to an arraylist...
                while ((message = bufferedReader.readLine()) !=null){
                    detailContent.add(message);
                }
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Log.d("System Out:load", "Loadfile does not exist");
        }

    }//Load file code
    public void loadDetailMetricsFile() {
        loadFileName = getMetricFileName();
        Log.d("Detail-LoadFileName", loadFileName);
        File loadFile = Detail.this.getFileStreamPath(loadFileName); //loadFileName
        if (loadFile.exists()){
            Log.d("System Out:LoadFile", "loadfile does exist!");
            try {
                FileInputStream fileInputStream = new FileInputStream(loadFile);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                String message;
                // this adds it to an arraylist...
                while ((message = bufferedReader.readLine()) !=null){
                    metricContent.add(message);
                }
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
        else {

            Log.d("System Out:load", "Loadfile does not exist");
            loadDetailsFileCorrect = false;
            loadMetricsFileCorrect = false;
        }

    }

}
