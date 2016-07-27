package com.example.nguyenhoaiduc.worktask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
    private ImageView mImageViewAdd;

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
        mImageViewAdd = (ImageView) findViewById(R.id.image_view_add_task);
        mListTask = (ListView) findViewById(R.id.list_view_task);
        mTaskListAdapter = new TaskListAdapter(this, R.layout.task_item_layout, mTaskArrayList);
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
}
