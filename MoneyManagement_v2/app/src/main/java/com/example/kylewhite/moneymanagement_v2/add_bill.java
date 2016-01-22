package com.example.kylewhite.moneymanagement_v2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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

public class add_bill extends AppCompatActivity {

    /*private static final String PREFS_NAME = "MoneyManagementPrefs";*/

    private EditText etBillName;
    //private EditText etBillStartDate;
    //private EditText etBillEndDate;
    private EditText etAmount;
    //private DatePickerDialog dpBillStartDate;
    //private DatePickerDialog dbBillEndDate;
    //private DateFormat mdateFormat;
    private String errorMsg;
    private Spinner dropdown_account;
    //private Spinner dropdown_dayOfWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ///////////////////////// Time Date Picker /////////////////////////
/*
        mdateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

        etBillStartDate = (EditText) findViewById(R.id.etBillStartDate);
        etBillStartDate.setInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);

        etBillEndDate = (EditText) findViewById(R.id.etBillEndDate);
        etBillEndDate.setInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
*/

        etBillName = (EditText) findViewById(R.id.etBillName);
        etBillName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        etAmount = (EditText) findViewById(R.id.etAmount);
        etAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        final classDbHelper mDbHelper = new classDbHelper(getApplicationContext());



        // floating action button functionality
        // comment out along with layout in activity_add_bill.xml if so desired
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (add_bill.this, add_expense.class);
                startActivity(intent);
            }
        });

        // insert data for the account the bill will be taken from
        dropdown_account = (Spinner)findViewById(R.id.spnBillingAccount);
        String[] accounts = new String[]{"Select an Account", "Account1", "Account2", "Account3"};
        ArrayAdapter<String> adapter_account = new ArrayAdapter<>(this, R.layout.my_spinner, accounts);
        adapter_account.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown_account.setAdapter(adapter_account);
/*

        // day of week selector
        dropdown_dayOfWeek = (Spinner)findViewById(R.id.spnFrequency);
        String[] freq = new String[]{"Day of Week", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        ArrayAdapter<String> adapter_freq = new ArrayAdapter<>(add_bill.this, android.R.layout.simple_spinner_dropdown_item, freq);
        adapter_freq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown_dayOfWeek.setAdapter(adapter_freq);
*/

        //////////////////// Button Handlers ////////////////////
        Button btnSubmit = (Button) findViewById(R.id.btnBillSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get writable database
                SQLiteDatabase mdb = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                // validate user input

                if ( !addDatabaseEntry(values) ){
                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                }
                else
                {
                    // adds all billing information to database
                    long newRowId = mdb.insert(classDbHelper.BILL_TABLE_NAME, null, values);
                    Toast.makeText(getApplicationContext(), errorMsg + " (id:" + newRowId + ") Successfully Added!", Toast.LENGTH_LONG).show();
                    finish();

                }

            }
        });

        Button btnCancel = (Button) findViewById(R.id.btnBillCancel);
        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
        return true;
    }*/

    // validates user input, return true if valid and populates
    // values to be stored in database

    private boolean addDatabaseEntry(ContentValues values){

        // Bill id
        /*Intent currIntent = getIntent();
        int billId = currIntent.getIntExtra("billId", 0);
        values.put(classDbHelper.BILL_FIELDS[1], billId);
        */
        
        // Name of Bill
        //EditText billName = (EditText) findViewById(R.id.etBillName);
        if (etBillName.getText().toString().equals("")){
            errorMsg = "Please enter a Name for this bill";
            return false;
        }
        values.put(classDbHelper.BILL_FIELDS[1], etBillName.getText().toString());

        // Billing account
        if( dropdown_account.getSelectedItemPosition() == 0 ) {

            errorMsg = "Please select a billing account";
            return false;

        }
        values.put(classDbHelper.BILL_FIELDS[2], dropdown_account.getSelectedItemPosition());
/*
        // Frequency
        if ( dropdown_dayOfWeek.getSelectedItemPosition() == 0 ) {

            errorMsg = "Please select a day";
            return false;

        }
        values.put(classDbHelper.BILL_FIELDS[3], dropdown_account.getSelectedItemPosition());

        // billing start date
        if (etBillStartDate.getText().toString().equals("")){

            errorMsg = "Please enter a start date";
            return false;

        }


        // billing end date
        if (etBillEndDate.getText().toString().equals("")){

            errorMsg = "Please enter an end date";
            return false;

        }
*/

        // amount of bill
        if (etAmount.getText().toString().equals("")){

            errorMsg = "Please enter an amount for the bill";
            return false;

        }
        values.put(classDbHelper.BILL_FIELDS[3], etAmount.getText().toString());

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

}
