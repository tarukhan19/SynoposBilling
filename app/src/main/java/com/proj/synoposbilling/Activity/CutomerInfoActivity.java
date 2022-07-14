package com.proj.synoposbilling.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.proj.synoposbilling.R;
import com.proj.synoposbilling.databinding.ActivityCustomerInfoBinding;
import com.proj.synoposbilling.databinding.ActivityLoginBinding;

public class CutomerInfoActivity extends AppCompatActivity {
    ActivityCustomerInfoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_customer_info);
        binding.saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CutomerInfoActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


    }

}