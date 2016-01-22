package com.example.kylewhite.moneymanagement_v2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;
import android.widget.Toast;

public class add_expense extends AppCompatActivity {

    private EditText etExpenseName;
    private EditText etExpenseAmount;
    private Spinner dropdown_location;
    private String errorMsg;

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

        etExpenseName = (EditText) findViewById(R.id.etExpenseName);
        etExpenseName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        etExpenseAmount = (EditText) findViewById(R.id.etExpenseAmount);
        etExpenseAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        // database connection
        final classDbHelper mDbHelper = new classDbHelper( getApplicationContext() );

        // insert data for the account the expense will be taken from
        dropdown_location = (Spinner)findViewById(R.id.spnLocation);
        String[] accounts = new String[]{"Select an Account", "Account1", "Account2", "Account3"};
        ArrayAdapter<String> adapter_location = new ArrayAdapter<String>(add_expense.this, R.layout.my_spinner, accounts);
        adapter_location.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown_location.setAdapter(adapter_location);

        //////////////////// Button Handlers ////////////////////
        Button btnSubmit = (Button) findViewById(R.id.btnExpenseSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get writeable database
                SQLiteDatabase mdb = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                // validate user input
                // TODO: addDatabaseEntry() call
                if( !getExpenseDatabaseInfo(values) ){
                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                }
                else
                {
                    long newRowId = mdb.insert(classDbHelper.EXPENSE_TABLE_NAME, null, values);
                    Toast.makeText(getApplicationContext(), errorMsg + " (id:" + newRowId + ") Successfully Added!", Toast.LENGTH_LONG).show();
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
