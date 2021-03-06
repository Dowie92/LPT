package alexdissertation.lpt;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Alex on 06/05/2016.
 */
public class ExpandableListView extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listHeader; //for the header of the List...
    private HashMap<String, List<String>> listChild;

    public ExpandableListView(Context context, List<String> listHeader, HashMap<String,List<String>> listChild){
        this.context = context;
        this.listHeader = listHeader;
        this.listChild = listChild;
    }
    @Override
    public int getGroupCount() {
       return this.listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listChild.get(this.listHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listChild.get(this.listHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null){
            LayoutInflater infalInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.parent_layout, null);
        }
       TextView parentView = (TextView) convertView.findViewById(R.id.parent_txt);
        parentView.setTypeface(null, Typeface.BOLD);
        parentView.setText(headerTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_layout, null);
        }
        final EditText checkBoxUserInput = (EditText) convertView.findViewById(R.id.checkBoxEditText);
        checkBoxUserInput.setText(childText);
        checkBoxUserInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    AddDetails ad = new AddDetails();
                    String userInput = String.valueOf(checkBoxUserInput.getText()); //gets the text from the edit Text
                    Log.d("checkbox userInput",userInput);
                    //ad.updateCheckbox(userInput);
                    //checkboxUserInput.setText(userInput); //adds sets the edit text value
                    //need to add another checkbox with editText

                }
                return false;
            }
        });
        CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.checkBox);
        //checkBox.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
