package com.example.kylewhite.moneymanagement_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button btnAddAccount = (Button) findViewById(R.id.btnMainAddAccount);
        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, addAccount.class);
                startActivity(intent);

            }
        });

        Button btnAddBill = (Button) findViewById(R.id.btnMainAddBill);
        btnAddBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, add_bill.class);
                startActivity(intent);

            }
        });

        Button btnAddExpense = (Button) findViewById(R.id.btnMainAddExpense);
        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, add_expense.class);
                startActivity(intent);

            }
        });

        Button btnTransfer = (Button) findViewById(R.id.btnMainTransfer);
        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, transfer.class);
                startActivity(intent);

            }
        });

        Button btnBillsList = (Button) findViewById(R.id.btnMainBillsList);
        btnBillsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, billsList.class);
                startActivity(intent);

            }
        });

        Button btnAccountsList = (Button) findViewById(R.id.btnMainAccountsList);
        btnAccountsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, accountsList.class);
                startActivity(intent);

            }
        });


    }

}
