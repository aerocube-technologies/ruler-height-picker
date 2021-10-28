package tech.aerocube.heightpickerexample;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;

import android.widget.TextView;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.Locale;

import tech.aerocube.rulerheightpicker.views.AeroRulerHeightPicker;


public class MainActivity extends AppCompatActivity {
    private AeroRulerHeightPicker myScrollingValuePicker;

    private TextView rulerText;

    MaterialButtonToggleGroup toggleGroup;
    boolean matric=true;
    float lastvalue=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rulerText = (TextView) findViewById(R.id.rulerview_value);
        initRulerView();
        toggleGroup=findViewById(R.id.toggleButton);
        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if(checkedId==R.id.cm){
                    switchToCM();
                }
                if(checkedId==R.id.ft){
                    switchToFT();
                }
            }
        });

    }

    private void switchToFT() {
        myScrollingValuePicker.setMaxValue(120);
        myScrollingValuePicker.setMinValue(39);
        myScrollingValuePicker.setValueTypeMultiple(12);
        myScrollingValuePicker.setMatric(false);
        matric=false;
        myScrollingValuePicker.initializeView(this,true);
    }

    private void switchToCM() {
        myScrollingValuePicker.setMaxValue(250);
        myScrollingValuePicker.setMinValue(100);
        myScrollingValuePicker.setValueTypeMultiple(5);
        myScrollingValuePicker.setMatric(true);
        matric=true;
        myScrollingValuePicker.initializeView(this,true);
    }

    /**
     * initRulerView
     */
    private void initRulerView() {
        myScrollingValuePicker = findViewById(R.id.myScrollingValuePicker);
        myScrollingValuePicker.setHapticFeedbackEnabled(true);
        myScrollingValuePicker.setOnAeroPickerScrollListener(new AeroRulerHeightPicker.OnAeroPickerScrollListener() {
            @Override
            public void onPickerScrolling(float value) {

                if(!matric) {
                    int feet = (int) (value / 12);
                    int inch = (int) (value - (feet * 12));
                    rulerText.setText(String.format(Locale.US, "%d'%d\"", feet, inch));
                }
                else{
                    rulerText.setText(String.format(Locale.US, "%d", (int)value));
                }

            }

            @Override
            public void onPickerStopped(float value) {
                if(!matric) {
                    int feet = (int) (value / 12);
                    int inch = (int) (value - (feet * 12));
                    rulerText.setText(String.format(Locale.US, "%d'%d\"", feet, inch));
                }
                else{
                    rulerText.setText(String.format(Locale.US, "%d", (int)value));
                }
            }
        });



    }


}