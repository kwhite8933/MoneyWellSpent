package com.example.kylewhite.moneymanagement_v2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 *
 * Created by Kyle on 1/4/2016.
 */
public class classBillItemAdapter extends ArrayAdapter<classBillItem> {
    private final Context context;
    private final ArrayList<classBillItem> billsArrayList;

    public classBillItemAdapter( Context context, ArrayList<classBillItem> billsArrayList ){
        super( context, R.layout.bills_list, billsArrayList );

        this.context = context;
        this.billsArrayList = billsArrayList;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View billsList = inflater.inflate(R.layout.bills_list, parent, false);

        TextView billsNameView = (TextView) billsList.findViewById(R.id.tvBillName);
        TextView billAmountView = (TextView) billsList.findViewById(R.id.tvBillAmount);

        billsNameView.setText(billsArrayList.get(position).getBillName());
        billAmountView.setText(billsArrayList.get(position).getAmount());

        return billsList;

    }

}
