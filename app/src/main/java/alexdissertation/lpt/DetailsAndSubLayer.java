package alexdissertation.lpt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DetailsAndSubLayer extends AppCompatActivity {
    private ArrayList <String> subTaskTitle;
    private ArrayAdapter <String> arrayAdapter;

    private ListView listView;

    private String loadFileName;

    private File subTaskFile;

    private LayerTitles subSaveFileDetail;
    private LayerTitles fileName;
    private String title; // need to keep as its referenced in bundle (Sort out later..)
    public int homeTitleLayer;
    private static int subTLay = 2;
    private String subTTitle;
    private String bundleHomeTitle;
    private boolean notificationSetting;
    static final int addDetailback = 1;

    private static boolean addBackUsed;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_and_sub_layer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        listView = (ListView) findViewById(R.id.subTaskListView);
        subTaskTitle = new ArrayList <String>();
        arrayAdapter = new subTaskLayoutAdapter(this, R.layout.subtasklayout,subTaskTitle);
        listView.setAdapter(arrayAdapter);


        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int titlePos;
                titlePos = position;
                subTTitle = subTaskTitle.get(position);

                if (subTaskTitle.get(position).contains("D1Q0jyf6fJ")){
                    //load the details activity...
                    Intent intentDetails = new Intent(getApplicationContext(), Detail.class);
                    new bundle().getBundleSubTTitle();
                    String getPath = getFileName(); //gets the path name
                    Log.d("onClickPathName", getPath);

                    intentDetails.putExtra("lastTitle", subTTitle);



                    startActivity(intentDetails);


                }
                else{
                    Intent intentDASL = new Intent(getApplicationContext(), DetailsAndSubLayer.class);
                    new bundle().bundleSubTLay(); //Run the check on subTlay value
                    intentDASL.putExtra("subTTitle", subTTitle); //To pass the subtitle value onto the add details class for the title + details...
                    intentDASL.putExtra("titleLayer", subTLay); //will need to keep for layer comparison method... (for now)
                    fileChecker(); // here just to show a list of files
                    // savefile details Class Version
                    subSaveFileDetail = new LayerTitles();// Creates the new object
                    LayerTitles.setSubTitle(subTTitle);//Sends the sub title details to the layer title class
                    LayerTitles.setLayer(String.valueOf(subTLay)); //Sends the Layer to the other class
                    LayerTitles.setPosition(String.valueOf(titlePos)); // sends the position of the item in the arraylist to the save file class
                    Log.d("onClick layerVal", String.valueOf(subTLay));
                    Log.d("onClick ArraySize", String.valueOf(LayerTitles.getArraySize()));
                    subSaveFileDetail.addSubToArray();
                    startActivity(intentDASL);
                    DetailsAndSubLayer.backPressedTwice = false;
                    DetailsAndSubLayer.subLayBack = false;
                    Toast.makeText(getApplicationContext(), "List item clicked at " + position, Toast.LENGTH_SHORT).show();
                }
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
                            // check needed to only allow for 1 per layer
                            int size =  subTaskTitle.size();
                            boolean contains = false;
                            for (int i = 0; i <size; i++){
                                String containsCheck;
                                containsCheck = subTaskTitle.get(i);
                                Log.d("Contains Check", containsCheck);
                                if (containsCheck.contains("D1Q0jyf6fJ")){
                                    contains = true;
                                }
                            }
                            Log.d("ContainsVal", String.valueOf(contains));

                            if (!contains){
                                // code to add the details...

                                Intent intent = new Intent(DetailsAndSubLayer.this, AddDetails.class);
                                intent.putExtra("subTitle", subTTitle);// add in the title details for title....
                                String detailsTitle = new bundle().getBundleSubTTitle();

                                if (detailsTitle == null){
                                    detailsTitle = new bundle().getBundleHomeTitle();
                                }

                                detailsTitle = detailsTitle+"details"+"D1Q0jyf6fJ";
                                //String detailsTitleSubString  = detailsTitle.replace("detailsD1Q0jyf6fJ"," Details");
                                subTaskTitle.add(detailsTitle);
                                arrayAdapter.notifyDataSetChanged();
                                try {
                                    saveSubTitleFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                contains = false;
                                Snackbar.make(view,"SnackbarExample", Snackbar.LENGTH_SHORT).show();
                                intent.putExtra("detailsTitle", detailsTitle);
                                startActivityForResult(intent, addDetailback);// will need to start activity for result....
                            }
                            else {
                                //Create alert Dialog to give message that the layer already contains a detail section...
                                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsAndSubLayer.this);
                                builder.setTitle("Warning:");
                                builder.setCancelable(false);
                                builder.setMessage("This layer contains a detail section. There can only be one detail section per layer");
                                builder.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogue, int id) {
                                        //Closes the Alert Dialog
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
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
    } // onCreate Method...

    @Override
    public void onActivityResult(int addDetailback, int resultCode, Intent data){
        try {
            super.onActivityResult(addDetailback, resultCode, data);
            if(addDetailback == 1){
                if (resultCode == AddDetails.RESULT_OK){
                    DetailsAndSubLayer.addBackUsed = data.getBooleanExtra("backPressedTrue", false);
                    Log.d ("addBackUsed Val", String.valueOf(DetailsAndSubLayer.addBackUsed));
                    if (DetailsAndSubLayer.addBackUsed){
                        deleteLastArrayPos();
                    }
                }
            }

        }catch (Exception ex){
            Toast.makeText(DetailsAndSubLayer.this, "nothing sent back", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteLastArrayPos(){
        int i = subTaskTitle.size();
        Log.d("AddDBack subT size", String.valueOf(i));
        subTaskTitle.remove(i-1);
        arrayAdapter.notifyDataSetChanged();
        try {
            saveSubTitleFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo MenuInfo){
        menu.add(0, v.getId(), 0, "Edit");
        menu.add(0, v.getId(), 0, "Delete");
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) MenuInfo;
        menu.setHeaderTitle("Edit or Delete"+" "+subTaskTitle.get(info.position));
    }
    public boolean onContextItemSelected(MenuItem item){
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final int i = info.position;
        final EditText editText = new EditText(this);
        if(item.getTitle()=="Edit"){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(editText);
            builder.setTitle("Edit Plan title");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialogue, int id){
                    fileChecker(); // to check the files before the change in a log

                    String editTextValue = editText.getText().toString(); // gets the new file Name
                    //User Clicked on Confirm button
                    // need to get the value of the item that the change is wanted...
                    String layer = LayerTitles.getLayer();
                    if (LayerTitles.getLayer() == null){
                        layer = String.valueOf(subTLay);
                        Log.d("DTSL-deleteListLayer", layer);
                    }
                    String getPath = getFileName();
                    Log.d("getPath", getPath);
                    String title = subTaskTitle.get(i);
                    Log.d("title", title);
                    if(title.contains("D1Q0jyf6fJ")){
                        // old values
                        String detailOldFileName = getPath+"detailsD1Q0jyf6fJ";
                        String detailToReplacelString = title+layer+i; // gets the title as it is in the fileName
                        int detailToReplaceInt = detailToReplacelString.length();// gets the length of this string
                        int detailOldFileNameLenth = detailOldFileName.length();
                        String removedTitle = detailOldFileName.substring(0,detailOldFileNameLenth-detailToReplaceInt);
                        String newDetailFileName = removedTitle+editTextValue+layer+i+"detailsD1Q0jyf6fJ"; // adds the new details to the end
                        cascadeFileRename(detailOldFileName,newDetailFileName);
                        subTaskTitle.remove(i);
                        subTaskTitle.add(i,editTextValue+layer+i+"detailsD1Q0jyf6fJ");
                        arrayAdapter.notifyDataSetChanged();

                        String metricOldFileName = getPath+"detailsMetricsD1Q0jyf6fJ";
                        String metricToReplacelString = title+layer+i; // gets the title as it is in the fileName
                        int metricToReplaceInt = metricToReplacelString.length();// gets the length of this string
                        int metricOldFileNameLenth = metricOldFileName.length();
                        String metricRemovedTitle = metricOldFileName.substring(0,metricOldFileNameLenth-metricToReplaceInt);
                        String newDetailMetricFileName = metricRemovedTitle+editTextValue+layer+i+"detailsMetricsD1Q0jyf6fJ"; // adds the new details to the end
                        cascadeFileRename(metricOldFileName,newDetailMetricFileName);

                    }
                    else{
                        title = getPath + title + layer + i;
                        String newName = getPath+editTextValue+layer+i;
                        cascadeFileRename(title, newName);
                        subTaskTitle.remove(i);
                        subTaskTitle.add(i,editTextValue);
                        arrayAdapter.notifyDataSetChanged();
                    }

                    try {
                        saveSubTitleFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fileChecker();
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
            builder.setMessage("Please confirm deleting this plan"+"\n\n"+"This will delete any subtasks and details that are under this");
            builder.setTitle("Delete Plan");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialogue, int id) {//User Clicked on the Confirm button
                    String layer = LayerTitles.getLayer();
                    if (LayerTitles.getLayer() == null){
                        layer = String.valueOf(subTLay);
                        Log.d("DTSL-deleteListLayer", layer);
                    }
                    String getPath = getFileName();
                    Log.d("getPath", getPath);
                    String title = subTaskTitle.get(i);
                    String metricDeleteFile = null;
                    if (title.contains("D1Q0jyf6fJ")){
                        title = getPath+"detailsD1Q0jyf6fJ";
                        metricDeleteFile = getPath+"detailsMetricsD1Q0jyf6fJ";
                        deleteFile(metricDeleteFile);
                    }
                    else {
                        title = getPath + title + layer + i;
                        Log.d("DeleteTitle", title);

                    }
                    //needs a check for deleting the metrics file too...

                    Log.d("DeleteTitle", title);
                    cascadeFileDelete(title);//runs the filechecker with the title for the files that it wants to delete

                    ; // need a check to ensure that this exists...

                    fileChecker();
                    subTaskTitle.remove(i); // Remove the listview item
                    arrayAdapter.notifyDataSetChanged();//updates the listview
                    for (String newPositionName: subTaskTitle){ //gets the items left in the arraylist
                        //creates the old position Name and the new position name
                        String oldPositionName;
                        int lastPos = subTaskTitle.indexOf(newPositionName);
                        oldPositionName = newPositionName+layer+(lastPos+1);
                        newPositionName = newPositionName+layer+lastPos;

                        cascadeFileRename(oldPositionName,newPositionName);

                    }
                    fileChecker();
                    try {
                        saveSubTitleFile(); //re-saves the file above to keep the change
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
            String listTitle = getItem(position);
            if (listTitle.contains("detailsD1Q0jyf6fJ")){
                listTitle= listTitle.replace("detailsD1Q0jyf6fJ"," Details"); //changes the list title to be a cleaner version of the title
            }
            mainViewHolder.subTitle.setText(listTitle);

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
        private String bundleSubTitle;
        public void getBundle() {
            title=null;

            Bundle extras = getIntent().getExtras();
            if(extras!=null){
                bundleSubTitle = extras.getString("subTTitle");
                homeTitleLayer = extras.getInt("titleLayer"); // gets the layer from the previous layer
                bundleHomeTitle = extras.getString("HomeTitle");

            }
        }
        public String getBundleSubTTitle(){
            getBundle();
            return bundleSubTitle;
        }
        public String getBundleHomeTitle(){
            getBundle();
            return bundleHomeTitle;
        }
        public int bundleSubTLay(){
            getBundle();
            if (homeTitleLayer == subTLay){
                subTLay = subTLay+1;
            }
            return subTLay;
        }
    }
    public void cascadeFileDelete(String t){// deleting all of the files that are linked to the item
        String path = String.valueOf(this.getFilesDir()); //The file Path
        final String fileContains; //Item being deleted
        fileContains = t;
        final File file = new File(path); // Creating object for files in the directory
        FilenameFilter fileFilter = new FilenameFilter(){ // filter to get only the files that are linked to the object
            @Override
            public boolean accept(File dir, String fn) {
                if (fn.contains(fileContains)){ //if it contains the item being deleted
                    return true;
                }
                return false;
            };

        };
        File[] files = file.listFiles(fileFilter); //Creates an arraylist of files that are connected to the item
        for (File file1 : files) { //Loop to delete the files
            Log.d("FileListToDelete", String.valueOf(file1).replace(String.valueOf(getFilesDir()), ""));
            fileDelete(file1); //deletes the files

        }

    }
    public void cascadeFileRename(String oldName, String newName){// for renaming the subtasks that are moved up - with the new ArrayPosition
        String path = String.valueOf(this.getFilesDir());
        final String fileContains= oldName;
        final String newFileContains = newName;
        String newFileName;
        File newFile;
        final File file = new File(path); // directory
        FilenameFilter fileFilter = new FilenameFilter(){
            @Override
            public boolean accept(File dir, String fn) {
                if (fn.contains(fileContains)){
                    return true;
                }
                return false;
            };

        };
        File[] files = file.listFiles(fileFilter);
        for (File file1 : files) {
            newFileName = String.valueOf(file1).replace(fileContains,newFileContains);
            newFile = new File(newFileName);
            file1.renameTo(newFile); // changes the filename..
        }

    }
    public void fileChecker(){
        String path = String.valueOf(this.getFilesDir());
        File file = new File(path);
        File[] files = file.listFiles();
        for (File file1 : files) {
            Log.d("FileCheckerFileList", String.valueOf(file1).replace(String.valueOf(getFilesDir()), ""));
        }

    }
    //deletes entire titles file
    public void fileDelete (){
        String deleteFileName = getFileName();
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
    public void fileDelete (File fileName){
        String deleteFileNameString = String.valueOf(fileName).replace(String.valueOf(getFilesDir()), "");
        File deleteFile = new File(this.getFilesDir(),File.separator + deleteFileNameString);// need a file to give the reader....
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
        fileName.setTitleFullConcat();
        fileNameString = LayerTitles.getFinalFileConcat();
        return fileNameString;
    }

    private static boolean backPressedTwice = false;
    private static boolean subLayBack = false;
    @Override
    public void onBackPressed(){
        finish(); //closes the activity...
        fileName = new LayerTitles();
        fileName.arrayDeleteLast(); // removes the last position in the name for the savefile when using back button
        String backLayerString;

        if (!subLayBack) { //TO allow for things to change in the second back press without effecting the first backpress...
            String subTLayBack = LayerTitles.getLayer();
            if (Integer.parseInt(subTLayBack) == 1); // if the value is 1 then do nothing to avoid an out of bounds exception
            else {
                DetailsAndSubLayer.subTLay = (Integer.parseInt(subTLayBack) - 1); //sets subTlay (this Class) to be the correct value...
                Log.d("BackSubTlay", String.valueOf(subTLay));
                subLayBack = true;
            }
        }

        if (backPressedTwice) {
            super.onBackPressed();
            backLayerString = LayerTitles.getLayer();
            int backLayerInt = Integer.parseInt(backLayerString); //cast the value to an int
            backLayerInt = backLayerInt -1; // changes the int value to be the correct layer
            LayerTitles.setLayer(String.valueOf(backLayerInt)); // resets it to a string and passes it back to the LayerTitles class

            if (subLayBack){
                String subTLayBack= LayerTitles.getLayer();;
                if (Integer.parseInt(subTLayBack) == 1);
                else{
                    DetailsAndSubLayer.subTLay = (Integer.parseInt(subTLayBack)-1);
                    Log.d("BackSubTlayTwice", String.valueOf(subTLay));
                }
            }
        }
        DetailsAndSubLayer.backPressedTwice = true;
    }

    //Save subtaskfileTitles for the subtask titles
    public void saveSubTitleFile () throws IOException {
        fileDelete();
        String saveFileName = getFileName();
        Log.d("Save Layer", LayerTitles.getLayer());
        Log.d("Save ArraySize", String.valueOf(LayerTitles.getArraySize()));

        subTaskFile = new File(this.getFilesDir(), saveFileName);   //Creates file with the previous plan name and layer
        FileWriter writer = new FileWriter(subTaskFile, true);
        int size = subTaskTitle.size();
        //Log.d("Save file arraySize", String.valueOf(size));
        for (int i = 0; i < size; i++) {
            String str = subTaskTitle.get(i);
            //Log.d("saveFile Output", str);
            writer.write(str + "\n");
        }
        writer.close();
    }

    //Load file code
    public void loadSubTitleFile() {
        loadFileName = getFileName();
        Log.d("DASL-LoadFileName", loadFileName);

        File loadFile = DetailsAndSubLayer.this.getFileStreamPath(loadFileName); //loadFileName
        if (loadFile.exists()){
            //Log.d("System Out:LoadFile", "loadfile does exist!");
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
            //.d("System Out:load", "Loadfile does not exist");
        }

    }

}
