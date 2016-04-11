package alexdissertation.lpt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private ArrayList <String> listViewItems;
    private ArrayAdapter <String> arrayAdapt;
    private ListView listView;
    private String editTextString;
    private File titlesSaveFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, AddPlanActivity.class);
                startActivityForResult(intent, 1);
                            }

        });

        listView = (ListView) findViewById(R.id.ListView);
        //Array of titles
        listViewItems = new ArrayList<String>();
        arrayAdapt = new titleScreenAdapter(this, R.layout.homescreenlayout, listViewItems);
        listView.setAdapter(arrayAdapt);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Home.this, "List item clicked at " + position, Toast.LENGTH_SHORT).show();
            }
        });

        //clickCallBack();
        loadFile();
    }

    //Screen adapter to build the listviewscreen
    private class titleScreenAdapter extends ArrayAdapter<String>{
        private int layout;
        public titleScreenAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            titleViewLayoutRef mainViewHolder = null;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                titleViewLayoutRef titleViewLayoutRef = new titleViewLayoutRef();
                titleViewLayoutRef.Title = (TextView) convertView.findViewById(R.id.titleTextView);
                titleViewLayoutRef.Save = (TextView) convertView.findViewById(R.id.Save);
                convertView.setTag(titleViewLayoutRef);
            }
            mainViewHolder = (titleViewLayoutRef) convertView.getTag();
            mainViewHolder.Save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            saveFile();
                            Toast.makeText(getContext(), "File Saved", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "File not found", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            mainViewHolder.Title.setText(getItem(position));
            return convertView;
        }
    }

    //class for the references of the homescreen layout
    public class titleViewLayoutRef {
        TextView Title;
        TextView Save;
    }

    //Gets the Data from the second Activity Data for the title
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data){
   super.onActivityResult(requestCode, resultCode, data);
       switch(requestCode){
           case 1:
       }
       //gets the data from title activity
       editTextString = data.getStringExtra("editText");
       //adds data to the array to go into the listview
       listViewItems.add(editTextString);
       //updates the listView
       arrayAdapt.notifyDataSetChanged();
   }

    /*
    // onclick event for the listview items to give user feedback
    private void clickCallBack() {
        ListView list = (ListView) findViewById(R.id.ListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> paret, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                String message = "You Clicked #" + position + " Which is string " + textView.getText().toString();
                Toast.makeText(Home.this, message, Toast.LENGTH_LONG).show();
            }
        });
    } */
    //deletes entire titles file
    public void titlesFileDelete (){
        File deleteFile = new File(this.getFilesDir(), "TitlesFile");
        if (!deleteFile.exists()){
            //Log to give feedback on if the file exists....
            Log.d("titlesfiledelete", "file does not exist ");
        }
        else {
            //Deleting the file
           boolean DTF = deleteFile.delete();
            //Feedback file has been deleted.
            Log.d("titlesFileDelete", "File Deleted");
        }
    }
    //Save Titles file code
    public void saveFile () throws IOException {
        titlesFileDelete();
        titlesSaveFile = new File(this.getFilesDir(), "TitlesFile");
        Log.d("file location", String.valueOf(titlesSaveFile));
        FileWriter writer = new FileWriter(titlesSaveFile, true);
        int size = listViewItems.size();
        Log.d("System Out:Size", String.valueOf(size));
        for (int i = 0; i < size; i++) {
            String str = listViewItems.get(i);
            Log.d("System Out:Str", str);
            writer.write(str + "\n");
        }
        writer.close();


    }
    //Load file code
    public void loadFile() {
        File loadFile = getFileStreamPath("TitlesFile");
        if (loadFile.exists()){
            Log.d("System Out:LoadFile", "loadfile does exist!");
            try {
                FileInputStream fileInputStream = new FileInputStream(loadFile);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                String message;
                while ((message = bufferedReader.readLine()) !=null){
                    Log.d("System Out:Str", message);
                    listViewItems.add(message);
                    arrayAdapt.notifyDataSetChanged();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }


}
