package com.example.kylewhite.moneymanagement_v2;

/**
 * Created by Kyle on 1/4/2016.
 * Class that creates a custom list adapter'
 *
 * Creating a custom list view that displays two lines instead of the standard one.
 */
public class classBillItem {

    private final String billName;
    private final String billAccount;
    private final String amount;
    private final int billId;

    classBillItem( String name, String billAccount, String amount, int billId ){

        super();
        this.billName = name;
        this.amount = amount;
        this.billId = billId;
        this.billAccount = billAccount;


    }

    public String getBillName() { return billName; }
    public String getBillAccount () { return billAccount; }
    public String getAmount() { return amount; }
    public int getAmountInt() { return Integer.valueOf(amount); }
    public int getBillId() { return billId; }

}
