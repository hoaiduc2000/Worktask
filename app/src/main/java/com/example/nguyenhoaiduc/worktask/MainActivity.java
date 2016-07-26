package com.example.nguyenhoaiduc.worktask;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import adapter.TaskListAdapter;
import model.Task;

/**
 * Created by nguyen.hoai.duc on 7/26/2016.
 */
public class MainActivity extends Activity {

    private ListView mListTask;
    private TaskListAdapter mTaskListAdapter;
    private ArrayList<Task> mTaskArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView() {
        mTaskArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            mTaskArrayList.add(new Task());
        mListTask = (ListView) findViewById(R.id.list_view_task);
        mTaskListAdapter = new TaskListAdapter(this, R.layout.task_item_layout, mTaskArrayList);
        mListTask.setAdapter(mTaskListAdapter);
        mTaskListAdapter.notifyDataSetChanged();
    }
}
