package vn.softfront.worktask.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import vn.softfront.worktask.R;

import java.util.ArrayList;

/**
 * Created by nguyen.hoai.duc on 8/2/2016.
 */
public class StatusDialogAdapter extends ArrayAdapter<String> {
    private Activity mContext;
    private int mLayoutId;
    private ArrayList<String> mListStatus;
    private OnDialogItemClickListener mOnDialogItemClickListener;
    private Dialog mDialog;
    private String[] mStringStatus;

    public StatusDialogAdapter(Activity context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        mContext = context;
        mLayoutId = resource;
        mListStatus = objects;
        mStringStatus = mContext.getResources().getStringArray(R.array.status);
    }

    public void setDialog(Dialog mDialog) {
        this.mDialog = mDialog;
    }

    public void setOnclick(OnDialogItemClickListener mOnDialogItemClickListener) {
        this.mOnDialogItemClickListener = mOnDialogItemClickListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(mLayoutId, null);
        }
        final TextView mTextViewStatus = (TextView) convertView.findViewById(R.id.text_view_item_dialog_status);
        final FrameLayout mFrameLayout = (FrameLayout) convertView.findViewById(R.id.frame_status);
        mTextViewStatus.setText(mListStatus.get(position) + " ");

        if (mListStatus.get(position).equals(mStringStatus[0]))
            mFrameLayout.setBackgroundColor(mContext.getResources()
                    .getColor(R.color.color_Status_New));
        else if (mListStatus.get(position).equals(mStringStatus[1]))
            mFrameLayout.setBackgroundColor(mContext.getResources()
                    .getColor(R.color.color_Status_Inprogress));
        else if (mListStatus.get(position).equals(mStringStatus[2]))
            mFrameLayout.setBackgroundColor(mContext.getResources()
                    .getColor(R.color.color_Status_Resolved));
        else if (mListStatus.get(position).equals(mStringStatus[3]))
            mFrameLayout.setBackgroundColor(mContext.getResources()
                    .getColor(R.color.color_Status_Closed));


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogItemClickListener.onItemClick(mListStatus.get(position), mDialog);
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return mListStatus.size();
    }

    public interface OnDialogItemClickListener {
        void onItemClick(String status, Dialog mDialog);
    }
}
