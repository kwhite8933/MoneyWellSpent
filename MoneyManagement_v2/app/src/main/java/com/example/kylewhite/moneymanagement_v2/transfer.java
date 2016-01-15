package com.example.kylewhite.moneymanagement_v2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class transfer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // floating action button at bottom of screen
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( transfer.this, add_bill.class );
                startActivity(intent);
            }
        });

        // insert data for the "to" account spinner
        Spinner dropdown_to = (Spinner)findViewById(R.id.spnAccountTo);
        String[] accounts = new String[]{"Select an Account", "Account1", "Account2", "Account3"};
        ArrayAdapter<String> adapter_to = new ArrayAdapter<String>(transfer.this, android.R.layout.simple_spinner_dropdown_item, accounts);
        adapter_to.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown_to.setAdapter(adapter_to);

        // insert data for the "from" account spinner
        Spinner dropdown_from = (Spinner)findViewById(R.id.spnAccountFrom);
        ArrayAdapter<String> adapter_from = new ArrayAdapter<String>(transfer.this, android.R.layout.simple_spinner_dropdown_item, accounts);
        adapter_from.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown_from.setAdapter(adapter_from);

        // insert data for the type of transfer to be executed
        Spinner dropdown_trans = (Spinner)findViewById(R.id.spnTransferType);
        String[] trans_type = new String[]{"Select transfer type", "One Time", "Recurring"};
        ArrayAdapter<String> adapter_type = new ArrayAdapter<String>(transfer.this, android.R.layout.simple_spinner_dropdown_item, trans_type);
        adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown_trans.setAdapter(adapter_type);

        // day of week selector
        Spinner dropdown_dayOfWeek = (Spinner)findViewById(R.id.spnFrequency);
        String[] freq = new String[]{"Day of Week", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        ArrayAdapter<String> adapter_freq = new ArrayAdapter<String>(transfer.this, android.R.layout.simple_spinner_dropdown_item, freq);
        adapter_freq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown_dayOfWeek.setAdapter(adapter_freq);


    }


}
