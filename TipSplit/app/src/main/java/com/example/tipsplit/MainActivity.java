package com.example.tipsplit;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText totalBillWithTax, numOfPeople;
    private TextView tipAmount, totalWithTip, totalPerPerson;
    private RadioGroup radioGroup;
    RadioButton rButton1, rButton2, rButton3, rButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize application controls
        totalBillWithTax = findViewById(R.id.totalBillWithTax);
        numOfPeople = findViewById(R.id.numOfPeople);
        tipAmount = findViewById(R.id.tipAmount);
        totalWithTip = findViewById(R.id.totalWithTip);
        totalPerPerson = findViewById(R.id.totalPerPerson);
        radioGroup = findViewById(R.id.radioGroup);

        // get all the radio buttons
        rButton1 = findViewById(R.id.rButton1);
        rButton2 = findViewById(R.id.rButton2);
        rButton3 = findViewById(R.id.rButton3);
        rButton4 = findViewById(R.id.rButton4);

        // get the GO button
        Button go = findViewById(R.id.goButton);

        // get the CLEAR button
        Button clear = findViewById(R.id.clear);
    }
    public void onTipSelection(View v){
        int viewId = v.getId();
        if(TextUtils.isEmpty(totalBillWithTax.getText().toString()))
        {
            // clear all the tip selection radio buttons
            rButton1.setChecked(false);
            rButton2.setChecked(false);
            rButton3.setChecked(false);
            rButton4.setChecked(false);
            return;
        }

        // store the value of the Bill entered
        String str  = totalBillWithTax.getText().toString();
        if(str.contains("$"))
        {
            str =  str.substring(1);
        }
        double billAmountEntererd  = Double.parseDouble(str);
        // declare temporary variables to store values
        double tipCalculated = 0.0;
        double totalBillWithTips = 0.0;

        // convert the entered Bill from double to string
        totalBillWithTax.setText("$" + String.format("%.2f", billAmountEntererd));

        // check tip selection through the radio buttons
        if (viewId == R.id.rButton1)
            tipCalculated = (billAmountEntererd * 12.0) / 100;
        else if (viewId == R.id.rButton2)
            tipCalculated = (billAmountEntererd * 15.0) / 100;
        else if (viewId == R.id.rButton3)
            tipCalculated = (billAmountEntererd * 18.0) / 100;
        else if (viewId == R.id.rButton4)
            tipCalculated = (billAmountEntererd * 20.0) / 100;

        tipAmount.setText("$" + String.format("%.2f", tipCalculated));

        // sum the entered bill with the calculated tip amount
        totalBillWithTips = billAmountEntererd + tipCalculated;
        totalWithTip.setText("$" + String.format("%.2f", totalBillWithTips));
    }

    public void onClickGo(View v)
    {
        // check if total bill value is entered
        if(TextUtils.isEmpty(totalBillWithTax.getText().toString()))
        {
            return;
        }

        // check if tip percentage is selected
        if(radioGroup.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(getApplicationContext(),"Select the tip percent",Toast.LENGTH_LONG).show();
            return;
        }

        // check if number of people is specified
        if(TextUtils.isEmpty(numOfPeople.getText().toString()))
        {
            return;
        }

        double totalBillWithTips = Double.parseDouble(totalWithTip.getText().toString().substring(1));
        // calculate total per person
        String personCount = numOfPeople.getText().toString();
        double billPerPerson =  totalBillWithTips / Double.parseDouble(personCount);
        totalPerPerson.setText("$" + String.format("%.2f", billPerPerson));
    }

    public void onClickClear(View v)
    {
        // Clear all the value controls
        totalBillWithTax.setText("");
        tipAmount.setText("");
        totalWithTip.setText("");
        numOfPeople.setText("");
        totalPerPerson.setText("");

        // clear all the radio buttons
        rButton1.setChecked(false);
        rButton2.setChecked(false);
        rButton3.setChecked(false);
        rButton4.setChecked(false);
    }
}