package com.example.kylewhite.moneymanagement_v2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * Created by Kyle on 1/2/2016.
 */
public class classDbHelper extends SQLiteOpenHelper {

    // tag for Log.i calls
    public static String TAG = classDbHelper.class.getSimpleName();

    // identifying database
    private static final int DB_VERSION = 12;
    public static final String DB_NAME = "MoneyManagement";

    /////////////////////////  Bills Table  /////////////////////////
    public static final String BILL_TABLE_NAME = "billDb";

    // all fields in Bills
    public static final String[] BILL_FIELDS = {"id", "bill_name", "bill_accnt","amount"};
    // creates table
    private static final String BILL_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + BILL_TABLE_NAME + " (" +
                    BILL_FIELDS[0] + " INTEGER PRIMARY KEY, " +
                    BILL_FIELDS[1] + " text, " +
                    BILL_FIELDS[2] + " text, " +
                    BILL_FIELDS[3] + " text" + // numeric(15,2)
                    " );" ;
    // numeric(15,2) creates a fixed-point numeric data type
    // rather than just using a standard float
    // prevents useless decimal values

    // used for easier querying of db
    public static final String BILL_SELECT_ALL = "SELECT * FROM " + BILL_TABLE_NAME;
    public static String billSelectById(int billId) {

        return "SELECT * FROM " + BILL_TABLE_NAME + " WHERE id='" + billId + "'";

    }

    // helper functions for datetime formatting
    /*public static String formatDaysOfWeek(String daysOfWeek) {

        String result = "";

        if (daysOfWeek.length() < 4) {
            return daysOfWeek;
        }

        for (int i = 0; i < daysOfWeek.length(); i++) {

            result += daysOfWeek.charAt(i);
            ++i;
            result += daysOfWeek.charAt(i);
            if (i < daysOfWeek.length() - 2) {
                result += ", ";
            }

        }
        return result;

    }*/

    /*public static String formatTime(String datetime) {

        return datetime.split(" ")[1].substring(0, 5);

    }*/

    /////////////////////////  Accounts Table  /////////////////////////
    public static final String ACCOUNT_TABLE_NAME = "accountDb";

    // all fields in Accounts
    public static final String[] ACCOUNT_FIELDS = {"id", "account_name", "starting_balance"};
    // creates table
    private static final String ACCOUNT_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE_NAME + " (" +
                    ACCOUNT_FIELDS[0] + " INTEGER PRIMARY KEY, " +
                    ACCOUNT_FIELDS[1] + " text, " +
                    ACCOUNT_FIELDS[2] + " REAL" + // numeric(15,2)
                    " );" ;
    // numeric(15,2) creates a fixed-point numeric data type
    // rather than just using a standard float
    // prevents useless decimal values

    // used for easier querying of db
    public static final String ACCOUNT_SELECT_ALL = "SELECT * FROM " + ACCOUNT_TABLE_NAME;
    public static String accountSelectById(int accountId) {

        return "SELECT * FROM " + ACCOUNT_TABLE_NAME + " WHERE id='" + accountId + "'";

    }

    /////////////////////////  Expense Table  /////////////////////////
    public static final String EXPENSE_TABLE_NAME = "expenseDb";

    // names fields in the expense database
    public static final String[] EXPENSE_FIELDS = {"id", "expense_name", "expense_account", "expense_amount"};

    public static final String EXPENSE_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + EXPENSE_TABLE_NAME + " (" +
                    EXPENSE_FIELDS[0] + " INTEGER PRIMARY KEY, " +
                    EXPENSE_FIELDS[1] + " text, " +
                    EXPENSE_FIELDS[2] + " text, " +
                    EXPENSE_FIELDS[3] + " REAL" +
                    " );" ;

    public static final String EXPENSE_SELECT_ALL = "SELECT * FROM " + EXPENSE_TABLE_NAME;
    public static String expenseSelectById( int expenseId ){
        return "SELECT * FROM " + EXPENSE_TABLE_NAME + "WHERE id='" + expenseId + "'";
    }

    /////////////////////////  Expense Table  /////////////////////////
    public static final String TRANSFER_TABLE_NAME = "transferDb";

    // names fields in the transfer database
    public static final String[] TRANSFER_FIELDS = {"id", "transfer_from_acct", "transfer_to_acct",
            "transfer_amount"};

    public static final String TRANSFER_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TRANSFER_TABLE_NAME + " (" +
                    TRANSFER_FIELDS[0] + " INTEGER PRIMARY KEY, " +
                    TRANSFER_FIELDS[1] + " text, " +
                    TRANSFER_FIELDS[2] + " text, " +
                    TRANSFER_FIELDS[3] + " REAL" +
                    " );" ;

    public static final String TRANSFER_SELECT_ALL = "SELECT * FROM " + TRANSFER_TABLE_NAME;
    public static String transferSelectById( int transferId ){
        return "SELECT * FROM " + TRANSFER_TABLE_NAME + "WHERE id='" + transferId + "'";
    }

    /////////////////////////  Deposit Table  /////////////////////////
    // TODO: Add a deposits table to the database

    /////////////////////////  Withdraw Table  /////////////////////////
    // TODO: Add a withdraws table to the database

    // Constructor required by SQlineOpenHelper
    classDbHelper(Context context){
        super( context , DB_NAME, null, DB_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL(BILL_TABLE_CREATE);
        db.execSQL(ACCOUNT_TABLE_CREATE);
        db.execSQL(EXPENSE_TABLE_CREATE);
        db.execSQL(TRANSFER_TABLE_CREATE);

    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ){

        db.execSQL("DROP TABLE IF EXISTS " + BILL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EXPENSE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSFER_TABLE_NAME);

    }

}
