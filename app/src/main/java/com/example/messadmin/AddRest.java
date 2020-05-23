package com.example.messadmin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.messadmin.Login.HomeActivity;
import com.example.messadmin.Utils.FireBaseMethods;

public class AddRest extends AppCompatActivity {

    private EditText name,type,rating,time,price;
    private Button submit;
    private FireBaseMethods fireBaseMethods;
    private Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rest);

        name=findViewById(R.id.et_rname);
        rating=findViewById(R.id.et_rating);
        type=findViewById(R.id.et_type);
        price=findViewById(R.id.et_avg_price);
        time=findViewById(R.id.et_time);

        mContext=AddRest.this;
        fireBaseMethods=new FireBaseMethods(mContext);

        submit=findViewById(R.id.bt_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fireBaseMethods.addNewRestaurant(name.getText().toString(),type.getText().toString(),time.getText().toString(),rating.getText().toString(),price.getText().toString());
                startActivity(new Intent(AddRest.this, HomeActivity.class));
                finish();
            }
        });
    }
}
