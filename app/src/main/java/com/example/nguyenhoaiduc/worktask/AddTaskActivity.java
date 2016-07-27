package com.example.nguyenhoaiduc.worktask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by nguyen.hoai.duc on 7/26/2016.
 */
public class AddTaskActivity extends Activity implements View.OnClickListener {

    private ImageView mImageViewBack;
    private ImageView mImageViewTimeStart;
    private ImageView mImageViewDateStart;
    private ImageView mImageViewTimeDue;
    private ImageView mImageViewDateDue;

    private EditText mEditTextTimeStart;
    private EditText mEditTextDateStart;
    private EditText mEditTextTimeDue;
    private EditText mEditTextDateDue;
    private EditText mEditTextEstimate;

    private TextView mTextViewDone;

    private TimePicker mTimePicker;
    private DatePicker mDatePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        initView();
    }

    public void initView() {
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);
        mImageViewTimeStart = (ImageView) findViewById(R.id.image_view_time_start_picker);
        mImageViewDateStart = (ImageView) findViewById(R.id.image_view_date_start_picker);
        mImageViewTimeDue = (ImageView) findViewById(R.id.image_view_time_due_picker);
        mImageViewDateDue = (ImageView) findViewById(R.id.image_view_date_due_picker);

        mEditTextTimeStart = (EditText) findViewById(R.id.edit_text_start_time);
        mEditTextDateStart = (EditText) findViewById(R.id.edit_text_start_date);
        mEditTextTimeDue = (EditText) findViewById(R.id.edit_text_due_time);
        mEditTextDateDue = (EditText) findViewById(R.id.edit_text_due_date);
        mEditTextEstimate = (EditText) findViewById(R.id.edit_text_estimate);

        mTextViewDone = (TextView) findViewById(R.id.text_view_done);
        mTextViewDone.setOnClickListener(this);

        mImageViewTimeStart.setOnClickListener(this);
        mImageViewDateStart.setOnClickListener(this);
        mImageViewTimeDue.setOnClickListener(this);
        mImageViewDateDue.setOnClickListener(this);
        mImageViewBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_view_time_start_picker:
                showTimePicker(mEditTextTimeStart);
                break;
            case R.id.image_view_date_start_picker:
                showDatePicker(mEditTextDateStart);
                break;
            case R.id.image_view_time_due_picker:
                showTimePicker(mEditTextTimeDue);
                break;
            case R.id.image_view_date_due_picker:
                showDatePicker(mEditTextDateDue);
                break;
            case R.id.image_view_back:
                finish();
                break;
            case R.id.text_view_done:
                finish();
                break;
        }
    }

    public void showTimePicker(final EditText mEditText) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                mEditText.setText(selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    public void showDatePicker(final EditText mEditText) {
        Calendar mcurrentTime = Calendar.getInstance();
        int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
        int month = mcurrentTime.get(Calendar.MONTH);
        int year = mcurrentTime.get(Calendar.YEAR);
        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        }, year, month, day);
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();
    }
}
