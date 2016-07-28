package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.nguyenhoaiduc.worktask.R;

import java.util.ArrayList;

import data.DBAdapter;
import model.Task;

/**
 * Created by nguyen.hoai.duc on 7/26/2016.
 */
public class TaskListAdapter extends ArrayAdapter<Task> {

    private Activity mContext;
    private int mLayoutId;
    private ArrayList<Task> mTaskItems;

    private String[] mStringStatus;
    private String[] mStringPriority;

    private int mPosition;


    private DBAdapter mDbAdapter;

    public TaskListAdapter(Activity context, int resource, ArrayList<Task> list, String[] mStringPriority, String[] mStringStatus) {
        super(context, resource, list);
        this.mContext = context;
        this.mLayoutId = resource;
        this.mTaskItems = list;
        this.mStringPriority = mStringPriority;
        this.mStringStatus = mStringStatus;
        initData();
    }

    @Override
    public int getCount() {
        return mTaskItems.size();
    }

    public void initData() {
        mDbAdapter = new DBAdapter(mContext);
        mDbAdapter.open();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getLayoutInflater();
            convertView = mInflater.inflate(mLayoutId, null);
        }
        final TextView mTextViewTitle = (TextView) convertView.findViewById(R.id.text_title);
        final TextView mTextViewStartDate = (TextView) convertView.findViewById(R.id.text_start_date);
        final TextView mTextViewDueDate = (TextView) convertView.findViewById(R.id.text_due_date);
        final TextView mTextViewPriority = (TextView) convertView.findViewById(R.id.text_priority);
        final FrameLayout mFrameLayoutPriority = (FrameLayout) convertView.findViewById(R.id.frame_priorities);
        final FrameLayout mFrameLayoutStatus = (FrameLayout) convertView.findViewById(R.id.frame_status);

        mTextViewTitle.setText(mTaskItems.get(position).getTitle());
        mTextViewStartDate.setText(mTaskItems.get(position).getStarttime() + " " + mTaskItems
                .get(position).getStartdate() + " - ");
        mTextViewDueDate.setText(mTaskItems.get(position).getDuetime() + " " + mTaskItems
                .get(position).getDuedate());
        mTextViewPriority.setText(mTaskItems.get(position).getPriority());

        if (mTaskItems.get(position).getPriority().equals(mStringPriority[1]))
            mFrameLayoutPriority.setBackgroundColor(mFrameLayoutPriority.getResources()
                    .getColor(R.color.color_Priorities_Low));
        if (mTaskItems.get(position).getPriority().equals(mStringPriority[0]) ||
                mTaskItems.get(position).getPriority().equals(mStringPriority[2]))
            mFrameLayoutPriority.setBackgroundColor(mFrameLayoutPriority.getResources()
                    .getColor(R.color.color_Priorities_Normal));
        if (mTaskItems.get(position).getPriority().equals(mStringPriority[3]))
            mFrameLayoutPriority.setBackgroundColor(mFrameLayoutPriority.getResources()
                    .getColor(R.color.color_Priorities_High));
        if (mTaskItems.get(position).getPriority().equals(mStringPriority[4]))
            mFrameLayoutPriority.setBackgroundColor(mFrameLayoutPriority.getResources()
                    .getColor(R.color.color_Priorities_Immediate));

        if (mTaskItems.get(position).getStatus().equals(mStringStatus[0]))
            mFrameLayoutStatus.setBackgroundColor(mFrameLayoutStatus.getResources()
                    .getColor(R.color.color_Status_New));
        if (mTaskItems.get(position).getStatus().equals(mStringStatus[1]))
            mFrameLayoutStatus.setBackgroundColor(mFrameLayoutStatus.getResources()
                    .getColor(R.color.color_Status_Inprogress));
        if (mTaskItems.get(position).getStatus().equals(mStringStatus[2]))
            mFrameLayoutStatus.setBackgroundColor(mFrameLayoutStatus.getResources()
                    .getColor(R.color.color_Status_Resolved));
        if (mTaskItems.get(position).getStatus().equals(mStringStatus[3]))
            mFrameLayoutStatus.setBackgroundColor(mFrameLayoutStatus.getResources()
                    .getColor(R.color.color_Status_Closed));

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialog(mFrameLayoutStatus, mTaskItems.get(position));
                return true;
            }
        });

        return convertView;
    }

    public void showDialog(final FrameLayout mFrameLayout, final Task mTask) {
        AlertDialog.Builder adb = new AlertDialog.Builder(mContext);

        adb.setSingleChoiceItems(mStringStatus, 0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface d, int n) {
                mPosition = n;
            }
        });
        adb.setNegativeButton(mContext.getResources().getText(R.string.cancel), null);
        adb.setPositiveButton(mContext.getResources().getText(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mStringStatus[mPosition].equals(mStringStatus[0])) {
                    mFrameLayout.setBackgroundColor(mFrameLayout.getResources()
                            .getColor(R.color.color_Status_New));
                    mDbAdapter.updateStatus(mTask.getId(), mStringStatus[0]);

                }
                if (mStringStatus[mPosition].equals(mStringStatus[1])) {
                    mFrameLayout.setBackgroundColor(mFrameLayout.getResources()
                            .getColor(R.color.color_Status_Inprogress));
                    mDbAdapter.updateStatus(mTask.getId(), mStringStatus[1]);
                }
                if (mStringStatus[mPosition].equals(mStringStatus[2])) {
                    mFrameLayout.setBackgroundColor(mFrameLayout.getResources()
                            .getColor(R.color.color_Status_Resolved));
                    mDbAdapter.updateStatus(mTask.getId(), mStringStatus[2]);
                }
                if (mStringStatus[mPosition].equals(mStringStatus[3])) {
                    mFrameLayout.setBackgroundColor(mFrameLayout.getResources()
                            .getColor(R.color.color_Status_Closed));
                    mDbAdapter.updateStatus(mTask.getId(), mStringStatus[3]);
                }
            }
        });
        adb.setTitle(mContext.getResources().getText(R.string.set_status));
        adb.show();
    }
}
