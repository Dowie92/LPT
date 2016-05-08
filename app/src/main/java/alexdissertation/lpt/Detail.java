package alexdissertation.lpt;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.StreamHandler;

public class Detail extends AppCompatActivity {
    private String loadFileName;
    private static ArrayList <String> detailContent = new ArrayList<String>();

    private static TextView titleTextView;
    private static TextView startDateTextView;
    private static TextView endDateTextView;
    private static TextView timeTextView;
    private static EditText detailsEditText;

    private static EditText checkBoxUserInput;
    private static CheckBox firstCheckbox;
    private static int checkListsize = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        detailContent.clear();
        int i = detailContent.size();

        Log.d("detailContentSize",String.valueOf(i));
        Log.d("^^ Detail Contsize","Should be 0 here ^^");

        loadDetailsFile();
        arrayListChecker();
        populateDetails();

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

    }

}
