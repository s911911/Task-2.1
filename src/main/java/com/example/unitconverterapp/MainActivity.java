package com.example.unitconverterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerFrom, spinnerTo;
    private EditText editTextValue;
    private Button buttonConvert;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        editTextValue = findViewById(R.id.editTextValue);
        buttonConvert = findViewById(R.id.buttonConvert);
        textViewResult = findViewById(R.id.textViewResult);

        //
        String[] units = {
                "Inch", "Foot", "Yard", "Mile",
                "Centimeter", "Meter", "Kilometer",
                "Pound", "Ounce", "Ton",
                "Gram", "Kilogram", "Celsius", "Fahrenheit", "Kelvin"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_main_item,
                units
        );
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromUnit = spinnerFrom.getSelectedItem().toString();
                String toUnit = spinnerTo.getSelectedItem().toString();


                if (editTextValue.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter the value you want to convert!", Toast.LENGTH_SHORT).show();
                    return;
                }

                double inputValue;
                try {
                    inputValue = Double.parseDouble(editTextValue.getText().toString().trim());
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "The input is invalid, please enter the number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (fromUnit.equals(toUnit)) {
                    textViewResult.setText("Same unit, no conversion required:" + inputValue);
                    return;
                }

                double result = convertUnits(fromUnit, toUnit, inputValue);

                textViewResult.setText(String.valueOf(result));
            }
        });
    }

    private double convertUnits(String fromUnit, String toUnit, double value) {

        double baseValue;

        if (isLengthUnit(fromUnit)) {
            baseValue = toMeter(fromUnit, value);
            return fromMeter(toUnit, baseValue);
        }

        if (isWeightUnit(fromUnit)) {
            baseValue = toKilogram(fromUnit, value);
            return fromKilogram(toUnit, baseValue);
        }

        if (isTemperatureUnit(fromUnit) && isTemperatureUnit(toUnit)) {
            return convertTemperature(fromUnit, toUnit, value);
        }

        Toast.makeText(this, "Conversion between types is not supported at this time!", Toast.LENGTH_SHORT).show();
        return 0;
    }

    private boolean isLengthUnit(String unit) {
        return unit.equals("Inch") || unit.equals("Foot") || unit.equals("Yard") || unit.equals("Mile")
                || unit.equals("Centimeter") || unit.equals("Meter") || unit.equals("Kilometer");
    }

    private boolean isWeightUnit(String unit) {
        return unit.equals("Pound") || unit.equals("Ounce") || unit.equals("Ton")
                || unit.equals("Gram") || unit.equals("Kilogram");
    }

    private boolean isTemperatureUnit(String unit) {
        return unit.equals("Celsius") || unit.equals("Fahrenheit") || unit.equals("Kelvin");
    }

    private double toMeter(String fromUnit, double value) {
        switch (fromUnit) {
            case "Inch":
                // 1 inch = 2.54 cm,0.0254 m
                return value * 0.0254;
            case "Foot":
                // 1 foot = 30.48 cm,0.3048 m
                return value * 0.3048;
            case "Yard":
                // 1 yard = 0.9144 m
                return value * 0.9144;
            case "Mile":
                // 1 mile = 1609.34 m
                return value * 1609.34;
            case "Centimeter":
                // 1 cm = 0.01 m
                return value * 0.01;
            case "Kilometer":
                // 1 km = 1000 m
                return value * 1000.0;
            case "Meter":
            default:
                return value;
        }
    }

    private double fromMeter(String toUnit, double meterValue) {
        switch (toUnit) {
            case "Inch":
                return meterValue / 0.0254;
            case "Foot":
                return meterValue / 0.3048;
            case "Yard":
                return meterValue / 0.9144;
            case "Mile":
                return meterValue / 1609.34;
            case "Centimeter":
                return meterValue * 100;
            case "Kilometer":
                return meterValue / 1000.0;
            case "Meter":
            default:
                return meterValue;
        }
    }

    private double toKilogram(String fromUnit, double value) {
        switch (fromUnit) {
            case "Pound":
                // 1 pound = 0.453592 kg
                return value * 0.453592;
            case "Ounce":
                // 1 ounce = 28.3495 g = 0.0283495 kg
                return value * 0.0283495;
            case "Ton":
                // 1 ton = 907.185 kg
                return value * 907.185;
            case "Gram":
                // 1 g = 0.001 kg
                return value * 0.001;
            case "Kilogram":
            default:
                return value;
        }
    }

    private double fromKilogram(String toUnit, double kgValue) {
        switch (toUnit) {
            case "Pound":
                return kgValue / 0.453592;
            case "Ounce":
                return kgValue / 0.0283495;
            case "Ton":
                return kgValue / 907.185;
            case "Gram":
                return kgValue * 1000.0;
            case "Kilogram":
            default:
                return kgValue;
        }
    }

    private double convertTemperature(String fromUnit, String toUnit, double value) {
        double celsiusValue;

        // Step1: from -> Celsius
        switch (fromUnit) {
            case "Celsius":
                celsiusValue = value;
                break;
            case "Fahrenheit":
                // C = (F - 32)/1.8
                celsiusValue = (value - 32) / 1.8;
                break;
            case "Kelvin":
                // C = K - 273.15
                celsiusValue = value - 273.15;
                break;
            default:
                celsiusValue = value;
        }

        // Step2: Celsius -> toUnit
        switch (toUnit) {
            case "Celsius":
                return celsiusValue;
            case "Fahrenheit":
                // F = C * 1.8 + 32
                return celsiusValue * 1.8 + 32;
            case "Kelvin":
                // K = C + 273.15
                return celsiusValue + 273.15;
            default:
                return celsiusValue;
        }
    }
}
