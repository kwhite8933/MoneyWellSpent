package com.example.kylewhite.moneymanagement_v2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class billsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills_list);
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
    }

    @Override
    public void onResume(){
        super.onResume();

        // populate list of bills from database
        final ListView lv = (ListView) findViewById(R.id.lvBillsList);
        ArrayList<classBillItem> billList = new ArrayList<>();

        // database query
        if (!populateListView(billList)){
            Toast.makeText(getApplicationContext(), "No bills have been added!", Toast.LENGTH_LONG).show();
        }

        classBillItemAdapter arrayAdapter = new classBillItemAdapter(this, billList);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Intent that sends bill information to the viewBill activity
                Intent intent = new Intent( billsList.this, viewBill.class );
                classBillItem item = ( classBillItem ) lv.getItemAtPosition( position );
                intent.putExtra( "billName", item.getBillName() );
                intent.putExtra( "billId", item.getBillId() );
                intent.putExtra( "amount", item.getAmount() );

                startActivity( intent );
            }
        });
    }

    // Queries the database for all bill data available and displays it in list view
    private boolean populateListView(ArrayList<classBillItem> billList){

        classDbHelper helper = new classDbHelper(getApplicationContext());
        SQLiteDatabase mDb = helper.getWritableDatabase();

        Cursor c = mDb.rawQuery(classDbHelper.BILL_SELECT_ALL, null);

        if( c.moveToFirst() ){

            while ( !c.isAfterLast() ){

                String name = c.getString(c.getColumnIndex(classDbHelper.BILL_FIELDS[1]));
                String account = c.getString(c.getColumnIndex(classDbHelper.BILL_FIELDS[2]));
                int billId = c.getInt(c.getColumnIndex(classDbHelper.BILL_FIELDS[0]));
                String amount = c.getString(c.getColumnIndex(classDbHelper.BILL_FIELDS[3]));

                billList.add( new classBillItem(name, account, amount, billId) );
                c.moveToNext();

            }
        } else {
            return false;
        }
        c.close();
        return true;
    }

}
