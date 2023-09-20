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
import com.amit.financeManager.transporters.SpinnerNameTransporter;

public class BilansIncomeFragment extends Fragment implements ClickListener {

    private int transaction_count = 0;
    private double transaction_money = 0;
    private TextView transMoney, transCount;
    private Context context;
    private List<Event> eventList = new ArrayList<Event>();

    public BilansIncomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        ((MainActivity) getActivity()).getSupportActionBar().setSubtitle(SpinnerNameTransporter.getName());
        return inflater.inflate(R.layout.fragment_bilans_income, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        transMoney = getView().findViewById(R.id.bilans_income_transaction_money);
        transCount = getView().findViewById(R.id.bilans_income_transaction_count);
        getData(view);
    }

    private void getData(View view) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        eventList = databaseHelper.getAllEventsIncomeByMonth(SpinnerNameTransporter.getName());

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bilans_income_events_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter eventAdapter = new RecyclerViewAdapter(eventList, this, new RecyclerViewAdapter.OnQuantityChangeListener() {
            @Override
            public void onQuantityChange(double change) {
                transaction_money += change;
                setColor(transaction_money, transMoney);
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
        transaction_count = eventList.size();
        transCount.setText(Integer.toString(transaction_count));
    }

    private void getFullMoneyReport(){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        transaction_money= databaseHelper.getMoneyIncomeByMonth(SpinnerNameTransporter.getName());
        setColor(transaction_money, transMoney);
    }

    private void setColor(double price, TextView view){
        DecimalFormat decim = new DecimalFormat("0.00");
        String priceForm = decim.format(transaction_money);

        if(price > 0.00){
            view.setTextColor(Color.parseColor("#048838"));
            view.setText("Rs. "+priceForm);
        }else if(price == 0.00){
            view.setTextColor(Color.BLACK);
            view.setText("Rs. "+ priceForm);
        }else if(price < 0.00){
            view.setTextColor(Color.RED);
            view.setText("Rs. "+priceForm);
        }
    }

    @Override
    public void onClick(Object data) {
    }
}