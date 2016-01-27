package com.example.kylewhite.moneymanagement_v2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class makeDeposit extends AppCompatActivity {

    private String errorMsg;
    private EditText etDepositAmount;
    private Spinner spnDepositAccount;
    private classDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_deposit);
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

        etDepositAmount = (EditText) findViewById(R.id.etDepositAmount);
        etDepositAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        // get instance of classDbHelper class
        mDbHelper = new classDbHelper(getApplicationContext());

        // data for the account the bill will be taken from
        spnDepositAccount = (Spinner)findViewById(R.id.spnDepositAccount);
        List<String> accounts = getAccounts();
        ArrayAdapter<String> adapter_account = new ArrayAdapter<>(this, R.layout.my_spinner, accounts);
        adapter_account.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDepositAccount.setAdapter(adapter_account);

        Button btnDepositCancel = (Button) findViewById(R.id.btnDepositCancel);
        btnDepositCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnDepositSubmit = (Button) findViewById(R.id.btnDepositSubmit);
        btnDepositSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                ContentValues updateValues = new ContentValues();

                if( !addDepositDatabaseEntry(values) ){
                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Float flDepositAmount = values.getAsFloat("amount");
                    String strAccountId = values.getAsString("account");
                    int intAccountId = Integer.valueOf(strAccountId);
                    Cursor c = mDb.rawQuery(classDbHelper.accountSelectById(intAccountId), null);
                    if( c.moveToFirst() ){

                        Float flAccountBalance = c.getFloat(c.getColumnIndex(classDbHelper.ACCOUNT_FIELDS[2]));
                        flAccountBalance = flAccountBalance + flDepositAmount;
                        updateValues.put(classDbHelper.ACCOUNT_FIELDS[2], flAccountBalance);
                        mDb.update(classDbHelper.ACCOUNT_TABLE_NAME, updateValues, "id = " + strAccountId, null);

                    }

                    // closes database connection
                    c.close();

                    finish();
                }

            }
        });

    }

    // TODO: Possibly add database information here.
    // In such a case, change the name of the function to reflect its functionality

    // validates user input
    public boolean addDepositDatabaseEntry(ContentValues values){

        if( etDepositAmount.getText().toString().equals("") ){
            errorMsg = "Please enter the amount you would like to deposit";
            return false;
        }
        values.put("amount", etDepositAmount.getText().toString());

        if( spnDepositAccount.getSelectedItemPosition() == 0 ){
            errorMsg = "Please select the account you would like to deposit money into";
            return false;
        }
        values.put("account", spnDepositAccount.getSelectedItemPosition());

        return true;

    }

    public List<String> getAccounts(){

        List<String> accounts = new ArrayList<>();

        SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
        Cursor c = mDb.rawQuery(classDbHelper.ACCOUNT_SELECT_ALL, null);

        accounts.add("Select an Account");
        if(c.moveToFirst()){
            do{
                accounts.add(c.getString(1));
            } while(c.moveToNext());
        }

        c.close();
        return accounts;
    }

}
