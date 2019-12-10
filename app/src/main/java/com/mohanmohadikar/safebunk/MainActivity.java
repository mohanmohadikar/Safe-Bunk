package com.mohanmohadikar.safebunk;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.safebunk.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    Double lattended, lheld, lcriteria;
    TextInputEditText attended, held, criteria;
    TextInputLayout attendedlayout, heldlayout, criterialayout;
    TextView percentage, suggestion;
    Button calculate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        attended = (TextInputEditText)findViewById(R.id.attended);
        held = (TextInputEditText)findViewById(R.id.held);
        criteria = (TextInputEditText)findViewById(R.id.criteria);
        attendedlayout = (TextInputLayout) findViewById(R.id.attendedlayout);
        heldlayout = (TextInputLayout) findViewById(R.id.heldlayout);
        criterialayout = (TextInputLayout) findViewById(R.id.criterialayout);
        calculate = (Button) findViewById(R.id.calculate);
        percentage = (TextView)findViewById(R.id.percentage);
        suggestion = (TextView)findViewById(R.id.suggestion);




        calculate.setOnClickListener(v->{





            if(isEmpty(attended) || isEmpty(held) || isEmpty(criteria)){
                Toast.makeText(this, " Given fields cannot be empty. ", Toast.LENGTH_SHORT).show();
            }


            else{



                lattended = Double.valueOf(attended.getEditableText().toString());
                lheld = Double.valueOf(held.getEditableText().toString());
                lcriteria = Double.valueOf(criteria.getEditableText().toString());
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }


                if(lattended>lheld){
                    held.setError("Lectures attended cannot be lesser than lectures held.");
                }
                else if(lcriteria > 100.0){
                    criteria.setError("Eligibility criteria cannot be greater than 100%.");
                }
                else if(lattended<lheld  &&lcriteria == 100.0){
                    percentage.setText(String.valueOf(PercentageAttended(lattended,lheld)) + "%");
                    suggestion.setText("It's impossible to achieve this.");
                }
                else{
                    percentage.setText(String.valueOf(PercentageAttended(lattended,lheld)) + "%");


                    if(canBunk(lattended,lheld,lcriteria)==0){
                        suggestion.setText("You don't have any safe bunks now.");
                    }
                    if(canBunk(lattended,lheld,lcriteria)>0){
                        suggestion.setText("You should attend next "+Integer.valueOf((int) Math.ceil(canBunk(lattended,lheld,lcriteria))).toString()+" lectures.");
                    }
                    if(canBunk(lattended,lheld,lcriteria)<0){
                        suggestion.setText("You can bunk next "+Integer.valueOf((int) Math.ceil((-1)*canBunk(lattended,lheld,lcriteria))).toString()+" lectures.");
                    }

                    //   suggestion.setText(String.valueOf(canBunk(lattended,lheld,lcriteria)));
                    // percentage.setText(String.valueOf((lattended*100/ lheld)));
                }

            }



        });

       // percentage.setText(PercentageAttended(lattended, lheld).toString());
    }

    private Double PercentageAttended(Double attended, Double held){
        return round(Double.valueOf(attended*100/held),2);
    }

    private Double canBunk(Double attended, Double held, Double criteria){

        Double aNow = attended;

        Double pNow = PercentageAttended(attended,held);
      //  Math.floor(PercentageAttended(attended,criteria));

        if(pNow<criteria){


            do{

                attended++;
                held++;

                pNow = PercentageAttended(attended,held);

            }while (pNow<criteria);

            return attended-aNow;


        }

        if(pNow>criteria){


            do{

                attended--;
                held++;

                pNow = PercentageAttended(attended,held);

            }while (pNow>criteria);

            return attended-aNow;
        }


        return attended-aNow;
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    private boolean isEmpty(TextInputEditText etText) {
        return etText.getEditableText().toString().trim().length() == 0;
    }

}
