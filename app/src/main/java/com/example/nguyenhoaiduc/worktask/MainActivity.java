package com.example.nguyenhoaiduc.worktask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import adapter.TaskListAdapter;
import data.DBAdapter;
import model.Task;

/**
 * Created by nguyen.hoai.duc on 7/26/2016.
 */
public class MainActivity extends Activity {

    private ListView mListTask;
    private TaskListAdapter mTaskListAdapter;
    private ArrayList<Task> mTaskArrayList;
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

    public void setCheckFilter() {
        mCheckBoxNew.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        mCheckBoxImpogress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        mCheckBoxResolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        mCheckBoxClosed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    public void initData() {
        mDbAdapter = new DBAdapter(this);
        mDbAdapter.open();
        mTaskArrayList = mDbAdapter.getAllTask();
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
}
