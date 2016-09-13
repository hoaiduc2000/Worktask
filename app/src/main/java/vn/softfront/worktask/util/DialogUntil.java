package vn.softfront.worktask.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import vn.softfront.worktask.R;

import vn.softfront.worktask.adapter.StatusDialogAdapter;

/**
 * Created by nguyen.hoai.duc on 7/29/2016.
 */
public class DialogUntil {

    private static Context mContext;

    public static IEvenOk mEvenOk;
    public static IEvenCancel mEvenCancel;
    public static IEvenDeleteDialog mEvenDeleteDialog;
    public static IEvenDelete mEvenDelete;
    public static StatusDialogAdapter mStatusDialogAdapter;
    public static int position;

    public static void init(Context context) {
        mContext = context;
    }

    public static void showAlertDialog(Context context, final String strTitle, final String strMessage,
                                       final String[] strChoice,
                                       final String strOk,
                                       final IEvenOk evenOk,
                                       final String strCancel,
                                       final IEvenCancel evenCancel,
                                       final IEvenDelete evenDelete,
                                       final int idTask,
                                       final Dialog mDialog) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(strTitle))
            alert.setTitle(strTitle);
        if (!TextUtils.isEmpty(strMessage))
            alert.setMessage(strMessage);
        if (strChoice != null)
            alert.setSingleChoiceItems(strChoice, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    position = which;
                }
            });
        if (!TextUtils.isEmpty(strOk)) {
            alert.setPositiveButton(strOk, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (evenOk != null) {
                        mEvenOk = evenOk;
                        mEvenOk.onOk(position);
                    }

                    if (evenDelete != null) {
                        mEvenDelete = evenDelete;
                        mEvenDelete.onDelete(idTask, mDialog);
                    }
                }
            });
        }
        if (!TextUtils.isEmpty(strCancel)) {
            alert.setNegativeButton(strCancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }

        alert.show();
    }


    public static void showDialog(Activity mContext, int idLayout, String title, int idTextViewCancel,
                                  int idTextViewDelete, final IEvenCancel evenCancel,
                                  final IEvenDeleteDialog evenDelete, StatusDialogAdapter mStatusDialogAdapter) {
        final Dialog mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (idLayout != 0) {
            mDialog.setContentView(idLayout);
        }
        if (idTextViewCancel != 0) {
            TextView mTextViewCancle = (TextView) mDialog.findViewById(R.id.text_view_cancel);
            mTextViewCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (evenCancel != null) {
                        mEvenCancel = evenCancel;
                        mEvenCancel.onCancel(mDialog);
                    }
                }
            });
        }

        if (idTextViewDelete != 0) {
            TextView mTextViewDelete = (TextView) mDialog.findViewById(R.id.text_view_delete);
            mTextViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (evenDelete != null) {
                        mEvenDeleteDialog = evenDelete;
                        mEvenDeleteDialog.onDeleteDialog(mDialog);
                    }
                }
            });
        }

        if (mStatusDialogAdapter != null) {
            ListView mListView = (ListView) mDialog.findViewById(R.id.list_view_status);
            mStatusDialogAdapter.setDialog(mDialog);
            mListView.setAdapter(mStatusDialogAdapter);
            mStatusDialogAdapter.notifyDataSetChanged();
        }
        if (title != null) {
            TextView mTextViewTitle = (TextView) mDialog.findViewById(R.id.text_view_dialog_title);
            mTextViewTitle.setText(title);
        }
        mDialog.show();
    }

    public interface IEvenOk {
        void onOk(int position);
    }

    public interface IEvenCancel {
        void onCancel(Dialog mDialog);
    }

    public interface IEvenDeleteDialog {
        void onDeleteDialog(Dialog mDialog);
    }

    public interface IEvenDelete {
        void onDelete(int id, Dialog mDialog);
    }
}
