package alexdissertation.lpt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddPlanActivity extends AppCompatActivity {
    private EditText userTitleEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userTitleEntry = (EditText) findViewById(R.id.TitleEntry);
        addcloseButton();
    }

    private void addcloseButton(){
        Button addclosebutton = (Button) findViewById(R.id.addclose);
        addclosebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Return Value
                Intent intent = new Intent();
                intent.putExtra("editText", userTitleEntry.getText().toString());
                setResult (RESULT_OK, intent);
                finish();
            }
        });
    }

}
