package com.example.nguyenhoaiduc.worktask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import data.DBAdapter;
import model.Task;

/**
 * Created by nguyen.hoai.duc on 7/26/2016.
 */
public class AddTaskActivity extends Activity implements View.OnClickListener {

    private ImageView mImageViewBack;
    private ImageView mImageViewTimeStart;
    private ImageView mImageViewDateStart;
    private ImageView mImageViewTimeDue;
    private ImageView mImageViewDateDue;

    private EditText mEditTextTitle;
    private EditText mEditTextDescription;
    private EditText mEditTextTimeStart;
    private EditText mEditTextDateStart;
    private EditText mEditTextTimeDue;
    private EditText mEditTextDateDue;
    private EditText mEditTextEstimate;

    private Spinner mSpinnerPriority;

    private TextView mTextViewDone;

    private DBAdapter mDbAdapter;


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

        mEditTextTitle = (EditText) findViewById(R.id.edit_text_title);
        mEditTextDescription = (EditText) findViewById(R.id.edit_text_description);
        mEditTextTimeStart = (EditText) findViewById(R.id.edit_text_start_time);
        mEditTextDateStart = (EditText) findViewById(R.id.edit_text_start_date);
        mEditTextTimeDue = (EditText) findViewById(R.id.edit_text_due_time);
        mEditTextDateDue = (EditText) findViewById(R.id.edit_text_due_date);
        mEditTextEstimate = (EditText) findViewById(R.id.edit_text_estimate);

        mSpinnerPriority = (Spinner) findViewById(R.id.spinner_priority);

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
                saveTask();
                finish();
                break;
        }
    }

    public void showTimePicker(final EditText mEditText) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
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
        Calendar mCurrentTime = Calendar.getInstance();
        int day = mCurrentTime.get(Calendar.DAY_OF_MONTH);
        int month = mCurrentTime.get(Calendar.MONTH);
        int year = mCurrentTime.get(Calendar.YEAR);
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

    public void saveTask() {
        mDbAdapter = new DBAdapter(this);
        mDbAdapter.open();

        Task mTask = new Task();
        mTask.setTitle(mEditTextTitle.getText().toString());
        mTask.setDescription(mEditTextDescription.getText().toString());
        mTask.setPriority(mSpinnerPriority.getSelectedItem().toString());
        mTask.setEstimate(mEditTextEstimate.getText().toString());
        mTask.setStarttime(mEditTextTimeStart.getText().toString());
        mTask.setStartdate(mEditTextDateStart.getText().toString());
        mTask.setDuetime(mEditTextTimeDue.getText().toString());
        mTask.setDuedate(mEditTextDateDue.getText().toString());

        long n = mDbAdapter.addTask(mTask);
        mDbAdapter.close();
        Toast.makeText(this,"Add sucess rowCount = "+n,Toast.LENGTH_LONG).show();
    }
}
