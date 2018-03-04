package com.example.sagar.billingapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity  {

    Button b;
    EditText CustName;
    EditText CustPhone;
    EditText CustAddr;
    RadioButton Wholesalerb;
    RadioButton Retailrb;
    char type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b = (Button) findViewById(R.id.NextButton);
        CustName = (EditText) findViewById(R.id.CustName);
        CustPhone = (EditText) findViewById(R.id.CustPhone);
        CustAddr = (EditText) findViewById(R.id.CustAddr);
        Wholesalerb = (RadioButton) findViewById(R.id.WholesalerRadioButton);
        Retailrb = (RadioButton) findViewById(R.id.RetailerRadioButton);

    }

    public void ClickFunction(View view) {
        Intent intent = new Intent(this, ItemSearch.class);
        intent.putExtra("CustomerName", CustName.getText().toString());
        intent.putExtra("CustomerPhone", CustPhone.getText().toString());
        intent.putExtra("CustomerAddress", CustAddr.getText().toString());
        if(Wholesalerb.isChecked() == true){
            type = 'W';
        }
        else if(Retailrb.isChecked()){
            type = 'R';
        }
        intent.putExtra("Type",type);
        startActivity(intent);
    }
}
