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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailsAndSubLayer extends AppCompatActivity {
    private ArrayList <String> subTaskTitle;
    private ArrayAdapter <String> arrayAdapter;
    private ListView listView;
    private File subTaskTitleFile;
    private File Layer1Details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                Intent intent = new Intent(getApplicationContext(), DetailsAndSubLayer.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "List item clicked at " + position, Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                // need to add in an adapter view
                //final int i = info.position;
                final EditText editText = new EditText(getApplicationContext());
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setHint("Subtask 1");
                //Alert Diag (Adding subtask or details?)
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsAndSubLayer.this);
                builder.setTitle("Add details or Subtask?");
                builder.setPositiveButton("Add Details", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogue, int id){
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
                                //needs arraylist to populate the list with....
                                String editTextValue = editText.getText().toString();
                                subTaskTitle.add(editTextValue);
                                arrayAdapter.notifyDataSetChanged();
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
                Toast.makeText(getApplicationContext(), "Edit Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class subTaskLayoutAdapter extends ArrayAdapter<String>{
        private int layout;
        public subTaskLayoutAdapter(Context context, int resource, List<String> objects){
        super (context, resource, objects);
        layout = resource;
    }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            subTaskLayoutRef mainViewHolder = null;
            if (mainViewHolder == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                subTaskLayoutRef subTaskLayoutRef = new subTaskLayoutRef();
                subTaskLayoutRef.subTitle = (TextView) convertView.findViewById(R.id.subTitle);
                subTaskLayoutRef.subDetail = (TextView) convertView.findViewById(R.id.subDetail);
                convertView.setTag(subTaskLayoutRef);
            }
            mainViewHolder = (subTaskLayoutRef) convertView.getTag();
            mainViewHolder.subTitle.setText(getItem(position));
            return convertView;

        }

    }
    //Class to reference the subtask layout file.
    public class subTaskLayoutRef{
        TextView subTitle;
        TextView subDetail;
    }

}
