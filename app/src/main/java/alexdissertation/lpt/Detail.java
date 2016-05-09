package alexdissertation.lpt;

import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

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
    private static ArrayList<String> metricContent = new ArrayList<String>();

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

        final Button metricsButton = (Button)findViewById(R.id.metricsAdd);
        if (metricsButton != null) {
            metricsButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // add in a new metric...
                    addMetric();
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

    public void addMetric(){
        final LinearLayout metricLayout = (LinearLayout)findViewById(R.id.metricsLinear); //gets the metric LL
        String presetNumber = "0";
        final LinearLayout linearLayout1 = new LinearLayout(Detail.this); // Creates the new LL - for title
        linearLayout1.setId(R.id.metricLinearLayout); // might be able to use this to loop through all items to get their values...
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


        if (metricLayout != null) { // as long as ML exists add a new layout
            metricLayout.addView(linearLayout1);
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
            metricLayout.addView(linearLayout2);
        }

        String amountTextViewText = "Amount";
        final TextView amountTextView= new TextView(Detail.this);
        amountTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        amountTextView.setGravity(Gravity.BOTTOM);
        amountTextView.setPadding(30,0,200,0);
        amountTextView.setText(amountTextViewText);

        final EditText metricsFirstNumber = new EditText(Detail.this);
        metricsFirstNumber.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        metricsFirstNumber.setGravity(Gravity.BOTTOM);
        metricsFirstNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        metricsFirstNumber.setImeOptions(EditorInfo.IME_ACTION_DONE);
        metricsFirstNumber.setText(presetNumber);

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
            metricLayout.addView(linearLayout3);
        }

        String completeTextText = "Complete";
        TextView completeText = new TextView(Detail.this);
        completeText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        completeText.setGravity(Gravity.BOTTOM);
        completeText.setPadding(30,0,200,0);
        completeText.setText(completeTextText);


        final EditText metricsCompleteNumber = new EditText(Detail.this);
        metricsCompleteNumber.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        metricsCompleteNumber.setGravity(Gravity.BOTTOM);
        metricsCompleteNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        metricsCompleteNumber.setImeOptions(EditorInfo.IME_ACTION_DONE);
        metricsCompleteNumber.setText(presetNumber);

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
            metricLayout.addView(linearLayout4);
        }


        final TextView finalStatText = new TextView(Detail.this);
        finalStatText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        finalStatText.setGravity(Gravity.BOTTOM);
        finalStatText.setPadding(30,80,50,100);
        finalStatText.setVisibility(View.GONE);


        linearLayout4.addView(finalStatText);

        //Stat analysis... get the % complete
        //calculation on the done click when the user has input from the second number
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
                        Log.d("number1Val", String.valueOf(number1));
                        Log.d("number2Val", String.valueOf(number2));

                        if (number2 > number1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);
                            builder.setTitle("Number Larger");
                            builder.setMessage("The number you have given for Complete is larger than the value Needed.\n" + "Keep Value?");
                            builder.setPositiveButton("Keep Value", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    double percentage = (number2 / number1) * 100;
                                    String percentageFormat = String.format("%.2f", percentage);
                                    Log.d("percentage complete", String.valueOf(percentageFormat) + "%");
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
                                    Toast.makeText(Detail.this, "The complete number has been cleared\nPlease enter another number!", Toast.LENGTH_LONG).show();
                                }
                            });
                            builder.show();

                        } else {
                            double percentage = (number2 / number1) * 100;
                            String percentageFormat = String.format("%.2f", percentage);
                            Log.d("percentage complete", String.valueOf(percentageFormat) + "%");
                            String finalStatTextText = "You have completed " + percentageFormat + "% of your Plan/Subtask";
                            finalStatText.setText(finalStatTextText);
                            finalStatText.setVisibility(View.VISIBLE);
                        }
                    }

                }
                return false;
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.GONE);
                linearLayout3.setVisibility(View.GONE);
                linearLayout4.setVisibility(View.GONE);

            }
        });

    }public void addMetric(String name, String amount, String complete){
        final LinearLayout metricLayout = (LinearLayout)findViewById(R.id.metricsLinear); //gets the metric LL
        final LinearLayout linearLayout1 = new LinearLayout(Detail.this); // Creates the new LL - for title
        linearLayout1.setId(R.id.metricLinearLayout); // might be able to use this to loop through all items to get their values...
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


        if (metricLayout != null) { // as long as ML exists add a new layout
            metricLayout.addView(linearLayout1);
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
            metricLayout.addView(linearLayout2);
        }

        String amountTextViewText = "Amount";
        final TextView amountTextView= new TextView(Detail.this);
        amountTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        amountTextView.setGravity(Gravity.BOTTOM);
        amountTextView.setPadding(30,0,200,0);
        amountTextView.setText(amountTextViewText);

        final EditText metricsFirstNumber = new EditText(Detail.this);
        metricsFirstNumber.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        metricsFirstNumber.setGravity(Gravity.BOTTOM);
        metricsFirstNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
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
            metricLayout.addView(linearLayout3);
        }

        String completeTextText = "Complete";
        TextView completeText = new TextView(Detail.this);
        completeText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        completeText.setGravity(Gravity.BOTTOM);
        completeText.setPadding(30,0,200,0);
        completeText.setText(completeTextText);


        final EditText metricsCompleteNumber = new EditText(Detail.this);
        metricsCompleteNumber.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        metricsCompleteNumber.setGravity(Gravity.BOTTOM);
        metricsCompleteNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
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
            metricLayout.addView(linearLayout4);
        }


        final TextView finalStatText = new TextView(Detail.this);
        finalStatText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        finalStatText.setGravity(Gravity.BOTTOM);
        finalStatText.setPadding(30,80,50,100);
        finalStatText.setVisibility(View.GONE);


        linearLayout4.addView(finalStatText);

        //Stat analysis... get the % complete
        //calculation on the done click when the user has input from the second number
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
                        Log.d("number1Val", String.valueOf(number1));
                        Log.d("number2Val", String.valueOf(number2));

                        if (number2 > number1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);
                            builder.setTitle("Number Larger");
                            builder.setMessage("The number you have given for Complete is larger than the value Needed.\n" + "Keep Value?");
                            builder.setPositiveButton("Keep Value", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    double percentage = (number2 / number1) * 100;
                                    String percentageFormat = String.format("%.2f", percentage);
                                    Log.d("percentage complete", String.valueOf(percentageFormat) + "%");
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
                                    Toast.makeText(Detail.this, "The complete number has been cleared\nPlease enter another number!", Toast.LENGTH_LONG).show();
                                }
                            });
                            builder.show();

                        } else {
                            double percentage = (number2 / number1) * 100;
                            String percentageFormat = String.format("%.2f", percentage);
                            Log.d("percentage complete", String.valueOf(percentageFormat) + "%");
                            String finalStatTextText = "You have completed " + percentageFormat + "% of your Plan/Subtask";
                            finalStatText.setText(finalStatTextText);
                            finalStatText.setVisibility(View.VISIBLE);
                        }
                    }

                }
                return false;
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        }

    }

}
