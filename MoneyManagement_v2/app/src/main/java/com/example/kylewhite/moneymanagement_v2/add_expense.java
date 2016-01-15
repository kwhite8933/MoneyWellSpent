package com.example.kylewhite.moneymanagement_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class add_expense extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(add_expense.this, billsList.class);
                startActivity(intent);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        // insert data for the account the expense will be taken from
        Spinner dropdown_location = (Spinner)findViewById(R.id.spnLocation);
        String[] accounts = new String[]{"Select an Account", "Account1", "Account2", "Account3"};
        ArrayAdapter<String> adapter_location = new ArrayAdapter<String>(add_expense.this, android.R.layout.simple_spinner_dropdown_item, accounts);
        adapter_location.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown_location.setAdapter(adapter_location);


    }

}
