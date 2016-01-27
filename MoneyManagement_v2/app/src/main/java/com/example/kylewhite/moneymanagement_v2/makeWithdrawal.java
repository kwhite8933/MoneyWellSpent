package com.example.kylewhite.moneymanagement_v2;

import android.content.ContentValues;
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

public class makeWithdrawal extends AppCompatActivity {

    private String errorMsg;
    private EditText etWithdrawAmount;
    private Spinner spnWithdrawAccount;
    private classDbHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_withdrawal);
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

        etWithdrawAmount = (EditText) findViewById(R.id.etWithdrawAmount);
        etWithdrawAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        // get instance of classDbHelper class
        mDbHelper = new classDbHelper(getApplicationContext());

        // data for the account the bill will be taken from
        spnWithdrawAccount = (Spinner)findViewById(R.id.spnWithdrawAccount);
        List<String> accounts = getAccounts();
        ArrayAdapter<String> adapter_account = new ArrayAdapter<>(this, R.layout.my_spinner, accounts);
        adapter_account.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnWithdrawAccount.setAdapter(adapter_account);


        Button btnWithdrawCancel = (Button) findViewById(R.id.btnWithdrawCancel);
        btnWithdrawCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnWithdrawSubmit = (Button) findViewById(R.id.btnWithdrawSubmit);
        btnWithdrawSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                ContentValues updateValues = new ContentValues();

                if( !addWithdrawDatabaseEntry(values) ){
                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Float flWithdrawAmount = values.getAsFloat("amount");
                    String strAccountId = values.getAsString("account");
                    int intAccountId = Integer.valueOf(strAccountId);
                    Cursor c = mDb.rawQuery(classDbHelper.accountSelectById(intAccountId), null);
                    if( c.moveToFirst() ){

                        Float flAccountBalance = c.getFloat(c.getColumnIndex(classDbHelper.ACCOUNT_FIELDS[2]));
                        flAccountBalance = flAccountBalance - flWithdrawAmount;
                        updateValues.put(classDbHelper.ACCOUNT_FIELDS[2], flAccountBalance);
                        mDb.update(classDbHelper.ACCOUNT_TABLE_NAME, updateValues, "id = " + strAccountId, null);

                    }

                    // close database connection
                    c.close();

                    // returns to the calling activity
                    finish();
                }

            }
        });

    }

    public boolean addWithdrawDatabaseEntry(ContentValues values){

        if( etWithdrawAmount.getText().toString().equals("") ){
            errorMsg = "Please enter the amount you would like to withdraw";
            return false;
        }
        values.put("amount", etWithdrawAmount.getText().toString());

        if( spnWithdrawAccount.getSelectedItemPosition() == 0 ){
            errorMsg = "Please select the account you would like to withdraw from";
            return false;
        }
        values.put("account", spnWithdrawAccount.getSelectedItemPosition());

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
