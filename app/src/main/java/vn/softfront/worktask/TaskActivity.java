package vn.softfront.worktask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import data.Database;
import model.Task;
import model.TimeFree;
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

    private Spinner mSpinnerPriority;
    private Spinner mSpinnerStatus;

    private TextView mTextViewDone;
    private TextView mTextViewHeader;
    private TextView mTextViewWarning;
    private TextView mTextViewStatus;

    private LinearLayout mLinearLayout;
    private FrameLayout mFrameTime;
    private TextView mTextView;
    private FrameLayout[] mFrameLayout;

    private ImageView mImageViewPre;
    private ImageView mImageViewNext;

    private TextView mTextViewTitle;
    private TextView mTextViewDescription;
    private TextView mTextViewPriority;
    private TextView mTextViewStartDate;
    private TextView mTextViewDueDate;
    private TextView mTextViewEstimate;
    private TextView mTextViewEstimateAdd;
    private TextView mTextViewPreDay;
    private TextView mTextViewCurrentDay;
    private TextView mTextViewNextDay;


    private Database mDatabase;
    private TimeUtils mTimeUtils;
    private String[] mStringMode;
    private String[] mStringPriority;
    private String[] mStringStatus;
    private String[] mStringHeader;
    private String[] mStringWaring;
    private String[] mTime;
    private String mMode;
    private String mStartTimeDefault;
    private String mStartDateDefault;
    private String mDueTimeDefault;
    private String mDueDateDefault;

    private String mTimeTemp;
    private String mDateTemp;

    private int mId;
    private int n;
    private Rect mRect;

    private ArrayList<TimeFree> mListTimeFree;
    private ArrayList<Task> mListTask;

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
                showTimePicker(mEditTextTimeStart, 0);
                break;
            case R.id.edit_text_start_date:
                showDatePicker(mEditTextDateStart, 0);
                break;
            case R.id.edit_text_due_time:
                showTimePicker(mEditTextTimeDue, 1);
                break;
            case R.id.edit_text_due_date:
                showDatePicker(mEditTextDateDue, 1);
                break;
            case R.id.image_view_return:
                finish();
                break;
            case R.id.text_view_done:
                saveTask();
                break;
            case R.id.image_view_back:
                n = n - 1;
                initDateBar(n);
                updateTimeTracker(TimeUtils.getCalendar(n)[1]);
                break;
            case R.id.image_view_next:
                n = n + 1;
                initDateBar(n);
                updateTimeTracker(TimeUtils.getCalendar(n)[1]);
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

    public void initDataEdit() {
        getTimeFree();
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
        mTextViewEstimateAdd.setText(mTask.getEstimate());
        setWarning(mTask);
        mDatabase.close();
    }

    public void setPreviewMode() {
        mDatabase.open();
        mEditTextTitle.setFocusable(false);
        mEditTextDescription.setFocusable(false);
        mEditTextTimeStart.setOnClickListener(null);
        mEditTextDateStart.setOnClickListener(null);
        mEditTextTimeDue.setOnClickListener(null);
        mEditTextDateDue.setOnClickListener(null);
        mSpinnerPriority.setEnabled(false);
        mSpinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDatabase.updateStatus(mId, mStringStatus[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void initView() {
        mImageViewBack = (ImageView) findViewById(R.id.image_view_return);
        mEditTextTitle = (EditText) findViewById(R.id.edit_text_title);
        mEditTextDescription = (EditText) findViewById(R.id.edit_text_description);
        mEditTextTimeStart = (EditText) findViewById(R.id.edit_text_start_time);
        mEditTextDateStart = (EditText) findViewById(R.id.edit_text_start_date);
        mEditTextTimeDue = (EditText) findViewById(R.id.edit_text_due_time);
        mEditTextDateDue = (EditText) findViewById(R.id.edit_text_due_date);


        mSpinnerPriority = (Spinner) findViewById(R.id.spinner_priority);
        mSpinnerStatus = (Spinner) findViewById(R.id.spinner_status);

        mTextViewDone = (TextView) findViewById(R.id.text_view_done);
        mTextViewHeader = (TextView) findViewById(R.id.text_view_header);
        mTextViewWarning = (TextView) findViewById(R.id.text_view_warning);
        mTextViewStatus = (TextView) findViewById(R.id.text_view_status);
        mTextViewTitle = (TextView) findViewById(R.id.text_view_title);
        mTextViewDescription = (TextView) findViewById(R.id.text_view_description);
        mTextViewPriority = (TextView) findViewById(R.id.text_view_priority);
        mTextViewStartDate = (TextView) findViewById(R.id.text_view_startdate);
        mTextViewDueDate = (TextView) findViewById(R.id.text_view_duedate);
        mTextViewEstimate = (TextView) findViewById(R.id.text_view_estimate);
        mTextViewEstimateAdd = (TextView) findViewById(R.id.text_view_estimate_add);

        mTextViewTitle.setText(getResources().getString(R.string.title_add) + " ");
        mTextViewDescription.setText(getResources().getString(R.string.description_add) + " ");
        mTextViewPriority.setText(getResources().getString(R.string.priority_add) + "  ");
        mTextViewStartDate.setText(getResources().getString(R.string.startdate_add) + " ");
        mTextViewDueDate.setText(getResources().getString(R.string.duedate_add) + " ");
        mTextViewEstimate.setText(getResources().getString(R.string.estimate_add) + " ");
        mTextViewStatus.setText(getResources().getString(R.string.status_add) + " ");

        mTextViewDone.setOnClickListener(this);

        mEditTextTimeStart.setOnClickListener(this);
        mEditTextDateStart.setOnClickListener(this);
        mEditTextTimeDue.setOnClickListener(this);
        mEditTextDateDue.setOnClickListener(this);
        mImageViewBack.setOnClickListener(this);

        mTextView = (TextView) this.findViewById(R.id.text_view_time);
        mFrameTime = (FrameLayout) this.findViewById(R.id.frame_time);

        mTextViewPreDay = (TextView) findViewById(R.id.text_view_pre_day);
        mTextViewCurrentDay = (TextView) findViewById(R.id.text_view_current_day);
        mTextViewNextDay = (TextView) findViewById(R.id.text_view_next_day);

        mImageViewPre = (ImageView) findViewById(R.id.image_view_back);
        mImageViewNext = (ImageView) findViewById(R.id.image_view_next);
        mImageViewPre.setOnClickListener(this);
        mImageViewNext.setOnClickListener(this);

        n = 0;
        initDateBar(0);
        mLinearLayout = (LinearLayout) findViewById(R.id.layout_time_tracker);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams
                .MATCH_PARENT, 1f);
        mFrameLayout = new FrameLayout[140];
        for (int i = 0; i < 140; i++) {
            mFrameLayout[i] = new FrameLayout(this);
            mFrameLayout[i].setId(i);
            mFrameLayout[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            mTextView.setText(getTime(v.getId()));
                            mFrameTime.setVisibility(View.VISIBLE);
                            mRect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                            return true;
                        case MotionEvent.ACTION_UP:
                            mFrameTime.setVisibility(View.GONE);
                            updateStartTime(v.getId());
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            if (!mRect.contains((int) event.getX(), (int) event.getY())) {
                                mTextView.setText(getTime(v.getId()));
                                mFrameTime.setVisibility(View.VISIBLE);
                                break;
                            }
                            return true;
                    }
                    return true;
                }
            });
            mLinearLayout.addView(mFrameLayout[i], mParams);
        }
        updateTimeTracker(TimeUtils.getCalendar(n)[1]);

        if (mMode.equals(mStringMode[0])) {
            mTextViewHeader.setText(mStringHeader[0]);
            mTextViewWarning.setVisibility(View.GONE);
            initDataEdit();
            setPreviewMode();

        } else if (mMode.equals(mStringMode[1])) {
            onTitleTextChance();
            initTime();
            setWarning(null);
            mTextViewDone.setVisibility(View.VISIBLE);
            mTextViewWarning.setVisibility(View.VISIBLE);
            mTextViewHeader.setText(mStringHeader[1]);
        } else if (mMode.equals(mStringMode[2])) {
            onTitleTextChance();
            mTextViewDone.setVisibility(View.VISIBLE);
            mTextViewHeader.setText(mStringHeader[2]);
            initDataEdit();
        } else if (mMode.equals(mStringMode[3])) {
            onTitleTextChance();
            getTimeFree();
            initTrackerTime();
            setWarning(null);
            mTextViewDone.setVisibility(View.VISIBLE);
            mTextViewWarning.setVisibility(View.VISIBLE);
            mTextViewHeader.setText(mStringHeader[1]);
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
        else if (mMode.equals(mStringMode[3]))
            mTime = mBundle.getStringArray(getResources().getString(R.string.createtime));

    }

    public void initTrackerTime() {
        mEditTextTimeStart.setText(mTime[0]);
        mEditTextDateStart.setText(mTime[1]);
        long startTime = TimeUtils.timeToMilisecond(mTime[0], mTime[1]);
        String[] dueTime = TimeUtils.endTime(startTime);
        mEditTextTimeDue.setText(dueTime[0]);
        mEditTextDateDue.setText(dueTime[1]);
        mTextViewEstimateAdd.setText(Constrans.INIT_ESTIMATE);

    }

    public void initDateBar(int n) {
        mTextViewPreDay.setText(TimeUtils.getCalendar(n - 1)[0]);
        mTextViewCurrentDay.setText(TimeUtils.getCalendar(n)[0]);
        mTextViewNextDay.setText(TimeUtils.getCalendar(n + 1)[0]);
    }

    public void updateTimeTracker(String date) {
        mDatabase.open();
        ArrayList<Task> mListTask = mDatabase.getAllTask();
        ArrayList<Task> mListTaskTemp = new ArrayList<>();
        for (int i = 0; i < mListTask.size(); i++)
            if (mListTask.get(i).getStartdate().equals(date))
                mListTaskTemp.add(mListTask.get(i));
        resetTimeTracker();
        initTimeTracker(mListTaskTemp);
    }

    public void updateStartTime(int id) {

        long dueTime = TimeUtils.timeToMilisecond(mEditTextTimeDue.getText().toString()
                , mEditTextDateDue.getText().toString());
        long newStartTime = TimeUtils.timeToMilisecond(getTime(id)
                , TimeUtils.getCalendar(n)[1]);

        if (newStartTime < System.currentTimeMillis())
            showDialog(getResources().getString(R.string.time_conflict));
        else {
            if (!TimeUtils.checkFreeTime(newStartTime, dueTime)) {
                String due[] = TimeUtils.endTime(newStartTime);
                mEditTextTimeDue.setText(due[0]);
                mEditTextDateDue.setText(due[1]);
            }

            mEditTextTimeStart.setText(getTime(id));
            mEditTextDateStart.setText(TimeUtils.getCalendar(n)[1]);

            long dueTime2 = TimeUtils.timeToMilisecond(mEditTextTimeDue.getText().toString()
                    , mEditTextDateDue.getText().toString());
            mTextViewEstimateAdd.setText(TimeUtils.getEstimateTime(newStartTime, dueTime2));
        }

    }

    public void initTimeTracker(ArrayList<Task> mTaskArrayList) {
        for (int i = 0; i < mFrameLayout.length; i++) {
            for (int j = 0; j < mTaskArrayList.size(); j++) {
                String start = mTaskArrayList.get(j).getStarttime();
                String due = mTaskArrayList.get(j).getDuetime();
                int n[] = null;
                try {
                    n = TimeUtils.position(start, due);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (i > n[0] && i < n[1])
                    mFrameLayout[i].setBackgroundColor(getResources()
                            .getColor(R.color.color_Priorities_Immediate));
            }
        }
    }

    public void resetTimeTracker() {
        for (int i = 0; i < mFrameLayout.length; i++)
            mFrameLayout[i].setBackgroundColor(getResources().getColor(R.color.color_date));
    }

    public String getTime(int n) {
        n = n * 10;
        String h = "";
        String m = "";
        int hour = n / (60);
        int minute = n % 60;
        if (hour < 10)
            h = "0" + hour;
        else if (hour == 0)
            h = "00";
        else
            h = hour + "";
        if (minute < 10)
            m = "0" + minute;
        else if (minute == 0)
            m = "00";
        else
            m = minute + "";

        return h + ":" + m;
    }

    public void getTimeFree() {
        mDatabase.open();
        ArrayList<Integer> mListTimePosition = new ArrayList<>();
        mListTimeFree = new ArrayList<>();
        mListTask = mDatabase.getAllTask();
        long dTime = 0;
        if (mListTask.size() > 0)
            dTime = TimeUtils.timeToMilisecond(mListTask.get(0)
                    .getStarttime(), mListTask.get(0).getStartdate());
        if (mMode.equals(mStringMode[2]))
            for (int i = 0; i < mListTask.size(); i++)
                if (mListTask.get(i).getId() == mId)
                    mListTask.remove(i);
        TimeFree mDefaultTimeFree = new TimeFree();
        if (mListTask.size() == 0) {
            mEditTextTimeStart.setText(TimeUtils.getCurrentTime());
            mEditTextDateStart.setText(TimeUtils.getCurrentDate());
            mEditTextTimeDue.setText(TimeUtils.endTime(TimeUtils.timeToMilisecond
                    (TimeUtils.getCurrentTime(), TimeUtils.getCurrentDate()))[0]);
            mEditTextDateDue.setText(TimeUtils.endTime(TimeUtils.timeToMilisecond
                    (TimeUtils.getCurrentTime(), TimeUtils.getCurrentDate()))[1]);
        } else if (mListTask.size() == 1) {
            mDefaultTimeFree.setStartTime(TimeUtils.timeToMilisecond(mListTask
                    .get(mListTask.size() - 1).getDuetime(), mListTask.get(mListTask.size() - 1)
                    .getDuedate()));
            mDefaultTimeFree.setDueTime(mDefaultTimeFree.getDefaultDueTime());
            mListTimeFree.add(mDefaultTimeFree);
            mEditTextTimeStart.setText(mListTask.get(0).getDuetime());
            mEditTextDateStart.setText(mListTask.get(0).getDuedate());
            mEditTextTimeDue.setText(TimeUtils.endTime(TimeUtils.timeToMilisecond(mListTask.get(0)
                    .getDuetime(), mListTask.get(0).getDuedate()))[0]);
            mEditTextDateDue.setText(TimeUtils.endTime(TimeUtils.timeToMilisecond(mListTask.get(0)
                    .getDuetime(), mListTask.get(0).getDuedate()))[1]);
        } else {
            mDefaultTimeFree.setStartTime(TimeUtils.timeToMilisecond(mListTask
                    .get(mListTask.size() - 1).getDuetime(), mListTask.get(mListTask.size() - 1)
                    .getDuedate()));
            mDefaultTimeFree.setDueTime(mDefaultTimeFree.getDefaultDueTime());
            mListTimeFree.add(mDefaultTimeFree);

            if (TimeUtils.checkFreeTime(System.currentTimeMillis(), dTime))
                mListTimeFree.add(new TimeFree(System.currentTimeMillis(), System.currentTimeMillis()
                        + TimeUtils.MINUTE * 30, dTime));

            for (int i = 0; i < mListTask.size(); i++)
                if (i < mListTask.size() - 1)
                    if (TimeUtils.checkFreeTime(TimeUtils.timeToMilisecond(mListTask.get(i)
                            .getDuetime(), mListTask.get(i).getDuedate()), TimeUtils.timeToMilisecond
                            (mListTask.get(i + 1).getStarttime(), mListTask.get(i + 1)
                                    .getStartdate()))) {
                        mListTimePosition.add(i);
                    }
            if (mListTimePosition.size() == 0) {
                String dueTime = mListTask.get(mListTask.size() - 1).getDuetime();
                String dueDate = mListTask.get(mListTask.size() - 1).getDuedate();
                mEditTextTimeStart.setText(dueTime);
                mEditTextDateStart.setText(dueDate);
                mEditTextTimeDue.setText(TimeUtils.endTime(TimeUtils.timeToMilisecond(dueTime, dueDate))[0]);
                mEditTextDateDue.setText(TimeUtils.endTime(TimeUtils.timeToMilisecond(dueTime, dueDate))[1]);
            } else {
                for (int i = 0; i < mListTimePosition.size(); i++) {
                    long startTime = TimeUtils.timeToMilisecond(mListTask.get(mListTimePosition.get(i))
                            .getDuetime(), mListTask.get(mListTimePosition.get(i)).getDuedate());
                    long dueTime = TimeUtils.timeToMilisecond(mListTask.get(mListTimePosition.get(i) + 1)
                            .getStarttime(), mListTask.get(mListTimePosition.get(i) + 1).getStartdate());
                    long dueTime2 = TimeUtils.timeToMilisecond(mListTask.get(mListTimePosition.get(i) + 1)
                            .getDuetime(), mListTask.get(mListTimePosition.get(i) + 1).getDuedate());

                    if (System.currentTimeMillis() <= startTime
                            || TimeUtils.checkFreeTime(System.currentTimeMillis(), dueTime))
                        mListTimeFree.add(new TimeFree(startTime, dueTime, dueTime2));
                }
            }

        }

    }

    public void initTime() {
        getTimeFree();
        mTextViewEstimateAdd.setText(Constrans.INIT_ESTIMATE);
        if (mListTask.size() >= 1) {
            if (mListTimeFree.size() == 1) {
                if (System.currentTimeMillis() > mListTimeFree.get(0).getStartTime()) {
                    mEditTextTimeStart.setText(TimeUtils.startTime()[0]);
                    mEditTextDateStart.setText(TimeUtils.startTime()[1]);
                    mEditTextTimeDue.setText(TimeUtils.endTime(System.currentTimeMillis())[0]);
                    mEditTextDateDue.setText(TimeUtils.endTime(System.currentTimeMillis())[1]);
                } else {
                    mEditTextTimeStart.setText(mListTask.get(mListTask.size() - 1).getDuetime());
                    mEditTextDateStart.setText(mListTask.get(mListTask.size() - 1).getDuedate());
                    mEditTextTimeDue.setText(TimeUtils.endTime(TimeUtils.timeToMilisecond(mListTask
                            .get(mListTask.size() - 1).getDuetime(), mListTask.get(mListTask.size() - 1)
                            .getDuedate()))[0]);
                    mEditTextDateDue.setText(TimeUtils.endTime(TimeUtils.timeToMilisecond(mListTask
                            .get(mListTask.size() - 1).getDuetime(), mListTask.get(mListTask.size() - 1)
                            .getDuedate()))[1]);
                }
            } else if (System.currentTimeMillis() > mListTimeFree.get(1).getStartTime()) {
                mEditTextTimeStart.setText(TimeUtils.milisecondToTime(System.currentTimeMillis())[0]);
                mEditTextDateStart.setText(TimeUtils.milisecondToTime(System.currentTimeMillis())[1]);
                mEditTextTimeDue.setText(TimeUtils.endTime(System.currentTimeMillis())[0]);
                mEditTextDateDue.setText(TimeUtils.endTime(System.currentTimeMillis())[1]);
            } else {
                mEditTextTimeStart.setText(TimeUtils.milisecondToTime(mListTimeFree.get(1).getStartTime())[0]);
                mEditTextDateStart.setText(TimeUtils.milisecondToTime(mListTimeFree.get(1).getStartTime())[1]);
                mEditTextTimeDue.setText(TimeUtils.endTime(mListTimeFree.get(1).getStartTime())[0]);
                mEditTextDateDue.setText(TimeUtils.endTime(mListTimeFree.get(1).getStartTime())[1]);
            }
            mStartTimeDefault = mEditTextTimeStart.getText().toString();
            mStartDateDefault = mEditTextDateStart.getText().toString();
            mDueTimeDefault = mEditTextTimeDue.getText().toString();
            mDueDateDefault = mEditTextDateDue.getText().toString();

        }
    }

    public void showTimePicker(final EditText mEditText, final int mode) {
        Calendar mCalendar = null;

        if (mode == 0) {
            mCalendar = TimeUtils.timeToCalendar(mEditTextTimeStart.getText().toString()
                    , mEditTextDateStart.getText().toString());
        } else {
            mCalendar = TimeUtils.timeToCalendar(mEditTextTimeDue.getText().toString()
                    , mEditTextDateDue.getText().toString());
        }
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String hour = selectedHour + "";
                String minute = selectedMinute + "";
                if (selectedHour < Constrans.MINUTE)
                    hour = "0" + hour;
                if (selectedMinute < Constrans.MINUTE)
                    minute = "0" + minute;
                mTimeTemp = hour + ":" + minute;
                if (mode == 0) {
                    if (checkAvailableStartTime(mTimeTemp, mEditTextDateStart.getText().toString()))
                        mEditText.setText(mTimeTemp);
                    else showDialog(getResources().getString(R.string.time_conflict));
                } else {
                    if (checkAvailableDueTime(mTimeTemp, mEditTextDateDue.getText().toString()))
                        mEditText.setText(mTimeTemp);
                    else showDialog(getResources().getString(R.string.time_conflict));
                }
                setEstimateTime();
                setWarning(null);
            }
        }, hour, minute, true);
        mTimePicker.show();
    }

    public void showDatePicker(final EditText mEditText, final int mode) {
        Calendar mCalendar = Calendar.getInstance();
        if (mode == 0) {
            mCalendar = TimeUtils.timeToCalendar(mEditTextTimeStart.getText().toString()
                    , mEditTextDateStart.getText().toString());
        } else {
            mCalendar = TimeUtils.timeToCalendar(mEditTextTimeDue.getText().toString()
                    , mEditTextDateDue.getText().toString());
        }
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        int month = mCalendar.get(Calendar.MONTH);
        int year = mCalendar.get(Calendar.YEAR);
        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String day = dayOfMonth + "";
                String month = monthOfYear + 1 + "";
                if (dayOfMonth < Constrans.MINUTE)
                    day = "0" + day;
                if (monthOfYear + 1 < Constrans.MINUTE)
                    month = "0" + month;
                mDateTemp = day + "/" + month + "/" + year;
                if (mode == 0) {
                    if (checkAvailableStartTime(mEditTextTimeStart.getText().toString(), mDateTemp))
                        mEditText.setText(mDateTemp);
                    else
                        showDialog(getResources().getString(R.string.date_conflict));
                } else {
                    if (checkAvailableDueTime(mEditTextTimeDue.getText().toString(), mDateTemp))
                        mEditText.setText(mDateTemp);
                    else showDialog(getResources().getString(R.string.date_conflict));
                }
                setEstimateTime();
                setWarning(null);
            }
        }, year, month, day);
        mDatePicker.show();
    }

    public boolean checkAvailableStartTime(String time, String date) {
        long startTime = TimeUtils.timeToMilisecond(time, date);
        int count = 0;
        int flag = 0;
        if (mListTimeFree.size() >= 1)
            while (count < (mListTimeFree.size())) {
                if (count == 0) {
                    if (startTime >= mListTimeFree.get(count).getStartTime()
                            && startTime >= System.currentTimeMillis())
                        flag++;
                } else if (startTime >= mListTimeFree.get(count).getStartTime()
                        && startTime <= mListTimeFree.get(count).getDueTime())
                    flag++;
                count++;
            }
        else if (startTime >= System.currentTimeMillis()) flag++;
        if (flag == 1)
            return true;
        else return false;
    }

    public boolean checkAvailableDueTime(String time, String date) {
        long dueTime = TimeUtils.timeToMilisecond(time, date);
        int count = 0;
        int flag = 0;
        if (mListTimeFree.size() >= 1)
            while (count < (mListTimeFree.size())) {
                if (count == 0) {
                    if (dueTime >= mListTimeFree.get(count).getDueTime())
                        flag++;
                } else if (dueTime > mListTimeFree.get(count).getStartTime()
                        && dueTime <= mListTimeFree.get(count).getDueTime())
                    flag++;
                count++;
            }
        else flag++;
        if (flag == 1)
            return true;
        else return false;
    }

    public boolean checkDuplicateTask() {
        mStartTimeDefault = mEditTextTimeStart.getText().toString();
        mStartDateDefault = mEditTextDateStart.getText().toString();
        mDueTimeDefault = mEditTextTimeDue.getText().toString();
        mDueDateDefault = mEditTextDateDue.getText().toString();

        int count = 0;
        long startTime = TimeUtils.timeToMilisecond(mStartTimeDefault, mStartDateDefault);
        long dueTime = TimeUtils.timeToMilisecond(mDueTimeDefault, mDueDateDefault);

        if (mListTimeFree.size() == 0)
            return true;
        else
            for (int i = 0; i < mListTimeFree.size(); i++) {
                if (i == 0) {
                    if (mListTimeFree.get(0).getStartTime() <= startTime && mListTimeFree.get(0)
                            .getStartTime() < dueTime)
                        count++;
                } else {
                    if (mListTimeFree.get(i).getStartTime() <= startTime && mListTimeFree.get(i)
                            .getStartTime() < dueTime && mListTimeFree.get(i).getDueTime() > startTime
                            && mListTimeFree.get(i).getDueTime() >= dueTime)
                        count++;

                }
            }
        if (count == 1)
            return true;
        return false;
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
            mTextViewEstimateAdd.setText(mEstimateTime);
        }
    }

    public void setWarning(Task mTask) {
        mTextViewWarning.setTextColor(getResources().getColor(R.color.color_text));
        int mFreeTime[] = null;
        int temp = 0;
        long startDate = 0;
        if (mListTimeFree.size() == 0)
            mTextViewWarning.setText(mStringWaring[0] + " ");
        else {
            if (mTask != null) {
                startDate = mTimeUtils.timeToMilisecond
                        (mTask.getStarttime(), mTask.getStartdate());

            } else if (!mEditTextTimeStart.getText().toString().equals("")
                    && !mEditTextDateStart.getText().toString().equals("")
                    && !mEditTextTimeDue.getText().toString().equals("")
                    && !mEditTextDateDue.getText().toString().equals("")) {
                startDate = mTimeUtils.timeToMilisecond(mEditTextTimeStart.getText().toString()
                        , mEditTextDateStart.getText().toString());
            }

            for (int i = 0; i < mListTimeFree.size(); i++) {
                if (i == 0) {
                    if (startDate >= mListTimeFree.get(0).getStartTime())
                        mTextViewWarning.setText(mStringWaring[0] + " ");
                    else {
                        mTextViewWarning.setText(mStringWaring[4] + " ");
                        mTextViewWarning.setTextColor(getResources()
                                .getColor(R.color.color_Priorities_Immediate));
                    }
                } else if (mListTimeFree.get(i).getStartTime() <= startDate
                        && startDate <= mListTimeFree.get(i).getDueTime()) {
                    mTextViewWarning.setTextColor(getResources().getColor(R.color.color_text));
                    temp = i;
                    mFreeTime = mTimeUtils.getFreeTime(startDate, mListTimeFree.get(temp).getDueTime());
                    if (mFreeTime[0] > Constrans.MAX_FREE_HOUR)
                        mTextViewWarning.setText(mStringWaring[0] + " ");
                    else if (mFreeTime[0] < Constrans.MAX_FREE_HOUR && mFreeTime[0] > Constrans.HOUR)
                        mTextViewWarning.setText(mStringWaring[1] + " " + mFreeTime[0]
                                + " " + mStringWaring[2] + " ");
                    else if (mFreeTime[0] == Constrans.HOUR)
                        mTextViewWarning.setText(mStringWaring[1] + " " + mFreeTime[0]
                                + " " + mStringWaring[2] + " ");
                    else if (mFreeTime[0] < Constrans.HOUR && mFreeTime[1] >= Constrans.MIN_FREE_MINUTES)
                        mTextViewWarning.setText(mStringWaring[1] + " " + mFreeTime[1]
                                + " " + mStringWaring[3] + " ");
                    else if (mFreeTime[0] < Constrans.HOUR && mFreeTime[1] < Constrans.MIN_FREE_MINUTES) {
                        mTextViewWarning.setText(mStringWaring[4] + " ");
                        mTextViewWarning.setTextColor(getResources()
                                .getColor(R.color.color_Priorities_Immediate));
                    }
                }
            }

        }
    }

    public void saveTask() {
        mDatabase.open();
        Task mTask = new Task();
        mTask.setTitle(mEditTextTitle.getText().toString());
        mTask.setDescription(mEditTextDescription.getText().toString());
        mTask.setPriority(mSpinnerPriority.getSelectedItem().toString());
        mTask.setEstimate(mTextViewEstimateAdd.getText().toString());
        mTask.setStatus(getResources().getString(R.string.status_new));
        mTask.setStarttime(mEditTextTimeStart.getText().toString());
        mTask.setStartdate(mEditTextDateStart.getText().toString());
        mTask.setDuetime(mEditTextTimeDue.getText().toString());
        mTask.setDuedate(mEditTextDateDue.getText().toString());

        long startTime = TimeUtils.timeToMilisecond(mTask.getStarttime(), mTask.getStartdate());
        long dueTime = TimeUtils.timeToMilisecond(mTask.getDuetime(), mTask.getDuedate());
        try {
            if (mTask.getTitle().length() == 0)
                throw new StringIndexOutOfBoundsException();
            if (mMode.equals(mStringMode[1]) || mMode.equals(mStringMode[3]))
                if (checkDuplicateTask()) {
                    if (TimeUtils.checkFreeTime(startTime, dueTime)) {
                        long n = mDatabase.addTask(mTask, mTask.getTitle());
                        if (n == -1) {
                            showDialog(this.getResources().getString(R.string.duplicated_task));
                        } else if (n == -2) {
                            showDialog(this.getResources().getString(R.string.validate_description));
                        } else {
                            Toast.makeText(this, getResources().getText(R.string.add_task_success), Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }
                    } else showDialog(this.getResources().getString(R.string.time_equal));

                } else {
                    showDialog(this.getResources().getString(R.string.duplicated_time) + "\n \n" +
                            this.getResources().getString(R.string.choose_time));
                }
            else if (checkDuplicateTask()) {
                if (TimeUtils.checkFreeTime(startTime, dueTime)) {
                    mTask.setStatus(mSpinnerStatus.getSelectedItem().toString());
                    mDatabase.editTask(mId, mTask);
                    Toast.makeText(this, getResources().getText(R.string.edit_task), Toast.LENGTH_LONG).show();
                    onBackPressed();
                } else showDialog(this.getResources().getString(R.string.time_equal));
            } else {
                showDialog(this.getResources().getString(R.string.duplicated_time) + "\n \n" +
                        this.getResources().getString(R.string.choose_time));
            }
            mDatabase.close();
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
