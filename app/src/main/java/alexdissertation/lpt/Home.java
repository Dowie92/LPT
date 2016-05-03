package alexdissertation.lpt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
    private String titlesFile = "TitlesFile";
    private File titlesSaveFile;
    private LayerTitles saveFileDetail;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final EditText editText = new EditText(Home.this);
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                    editText.setHint("Plan1");
                    //Alert Diag 2 (Adding Title)
                    AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                    builder.setView(editText);
                    builder.setTitle("Add Plan?");
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogue, int id) {
                            String editTextValue = editText.getText().toString();
                            listViewItems.add(editTextValue);
                            arrayAdapt.notifyDataSetChanged();
                            try {
                                saveFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Snackbar.make(view, "Plan" + editTextValue + "Added", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogue, int id) {
                            Snackbar.make(view, "Cancelled", Snackbar.LENGTH_SHORT).show();
                            //User Clicked Cancel.. do nothing
                        }
                    });
                    AlertDialog subTTitledialogue = builder.create();
                    subTTitledialogue.show();
                }

            });
        }

        listView = (ListView) findViewById(R.id.ListView);
        //Array of titles
        listViewItems = new ArrayList<String>();
        arrayAdapt = new titleScreenAdapter(this, R.layout.homescreenlayout, listViewItems);
        listView.setAdapter(arrayAdapt);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Home.this, DetailsAndSubLayer.class);
                String title;
                int titlePos;
                titlePos = position;
                LayerTitles.setLayer(String.valueOf(2));
                title = listViewItems.get(position);

                //Adds the home level details to LT Class
                saveFileDetail = new LayerTitles();
                LayerTitles.setHomeTitle(title);
                LayerTitles.setHomePosition(String.valueOf(titlePos));
                LayerTitles.setHomeLayer();
                saveFileDetail.addHomeToArray(); // adds the home title details to array when the part is clicked..
                saveFileDetail.setTitleFullConcat();

               //sending the layer details onto Layer 2
                intent.putExtra("titleLayer", 1); // will need to keep to allow for layer comparison method..
                startActivity(intent);
                Toast.makeText(Home.this, "List item clicked at " + position, Toast.LENGTH_SHORT).show();
            }
            });

        loadFile();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo MenuInfo){
        menu.add(0, v.getId(), 0, "Edit");
        menu.add(0, v.getId(), 0, "Delete");
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) MenuInfo;
        menu.setHeaderTitle(listViewItems.get(info.position));

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
                    listViewItems.remove(i);
                    listViewItems.add(i,editTextValue);
                    arrayAdapt.notifyDataSetChanged();
                    try {
                        saveFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //titlesLineEdit();
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
                    listViewItems.remove(i);
                    arrayAdapt.notifyDataSetChanged();
                    try {
                        saveFile();
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

    //Screen adapter to build the listviewscreen
    private class titleScreenAdapter extends ArrayAdapter<String>{
        private int layout;
        public titleScreenAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            titleViewLayoutRef mainViewHolder;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                titleViewLayoutRef titleViewLayoutRef = new titleViewLayoutRef();
                titleViewLayoutRef.Title = (TextView) convertView.findViewById(R.id.titleTextView);

                convertView.setTag(titleViewLayoutRef);
            }
            mainViewHolder = (titleViewLayoutRef) convertView.getTag();
            mainViewHolder.Title.setText(getItem(position));
            return convertView;
        }
    }

    //class for the references of the home screen layout
    public class titleViewLayoutRef {
        TextView Title;
        TextView Save;
    }

    //deletes entire titles file
    public void titlesFileDelete (){
        File deleteFile = new File(this.getFilesDir(), titlesFile);
        if (!deleteFile.exists()){
            //Log to give feedback on if the file exists....
            //Log.d("titlesfiledelete", "file does not exist ");
        }
        else {
            //Deleting the file
           boolean DTF = deleteFile.delete();
            //Feedback file has been deleted.
            //Log.d("titlesFileDelete", "File Deleted");
        }
    }
    //Save Titles file code
    public void saveFile () throws IOException {
        titlesFileDelete();
        titlesSaveFile = new File(this.getFilesDir(), titlesFile);
        FileWriter writer = new FileWriter(titlesSaveFile, true);
        int size = listViewItems.size();
        //Log.d("System Out:Size", String.valueOf(size));
        for (int i = 0; i < size; i++) {
            String str = listViewItems.get(i);
            //Log.d("System Out:Str", str);
            writer.write(str + "\n");
        }
        writer.close();
    }
    //Load file code
    public void loadFile() {
        File loadFile = getFileStreamPath(titlesFile);
        if (loadFile.exists()){
            //Log.d("System Out:LoadFile", "loadfile does exist!");
            try {
                FileInputStream fileInputStream = new FileInputStream(loadFile);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                String message;
                while ((message = bufferedReader.readLine()) !=null){
                    //Log.d("System Out:Str", message);
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
            //Log.d("System Out:load", "Loadfile does not exist");
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
