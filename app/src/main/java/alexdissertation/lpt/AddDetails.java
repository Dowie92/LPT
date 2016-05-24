package alexdissertation.lpt;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
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
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddDetails extends AppCompatActivity {
    private String title;
    private String startDate;
    private String time;
    private String detailsText;

    private File detailsFile;
    private ArrayList <String> details = new ArrayList<String>();

    private static TextView titleTextView;
    private static TextView startDateTextView;
    private static TextView endDateTextView;
    private static TextView timeTextView;
    private static EditText detailsEditText;
    private static String dateSelected;
    private static String buttonUsed;

    private static EditText checkBoxUserInput;
    private static CheckBox firstCheckbox;
    private static int checkListsize = 0;
    private static ArrayList<EditText> allEditText = new ArrayList<EditText>();
    private static ArrayList<String> editTextValues = new ArrayList<String >();

    private static ArrayList<EditText>metricEditTexts = new ArrayList<EditText>();
    private static ArrayList<String>metricEditTextValues = new ArrayList<String>();

    private String metricTypeSelected;

    //for the expandable list view and not needed at the moment
    /*private static ExpandableListAdapter expandableListAdapter;
    private static ExpandableListView expandableListView;
    private static List<String> listHeader;
    private static HashMap<String,List<String>> listChild;

    private static String userInput;*/


    public static void setDateSelected(String t){
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

        allEditText.clear();
        editTextValues.clear();
        metricEditTexts.clear();
        metricEditTextValues.clear();

        //get the expandableListView
        //expandableListView = (ExpandableListView)findViewById(R.id.ChecklistexpandableListView);
        //prepping list data...(Might not be needed)
        //prepareListData(); // class actually needed...
        //expandableListAdapter = new alexdissertation.lpt.ExpandableListView(this,listHeader,listChild);
        //setting the List Adapter
        //expandableListView.setAdapter(expandableListAdapter);

        arrayListChecker();


        Calendar c = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        startDate = dateFormat.format(c.getTime());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        time = timeFormat.format(c.getTime());


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
                }
            }
        });

        titleTextView = (TextView)findViewById(R.id.Title);
        title = new addDetailsBundle().bundlePrevLayerTitle();
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

        detailsEditText = (EditText)findViewById(R.id.detailsEditText);
        if (detailsEditText !=null){
            detailsEditText.setText(detailsText);
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
        final Button metricsButton = (Button)findViewById(R.id.metricsAdd);
        if (metricsButton != null) {
            metricsButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // add in a new metric...
                    addMetricAlertDialog();

                }
            });
        }

    }// oncreate method end

    /* expandable list view removed for now
    public void prepareListData (){
        listHeader = new ArrayList<String>();
        listChild = new HashMap<String, List<String>>();
        listHeader.add("CheckList"); //adding the header Data...
        List<String>CheckList = new ArrayList<String>();
        CheckList.add(""); // adding the first one for the users to use
        listChild.put(listHeader.get(0), CheckList);//adding to the view

    }*/

    /* Notification implementation (needs proper research and implementation...
    public void createNotification(View view){ //creates an alarm for going past end date and time
        //build the notification
        Intent intent = new Intent(this, Detail.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.ic_access_alarms_black_18dp);
        notificationBuilder.setContentTitle("Plan has ended");
        notificationBuilder.setContentText("The (insert plan here) has gone past the end date you have set");
        notificationBuilder.setContentIntent(resultPendingIntent);


        int notificationID = 1; //sets the id so that it can be updated
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, notificationBuilder.build());

        //prepare intent which is triggered when the notification is selected
    }*/
    public void metricEditTextToArray(){
        int size = metricEditTexts.size();
        metricEditTextValues = new ArrayList<String>();
        for (int i = 0; i<size; i++){ // loops through the amount of sets of metrics added
            metricEditTextValues.add(String.valueOf(metricEditTexts.get(i).getText())); //loops through to get the values and add them to a string array
        }
    }

    public void editTextValuesToArray(){
        int size = allEditText.size();
        editTextValues.add(String.valueOf(checkBoxUserInput.getText())); // adds the first edit text value to the array
        for (int i = 0; i<size; i++){ //loops through the dynamically created arrays to add their values to the array
            editTextValues.add(String.valueOf(allEditText.get(i).getText()));
        }

    }
    public void addMetricAlertDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(AddDetails.this);
        View addDetailView = layoutInflater.inflate(R.layout.metricsinputdialog, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddDetails.this);
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
        final ArrayAdapter<String> metricsDataAdapter = new ArrayAdapter<String>(AddDetails.this, android.R.layout.simple_spinner_item, metricSpinner);
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
        if(metricTypeSelected.equals("Whole numbers")){
            wholeNumberSelected = true;
        }
        else {
            wholeNumberSelected =false;
        }
        Log.d("wholeNumberSelected", String.valueOf(wholeNumberSelected));

        return wholeNumberSelected;
    }

    public void addMetric(String nameVal, String amountVal, String completeVal ){
        final LinearLayout metricLayout = (LinearLayout)findViewById(R.id.metricsLinear); //gets the metric LL
        boolean addMetricCheck = addMetricTypeCheck();

        final LinearLayout overLinearLayout = new  LinearLayout(AddDetails.this);
        overLinearLayout.setId(R.id.metricLinearLayout); // might be able to use this to loop through all items to get their values...
        overLinearLayout.setOrientation(LinearLayout.VERTICAL);
        overLinearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.metricsborder));
        overLinearLayout.setPadding(0,0,0,50);
        overLinearLayout.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        if (metricLayout != null) { // as long as ML exists add a new layout
            metricLayout.addView(overLinearLayout);
        }

        final LinearLayout linearLayout1 = new LinearLayout(AddDetails.this); // Creates the new LL - for title
        linearLayout1.setId(R.id.metricLinearLayout); // might be able to use this to loop through all items to get their values...
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


        if (metricLayout != null) { // as long as ML exists add a new layout
            overLinearLayout.addView(linearLayout1);
        }

        String nameTextText = "Metric Name";
        final TextView metricNameText = new TextView(AddDetails.this);
        metricNameText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        metricNameText.setGravity(Gravity.BOTTOM);
        metricNameText.setPadding(30,0,30,0);
        metricNameText.setText(nameTextText);

        String metricNameHint = "word count";
        final EditText metricName = new EditText(AddDetails.this);
        metricName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        metricName.setGravity(Gravity.BOTTOM);
        metricName.setHint(metricNameHint);
        metricName.setImeOptions(EditorInfo.IME_ACTION_DONE);
        metricName.setInputType(InputType.TYPE_CLASS_TEXT);
        metricName.setText(nameVal);

        metricEditTexts.add(metricName);

        final Button deleteButton = new Button(AddDetails.this);
        deleteButton.setCompoundDrawablesWithIntrinsicBounds( 0,0,R.drawable.ic_delete_black_18dp,0);
        deleteButton.getBackground().setAlpha(0);


        linearLayout1.addView(metricNameText);
        linearLayout1.addView(metricName);
        linearLayout1.addView(deleteButton);

        //Layer 1 sorted...

        //Layer2
        final LinearLayout linearLayout2 = new LinearLayout(AddDetails.this); // creates the LL for to be added
        linearLayout2.setId(R.id.metricLinearLayout); // might be able to use this to loop through all items to get their values...
        linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout2.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        if (metricLayout != null) { // as long as ML exists add a new layout
            overLinearLayout.addView(linearLayout2);
        }

        String amountTextViewText = "Amount";
        final TextView amountTextView= new TextView(AddDetails.this);
        amountTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        amountTextView.setGravity(Gravity.BOTTOM);
        amountTextView.setPadding(30,0,200,0);
        amountTextView.setText(amountTextViewText);


        final EditText metricsFirstNumber = new EditText(AddDetails.this);
        metricsFirstNumber.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        metricsFirstNumber.setGravity(Gravity.BOTTOM);
        if (addMetricCheck){
            metricsFirstNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        else{
            metricsFirstNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }

        metricsFirstNumber.setImeOptions(EditorInfo.IME_ACTION_DONE);
        metricsFirstNumber.setText(amountVal);

        metricEditTexts.add(metricsFirstNumber);

        String placeHolder = "";
        final TextView placeHolderTextV = new TextView(AddDetails.this);
        placeHolderTextV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        placeHolderTextV.setGravity(Gravity.BOTTOM);
        placeHolderTextV.setText(placeHolder);
        placeHolderTextV.setPadding(400,0,0,0);

        linearLayout2.addView(amountTextView);
        linearLayout2.addView(metricsFirstNumber);
        linearLayout2.addView(placeHolderTextV);


        //Linear Layout 2 done

        final LinearLayout linearLayout3 = new LinearLayout(AddDetails.this); // creates the LL for the complete
        linearLayout3.setId(R.id.metricLinearLayout); // might be able to use this to loop through all items to get their values...
        linearLayout3.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout3.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        if (metricLayout != null) { // as long as ML exists add a new layout
            overLinearLayout.addView(linearLayout3);
        }

        String completeTextText = "Complete";
        TextView completeText = new TextView(AddDetails.this);
        completeText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        completeText.setGravity(Gravity.BOTTOM);
        completeText.setPadding(30,0,200,0);
        completeText.setText(completeTextText);


        final EditText metricsCompleteNumber = new EditText(AddDetails.this);
        metricsCompleteNumber.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        metricsCompleteNumber.setGravity(Gravity.BOTTOM);
        if (addMetricCheck){
            metricsCompleteNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        else{
            metricsCompleteNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        metricsCompleteNumber.setImeOptions(EditorInfo.IME_ACTION_DONE);
        metricsCompleteNumber.setText(completeVal);

        metricEditTexts.add(metricsCompleteNumber);

        // need an onclick done for this to perform the stat analysis to get the % done....
        //will need checks to make sure that it is correct... no lower than 1st val numbers... no too large numbers

        String placeHolder2 = "";
        final TextView placeHolderTextV2 = new TextView(AddDetails.this);
        placeHolderTextV2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        placeHolderTextV2.setGravity(Gravity.BOTTOM);
        placeHolderTextV2.setText(placeHolder2);
        placeHolderTextV2.setPadding(400,0,0,0);

        linearLayout3.addView(completeText);
        linearLayout3.addView(metricsCompleteNumber);
        linearLayout3.addView(placeHolderTextV2);


        final LinearLayout linearLayout4 = new LinearLayout(AddDetails.this); //creates the LL completeness
        linearLayout4.setId(R.id.metricLinearLayout); // might be able to use this to loop through all items to get their values...
        linearLayout4.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout4.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        if (metricLayout != null) { // as long as ML exists add a new layout
            overLinearLayout.addView(linearLayout4);
        }


        final TextView finalStatText = new TextView(AddDetails.this);
        finalStatText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        finalStatText.setGravity(Gravity.BOTTOM);
        finalStatText.setPadding(30,80,50,100);
        finalStatText.setVisibility(View.GONE);


        linearLayout4.addView(finalStatText);

        metricsCompleteNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if ((String.valueOf(metricsFirstNumber.getText())).equals("") || (String.valueOf(metricsCompleteNumber.getText())).equals("")) {
                        //Alert Dialog to let the user know...
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddDetails.this);
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
                        final double number1 = Double.parseDouble(String.valueOf(metricsFirstNumber.getText()));
                        final double number2 = Double.parseDouble(String.valueOf(metricsCompleteNumber.getText()));
                        Log.d("number1Val", String.valueOf(number1));
                        Log.d("number2Val", String.valueOf(number2));

                        if (number2 > number1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddDetails.this);
                            builder.setTitle("Number Larger");
                            builder.setMessage("The number you have given for Complete is larger than the value Needed.\n" + "Keep Value?");
                            builder.setPositiveButton("Keep Value", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    double percentage = (number2 / number1) * 100;
                                    String percentageFormat = String.format("%.2f", percentage);
                                    if (number1 ==0 && number2 ==0){
                                        percentageFormat = "0";
                                    }
                                    String finalStatTextText = "You have Completed " + percentageFormat + "% of your Plan/Subtask";
                                    finalStatText.setText(finalStatTextText);
                                    finalStatText.setVisibility(View.VISIBLE);
                                    //closes the alert Dialog
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    metricsCompleteNumber.setText("");
                                    Toast.makeText(AddDetails.this, "The complete number has been cleared\nPlease enter another number!", Toast.LENGTH_LONG).show();
                                }
                            });
                            builder.show();

                        } else {
                            double percentage = (number2 / number1) * 100;
                            String percentageFormat = String.format("%.2f", percentage);
                            if (number1 ==0 && number2 ==0){
                                percentageFormat = "0";
                            }
                            String finalStatTextText = "You have completed " + percentageFormat + "% of your Plan/Subtask";
                            finalStatText.setText(finalStatTextText);
                            finalStatText.setVisibility(View.VISIBLE);
                        }
                    }

                }

            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Are you sure you would like to delete this metric?";
                AlertDialog.Builder builder = new AlertDialog.Builder(AddDetails.this);
                builder.setTitle("Congrats");
                builder.setMessage(message);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        metricEditTexts.remove(metricName);
                        metricEditTexts.remove(metricsFirstNumber);
                        metricEditTexts.remove(metricsCompleteNumber);
                        overLinearLayout.setVisibility(View.GONE);
                        linearLayout1.setVisibility(View.GONE);
                        linearLayout2.setVisibility(View.GONE);
                        linearLayout3.setVisibility(View.GONE);
                        linearLayout4.setVisibility(View.GONE);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //closes the alert Dialog
                    }
                });
                builder.show();
                //Removes the relevant items from the Metric EditText array list



            }
        });

    }


    public void updateCheckbox(){
        final LinearLayout firstLinearLayout = (LinearLayout)findViewById(R.id.checkBoxLinear);
        final LinearLayout linearLayout = new LinearLayout(AddDetails.this);
        linearLayout.setId(checkListsize);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        if (firstLinearLayout != null) {
            firstLinearLayout.addView(linearLayout);
        }

        CheckBox checkBox = new CheckBox(AddDetails.this);
        checkBox.setGravity(Gravity.BOTTOM);

        final EditText editText = new EditText(AddDetails.this);
        editText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        editText.setGravity(Gravity.BOTTOM);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        allEditText.add(editText);

        final Button deleteButton = new Button(AddDetails.this);
        deleteButton.setCompoundDrawablesWithIntrinsicBounds( 0,0,R.drawable.ic_delete_black_18dp,0);
        deleteButton.getBackground().setAlpha(0);
        deleteButton.setVisibility(View.GONE);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allEditText.remove(editText);
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
                    //allEditTextValues.add(String.valueOf(editText.getText()));
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


    @Override
    public void onBackPressed(){
        boolean addBackPressed = true;
        Intent data = new Intent();
        data.putExtra("backPressedTrue", addBackPressed);
        setResult(RESULT_OK, data);
        finish();
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        Log.d("View", String.valueOf(v));
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
        if (String.valueOf(v) != null){ // chceck on which button was used in selection
            if (String.valueOf(v).contains("startDateButton")){
                buttonUsed = "startDateButton";
            }
            if (String.valueOf(v).contains("endDateButton")){
                buttonUsed = "endDateButton";
            }
        }
        Log.d("buttonUsed",buttonUsed);
    }

    public void showDatePickerDialog() { // might not be needed??
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
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
        //
        public void onDateSet(DatePicker view, int year, int month, int day) {// sets the date selected
            boolean dateCheck = false;
            // Do something with the date chosen by the user
            if (day <10){ // adds a 0 infront of the day if it is <10
                day = Integer.parseInt("0")+day;
            }
            if (month<10){ //adds a 0 infront of the month is it is ,10
                month = Integer.parseInt("0")+month;
            }
            StringBuilder someDate = new StringBuilder().append(day).append("/").append(month+1).append("/").append(year);
            String aDate = String.valueOf(someDate);
            Log.d ("anotherDate", aDate);
            AddDetails.setDateSelected(aDate); //sets the value of dateSelected

            Log.d("buttonusedfinal", buttonUsed);
            if (buttonUsed != null) {
                if (buttonUsed.equals("startDateButton")) {
                    startDateTextView.setText(dateSelected);
                    endDateTextView.setText(dateSelected);
                }
                if (buttonUsed.equals("endDateButton")) {
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
                }
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
                try {
                    detailsMetricSaveFile();
                }catch (IOException e) {
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
    public class addDetailsBundle{
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
            bundlePrevLayerTitle = bundlePrevLayerTitle.replace("D1Q0jyf6fJ","");
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

    public void arrayListChecker(){
        int size = details.size();
        for(int i=0; i<size; i++){
            String arrayVal = details.get(i);
            Log.d("AddDetailArrayVal", arrayVal);
        }

    }
    public void getDetails(){// gets all of the details and adds it to an array list to go into a file
        details.clear();
        int addDetailsArraySize = details.size();
        Log.d("addDetailsArraySize", String.valueOf(addDetailsArraySize));
        Log.d("^^size should be", "0 here^^");

        String title = String.valueOf(titleTextView.getText());
        Log.d("AddDTitle", title);
        details.add(title);
        String sDate = String.valueOf(startDateTextView.getText());
        Log.d("AddDSDate", sDate);
        details.add(sDate);
        String eDate = String.valueOf(endDateTextView.getText());
        Log.d("AddDEDate", eDate );
        details.add(eDate);
        String time = String.valueOf(timeTextView.getText());
        Log.d("AddDTime", time);
        details.add(time);
        String editTextDetails = String.valueOf(detailsEditText.getText());
        Log.d("editTextDetails", editTextDetails);
        details.add(editTextDetails);
    }

    public void metricsNullValCheck() {// does not appear like this is needed...
        int metricEdValSize = metricEditTextValues.size();
        String metricEdVals;
        for (int i = 0; i < metricEdValSize; i++) { // loop for metrics values... probably need a check on Name being null...
            metricEdVals = metricEditTextValues.get(i);
            if (metricEdVals.length() == 0) break;
            {
                //build an alert dialog to warn the user
                AlertDialog.Builder builder = new AlertDialog.Builder(AddDetails.this);
                builder.setTitle("Warning:");
                builder.setCancelable(false);
                builder.setMessage("One of the metrics name, amount or complete does not have a value\n\nPlease enter a value for these");
                builder.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogue, int id) {
                        //Closes the Alert Dialog
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    public void detailsSaveFile() throws IOException {
        getFileName();
        getDetails();
        //loop and edit Text details will be needed to be added to the details file add...
        editTextValuesToArray(); //calls the method to add all of the string values to the savefile array
        String edVal;
        metricEditTextToArray();
        String saveFileName = getFileName()+"D1Q0jyf6fJ";
        Log.d("detailsSaveFN", saveFileName);
        detailsFile = new File(this.getFilesDir(), saveFileName);   //Creates file with the previous plan name and layer
        FileWriter writer = new FileWriter(detailsFile, true);
        int size = details.size(); // calling an array....
        for (int i = 0; i < size; i++) { // adding the main details to the array
            String str = details.get(i);
            Log.d("saveFile Output", str);
            writer.write(str + "\n");
        }
        int edValSize = editTextValues.size();
        for (int i = 0; i<edValSize; i++){ // adding the checklist details to the array
            edVal = editTextValues.get(i);
            Log.d("editTextVal", edVal);
            writer.write(edVal + "\n");
        }
        writer.close();

    }

    public void detailsMetricSaveFile() throws IOException {
        getFileName();
        String metricEdVals;
        metricEditTextToArray();
        String saveFileName = getFileName() + "Metrics" + "D1Q0jyf6fJ";
        Log.d("detailsSaveFN", saveFileName);
        detailsFile = new File(this.getFilesDir(), saveFileName);   //Creates file with the previous plan name and layer
        FileWriter writer = new FileWriter(detailsFile, true);
        int metricEdValSize = metricEditTextValues.size();
        for (int i = 0; i < metricEdValSize; i++) { // loop for metrics values... probably need a check on Name being null...
            metricEdVals = metricEditTextValues.get(i);
            Log.d("metricEdVals", metricEdVals);
            writer.write(metricEdVals + "\n");
        }
        writer.close();
    }
}
