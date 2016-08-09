package vn.softfront.worktask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import data.Database;
import model.Task;
import util.Constrans;
import util.TimeUtils;
import util.DialogUntil;

/**
 * Created by nguyen.hoai.duc on 7/26/2016.
 */
public class TaskActivity extends Activity implements View.OnClickListener {

    private ImageView mImageViewBack;

    private EditText mEditTextTitle;
    private EditText mEditTextDescription;
    private EditText mEditTextTimeStart;
    private EditText mEditTextDateStart;
    private EditText mEditTextTimeDue;
    private EditText mEditTextDateDue;
    private EditText mEditTextEstimate;

    private Spinner mSpinnerPriority;
    private Spinner mSpinnerStatus;

    private TextView mTextViewDone;
    private TextView mTextViewHeader;
    private TextView mTextViewWarning;
    private TextView mTextViewStatus;

    private Database mDatabase;
    private TimeUtils mTimeUtils;

    private String[] mStringMode;
    private String[] mStringPriority;
    private String[] mStringStatus;
    private String[] mStringHeader;
    private String[] mStringWaring;
    private String mMode;
    private int mId;
    private boolean flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        initData();
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_text_start_time:
                showTimePicker(mEditTextTimeStart);
                break;
            case R.id.edit_text_start_date:
                showDatePicker(mEditTextDateStart);
                break;
            case R.id.edit_text_due_time:
                showTimePicker(mEditTextTimeDue);
                break;
            case R.id.edit_text_due_date:
                showDatePicker(mEditTextDateDue);
                break;
            case R.id.image_view_back:
                finish();
                break;
            case R.id.text_view_done:
                saveTask();
                break;
        }
    }

    public void onTitleTextChance() {
        mEditTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTextViewHeader.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void onEstimateTextChange() {
        flag = true;
        mEditTextEstimate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float estimate = 0;
                if (flag == true)
                    try {
                        estimate = Float.parseFloat(s.toString());
                        mEditTextTimeDue.setText(TimeUtils.getDueTime(mTimeUtils.timeToMilisecond
                                (mEditTextTimeStart.getText().toString()
                                        , mEditTextDateStart.getText().toString()), estimate));
                        mEditTextDateDue.setText(TimeUtils.getDueDate(mTimeUtils.timeToMilisecond
                                (mEditTextTimeStart.getText().toString()
                                        , mEditTextDateStart.getText().toString()), estimate));
                    } catch (Exception e) {

                    }
                setWarning(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void initDataEdit() {
        mDatabase.open();
        Task mTask = mDatabase.getTask(mId);
        mEditTextTitle.setText(mTask.getTitle());
        mTextViewStatus.setVisibility(View.VISIBLE);
        mSpinnerStatus.setVisibility(View.VISIBLE);
        mEditTextDescription.setText(mTask.getDescription());
        for (int i = 0; i < mStringPriority.length; i++)
            if (mTask.getPriority().equals(mStringPriority[i]))
                mSpinnerPriority.setSelection(i);
        for (int i = 0; i < mStringStatus.length; i++)
            if (mTask.getStatus().equals(mStringStatus[i]))
                mSpinnerStatus.setSelection(i);
        mEditTextTimeStart.setText(mTask.getStarttime());
        mEditTextDateStart.setText(mTask.getStartdate());
        mEditTextTimeDue.setText(mTask.getDuetime());
        mEditTextDateDue.setText(mTask.getDuedate());
        mEditTextEstimate.setText(mTask.getEstimate());
        setWarning(mTask);
    }


    public void initView() {
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);
        mEditTextTitle = (EditText) findViewById(R.id.edit_text_title);
        mEditTextDescription = (EditText) findViewById(R.id.edit_text_description);
        mEditTextTimeStart = (EditText) findViewById(R.id.edit_text_start_time);
        mEditTextDateStart = (EditText) findViewById(R.id.edit_text_start_date);
        mEditTextTimeDue = (EditText) findViewById(R.id.edit_text_due_time);
        mEditTextDateDue = (EditText) findViewById(R.id.edit_text_due_date);
        mEditTextEstimate = (EditText) findViewById(R.id.edit_text_estimate);

        mSpinnerPriority = (Spinner) findViewById(R.id.spinner_priority);
        mSpinnerStatus = (Spinner) findViewById(R.id.spinner_status);

        mTextViewDone = (TextView) findViewById(R.id.text_view_done);
        mTextViewHeader = (TextView) findViewById(R.id.text_view_header);
        mTextViewWarning = (TextView) findViewById(R.id.text_view_warning);
        mTextViewStatus = (TextView) findViewById(R.id.text_view_status);

        mTextViewDone.setOnClickListener(this);

        mEditTextTimeStart.setOnClickListener(this);
        mEditTextDateStart.setOnClickListener(this);
        mEditTextTimeDue.setOnClickListener(this);
        mEditTextDateDue.setOnClickListener(this);
        mImageViewBack.setOnClickListener(this);

        if (mMode.equals(mStringMode[0])) {
            mTextViewHeader.setText(mStringHeader[0]);
            initDataEdit();

        } else if (mMode.equals(mStringMode[1])) {
            onTitleTextChance();
            onEstimateTextChange();
            initTime();
            mTextViewDone.setVisibility(View.VISIBLE);
            mTextViewWarning.setVisibility(View.VISIBLE);
            mTextViewHeader.setText(mStringHeader[1]);
        } else if (mMode.equals(mStringMode[2])) {
            onTitleTextChance();
            onEstimateTextChange();
            mTextViewDone.setVisibility(View.VISIBLE);
            mTextViewHeader.setText(mStringHeader[2]);
            initDataEdit();
        }
    }

    public void initData() {
        mDatabase = new Database(this);
        mTimeUtils = new TimeUtils();
        mStringMode = getResources().getStringArray(R.array.mode);
        mStringHeader = getResources().getStringArray(R.array.header);
        mStringPriority = getResources().getStringArray(R.array.priority);
        mStringStatus = getResources().getStringArray(R.array.status);
        mStringWaring = getResources().getStringArray(R.array.warning);
        Bundle mBundle = getIntent().getExtras();
        mMode = mBundle.getString(this.getResources().getString(R.string.mode));
        if (mMode.equals(mStringMode[0]) || mMode.equals(mStringMode[2]))
            mId = mBundle.getInt(this.getResources().getString(R.string.id));
    }

    public void initTime() {
        mEditTextTimeStart.setText(TimeUtils.getCurrentTime());
        mEditTextDateStart.setText(TimeUtils.getCurrentDate());
        mEditTextTimeDue.setText(TimeUtils.getCurrentTime());
        mEditTextDateDue.setText(TimeUtils.getCurrentDate());
    }

    public void showTimePicker(final EditText mEditText) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String hour = selectedHour + "";
                String minute = selectedMinute + "";
                if (selectedHour < Constrans.minute)
                    hour = "0" + hour;
                if (selectedMinute < Constrans.minute)
                    minute = "0" + minute;
                flag = false;
                mEditText.setText(hour + ":" + minute);
                setEstimateTime();
                setWarning(null);
                flag = true;
            }
        }, hour, minute, true);
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
                String day = dayOfMonth + "";
                String month = monthOfYear + 1 + "";
                if (dayOfMonth < Constrans.minute)
                    day = "0" + day;
                if (monthOfYear + 1 < Constrans.minute)
                    month = "0" + month;
                flag = false;
                mEditText.setText(day + "/" + month + "/" + year);
                setEstimateTime();
                setWarning(null);
                flag = true;
            }
        }, year, month, day);
        mDatePicker.show();
    }

    public void setEstimateTime() {
        if (!mEditTextTimeStart.getText().toString().equals("")
                && !mEditTextDateStart.getText().toString().equals("")
                && !mEditTextTimeDue.getText().toString().equals("")
                && !mEditTextDateDue.getText().toString().equals("")) {
            long start = mTimeUtils.timeToMilisecond(mEditTextTimeStart.getText().toString()
                    , mEditTextDateStart.getText().toString());
            long due = mTimeUtils.timeToMilisecond(mEditTextTimeDue.getText().toString()
                    , mEditTextDateDue.getText().toString());
            String mEstimateTime = mTimeUtils.getEstimateTime(start, due);
            mEditTextEstimate.setText(mEstimateTime);
        }
    }

    public void setWarning(Task mTask) {
        mTextViewWarning.setTextColor(getResources().getColor(R.color.color_text));
        int mFreeTime[] = null;
        if (mTask != null)
            mFreeTime = mTimeUtils.getFreeTime(System.currentTimeMillis(), mTimeUtils.timeToMilisecond
                    (mTask.getDuetime(), mTask.getDuedate()));
        else if (!mEditTextTimeStart.getText().toString().equals("")
                && !mEditTextDateStart.getText().toString().equals("")
                && !mEditTextTimeDue.getText().toString().equals("")
                && !mEditTextDateDue.getText().toString().equals("")) {
            long start = mTimeUtils.timeToMilisecond(mEditTextTimeStart.getText().toString()
                    , mEditTextDateStart.getText().toString());
            long due = mTimeUtils.timeToMilisecond(mEditTextTimeDue.getText().toString()
                    , mEditTextDateDue.getText().toString());
            mFreeTime = mTimeUtils.getFreeTime(start, due);
        }
        if (mFreeTime[0] > Constrans.maxFreeHour)
            mTextViewWarning.setText(mStringWaring[0]);
        else if (mFreeTime[0] < Constrans.maxFreeHour && mFreeTime[0] > Constrans.hour)
            mTextViewWarning.setText(mStringWaring[1] + " " + mFreeTime[0] + " " + mStringWaring[2]);
        else if (mFreeTime[0] == Constrans.hour)
            mTextViewWarning.setText(mStringWaring[1] + " " + mFreeTime[0] + " " + mStringWaring[2]);
        else if (mFreeTime[0] < Constrans.hour && mFreeTime[1] >= Constrans.minFreeMinutes)
            mTextViewWarning.setText(mStringWaring[1] + " " + mFreeTime[1] + " " + mStringWaring[3]);
        else if (mFreeTime[0] < Constrans.hour && mFreeTime[1] < Constrans.minFreeMinutes) {
            mTextViewWarning.setText(mStringWaring[4]);
            mTextViewWarning.setTextColor(getResources().getColor(R.color.color_Priorities_Immediate));
        }
    }

    public void saveTask() {
        mDatabase.open();
        Task mTask = new Task();
        mTask.setTitle(mEditTextTitle.getText().toString());
        mTask.setDescription(mEditTextDescription.getText().toString());
        mTask.setPriority(mSpinnerPriority.getSelectedItem().toString());
        mTask.setEstimate(mEditTextEstimate.getText().toString());
        mTask.setStatus(getResources().getString(R.string.status_new));
        mTask.setStarttime(mEditTextTimeStart.getText().toString());
        mTask.setStartdate(mEditTextDateStart.getText().toString());
        mTask.setDuetime(mEditTextTimeDue.getText().toString());
        mTask.setDuedate(mEditTextDateDue.getText().toString());
        try {
            char c = mEditTextTitle.getText().toString().charAt(0);
            int m = Integer.parseInt(mEditTextEstimate.getText().toString());
            if (mMode.equals(mStringMode[1])) {
                long n = mDatabase.addTask(mTask, mTask.getTitle());
                if (n == -1) {
                    showDialog(this.getResources().getString(R.string.duplicated_task));
                } else {
                    Toast.makeText(this, getResources().getText(R.string.add_task_success), Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            } else {
                mTask.setStatus(mSpinnerStatus.getSelectedItem().toString());
                mDatabase.editTask(mId, mTask);
                Toast.makeText(this, getResources().getText(R.string.edit_task), Toast.LENGTH_LONG).show();
                onBackPressed();
            }
            mDatabase.close();
        } catch (NumberFormatException e) {
            showDialog(this.getResources().getString(R.string.empty_field_estimate));
        } catch (StringIndexOutOfBoundsException e) {
            showDialog(this.getResources().getString(R.string.empty_title_validate));
        }
    }

    public void showDialog(String message) {
        DialogUntil.showAlertDialog(this, null, message,
                null, this.getResources().getString(R.string.ok), null,
                null, null, null, 0, null);
    }


}
