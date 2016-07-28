package com.example.nguyenhoaiduc.worktask;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adapter.TaskListAdapter;
import data.DBAdapter;
import model.Task;

/**
 * Created by nguyen.hoai.duc on 7/26/2016.
 */
public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    private ListView mListTask;
    private TaskListAdapter mTaskListAdapter;
    private ArrayList<Task> mTaskArrayList;
    private ArrayList<Task> mTaskArrayListTemp;
    private ArrayList<String> mFilterList;
    private ImageView mImageViewAdd;

    private DBAdapter mDbAdapter;

    private String[] mStringStatus;
    private String[] mStringPriority;

    private CheckBox mCheckBoxNew;
    private CheckBox mCheckBoxImpogress;
    private CheckBox mCheckBoxResolved;
    private CheckBox mCheckBoxClosed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    public void initView() {

        mCheckBoxNew = (CheckBox) findViewById(R.id.checkbox_new);
        mCheckBoxImpogress = (CheckBox) findViewById(R.id.checkbox_improgress);
        mCheckBoxResolved = (CheckBox) findViewById(R.id.checkbox_resolved);
        mCheckBoxClosed = (CheckBox) findViewById(R.id.checkbox_close);

        mCheckBoxNew.setOnCheckedChangeListener(this);
        mCheckBoxImpogress.setOnCheckedChangeListener(this);
        mCheckBoxResolved.setOnCheckedChangeListener(this);
        mCheckBoxClosed.setOnCheckedChangeListener(this);

        mImageViewAdd = (ImageView) findViewById(R.id.image_view_add_task);
        mListTask = (ListView) findViewById(R.id.list_view_task);
        mTaskListAdapter = new TaskListAdapter(this, R.layout.task_item_layout, mTaskArrayList, mStringPriority, mStringStatus);
        mListTask.setAdapter(mTaskListAdapter);
        mTaskListAdapter.notifyDataSetChanged();
        mImageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplication(), AddTaskActivity.class);
                startActivity(mIntent);
            }
        });
    }

    public void initData() {
        mDbAdapter = new DBAdapter(this);
        mDbAdapter.open();
        mTaskArrayListTemp = new ArrayList<>();
        mFilterList = new ArrayList<>();
        mTaskArrayList = mDbAdapter.getAllTask();
        mTaskArrayListTemp.addAll(mTaskArrayList);
        mStringPriority = getResources().getStringArray(R.array.priority);
        mStringStatus = getResources().getStringArray(R.array.status);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mDbAdapter.open();
        mTaskArrayList.clear();
        mTaskArrayList.addAll(mDbAdapter.getAllTask());
        mTaskListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.checkbox_new:
                if (isChecked)
                    addToFilter(mStringStatus[0]);
                else
                    removeFromFilter(mStringStatus[0]);
                checkNoChecked();
                break;

            case R.id.checkbox_improgress:
                if (isChecked)
                    addToFilter(mStringStatus[1]);
                else
                    removeFromFilter(mStringStatus[1]);
                checkNoChecked();
                break;

            case R.id.checkbox_resolved:
                if (isChecked)
                    addToFilter(mStringStatus[2]);
                else
                    removeFromFilter(mStringStatus[2]);
                checkNoChecked();
                break;

            case R.id.checkbox_close:
                if (isChecked)
                    addToFilter(mStringStatus[3]);
                else
                    removeFromFilter(mStringStatus[3]);
                checkNoChecked();
                break;
            default:

        }
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
        ArrayList<Task> mListTaskFilter = new ArrayList<>();
        mDbAdapter.open();
        mListTaskFilter = mDbAdapter.filterTask(mFilterList);
        mTaskArrayList.clear();
        mTaskArrayList.addAll(mListTaskFilter);
        mTaskListAdapter.notifyDataSetChanged();
    }

    public void checkNoChecked() {
        if (!mCheckBoxNew.isChecked() && !mCheckBoxImpogress.isChecked() &&
                !mCheckBoxResolved.isChecked() && !mCheckBoxClosed.isChecked()) {
            mTaskArrayList.clear();
            mTaskArrayList.addAll(mTaskArrayListTemp);
            mTaskListAdapter.notifyDataSetChanged();
        }
    }

}
