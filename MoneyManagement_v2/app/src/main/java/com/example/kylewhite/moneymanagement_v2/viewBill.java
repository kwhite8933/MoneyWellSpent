package com.example.kylewhite.moneymanagement_v2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class viewBill extends AppCompatActivity {

    private TextView tvVBBillName;
    private TextView tvVBAmount;
    private TextView tvVBAccount;
    private ListView lvVBUpcomingBills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewBill.this, accountsList.class);
                startActivity(intent);
            }
        });

        Intent currIntent = getIntent();
        final int billId = currIntent.getIntExtra("billId", 0);
        if( billId == 0 ){

            Toast.makeText(getApplicationContext(), "Invalid BillId", Toast.LENGTH_LONG ).show();
            finish();

        }

        tvVBBillName = (TextView) findViewById(R.id.tvVBBillName);
        tvVBAccount = (TextView) findViewById(R.id.tvVBAccount);
        tvVBAmount = (TextView) findViewById(R.id.tvVBAmount);
        lvVBUpcomingBills = (ListView) findViewById(R.id.lvVBUpcomingBills);

    }

    @Override
    public void onResume(){
        super.onResume();

        Intent intent = getIntent();
        int billId = intent.getIntExtra("billId", 0);
        if( billId == 0 ){

            Toast.makeText(getApplicationContext(), "Invalid billId", Toast.LENGTH_LONG ).show();
            finish();

        }

        // get bill data from database via the intent
        if( !getBillDatabaseInfo(billId) ) {
            Toast.makeText(getApplicationContext(), "Error Loading Data...", Toast.LENGTH_LONG).show();
        }

        //TODO: function call for future bills goes here
        //Modeled after getTaskDatabaseData from MAC

    }

    private boolean getBillDatabaseInfo( int billId ){

        classDbHelper helper = new classDbHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        final Cursor c = db.rawQuery(classDbHelper.billSelectById(billId), null);

        if( c.moveToFirst() ){

            // Bill Name
            tvVBBillName.setText(c.getString(c.getColumnIndex(classDbHelper.BILL_FIELDS[1])));

            // Bill Account
            tvVBAccount.setText(c.getString(c.getColumnIndex(classDbHelper.BILL_FIELDS[2])));

            // Bill Amount
            tvVBAmount.setText(c.getString(c.getColumnIndex(classDbHelper.BILL_FIELDS[3])));
        } else {
            c.close();
            return false;
        }
        c.close();
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_add_account, menu);
        return true;
    }

}
