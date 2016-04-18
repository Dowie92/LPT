import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Alex on 13/04/2016.
 */
public class DeleteTitleDialogue extends DialogFragment{


    public Dialog onCreateDialogue(Bundle  savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please confirm deleting this plan");
        builder.setTitle("Delete Plan");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogue, int id) {
                //User Clicked on the Confirm button
                // Remove the listview item
                //listViewItems.remove(i);
                //arrayAdapt.notifyDataSetChanged();
                /*try {
                    //saveFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                //Toast.makeText(getApplicationContext(),"Item deleted",Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //User Cancelled the Dialogue
            }
        });

        Dialog deleteDialogue = builder.create();

        return deleteDialogue;
    }




}
