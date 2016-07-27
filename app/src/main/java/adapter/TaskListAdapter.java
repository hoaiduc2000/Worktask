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

    public TaskListAdapter(Activity context, int resource, ArrayList<Task> list, String[] mStringPriority, String[] mStringStatus) {
        super(context, resource, list);
        this.mContext = context;
        this.mLayoutId = resource;
        this.mTaskItems = list;
        this.mStringPriority = mStringPriority;
        this.mStringStatus = mStringStatus;
    }

    @Override
    public int getCount() {
        return mTaskItems.size();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

        if (mTaskItems.get(position).getPriority().equals(mStringPriority[0]))
            mFrameLayoutPriority.setBackgroundColor(mFrameLayoutPriority.getResources()
                    .getColor(R.color.color_Priorities_Low));
        if (mTaskItems.get(position).getPriority().equals(mStringPriority[1]))
            mFrameLayoutPriority.setBackgroundColor(mFrameLayoutPriority.getResources()
                    .getColor(R.color.color_Priorities_Normal));
        if (mTaskItems.get(position).getPriority().equals(mStringPriority[2]))
            mFrameLayoutPriority.setBackgroundColor(mFrameLayoutPriority.getResources()
                    .getColor(R.color.color_Priorities_High));
        if (mTaskItems.get(position).getPriority().equals(mStringPriority[3]))
            mFrameLayoutPriority.setBackgroundColor(mFrameLayoutPriority.getResources()
                    .getColor(R.color.color_Priorities_Immediate));

        mFrameLayoutStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(mFrameLayoutStatus);
            }
        });

            return convertView;
    }

    public void showDialog(FrameLayout mFrameLayout) {
        AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
        adb.setSingleChoiceItems(mStringStatus, 0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface d, int n) {

            }

        });
        adb.setNegativeButton("Cancel", null);
        adb.setPositiveButton("OK", null);
        adb.setTitle("Set Status");
        adb.show();
    }
}
