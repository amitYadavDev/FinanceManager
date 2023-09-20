package com.amit.financeManager.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.amit.financeManager.MainActivity;
import com.amit.financeManager.R;
import com.amit.financeManager.adapters.RecyclerViewAdapter;
import com.amit.financeManager.databases.DatabaseHelper;
import com.amit.financeManager.listeners.ClickListener;
import com.amit.financeManager.models.Event;
import com.amit.financeManager.transporters.MonthTransporter;

public class IncomeFragment extends Fragment implements ClickListener {

    private int transaction_count = 0;
    private double transaction_money = 0;
    private TextView transMoney, transCount;
    private Context context;
    private List<Event> eventList = new ArrayList<Event>();

    public IncomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        ((MainActivity) getActivity()).getSupportActionBar().setSubtitle(MonthTransporter.getMonth());
        return inflater.inflate(R.layout.fragment_income, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        transMoney = getView().findViewById(R.id.transaction_money);
        transCount = getView().findViewById(R.id.transaction_count);
        getData(view);
    }

    private void getData(View view) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        eventList = databaseHelper.getAllEventsIncome();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.item_events_list_income);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter eventAdapter = new RecyclerViewAdapter(eventList, this, new RecyclerViewAdapter.OnQuantityChangeListener() {
            @Override
            public void onQuantityChange(double change) {
                transaction_money += change;

                DecimalFormat decim = new DecimalFormat("0.00");
                String price = decim.format(transaction_money);

                if (transaction_money > 0.00) {
                    transMoney.setTextColor(Color.parseColor("#048838"));
                    transMoney.setText("Rs. " +price);
                } else if (transaction_money == 0.00) {
                    transMoney.setTextColor(Color.BLACK);
                    transMoney.setText("Rs. " +price);
                } else if (transaction_money < 0.00) {
                    transMoney.setTextColor(Color.RED);
                    transMoney.setText("Rs. " +price);
                }
            }

            @Override
            public void onTransactionChange(int change) {
                transaction_count += change;
                transCount.setText(Integer.toString(transaction_count));
            }
        });

        getFullMoneyReport();
        getFullTransCountReport();

        recyclerView.setAdapter(eventAdapter);
        eventAdapter.setEventList(eventList);
    }

    private void getFullTransCountReport(){
        transCount = getView().findViewById(R.id.transaction_count_income);
        transaction_count = eventList.size();
        transCount.setText(Integer.toString(transaction_count));
    }

    private void getFullMoneyReport(){
        transMoney = getView().findViewById(R.id.transaction_money_income);

        transaction_money = 0;
        for(Event event : eventList){
            double price = Double.parseDouble(event.getPrice());
            transaction_money += price;
        }

        DecimalFormat decim = new DecimalFormat("0.00");
        String price = decim.format(transaction_money);

        if(transaction_money > 0.00){
            transMoney.setTextColor(Color.parseColor("#048838"));
            transMoney.setText("Rs. " +price);
        }else if(transaction_money == 0.00){
            transMoney.setTextColor(Color.BLACK);
            transMoney.setText("Rs. " +price);
        }else if(transaction_money < 0.00){
            transMoney.setTextColor(Color.RED);
            transMoney.setText("Rs. " +price);
        }
    }

    @Override
    public void onClick(Object data) {
    }
}