package alexdissertation.lpt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailsAndSubLayer extends AppCompatActivity {
    private ArrayList <String> subTaskTitle;
    private ArrayAdapter <String> arrayAdapter;
    private ListView listView;
    private File subTaskFile;
    private File Layer1Details;// for later...


    private LayerTitles subSaveFileDetail;
    private LayerTitles fileName;
    private String title; // need to keep as its referenced in bundle (Sort out later..)
    public int titleLayer;
    private int subTLay = 2;
    private String subTTitle;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_and_sub_layer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fileChecker();

        listView = (ListView) findViewById(R.id.subTaskListView);
        subTaskTitle = new ArrayList <String>();
        arrayAdapter = new subTaskLayoutAdapter(this, R.layout.subtasklayout,subTaskTitle);
        listView.setAdapter(arrayAdapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DetailsAndSubLayer.class);
                //String subTTitle;
                int titlePos;
                titlePos = position;
                subTTitle = subTaskTitle.get(position);
                new bundle().bundleSubTLay(); //Run the check on subTlay value
                intent.putExtra("titleLayer", subTLay); //will need to keep for layer comparison method... (for now)

                // savefile details Class Version
                subSaveFileDetail = new LayerTitles();// Creates the new object
                LayerTitles.setSubTitle(subTTitle);//Sends the sub title details to the layer title class
                LayerTitles.setLayer(String.valueOf(subTLay)); //Sends the Layer to the other class
                LayerTitles.setPosition(String.valueOf(titlePos)); // sends the position of the item in the arraylist to the save file class
                subSaveFileDetail.addSubToArray();
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "List item clicked at " + position, Toast.LENGTH_SHORT).show();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    // need to add in an adapter view
                    final EditText editText = new EditText(DetailsAndSubLayer.this);
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                    editText.setHint("Subtask 1");

                    //Alert Diag (Adding subtask or details?)
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailsAndSubLayer.this);
                    builder.setTitle("Add details or Subtask?");
                    builder.setPositiveButton("Add Details", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialogue, int id){
                            Intent intent = new Intent(DetailsAndSubLayer.this, AddDetails.class);
                            intent.putExtra("subTitle", subTTitle);// add in the title details for title....
                            startActivity(intent);// will need to start activity for result....
                            Snackbar.make(view,"Something needs to be added....", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Add SubTask", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialogue, int id){
                            //Alert Diag 2 (Adding Title)
                            AlertDialog.Builder builder = new AlertDialog.Builder(DetailsAndSubLayer.this);
                            builder.setView(editText);
                            builder.setTitle("Subtask title");
                            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialogue, int id){
                                    String editTextValue = editText.getText().toString();
                                    subTaskTitle.add(editTextValue);
                                    arrayAdapter.notifyDataSetChanged();
                                    try {
                                        saveSubTitleFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialogue, int id){
                                    //User Clicked Cancel
                                }
                            });
                            AlertDialog subTTitledialogue = builder.create();
                            subTTitledialogue.show();
                                                }
                    });
                    AlertDialog dialogue = builder.create();
                    dialogue.show();
                    }
            });
        }
        loadSubTitleFile();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo MenuInfo){
        menu.add(0, v.getId(), 0, "Edit");
        menu.add(0, v.getId(), 0, "Delete");
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) MenuInfo;
        menu.setHeaderTitle(subTaskTitle.get(info.position));
    }
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final int i = info.position;
        final EditText editText = new EditText(this);
        if(item.getTitle()=="Edit"){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(editText);
            builder.setTitle("Edit Plan title");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialogue, int id){
                    //User Clicked on Confirm button
                    String editTextValue = editText.getText().toString();
                    subTaskTitle.remove(i);
                    subTaskTitle.add(i,editTextValue);
                    arrayAdapter.notifyDataSetChanged();
                    try {
                        saveSubTitleFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // needs a file delete and lower file delete method to get rid of the files below what is being deleted
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialogue, int id){
                    //User Clicked Cancel
                }
            });
            AlertDialog dialogue = builder.create();
            dialogue.show();
            Toast.makeText(getApplicationContext(), "Edit Clicked", Toast.LENGTH_SHORT).show();
        }
        if(item.getTitle()=="Delete"){
            //could be recoded to own class
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please confirm deleting this plan");
            builder.setTitle("Delete Plan");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialogue, int id) {
                    //User Clicked on the Confirm button
                    // Remove the listview item
                    subTaskTitle.remove(i);
                    arrayAdapter.notifyDataSetChanged();
                    try {
                        saveSubTitleFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(),"Item deleted",Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //User Cancelled the Dialogue
                }
            });
            AlertDialog dialogue = builder.create();
            dialogue.show();
        }
        return  true;

    }
    public class subTaskLayoutAdapter extends ArrayAdapter<String>{
        private int layout;
        public subTaskLayoutAdapter(Context context, int resource, List<String> objects){
        super (context, resource, objects);
        layout = resource;
    }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            subTaskLayoutRef mainViewHolder;
            if (convertView == null){
                LayoutInflater inflater = LayoutInflater.from(DetailsAndSubLayer.this);
                convertView = inflater.inflate(layout, parent, false);
                subTaskLayoutRef subTaskLayoutRef = new subTaskLayoutRef();
                subTaskLayoutRef.subTitle = (TextView) convertView.findViewById(R.id.subTitle);
                subTaskLayoutRef.subDetail = (TextView) convertView.findViewById(R.id.subDetail);
                convertView.setTag(subTaskLayoutRef);
            }
            mainViewHolder = (subTaskLayoutRef) convertView.getTag();
            mainViewHolder.subTitle.setText(getItem(position));
            //toolbar implementation need changing to allow for proper use...
            /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null) {
                toolbar.setTitle(getFileName());
            }*/

            return convertView;
            }

    }
    //Class to reference the subtask layout file.
    public class subTaskLayoutRef{
        TextView subTitle;
        TextView subDetail;
    }

    //Gets the layer from the previous levels to allow for filename key generation...
    public class bundle {
        public void getBundle() {
            title=null;
            Bundle extras = getIntent().getExtras();
            if(extras!=null){
                titleLayer = extras.getInt("titleLayer"); // gets the layer from the previous layer
            }
        }
        public int bundleSubTLay(){
            getBundle();
            if (titleLayer == subTLay){
                subTLay = subTLay+1;
            }
            return subTLay;
        }
    }
    public void fileChecker(){
        String path = String.valueOf(this.getFilesDir());
        File file = new File(path);
        File[] files = file.listFiles();
        String fileName;
        fileName = Arrays.toString(files);
        fileName.replace(String.valueOf(getFilesDir()),"");
        Log.d("FileList", fileName);
    }

    //deletes entire titles file
    public void fileDelete (){
        String deleteFileName = getFileName();
        Log.d("DeleteFile", deleteFileName);
        File deleteFile = new File(this.getFilesDir(),File.separator + deleteFileName);// need a file to give the reader....
        Log.d("delete: deletefile..", String.valueOf(deleteFile));
        if (!deleteFile.exists()){
            //Log to give feedback on if the file exists....
            Log.d("Delete", "file does not exist ");
        }
        else {
            //Deleting the file
            boolean DTF = deleteFile.delete();
            //Feedback file has been deleted.
            Log.d("Delete", "File Deleted");
        }
    }

    public String getFileName(){ //returns the File name from LT method...
        fileName = new LayerTitles();
        String fileNameString;
        String Tester;
        fileName.setTitleFullConcat();
        Tester = LayerTitles.getFinalFileConcat(); // 1st run of hometconc1... correctly works..
        Log.d ("filenameTester", Tester);
        fileNameString = LayerTitles.getFinalFileConcat();
        return fileNameString;
    }

    @Override
    public void onBackPressed(){
        finish();
        fileName = new LayerTitles();
        fileName.arrayDeleteLast();
    }


    //Save subtaskfileTitles for the subtask titles
    public void saveSubTitleFile () throws IOException {
        fileDelete();
        String saveFileName = getFileName();
        Log.d("SaveFileName", saveFileName);
        //saveFileName = getBackButtonFileName();
        //Log.d("SaveFileNameChecker", saveFileName);

        subTaskFile = new File(this.getFilesDir(), saveFileName);   //Creates file with the previous plan name and layer
        FileWriter writer = new FileWriter(subTaskFile, true);
        int size = subTaskTitle.size();
        Log.d("Save file arraySize", String.valueOf(size));
        for (int i = 0; i < size; i++) {
            String str = subTaskTitle.get(i);
            Log.d("saveFile Output", str);
            writer.write(str + "\n");
        }
        writer.close();
    }

    //Load file code
    public void loadSubTitleFile() {
        String loadFileName = getFileName();
        Log.d("LoadFileName", loadFileName);

        File loadFile = DetailsAndSubLayer.this.getFileStreamPath(loadFileName); //loadFileName
        if (loadFile.exists()){
            Log.d("System Out:LoadFile", "loadfile does exist!");
            try {
                FileInputStream fileInputStream = new FileInputStream(loadFile);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                String message;
                while ((message = bufferedReader.readLine()) !=null){
                    subTaskTitle.add(message);
                    arrayAdapter.notifyDataSetChanged();
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
