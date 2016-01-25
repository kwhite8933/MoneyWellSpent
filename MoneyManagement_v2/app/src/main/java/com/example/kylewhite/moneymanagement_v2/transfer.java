package com.example.kylewhite.moneymanagement_v2;

import android.support.v7.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class transfer extends AppCompatActivity {

    private EditText etTransferAmount;
    private Spinner dropdown_from;
    private Spinner dropdown_to;
    private String errorMsg;
    private classDbHelper mDbHelper;

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

        etTransferAmount = (EditText) findViewById(R.id.etTransferAmount);
        etTransferAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        mDbHelper = new classDbHelper( getApplicationContext() );

        // insert data for the "to" account spinner
        dropdown_to = (Spinner)findViewById(R.id.spnAccountTo);
        List<String> accounts = getAccounts();
        ArrayAdapter<String> adapter_to = new ArrayAdapter<>(transfer.this, R.layout.my_spinner, accounts);
        adapter_to.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown_to.setAdapter(adapter_to);

        // insert data for the "from" account spinner
        dropdown_from = (Spinner)findViewById(R.id.spnAccountFrom);
        ArrayAdapter<String> adapter_from = new ArrayAdapter<>(transfer.this, R.layout.my_spinner, accounts);
        adapter_from.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown_from.setAdapter(adapter_from);

        /*// insert data for the type of transfer to be executed
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
*/


        Button btnCancel = (Button) findViewById(R.id.btnTransCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnSubmit = (Button) findViewById(R.id.btnTransSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                ContentValues updateValues = new ContentValues();

                // Validate user input
                if( !addTransferDatabaseEntry(values) ){
                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                }
                else
                {
                    long newRowId = mDb.insert(classDbHelper.TRANSFER_TABLE_NAME, null, values);
                    Toast.makeText(getApplicationContext(), errorMsg + " (id:" + newRowId + ") Successfully Added!", Toast.LENGTH_LONG).show();

                    // gets transfer information and updates account balances accordingly
                    Float flAmount = values.getAsFloat(classDbHelper.TRANSFER_FIELDS[3]);
                    String strFromAccountId = values.getAsString(classDbHelper.TRANSFER_FIELDS[2]);
                    int intFromAccountId = Integer.valueOf(strFromAccountId);
                    Cursor cFrom = mDb.rawQuery(classDbHelper.accountSelectById(intFromAccountId),null);
                    if (cFrom.moveToFirst()){

                        Float flTransferFromAmount = cFrom.getFloat(cFrom.getColumnIndex(classDbHelper.ACCOUNT_FIELDS[2]));
                        flTransferFromAmount = flTransferFromAmount - flAmount;
                        updateValues.put(classDbHelper.ACCOUNT_FIELDS[2], flTransferFromAmount);
                        mDb.update(classDbHelper.ACCOUNT_TABLE_NAME, updateValues,"id = " + strFromAccountId, null);

                    }

                    String strToAccountId = values.getAsString(classDbHelper.TRANSFER_FIELDS[1]);
                    int intToAccountId = Integer.valueOf(strToAccountId);
                    Cursor cTo = mDb.rawQuery(classDbHelper.accountSelectById(intToAccountId), null);
                    if( cTo.moveToFirst() ){

                        Float flTransferToAmount = cTo.getFloat(cTo.getColumnIndex(classDbHelper.ACCOUNT_FIELDS[2]));
                        flTransferToAmount = flTransferToAmount + flAmount;
                        updateValues.put(classDbHelper.ACCOUNT_FIELDS[2], flTransferToAmount);
                        mDb.update(classDbHelper.ACCOUNT_TABLE_NAME, updateValues, "id = " + strToAccountId, null);

                    }

                    // closes database connection
                    cTo.close();
                    cFrom.close();

                    // activity is finished and returns back to the activity that called this activity
                    finish();
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private boolean addTransferDatabaseEntry(ContentValues values){

        if( dropdown_from.getSelectedItemPosition() == 0 ){
            errorMsg = "Please select the account to transfer from";
            return false;
        }
        values.put(classDbHelper.TRANSFER_FIELDS[1], dropdown_from.getSelectedItemPosition());

        if( dropdown_to.getSelectedItemPosition() == 0 ){
            errorMsg = "Please select the account to transfer to";
            return false;
        }
        values.put(classDbHelper.TRANSFER_FIELDS[2], dropdown_to.getSelectedItemPosition());

        if( etTransferAmount.getText().toString().equals("") ){
            errorMsg = "Please enter the amount for the transfer";
            return false;
        }
        values.put(classDbHelper.TRANSFER_FIELDS[3], etTransferAmount.getText().toString());

        return true;

    }

    // method that hides the keyboard (that appears when clicking an EditText) when the screen is
    // tapped anywhere besides the keyboard.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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
