package com.casic.bluebot.view;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.casic.bluebot.R;
import com.casic.bluebot.common.DialogCopy;
import com.casic.bluebot.common.LongClickLinkMovementMethod;


/**
 * Created by chaochen on 14/12/22.
 */
public class ContentAreaBase {

    protected TextView content;
    protected Html.ImageGetter imageGetter;

    public ContentAreaBase(View convertView, View.OnClickListener onClickContent, Html.ImageGetter imageGetterParamer) {
        content = (TextView) convertView.findViewById(R.id.content);
        content.setMovementMethod(LongClickLinkMovementMethod.getInstance());
        content.setOnClickListener(onClickContent);
        content.setOnLongClickListener(DialogCopy.getInstance());

        imageGetter = imageGetterParamer;
    }

    public void clearConentLongClick() {
        content.setOnLongClickListener(null);
    }

    public void setData(Object data) {
       /* String contentString = "";
        if (data instanceof TaskObject.TaskComment) {
            TaskObject.TaskComment comment = (TaskObject.TaskComment) data;
            contentString = comment.content;
        } else if (data instanceof Commit) {
            Commit commit = (Commit) data;
            contentString = commit.getTitle();
        }

        Global.MessageParse maopaoData = HtmlContent.parseReplacePhoto(contentString);
        if (maopaoData.text.isEmpty()) {
            content.setVisibility(View.GONE);
        } else {
            content.setTag(MaopaoListFragment.TAG_COMMENT_TEXT, maopaoData.text);
            content.setTag(data);
            content.setVisibility(View.VISIBLE);
            content.setText(Global.changeHyperlinkColor(maopaoData.text, imageGetter, Global.tagHandler));
        }*/
    }
}
