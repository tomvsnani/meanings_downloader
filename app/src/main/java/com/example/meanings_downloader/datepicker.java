package com.example.meanings_downloader;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class datepicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener{


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c=Calendar.getInstance();
        int Year=c.get(Calendar.HOUR_OF_DAY);
        int Month=c.get(Calendar.MINUTE);
        int day=c.get(Calendar.DAY_OF_MONTH);
        return new TimePickerDialog(getActivity(),this,Year,Month,false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.d("DAYIS",String.valueOf(minute));
    }
}
