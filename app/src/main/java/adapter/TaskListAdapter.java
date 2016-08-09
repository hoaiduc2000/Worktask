package adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import vn.softfront.worktask.R;

import java.util.ArrayList;

import data.Database;
import model.Task;
import util.TimeUtils;

/**
 * Created by nguyen.hoai.duc on 7/26/2016.
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder> {

    private Activity mContext;
    private ArrayList<Task> mTaskItems;

    private String[] mStringStatus;
    private String[] mStringPriority;

    private OnClickItemListener mOnClickItemListener;
    private OnLongClickItemListener mOnLongClickItemListener;
    private OnClickEditListener mOnClickEditListener;

    private Database mDatabase;
    private TimeUtils mTimeUtils;

    public TaskListAdapter(Activity context, ArrayList<Task> list, String[] mStringPriority, String[] mStringStatus) {
        this.mContext = context;
        this.mTaskItems = list;
        this.mStringPriority = mStringPriority;
        this.mStringStatus = mStringStatus;
        initData();
    }

    public void initData() {
        mTimeUtils = new TimeUtils();
        mDatabase = new Database(mContext);
        mDatabase.open();
    }

    public void showDialog(final Task mTask) {
        if (mOnLongClickItemListener != null) {
            ArrayList<String> mListStatus = new ArrayList<>();

            if (mTask.getStatus().equals(mStringStatus[0])) {
                for (int i = 0; i < mStringStatus.length; i++)
                    if (!mTask.getStatus().equals(mStringStatus[i])
                            && !mStringStatus[i].equals(mStringStatus[2]))
                        mListStatus.add(mStringStatus[i]);
            } else if (mTask.getStatus().equals(mStringStatus[1])) {
                for (int i = 0; i < mStringStatus.length; i++)
                    if (!mTask.getStatus().equals(mStringStatus[i])
                            && !mStringStatus[i].equals(mStringStatus[0])
                            && !mStringStatus[i].equals(mStringStatus[3]))
                        mListStatus.add(mStringStatus[i]);
            } else if (mTask.getStatus().equals(mStringStatus[2])) {
                for (int i = 0; i < mStringStatus.length; i++)
                    if (!mTask.getStatus().equals(mStringStatus[i])
                            && !mStringStatus[i].equals(mStringStatus[0])
                            && !mStringStatus[i].equals(mStringStatus[1]))
                        mListStatus.add(mStringStatus[i]);
            } else if (mTask.getStatus().equals(mStringStatus[3])) {
                for (int i = 0; i < mStringStatus.length; i++)
                    if (!mTask.getStatus().equals(mStringStatus[i])
                            && !mStringStatus[i].equals(mStringStatus[1])
                            && !mStringStatus[i].equals(mStringStatus[2]))
                        mListStatus.add(mStringStatus[i]);
            }
            mOnLongClickItemListener.onLongClickItem(mTask.getId(), mListStatus, mTask.getTitle());
        }
    }

    public void setOnClickItemListener(OnClickItemListener mOnClickItemListener) {
        this.mOnClickItemListener = mOnClickItemListener;
    }

    public void setOnLongClickItemListener(OnLongClickItemListener mOnLongClickItemListener) {
        this.mOnLongClickItemListener = mOnLongClickItemListener;
    }

    public void setOnClickEditListener(OnClickEditListener mOnClickEditListener) {
        this.mOnClickEditListener = mOnClickEditListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.mTextViewTitle.setText(mTaskItems.get(position).getTitle());
        holder.mTextViewStartDate.setText(mTaskItems.get(position).getStarttime() + " " + mTaskItems
                .get(position).getStartdate() + " - ");
        holder.mTextViewDueDate.setText(mTaskItems.get(position).getDuetime() + " " + mTaskItems
                .get(position).getDuedate());
        holder.mTextViewPriority.setText(mTaskItems.get(position).getPriority());
        checkOverDeadline(mTaskItems.get(position), holder.mTextViewDueDate);

        if (mTaskItems.get(position).getPriority().equals(mStringPriority[1]))
            holder.mFrameLayoutPriority.setBackgroundColor(holder.mFrameLayoutPriority.getResources()
                    .getColor(R.color.color_Priorities_Low));
        else if (mTaskItems.get(position).getPriority().equals(mStringPriority[0]))
            holder.mFrameLayoutPriority.setBackgroundColor(holder.mFrameLayoutPriority.getResources()
                    .getColor(R.color.color_Priorities_Normal));
        else if (mTaskItems.get(position).getPriority().equals(mStringPriority[2]))
            holder.mFrameLayoutPriority.setBackgroundColor(holder.mFrameLayoutPriority.getResources()
                    .getColor(R.color.color_Priorities_High));
        else if (mTaskItems.get(position).getPriority().equals(mStringPriority[3]))
            holder.mFrameLayoutPriority.setBackgroundColor(holder.mFrameLayoutPriority.getResources()
                    .getColor(R.color.color_Priorities_Immediate));

        if (mTaskItems.get(position).getStatus().equals(mStringStatus[0]))
            holder.mFrameLayoutStatus.setBackgroundColor(holder.mFrameLayoutStatus.getResources()
                    .getColor(R.color.color_Status_New));
        else if (mTaskItems.get(position).getStatus().equals(mStringStatus[1]))
            holder.mFrameLayoutStatus.setBackgroundColor(holder.mFrameLayoutStatus.getResources()
                    .getColor(R.color.color_Status_Inprogress));
        else if (mTaskItems.get(position).getStatus().equals(mStringStatus[2]))
            holder.mFrameLayoutStatus.setBackgroundColor(holder.mFrameLayoutStatus.getResources()
                    .getColor(R.color.color_Status_Resolved));
        else if (mTaskItems.get(position).getStatus().equals(mStringStatus[3]))
            holder.mFrameLayoutStatus.setBackgroundColor(holder.mFrameLayoutStatus.getResources()
                    .getColor(R.color.color_Status_Closed));

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialog(mTaskItems.get(position));
                return true;
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickItemListener != null)
                    mOnClickItemListener.onClickItem(mTaskItems.get(position).getId());
            }
        });

        holder.mImageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickEditListener != null)
                    mOnClickEditListener.onClickEdit(mTaskItems.get(position).getId());
            }
        });


    }

    @Override
    public int getItemCount() {
        return mTaskItems.size();
    }

    public interface OnClickItemListener {
        void onClickItem(int id);
    }

    public interface OnLongClickItemListener {
        void onLongClickItem(int id, ArrayList<String> status, String title);
    }

    public interface OnClickEditListener {
        void onClickEdit(int id);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewTitle;
        public TextView mTextViewStartDate;
        public TextView mTextViewDueDate;
        public TextView mTextViewPriority;
        public FrameLayout mFrameLayoutPriority;
        public FrameLayout mFrameLayoutStatus;
        public ImageView mImageViewEdit;
        public View mView;

        public MyViewHolder(View view) {
            super(view);
            mTextViewTitle = (TextView) view.findViewById(R.id.text_title);
            mTextViewStartDate = (TextView) view.findViewById(R.id.text_start_date);
            mTextViewDueDate = (TextView) view.findViewById(R.id.text_due_date);
            mTextViewPriority = (TextView) view.findViewById(R.id.text_priority);
            mFrameLayoutPriority = (FrameLayout) view.findViewById(R.id.frame_priorities);
            mFrameLayoutStatus = (FrameLayout) view.findViewById(R.id.frame_status);
            mImageViewEdit = (ImageView) view.findViewById(R.id.image_edit);
            mView = view;
        }
    }

    public void checkOverDeadline(Task mTask, TextView mTextView) {
        int[] mTime = mTimeUtils.getFreeTime(System.currentTimeMillis(),mTimeUtils.timeToMilisecond(mTask.getDuetime()
                , mTask.getDuedate()));
        if (mTime[0] < 1 && mTime[1] < 0)
            mTextView.setTextColor(mContext.getResources().getColor(R.color.color_Priorities_Immediate));
        else
            mTextView.setTextColor(mContext.getResources().getColor(R.color.color_date));

    }
}
