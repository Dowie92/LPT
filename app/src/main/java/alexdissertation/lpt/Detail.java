package alexdissertation.lpt;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        titleTextView = (TextView)findViewById(R.id.Title);
        if (titleTextView != null) {
            titleTextView.setText(detailContent.get(0));
        }

        startDateTextView = (TextView)findViewById(R.id.startDate);
        if (startDateTextView !=null){
            startDateTextView.setText(detailContent.get(1));
        }
        endDateTextView = (TextView)findViewById(R.id.endDate);
        if (endDateTextView !=null){
            endDateTextView.setText(detailContent.get(2));
        }
        timeTextView = (TextView)findViewById(R.id.timeText);
        if (timeTextView !=null){
            timeTextView.setText(detailContent.get(3));
        }

    }

    public String getFileName(){
        LayerTitles fileName = new LayerTitles();
        String fileNameString;
        String Tester;
        fileName.setTitleFullConcat();//runs LT class to set the filename
        Tester = LayerTitles.getFinalFileConcat(); // 1st run of hometconc1... correctly works..
        Log.d ("filenameTester", Tester);
        fileNameString = LayerTitles.getFinalFileConcat()+"details0";
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
