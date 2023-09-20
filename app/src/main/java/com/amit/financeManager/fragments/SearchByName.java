package com.amit.financeManager.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.amit.financeManager.R;
import com.amit.financeManager.adapters.RecyclerViewAdapter;
import com.amit.financeManager.databases.DatabaseHelper;
import com.amit.financeManager.listeners.ClickListener;
import com.amit.financeManager.models.Event;

public class SearchByName extends Fragment implements ClickListener {

    private int transaction_count = 0;
    private double transaction_money = 0;
    private Context context;
    private List<Event> eventList = new ArrayList<Event>();
    private TextView transMoney, transCount;
    private EditText nameInput;

    public SearchByName() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_search_by_name, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        transMoney = getView().findViewById(R.id.transaction_money);
        transCount = getView().findViewById(R.id.transaction_count);
        nameInput = getView().findViewById(R.id.item_name_input);

        Button button = getView().findViewById(R.id.button_find_by_name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = nameInput.getText().toString();
                getData(view, input);
                dismissKeyboard(getActivity());
            }
        });
    }

    private void getData(View view, String input) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        eventList = databaseHelper.getAllEventsByName(input);

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.item_events_list_by_name_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
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

        getFullMoneyReport(input);
        getFullTransCountReport();

        recyclerView.setAdapter(eventAdapter);
        eventAdapter.setEventList(eventList);
    }

    private void getFullTransCountReport(){
        transaction_count = eventList.size();
        transCount.setText(Integer.toString(transaction_count));
    }

    private void getFullMoneyReport(String input){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        transaction_money= databaseHelper.getAllMoneyByName(input);
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
            view.setText("Rs. "+priceForm);
        }else if(price < 0.00){
            view.setTextColor(Color.RED);
            view.setText("Rs. "+priceForm);
        }
    }

    @Override
    public void onClick(Object data) {
    }

    private void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }
}