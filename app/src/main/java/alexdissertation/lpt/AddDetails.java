package alexdissertation.lpt;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import android.widget.TextView;
import android.widget.TimePicker;
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
    //private String endDate; probs not needed
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
    private ArrayList<EditText> allEditText = new ArrayList<EditText>();
    private ArrayList<String> editTextValues = new ArrayList<String >();

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
                    //do nothing
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
    public void editTextValuesToArray(){
        int size = allEditText.size();
        String edVal = null;
        for (int i = 0; i<size; i++){
            editTextValues.add(String.valueOf(allEditText.get(i).getText()));
        }

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
        allEditText.add(editText);



        final Button deleteButton = new Button(AddDetails.this);
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

    public void showDatePickerDialog() {
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
    public void detailsSaveFile() throws IOException {
        getFileName();
        getDetails();
        //loop and edit Text details will be needed to be added to the details file add...
        editTextValuesToArray();
        String edVal;
        int edValSize = editTextValues.size();
        for (int i = 0; i<edValSize; i++){
            edVal = editTextValues.get(i);
            Log.d("editTextVal", edVal);
        }

        String saveFileName = getFileName()+"D1Q0jyf6fJ";
        Log.d("detailsSaveFN", saveFileName);
        detailsFile = new File(this.getFilesDir(), saveFileName);   //Creates file with the previous plan name and layer
        FileWriter writer = new FileWriter(detailsFile, true);
        int size = details.size(); // calling an array....


        //Log.d("Save file arraySize", String.valueOf(size));
        for (int i = 0; i < size; i++) { // adding the array to the file
            String str = details.get(i);
            Log.d("saveFile Output", str);
            writer.write(str + "\n");
        }
        writer.close();

    }

}
