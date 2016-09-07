package vn.softfront.worktask;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Time;

import data.Database;
import model.Task;
import util.TimeUtils;

/**
 * Created by nguyen.hoai.duc on 8/29/2016.
 */
public class DetailTaskActivity extends Activity {
    private Database mDatabase;
    private Task mTask;
    private int mId;

    private String[] mStringPriority;

    private TextView mTextViewTitle;
    private TextView mTextViewDescription;
    private TextView mTextViewPriority;
    private TextView mTextViewStartDate;
    private TextView mTextViewDueDate;
    private TextView mTextViewEstimate;
    private TextView mTextViewStatus;

    private ImageView mImageViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);
        initView();
        initData();
    }

    public void initData() {
        mDatabase = new Database(this);
        mDatabase.open();
        Bundle mBundle = getIntent().getExtras();
        mId = mBundle.getInt(getResources().getString(R.string.id));
        mTask = mDatabase.getTask(mId);
        mDatabase.close();
        long dueTime = TimeUtils.timeToMilisecond(mTask.getDuetime(), mTask.getDuedate());
        mStringPriority = getResources().getStringArray(R.array.priority);
        if (mTask.getPriority().equals(mStringPriority[0]))
            mTextViewPriority.setTextColor(getResources().getColor(R.color.color_Priorities_Normal));
        else if (mTask.getPriority().equals(mStringPriority[1]))
            mTextViewPriority.setTextColor(getResources().getColor(R.color.color_Priorities_Low));
        else if (mTask.getPriority().equals(mStringPriority[2]))
            mTextViewPriority.setTextColor(getResources().getColor(R.color.color_Priorities_High));
        else if (mTask.getPriority().equals(mStringPriority[3]))
            mTextViewPriority.setTextColor(getResources().getColor(R.color.color_Priorities_Immediate));
        mTextViewTitle.setText(mTask.getTitle());
        mTextViewDescription.setText(mTask.getDescription());
        mTextViewPriority.setText(mTask.getPriority());
        mTextViewStartDate.setText(mTask.getStarttime() + " " + mTask.getStartdate());
        if (dueTime < System.currentTimeMillis())
            mTextViewDueDate.setTextColor(getResources().getColor(R.color.color_Priorities_Immediate));
        mTextViewDueDate.setText(mTask.getDuetime() + " " + mTask.getDuedate());
        mTextViewEstimate.setText(mTask.getEstimate());
        mTextViewStatus.setText(mTask.getStatus());
    }

    public void initView() {
        mTextViewTitle = (TextView) findViewById(R.id.text_view_header_detail);
        mTextViewDescription = (TextView) findViewById(R.id.text_view_description_content);
        mTextViewPriority = (TextView) findViewById(R.id.text_view_priority_content);
        mTextViewStartDate = (TextView) findViewById(R.id.text_view_startdate_content);
        mTextViewDueDate = (TextView) findViewById(R.id.text_view_duedate_content);
        mTextViewEstimate = (TextView) findViewById(R.id.text_view_estimate_content);
        mTextViewStatus = (TextView) findViewById(R.id.text_view_status_content);

        mImageViewBack = (ImageView) findViewById(R.id.image_view_back_detail);
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
