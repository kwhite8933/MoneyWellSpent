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

public class accountsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_list);
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

        // populate list of accounts from database
        final ListView lv = (ListView) findViewById(R.id.lvAccountsList);
        ArrayList<classAccountItem> accountsList = new ArrayList<>();

        // database query
        if (!populateListView(accountsList)){
            Toast.makeText(getApplicationContext(), "No accounts have been added!", Toast.LENGTH_LONG).show();
        }

        classAccountItemAdapter arrayAdapter = new classAccountItemAdapter(this, accountsList);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Intent that sends account information to the viewAccount activity
                Intent intent = new Intent(accountsList.this, viewAccount.class);
                classAccountItem item = (classAccountItem) lv.getItemAtPosition(position);
                intent.putExtra("accountId", item.getAccountId());
                intent.putExtra("startingBalance", item.getStartingBalance());
                intent.putExtra("accountName", item.getAccountName());

                startActivity(intent);
            }
        });
    }

    // Queries the database for all account data available and displays it in list view
    private boolean populateListView(ArrayList<classAccountItem> accountList){

        classDbHelper helper = new classDbHelper(getApplicationContext());
        SQLiteDatabase mDb = helper.getWritableDatabase();

        Cursor c = mDb.rawQuery(classDbHelper.ACCOUNT_SELECT_ALL, null);

        if( c.moveToFirst() ){

            while ( !c.isAfterLast() ){

                String name = c.getString(c.getColumnIndex(classDbHelper.ACCOUNT_FIELDS[1]));
                String startingBalance = c.getString(c.getColumnIndex(classDbHelper.ACCOUNT_FIELDS[2]));
                int accountId = c.getInt(c.getColumnIndex(classDbHelper.ACCOUNT_FIELDS[0]));

                accountList.add( new classAccountItem(name, startingBalance, accountId) );
                c.moveToNext();

            }
        } else {
            return false;
        }
        c.close();
        return true;
    }

}
