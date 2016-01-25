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
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addAccount extends AppCompatActivity {

    private EditText etAccountName;
    private EditText etAccountStartingBalance;
    private String errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addAccount.this, transfer.class);
                startActivity(intent);
            }
        });

        etAccountName = (EditText) findViewById(R.id.etAccountName);
        etAccountName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        etAccountStartingBalance = (EditText) findViewById(R.id.etAccountStartingBalance);
        etAccountStartingBalance.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        final classDbHelper mDbHelper = new classDbHelper(getApplicationContext());

        ///////////////////////// Button Handlers /////////////////////////
        Button btnSubmit = (Button) findViewById(R.id.btnAddAccount);
        btnSubmit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                // get instance of database
                SQLiteDatabase mdb = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                // validate user input
                if( !addDatabaseEntry(values) ){
                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                } else {

                    long newRowId = mdb.insert(classDbHelper.ACCOUNT_TABLE_NAME, null, values);
                    Toast.makeText(getApplicationContext(), errorMsg + " (id:" + newRowId + ") Successfully Added!",
                            Toast.LENGTH_LONG).show();
                    //finish();

                }

            }

        });

        Button btnCancel = (Button) findViewById(R.id.btnAccountCancel);
        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_account, menu);
        return true;
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

    private boolean addDatabaseEntry(ContentValues values){

        // Name of Account
        if( etAccountName.getText().toString().equals("")){
            errorMsg = "Please enter a name for this account";
            return false;
        }
        values.put(classDbHelper.ACCOUNT_FIELDS[1], etAccountName.getText().toString());

        // Starting balance for account
        if( etAccountStartingBalance.getText().toString().equals("")){
            errorMsg = "Please enter a starting balance for this account";
            return false;
        }
        values.put(classDbHelper.ACCOUNT_FIELDS[2], Float.parseFloat( etAccountStartingBalance.getText().toString()) );

        return true;

    }

    // method that hides the keyboard (that appears when clicking an EditText) when the screen is
    // tapped anywhere besides the keyboard.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        // try - catch block doesn't resolve warning but probably a good idea to add
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

}
