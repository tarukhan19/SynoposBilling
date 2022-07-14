package com.proj.synoposbilling.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.proj.synoposbilling.R;
import com.proj.synoposbilling.databinding.ActivitySelectBillTypeBinding;

public class SelectBillTypeActivity extends AppCompatActivity {
    ActivitySelectBillTypeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_select_bill_type);
        binding.takeawayBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectBillTypeActivity.this, CutomerInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        binding.dineinBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectBillTypeActivity.this, CutomerInfoActivity.class);
                startActivity(intent);
            }
        });

        binding.deliveryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectBillTypeActivity.this, CutomerInfoActivity.class);
                startActivity(intent);
            }
        });


    }

}