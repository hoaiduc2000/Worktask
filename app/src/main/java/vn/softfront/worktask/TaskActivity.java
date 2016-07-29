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
import util.ConvertTime;
import util.DialogUntil;

/**
 * Created by nguyen.hoai.duc on 7/26/2016.
 */
public class TaskActivity extends Activity implements View.OnClickListener {

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
    private Spinner mSpinnerStatus;

    private TextView mTextViewDone;
    private TextView mTextViewHeader;
    private TextView mTextViewWarning;
    private TextView mTextViewStatus;

    private Database mDatabase;
    private ConvertTime mConvertTime;

    private String[] mStringMode;
    private String[] mStringPriority;
    private String[] mStringStatus;
    private String[] mStringHeader;
    private String[] mStringWaring;
    private String mMode;
    private int mId;


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
                break;
        }
    }

    public void onTextChance() {
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
        mSpinnerStatus = (Spinner) findViewById(R.id.spinner_status);

        mTextViewDone = (TextView) findViewById(R.id.text_view_done);
        mTextViewHeader = (TextView) findViewById(R.id.text_view_header);
        mTextViewWarning = (TextView) findViewById(R.id.text_view_warning);
        mTextViewStatus = (TextView) findViewById(R.id.text_view_status);


        mTextViewDone.setOnClickListener(this);

        mImageViewTimeStart.setOnClickListener(this);
        mImageViewDateStart.setOnClickListener(this);
        mImageViewTimeDue.setOnClickListener(this);
        mImageViewDateDue.setOnClickListener(this);
        mImageViewBack.setOnClickListener(this);

        if (mMode.equals(mStringMode[0])) {
            mTextViewHeader.setText(mStringHeader[0]);
            initDataEdit();

        } else if (mMode.equals(mStringMode[1])) {
            onTextChance();
            initTime();
            mTextViewDone.setVisibility(View.VISIBLE);
            mTextViewWarning.setVisibility(View.GONE);
            mTextViewHeader.setText(mStringHeader[1]);
        } else if (mMode.equals(mStringMode[2])) {
            mTextViewDone.setVisibility(View.VISIBLE);
            mTextViewHeader.setText(mStringHeader[2]);
            initDataEdit();
        }
    }

    public void initData() {
        mDatabase = new Database(this);
        mConvertTime = new ConvertTime();
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
        mEditTextTimeStart.setText(ConvertTime.getCurrentTime());
        mEditTextDateStart.setText(ConvertTime.getCurrentDate());
        mEditTextTimeDue.setText(ConvertTime.getCurrentTime());
        mEditTextDateDue.setText(ConvertTime.getCurrentDate());
    }

    public void showTimePicker(final EditText mEditText) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if (selectedMinute < Constrans.minute)
                    mEditText.setText(selectedHour + ":" + "0" + selectedMinute);
                else
                    mEditText.setText(selectedHour + ":" + selectedMinute);
                setEstimateTime();
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
                mEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                setEstimateTime();
            }
        }, year, month, day);
        mDatePicker.show();
    }

    public void setWarning(Task mTask) {
        int mFreeTime[] = mConvertTime.getFreeTime(mConvertTime.timeToMilisecond(mTask.getStarttime()
                , mTask.getStartdate()));
        if (mFreeTime[0] > Constrans.maxEstimateHour)
            mTextViewWarning.setText(mStringWaring[0]);
        else if (mFreeTime[0] < Constrans.maxEstimateHour && mFreeTime[0] > Constrans.hour)
            mTextViewWarning.setText(mStringWaring[1] + " " + mFreeTime[0] + " " + mStringWaring[2]);
        else if (mFreeTime[0] == Constrans.hour)
            mTextViewWarning.setText(mStringWaring[1] + " " + mFreeTime[0] + " " + mStringWaring[2]);
        else if (mFreeTime[0] < Constrans.hour && mFreeTime[1] >= Constrans.minEstimateMinutes)
            mTextViewWarning.setText(mStringWaring[1] + " " + mFreeTime[1] + " " + mStringWaring[3]);
        else if (mFreeTime[0] < Constrans.hour && mFreeTime[1] < Constrans.minEstimateMinutes) {
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


        if (mMode.equals(mStringMode[1])) {
            long n = mDatabase.addTask(mTask, mTask.getTitle());
            if (n == 0)
                showDialog(this.getResources().getString(R.string.empty_title_validate));
            else if (n == -1){
                showDialog(this.getResources().getString(R.string.duplicated_task));
            }
            else{
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
    }

    public void showDialog(String message) {
        DialogUntil.showAlertDialog(this, null, message,
                null, this.getResources().getString(R.string.ok), null,
                null, null, null, 0, null);
    }

    public void setEstimateTime() {
        if (!mEditTextTimeStart.getText().toString().equals("")
                && !mEditTextDateStart.getText().toString().equals("")
                && !mEditTextTimeDue.getText().toString().equals("")
                && !mEditTextDateDue.getText().toString().equals("")) {
            long start = mConvertTime.timeToMilisecond(mEditTextTimeStart.getText().toString()
                    , mEditTextDateStart.getText().toString());
            long due = mConvertTime.timeToMilisecond(mEditTextTimeDue.getText().toString()
                    , mEditTextDateDue.getText().toString());
            String mEstimateTime = mConvertTime.getEstimateTime(start, due);
            mEditTextEstimate.setText(mEstimateTime);
        }
    }
}
