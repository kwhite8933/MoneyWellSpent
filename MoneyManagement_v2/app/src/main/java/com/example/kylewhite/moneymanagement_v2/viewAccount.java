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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class viewAccount extends AppCompatActivity {

    private TextView tvVAStartingBalance;
    private TextView tvVAAccountName;
    private ListView lvVAUpcomingTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account);
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

        Intent currIntent = getIntent();
        final int accountId = currIntent.getIntExtra( "accountId",0 );
        if( accountId == 0 ) {
            Toast.makeText(getApplicationContext(), "Invalid account Id", Toast.LENGTH_LONG).show();
            finish();
        }
        tvVAAccountName = (TextView) findViewById(R.id.tvVAAccountName);
        tvVAStartingBalance = (TextView) findViewById(R.id.tvVAStartingBalance);
        lvVAUpcomingTransactions = (ListView) findViewById(R.id.lvVAUpcomingTransactions);

    }

    @Override
    public void onResume(){
        super.onResume();

        Intent intent = getIntent();
        int accountId = intent.getIntExtra( "accountId",0 );
        if( accountId == 0 ){

            Toast.makeText(getApplicationContext(), "Invalid account Id", Toast.LENGTH_LONG).show();
            finish();

        }

        // get account data via intent
        if ( !getAccountDatabaseInfo(accountId) ){
            Toast.makeText(getApplicationContext(), "Error Loading Data...", Toast.LENGTH_LONG).show();
        }

    }

    private boolean getAccountDatabaseInfo( int accountId ){

        classDbHelper helper = new classDbHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        final Cursor c = db.rawQuery(classDbHelper.accountSelectById(accountId), null);

        if( c.moveToFirst() ){

            // Account Name
            tvVAAccountName.setText(c.getString(c.getColumnIndex(classDbHelper.ACCOUNT_FIELDS[1])));

            // Account Starting Balance
            tvVAStartingBalance.setText(c.getString(c.getColumnIndex(classDbHelper.ACCOUNT_FIELDS[2])));

            /* below is a way to convert the string value of the starting balance to a double
             * for manipulation of the balance for transfer, billing, expenses, etc.
             * Also formats the value to have the format of US currency ( #.## , 2 decimal places )

            String strSelector = c.getString(c.getColumnIndex(classDbHelper.ACCOUNT_FIELDS[2]));
            double flSelector = Double.parseDouble(strSelector);
            flSelector = flSelector - 1.00;
            tvVAStartingBalance.setText(String.format("%.2f",flSelector));

            */

        } else {
            c.close();
            return false;
        }
        c.close();
        return true;

    }

}
