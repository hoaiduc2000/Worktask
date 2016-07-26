package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import model.Task;

/**
 * Created by nguyen.hoai.duc on 7/26/2016.
 */
public class TaskListAdapter extends ArrayAdapter<Task> {

    private Activity mContext;
    private int mLayoutId;
    private ArrayList<Task> mTaskItems;

    public TaskListAdapter(Activity context, int resource, ArrayList<Task> list) {
        super(context, resource, list);
        this.mContext = context;
        this.mLayoutId = resource;
        this.mTaskItems = list;
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

        return convertView;
    }
}
