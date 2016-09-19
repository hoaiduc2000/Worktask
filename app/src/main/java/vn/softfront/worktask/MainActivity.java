package vn.softfront.worktask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import vn.softfront.worktask.adapter.StatusDialogAdapter;
import vn.softfront.worktask.adapter.TaskListAdapter;
import vn.softfront.worktask.data.Database;
import vn.softfront.worktask.model.Task;
import vn.softfront.worktask.model.TimeUnit;
import vn.softfront.worktask.util.Constrans;
import vn.softfront.worktask.util.TimeUtils;
import vn.softfront.worktask.util.DialogUntil;
import vn.softfront.worktask.util.Divider;

/**
 * Created by nguyen.hoai.duc on 7/26/2016.
 */
public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener,
        TaskListAdapter.OnClickItemListener, TaskListAdapter.OnLongClickItemListener,
        TaskListAdapter.OnClickEditListener, DialogUntil.IEvenCancel,
        DialogUntil.IEvenDeleteDialog, DialogUntil.IEvenDelete, StatusDialogAdapter.OnDialogItemClickListener, View.OnClickListener, View.OnTouchListener {

    private RecyclerView mRecyclerView;
    private TaskListAdapter mTaskListAdapter;

    private LinearLayout mLinearLayout;
    private FrameLayout mFrameTime;
    private TextView mTextView;
    private FrameLayout[] mFrameLayout;

    private TextView mTextViewPreDay;
    private TextView mTextViewCurrentDay;
    private TextView mTextViewNextDay;

    private StatusDialogAdapter mStatusDialogAdapter;
    private ArrayList<Task> mTaskArrayList;
    private ArrayList<Task> mTaskArrayListTemp;
    private ArrayList<String> mFilterList;
    private ArrayList<TimeUnit> mListTimeUnit;

    private ImageView mImageViewAdd;
    private ImageView mImageViewPre;
    private ImageView mImageViewNext;

    private Database mDatabase;

    private String[] mStringStatus;
    private String[] mStringPriority;
    private String[] mStringMode;

    private CheckBox mCheckBoxNew;
    private CheckBox mCheckBoxImpogress;
    private CheckBox mCheckBoxResolved;
    private CheckBox mCheckBoxClosed;
    private int mId;
    private int n;
    private int mPosition;
    private int mWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(vn.softfront.worktask.R.layout.activity_main);
        initData();
        initView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mDatabase.open();
        updateTimeTracker(TimeUtils.getCalendar(n)[1]);
        checkCB(mCheckBoxNew.isChecked(), Constrans.CB_NEW);
        checkCB(mCheckBoxImpogress.isChecked(), Constrans.CB_IMPOGRESS);
        checkCB(mCheckBoxResolved.isChecked(), Constrans.CB_RESOLVED);
        checkCB(mCheckBoxClosed.isChecked(), Constrans.CB_CLOSED);
        sortTask();
        mTaskListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.checkbox_new:
                checkCB(isChecked, Constrans.CB_NEW);
                break;

            case R.id.checkbox_improgress:
                checkCB(isChecked, Constrans.CB_IMPOGRESS);
                break;

            case R.id.checkbox_resolved:
                checkCB(isChecked, Constrans.CB_RESOLVED);
                break;

            case R.id.checkbox_close:
                checkCB(isChecked, Constrans.CB_CLOSED);
                break;
            default:
        }
    }

    @Override
    public void onClickItem(int id) {
        Intent mIntent = new Intent(getApplication(), DetailTaskActivity.class);
        mIntent.putExtra(this.getResources().getString(R.string.id), id);
        startActivity(mIntent);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    @Override
    public void onClickEdit(int id) {

        Intent mIntent = new Intent(getApplication(), TaskActivity.class);
        mIntent.putExtra(this.getResources().getString(R.string.mode), mStringMode[2]);
        mIntent.putExtra(this.getResources().getString(R.string.id), id);
        startActivity(mIntent);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    @Override
    public void onLongClickItem(int id, ArrayList<String> mListStatus, String title) {
        DialogUntil.showDialog(this, R.layout.dialog_layout, title, R.id.text_view_cancel,
                R.id.text_view_delete, this, this, initDialogAdapter(mListStatus));
        mStatusDialogAdapter.setOnclick(this);
        mId = id;
    }

    @Override
    public void onCancel(Dialog mDialog) {
        mDialog.dismiss();
    }

    @Override
    public void onDeleteDialog(Dialog mDialog) {
        DialogUntil.showAlertDialog(this, null, this.getResources().getString(R.string.delete_confirm),
                null, this.getResources().getString(R.string.ok), null,
                this.getResources().getString(R.string.cancel), null, this, mId, mDialog);
    }

    @Override
    public void onDelete(int id, Dialog mDialog) {
        mDatabase.open();
        mTaskArrayList.clear();
        mTaskArrayListTemp.clear();
        mDatabase.deleteTask(id);
        updateTimeTracker(TimeUtils.getCalendar(n)[1]);
        checkCB(mCheckBoxNew.isChecked(), Constrans.CB_NEW);
        checkCB(mCheckBoxImpogress.isChecked(), Constrans.CB_IMPOGRESS);
        checkCB(mCheckBoxResolved.isChecked(), Constrans.CB_RESOLVED);
        checkCB(mCheckBoxClosed.isChecked(), Constrans.CB_CLOSED);
        sortTask();
        mTaskListAdapter.notifyDataSetChanged();
        mDialog.dismiss();
    }

    @Override
    public void onItemClick(String status, Dialog mDialog) {
        mDatabase.open();
        mDatabase.updateStatus(mId, status);
        updateTimeTracker(TimeUtils.getCalendar(n)[1]);
        checkCB(mCheckBoxNew.isChecked(), Constrans.CB_NEW);
        checkCB(mCheckBoxImpogress.isChecked(), Constrans.CB_IMPOGRESS);
        checkCB(mCheckBoxResolved.isChecked(), Constrans.CB_RESOLVED);
        checkCB(mCheckBoxClosed.isChecked(), Constrans.CB_CLOSED);
        mDialog.dismiss();
    }

    public StatusDialogAdapter initDialogAdapter(ArrayList<String> strStatus) {
        mStatusDialogAdapter = new StatusDialogAdapter(this, R.layout.item_dialog_layout, strStatus);
        return mStatusDialogAdapter;
    }

    public void initView() {
        mListTimeUnit = new ArrayList<>();
        mCheckBoxNew = (CheckBox) findViewById(R.id.checkbox_new);
        mCheckBoxImpogress = (CheckBox) findViewById(R.id.checkbox_improgress);
        mCheckBoxResolved = (CheckBox) findViewById(R.id.checkbox_resolved);
        mCheckBoxClosed = (CheckBox) findViewById(R.id.checkbox_close);

        mCheckBoxNew.setOnCheckedChangeListener(this);
        mCheckBoxImpogress.setOnCheckedChangeListener(this);
        mCheckBoxResolved.setOnCheckedChangeListener(this);
        mCheckBoxClosed.setOnCheckedChangeListener(this);

        mImageViewAdd = (ImageView) findViewById(R.id.image_view_add_task);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view_task);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new Divider(this));

        mTaskListAdapter = new TaskListAdapter(this, mTaskArrayList, mStringPriority, mStringStatus);

        mTextView = (TextView) findViewById(R.id.text_view_time);
        mFrameTime = (FrameLayout) findViewById(R.id.frame_time);

        mTextViewPreDay = (TextView) findViewById(R.id.text_view_pre_day);
        mTextViewCurrentDay = (TextView) findViewById(R.id.text_view_current_day);
        mTextViewNextDay = (TextView) findViewById(R.id.text_view_next_day);

        mImageViewPre = (ImageView) findViewById(R.id.image_view_back);
        mImageViewNext = (ImageView) findViewById(R.id.image_view_next);
        mImageViewPre.setOnClickListener(this);
        mImageViewPre.setOnTouchListener(this);
        mImageViewNext.setOnClickListener(this);
        mImageViewNext.setOnTouchListener(this);

        mTaskListAdapter.setOnClickItemListener(this);
        mTaskListAdapter.setOnLongClickItemListener(this);
        mTaskListAdapter.setOnClickEditListener(this);
        mRecyclerView.setAdapter(mTaskListAdapter);
        mTaskListAdapter.notifyDataSetChanged();
        mImageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplication(), TaskActivity.class);
                mIntent.putExtra(getResources().getString(R.string.mode), mStringMode[1]);
                startActivity(mIntent);
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            }
        });
        initCheckBox();
        n = 0;
        initDateBar(0);
        mLinearLayout = (LinearLayout) findViewById(R.id.layout_time_tracker);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .MATCH_PARENT, LinearLayout.LayoutParams
                .MATCH_PARENT, 1.0f);

        mFrameLayout = new FrameLayout[144];
        for (int i = 0; i < 144; i++) {
            mFrameLayout[i] = new FrameLayout(this);
            mFrameLayout[i].setId(i);
            mFrameLayout[i].setClickable(false);
            mLinearLayout.addView(mFrameLayout[i], mParams);
        }
        mLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        for (int i = 0; i < mListTimeUnit.size(); i++)
                            if (event.getX() >= mListTimeUnit.get(i).getStart() && event.getX()
                                    < mListTimeUnit.get(i).getEnd()) {
                                mPosition = i;
                                mTextView.setText(getTime(mFrameLayout[mPosition].getId()));
                            }
                        mFrameTime.setVisibility(View.VISIBLE);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mFrameTime.setVisibility(View.GONE);
                        if (checkConflictTime(getTime(mFrameLayout[mPosition].getId()),
                                TimeUtils.getCalendar(n)[1]))
                            createTask(mFrameLayout[mPosition].getId());
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        if (event.getX() >= 0 && event.getX() <= mWidth) {
                            for (int i = 0; i < mListTimeUnit.size(); i++)
                                if (event.getX() >= mListTimeUnit.get(i).getStart() && event.getX()
                                        < mListTimeUnit.get(i).getEnd()) {
                                    mPosition = i;
                                    mTextView.setText(getTime(mFrameLayout[mPosition].getId()));
                                }
                            mFrameTime.setVisibility(View.VISIBLE);
                        }
                        return true;
                }
                return true;
            }
        });
        updateTimeTracker(TimeUtils.getCalendar(n)[1]);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mListTimeUnit.clear();
        mWidth = mLinearLayout.getWidth();
        float n = (float) mWidth / 144;
        for (int i = 0; i < 144; i++) {
            TimeUnit mTimeUnit = new TimeUnit();
            mTimeUnit.setStart(i * n);
            mTimeUnit.setEnd((i + 1) * n);
            mListTimeUnit.add(mTimeUnit);
        }
    }

    public void createTask(int time) {
        String mTime[] = new String[]{getTime(time), TimeUtils.getCalendar(n)[1]};
        Intent mIntent = new Intent(this, TaskActivity.class);
        mIntent.putExtra(getResources().getString(R.string.mode), mStringMode[3]);
        mIntent.putExtra(getResources().getString(R.string.createtime), mTime);
        startActivity(mIntent);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
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
            if (mListTask.get(i).getStartdate().equals(date) || mListTask.get(i)
                    .getDuedate().equals(date))
                if (mListTask.get(i).getStatus().equals(mStringStatus[0]) || mListTask.get(i)
                        .getStatus().equals(mStringStatus[1]))
                    mListTaskTemp.add(mListTask.get(i));
        resetTimeTracker();
        initTimeTracker(mListTaskTemp, mListTask);
    }

    public void initTimeTracker(ArrayList<Task> mTaskArrayList, ArrayList<Task> mTaskArrayListFull) {
        for (int j = 0; j < mTaskArrayList.size(); j++) {
            String start = mTaskArrayList.get(j).getStarttime();
            String due = mTaskArrayList.get(j).getDuetime();
            String startDate = mTaskArrayList.get(j).getStartdate();
            String dueDate = mTaskArrayList.get(j).getDuedate();
            long subDay = TimeUtils.dateToMilisecond(dueDate)
                    - TimeUtils.dateToMilisecond(startDate);
            int m[] = null;
            try {
                m = TimeUtils.position(start, due);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (startDate.equals(dueDate)) {
                for (int i = 0; i < mFrameLayout.length; i++)
                    if (i >= m[0] && i <= m[1])
                        changeColor(i);
            } else if (subDay >= TimeUtils.DAY) {
                if (startDate.equals(TimeUtils.getCalendar(n)[1])) {
                    for (int i = 0; i < mFrameLayout.length; i++)
                        if (i >= m[0])
                            changeColor(i);
                } else if (dueDate.equals(TimeUtils.getCalendar(n)[1])) {
                    for (int i = 0; i < mFrameLayout.length; i++)
                        if (i <= m[1])
                            changeColor(i);
                }
            }
        }
        for (int i = 0; i < mTaskArrayListFull.size(); i++)
            if (!mTaskArrayListFull.get(i).getStatus().equals(mStringStatus[0]) && !mTaskArrayListFull
                    .get(i).getStatus().equals(mStringStatus[1]))
                mTaskArrayListFull.remove(i);
        for (int i = 0; i < mTaskArrayListFull.size(); i++) {
            if (TimeUtils.dateToMilisecond(TimeUtils.getCalendar(n)[1])
                    > TimeUtils.dateToMilisecond(mTaskArrayListFull.get(i).getStartdate())
                    && TimeUtils.dateToMilisecond(TimeUtils.getCalendar(n)[1])
                    < TimeUtils.dateToMilisecond(mTaskArrayListFull.get(i).getDuedate()))
                for (int j = 0; j < mFrameLayout.length; j++)
                    changeColor(j);
        }
    }

    public void resetTimeTracker() {
        for (int i = 0; i < mFrameLayout.length; i++) {
            mFrameLayout[i].setBackgroundColor(getResources().getColor(R.color.color_date));
            mFrameLayout[i].setOnTouchListener(null);
        }
    }

    public void changeColor(int n) {
        mFrameLayout[n].setBackgroundColor(getResources()
                .getColor(R.color.color_bar_tracker));
        mFrameLayout[n].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    public boolean checkConflictTime(String time, String date) {
        long mTime = TimeUtils.timeToMilisecond(time, date);
        if (mTime < System.currentTimeMillis()) {
            DialogUntil.showAlertDialog(this, null, getResources().getString(R.string.time_conflict),
                    null, this.getResources().getString(R.string.ok), null,
                    null, null, null, 0, null);
            return false;
        } else
            return true;
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

    public void initData() {
        mStringPriority = getResources().getStringArray(R.array.priority);
        mStringStatus = getResources().getStringArray(R.array.status);
        mStringMode = getResources().getStringArray(R.array.mode);
        mDatabase = new Database(this);
        mDatabase.open();
        mTaskArrayListTemp = new ArrayList<>();
        mFilterList = new ArrayList<>();
        mTaskArrayList = mDatabase.getAllTask();
        sortTask();
        mTaskArrayListTemp.addAll(mTaskArrayList);
    }

    public void addToFilter(String status) {
        Set<String> hs = new HashSet<>();
        mFilterList.add(status);
        hs.addAll(mFilterList);
        mFilterList.clear();
        mFilterList.addAll(hs);
        getFilterList();

    }

    public void removeFromFilter(String status) {
        for (int i = 0; i < mFilterList.size(); i++)
            if (mFilterList.get(i).equals(status))
                mFilterList.remove(i);
        getFilterList();
    }

    public void getFilterList() {
        ArrayList<Task> mListTaskFilter;
        mDatabase.open();
        mListTaskFilter = mDatabase.filterTask(mFilterList);
        mTaskArrayList.clear();
        mTaskArrayList.addAll(mListTaskFilter);
        sortTask();
        mTaskListAdapter.notifyDataSetChanged();
    }

    public void checkNoChecked() {
        if (!mCheckBoxNew.isChecked() && !mCheckBoxImpogress.isChecked() &&
                !mCheckBoxResolved.isChecked() && !mCheckBoxClosed.isChecked()) {
            mTaskArrayList.clear();
            mTaskListAdapter.notifyDataSetChanged();
        }
    }

    public void sortTask() {
        TimeUtils.sortStartDate(mTaskArrayList);
    }

    public void checkCB(boolean isCheck, int n) {
        if (isCheck)
            addToFilter(mStringStatus[n]);
        else
            removeFromFilter(mStringStatus[n]);
        checkNoChecked();
    }

    public void initCheckBox() {
        mCheckBoxNew.setChecked(true);
        mCheckBoxImpogress.setChecked(true);
        checkCB(mCheckBoxNew.isChecked(), Constrans.CB_NEW);
        checkCB(mCheckBoxImpogress.isChecked(), Constrans.CB_IMPOGRESS);
    }

}
