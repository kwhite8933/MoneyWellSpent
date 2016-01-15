package com.example.kylewhite.moneymanagement_v2;

/**
 * Created by Kyle on 1/13/2016.
 *
 */
public class classAccountItem {

    private final String accountName;
    private final String startingBalance;
    private final int accountId;

    classAccountItem( String accountName, String startingBalance, int accountId ){

        super();
        this.accountName = accountName;
        this.startingBalance = startingBalance;
        this.accountId = accountId;

    }

    public String getAccountName(){ return accountName; }
    public String getStartingBalance() { return startingBalance; }
    public int getAccountId() { return accountId; }


}
