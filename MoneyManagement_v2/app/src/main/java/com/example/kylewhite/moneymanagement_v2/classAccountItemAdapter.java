package com.example.kylewhite.moneymanagement_v2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kyle on 1/13/2016.
 *
 */
public class classAccountItemAdapter extends ArrayAdapter<classAccountItem> {
    private final Context context;
    private final ArrayList<classAccountItem> accountsArrayList;

    public classAccountItemAdapter( Context context, ArrayList<classAccountItem> accountsArrayList ){
        super( context, R.layout.accounts_list, accountsArrayList );

        this.context = context;
        this.accountsArrayList = accountsArrayList;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View accountsList = inflater.inflate(R.layout.accounts_list, parent, false);

        TextView accountsNameView = (TextView) accountsList.findViewById(R.id.tvVAAccountName);
        TextView accountStartingBalanceView = (TextView) accountsList.findViewById(R.id.tvAccountBalance);

        accountsNameView.setText(accountsArrayList.get(position).getAccountName());
        accountStartingBalanceView.setText(accountsArrayList.get(position).getStartingBalance());

        return accountsList;

    }


}
