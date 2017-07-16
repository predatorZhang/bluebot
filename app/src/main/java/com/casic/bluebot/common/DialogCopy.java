package com.casic.bluebot.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.casic.bluebot.R;
import com.casic.bluebot.common.widget.CustomDialog;

/**
 * Created by chaochen on 15/1/29.
 */
public class DialogCopy {

    private static View.OnLongClickListener onLongClickListener;

    public static View.OnLongClickListener getInstance() {
        if (onLongClickListener == null) {
            onLongClickListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setItems(R.array.message_action_text_copy, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                TextView textView = (TextView) v;
                                Context context = v.getContext();
                                //TODO LIST：复制textView中的内容
                          /*      Object dataString = textView.getTag(MaopaoListFragment.TAG_COMMENT_TEXT);
                                String text;
                                if (dataString instanceof String) {
                                    text = (String) dataString;
                                } else {
                                    text = textView.getText().toString();
                                }*/
                                String text = textView.getText().toString();
                                Global.copy(context, text);
                                Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    AlertDialog dialog = builder.show();
                    CustomDialog.dialogTitleLineColor(v.getContext(), dialog);
                    return true;
                }
            };
        }

        return onLongClickListener;
    }
}
