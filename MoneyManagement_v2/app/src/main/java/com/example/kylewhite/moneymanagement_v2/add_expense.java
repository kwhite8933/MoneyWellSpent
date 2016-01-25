package com.example.kylewhite.moneymanagement_v2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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

public class add_expense extends AppCompatActivity {

    private EditText etExpenseName;
    private EditText etExpenseAmount;
    private Spinner dropdown_location;
    private String errorMsg;
    private classDbHelper mDbHelper;

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

        // sets the keyboard for each EditText for a smoother UX
        etExpenseName = (EditText) findViewById(R.id.etExpenseName);
        etExpenseName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS); // first letter of each word is capitalized

        etExpenseAmount = (EditText) findViewById(R.id.etExpenseAmount);
        etExpenseAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); // strictly number keyboard with decimals allowed

        // database connection
        mDbHelper = new classDbHelper( getApplicationContext() );

        // data for the account the expense will be taken from
        // TODO: change name of spinner from location to something more similar to accounts
        dropdown_location = (Spinner)findViewById(R.id.spnLocation);
        List<String> accounts = getAccounts();
        ArrayAdapter<String> adapter_location = new ArrayAdapter<>(add_expense.this, R.layout.my_spinner, accounts);
        adapter_location.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown_location.setAdapter(adapter_location);

        //////////////////// Button Handlers ////////////////////
        Button btnSubmit = (Button) findViewById(R.id.btnExpenseSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get writable database
                SQLiteDatabase mdb = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues(); // values that will insert the newest created expense to the database
                ContentValues updateValues = new ContentValues(); // values that update the account balance with respect to the new expense

                // validate user input and populate content values to be inserted to database
                if( !getExpenseDatabaseInfo(values) ){
                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                }
                else
                {
                    // inserts newly created expense int the database
                    long newRowId = mdb.insert(classDbHelper.EXPENSE_TABLE_NAME, null, values);
                    Toast.makeText(getApplicationContext(), errorMsg + " (id:" + newRowId + ") Successfully Added!", Toast.LENGTH_LONG).show();

                    // gets expense information and updates account balance accordingly
                    Float amount = values.getAsFloat(classDbHelper.EXPENSE_FIELDS[3]);
                    String strAccountId = values.getAsString(classDbHelper.EXPENSE_FIELDS[2]);
                    int intAccountId = Integer.valueOf(strAccountId);
                    Cursor c = mdb.rawQuery(classDbHelper.accountSelectById(intAccountId),null);
                    if (c.moveToFirst()){

                        Float flExpenseBalance = c.getFloat(c.getColumnIndex(classDbHelper.ACCOUNT_FIELDS[2]));
                        flExpenseBalance = flExpenseBalance - amount;
                        updateValues.put(classDbHelper.ACCOUNT_FIELDS[2], flExpenseBalance);
                        mdb.update(classDbHelper.ACCOUNT_TABLE_NAME, updateValues,"id = " + strAccountId, null);

                    }

                    // closes database connection
                    c.close();

                    // activity is finished and returns back to whatever activity called this activity
                    finish();
                }

            }
        });

        Button btnCancel = (Button) findViewById(R.id.btnExpenseCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    // validation of from data and population of content values with expense information
    private boolean getExpenseDatabaseInfo( ContentValues values ){

        if( etExpenseName.getText().toString().equals("") ){
            errorMsg = "Please enter a name for this expense";
            return false;
        }
        values.put(classDbHelper.EXPENSE_FIELDS[1], etExpenseName.getText().toString());

        if( dropdown_location.getSelectedItemPosition() == 0 ){
            errorMsg = "Please select an account";
            return false;
        }
        values.put(classDbHelper.EXPENSE_FIELDS[2], dropdown_location.getSelectedItemPosition() );

        if( etExpenseAmount.getText().toString().equals("") ){
            errorMsg = "Please enter the amount of the expense";
            return false;
        }
        values.put(classDbHelper.EXPENSE_FIELDS[3], etExpenseAmount.getText().toString());

        /*String strSelector = c.getString(c.getColumnIndex(classDbHelper.ACCOUNT_FIELDS[2]));
        double flSelector = Double.parseDouble(strSelector);
        flSelector = flSelector - 1.00;
        tvVAStartingBalance.setText(String.format("%.2f", flSelector)); */

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

        // get an instance of the database
        SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
        Cursor c = mDb.rawQuery(classDbHelper.ACCOUNT_SELECT_ALL, null);

        // adds a default selection to the beginning of the accounts list
        accounts.add("Select an Account");

        // inputs the remaining accounts that the user creates
        // any accounts created by the user will be dynamically added to this list
        if(c.moveToFirst()){
            do{
                accounts.add(c.getString(1));
            } while(c.moveToNext());
        }

        // returns the list of every account created by the user
        return accounts;
    }


}
