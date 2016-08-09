package vn.softfront.worktask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import adapter.StatusDialogAdapter;
import adapter.TaskListAdapter;
import data.Database;
import model.Task;
import util.Constrans;
import util.TimeUtils;
import util.DialogUntil;
import util.Divider;

/**
 * Created by nguyen.hoai.duc on 7/26/2016.
 */
public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener,
        TaskListAdapter.OnClickItemListener, TaskListAdapter.OnLongClickItemListener,
        TaskListAdapter.OnClickEditListener, DialogUntil.IEvenCancel,
        DialogUntil.IEvenDeleteDialog, DialogUntil.IEvenDelete, StatusDialogAdapter.OnDialogItemClickListener {

    private RecyclerView mRecyclerView;
    private TaskListAdapter mTaskListAdapter;

    private StatusDialogAdapter mStatusDialogAdapter;
    private ArrayList<Task> mTaskArrayList;
    private ArrayList<Task> mTaskArrayListTemp;
    private ArrayList<String> mFilterList;

    private ImageView mImageViewAdd;

    private Database mDatabase;

    private String[] mStringStatus;
    private String[] mStringPriority;
    private String[] mStringMode;

    private CheckBox mCheckBoxNew;
    private CheckBox mCheckBoxImpogress;
    private CheckBox mCheckBoxResolved;
    private CheckBox mCheckBoxClosed;
    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(vn.softfront.worktask.R.layout.activity_main);
        initData();
        initView();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        mDatabase.open();
        checkCB(mCheckBoxNew.isChecked(), Constrans.cbNew);
        checkCB(mCheckBoxImpogress.isChecked(), Constrans.cbImpogress);
        checkCB(mCheckBoxResolved.isChecked(), Constrans.cbResolved);
        checkCB(mCheckBoxClosed.isChecked(), Constrans.cbClosed);
        sortTask();
        mTaskListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.checkbox_new:
                checkCB(isChecked, Constrans.cbNew);
                break;

            case R.id.checkbox_improgress:
                checkCB(isChecked, Constrans.cbImpogress);
                break;

            case R.id.checkbox_resolved:
                checkCB(isChecked, Constrans.cbResolved);
                break;

            case R.id.checkbox_close:
                checkCB(isChecked, Constrans.cbClosed);
                break;
            default:
        }
    }


    @Override
    public void onClickItem(int id) {
        Intent mIntent = new Intent(getApplication(), TaskActivity.class);
        mIntent.putExtra(this.getResources().getString(R.string.mode), mStringMode[0]);
        mIntent.putExtra(this.getResources().getString(R.string.id), id);
        startActivity(mIntent);
    }

    @Override
    public void onClickEdit(int id) {

        Intent mIntent = new Intent(getApplication(), TaskActivity.class);
        mIntent.putExtra(this.getResources().getString(R.string.mode), mStringMode[2]);
        mIntent.putExtra(this.getResources().getString(R.string.id), id);
        startActivity(mIntent);
    }


    @Override
    public void onLongClickItem(int id, ArrayList<String> mListStatus, String title) {
        DialogUntil.showDialog(this, R.layout.dialog_layout, title, R.id.text_view_cancel,
                R.id.text_view_delete, this, this, initDialogAdapter(mListStatus));
        mStatusDialogAdapter.setOnclick(this);
        mId = id;
    }

    @Override
    public void onCancel(Dialog mDialog) {
        mDialog.dismiss();
    }

    @Override
    public void onDeleteDialog(Dialog mDialog) {
        DialogUntil.showAlertDialog(this, null, this.getResources().getString(R.string.delete_confirm),
                null, this.getResources().getString(R.string.ok), null,
                this.getResources().getString(R.string.cancel), null, this, mId, mDialog);
    }

    @Override
    public void onDelete(int id, Dialog mDialog) {
        mDatabase.open();
        mTaskArrayList.clear();
        mTaskArrayListTemp.clear();
        mDatabase.deleteTask(id);
        checkCB(mCheckBoxNew.isChecked(), Constrans.cbNew);
        checkCB(mCheckBoxImpogress.isChecked(), Constrans.cbImpogress);
        checkCB(mCheckBoxResolved.isChecked(), Constrans.cbResolved);
        checkCB(mCheckBoxClosed.isChecked(), Constrans.cbClosed);
        sortTask();
        mTaskListAdapter.notifyDataSetChanged();
        mDatabase.close();
        mDialog.dismiss();
    }

    @Override
    public void onItemClick(String status, Dialog mDialog) {
        mDatabase.open();
        mDatabase.updateStatus(mId, status);
        checkCB(mCheckBoxNew.isChecked(), Constrans.cbNew);
        checkCB(mCheckBoxImpogress.isChecked(), Constrans.cbImpogress);
        checkCB(mCheckBoxResolved.isChecked(), Constrans.cbResolved);
        checkCB(mCheckBoxClosed.isChecked(), Constrans.cbClosed);
        mDialog.dismiss();
    }

    public StatusDialogAdapter initDialogAdapter(ArrayList<String> strStatus) {
        mStatusDialogAdapter = new StatusDialogAdapter(this, R.layout.item_dialog_layout, strStatus);
        return mStatusDialogAdapter;

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
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view_task);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new Divider(this));

        mTaskListAdapter = new TaskListAdapter(this, mTaskArrayList, mStringPriority, mStringStatus);

        mTaskListAdapter.setOnClickItemListener(this);
        mTaskListAdapter.setOnLongClickItemListener(this);
        mTaskListAdapter.setOnClickEditListener(this);
        mRecyclerView.setAdapter(mTaskListAdapter);
        mTaskListAdapter.notifyDataSetChanged();
        mImageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplication(), TaskActivity.class);
                mIntent.putExtra(getResources().getString(R.string.mode), mStringMode[1]);
                startActivity(mIntent);
            }
        });
        initCheckBox();
    }

    public void initData() {
        mStringPriority = getResources().getStringArray(R.array.priority);
        mStringStatus = getResources().getStringArray(R.array.status);
        mStringMode = getResources().getStringArray(R.array.mode);
        mDatabase = new Database(this);
        mDatabase.open();
        mTaskArrayListTemp = new ArrayList<>();
        mFilterList = new ArrayList<>();
        mTaskArrayList = mDatabase.getAllTask();
        sortTask();
        mTaskArrayListTemp.addAll(mTaskArrayList);
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
        mDatabase.open();
        mListTaskFilter = mDatabase.filterTask(mFilterList);
        mTaskArrayList.clear();
        mTaskArrayList.addAll(mListTaskFilter);
        sortTask();
        mTaskListAdapter.notifyDataSetChanged();
    }

    public void checkNoChecked() {
        if (!mCheckBoxNew.isChecked() && !mCheckBoxImpogress.isChecked() &&
                !mCheckBoxResolved.isChecked() && !mCheckBoxClosed.isChecked()) {
            mTaskArrayList.clear();
            mTaskListAdapter.notifyDataSetChanged();
        }
    }

    public void sortTask() {
        TimeUtils.sortDate(mTaskArrayList);
    }

    public void setUnChecked() {
        mCheckBoxNew.setChecked(false);
        mCheckBoxImpogress.setChecked(false);
        mCheckBoxResolved.setChecked(false);
        mCheckBoxClosed.setChecked(false);
    }

    public void checkCB(boolean isCheck, int n) {
        if (isCheck)
            addToFilter(mStringStatus[n]);
        else
            removeFromFilter(mStringStatus[n]);
        checkNoChecked();
    }

    public void initCheckBox() {
        mCheckBoxNew.setChecked(true);
        mCheckBoxImpogress.setChecked(true);
        checkCB(mCheckBoxNew.isChecked(), Constrans.cbNew);
        checkCB(mCheckBoxImpogress.isChecked(), Constrans.cbImpogress);
    }

}
