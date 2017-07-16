package com.casic.bluebot.adaptor;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.casic.bluebot.R;
import com.casic.bluebot.SensorHubApplication;
import com.casic.bluebot.activity.PhotoPickActivity;
import com.casic.bluebot.bean.ImageInfo;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * Created by chenchao on 15/5/6.
 *
 */
public class GridPhotoAdapter extends CursorAdapter {

    final int itemWidth;
    LayoutInflater mInflater;
    PhotoPickActivity mActivity;
    //
//    enum Mode { All, Folder }
//    private Mode mMode = Mode.All;
//
//    void setmMode(Mode mMode) {
//        this.mMode = mMode;
//    }
    View.OnClickListener mClickItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mActivity.clickPhotoItem(v);
        }
    };

    public GridPhotoAdapter(Context context, Cursor c, boolean autoRequery, PhotoPickActivity activity) {
        super(context, c, autoRequery);
        mInflater = LayoutInflater.from(context);
        mActivity = activity;
        int spacePix = context.getResources().getDimensionPixelSize(R.dimen.pickimage_gridlist_item_space);
        itemWidth = (SensorHubApplication.sWidthPix - spacePix * 4) / 3;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View convertView = mInflater.inflate(R.layout.photopick_gridlist_item, parent, false);
        ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
        layoutParams.height = itemWidth;
        layoutParams.width = itemWidth;
        convertView.setLayoutParams(layoutParams);


        GridViewHolder holder = new GridViewHolder();
        holder.icon = (ImageView) convertView.findViewById(R.id.icon);
        holder.iconFore = (ImageView) convertView.findViewById(R.id.iconFore);
        holder.check = (CheckBox) convertView.findViewById(R.id.check);
        PhotoPickActivity.GridViewCheckTag checkTag = new PhotoPickActivity.GridViewCheckTag(holder.iconFore);
        holder.check.setTag(checkTag);
        holder.check.setOnClickListener(mClickItem);
        convertView.setTag(holder);

        ViewGroup.LayoutParams iconParam = holder.icon.getLayoutParams();
        iconParam.width = itemWidth;
        iconParam.height = itemWidth;
        holder.icon.setLayoutParams(iconParam);

        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        GridViewHolder holder;
        holder = (GridViewHolder) view.getTag();

        ImageLoader imageLoader = ImageLoader.getInstance();

        String path = ImageInfo.pathAddPreFix(cursor.getString(1));
        imageLoader.displayImage(path, holder.icon, PhotoPickActivity.optionsImage);

        ((PhotoPickActivity.GridViewCheckTag) holder.check.getTag()).path = path;

        boolean picked = mActivity.isPicked(path);
        holder.check.setChecked(picked);
        holder.iconFore.setVisibility(picked ? View.VISIBLE : View.INVISIBLE);
    }

    static class GridViewHolder {
        ImageView icon;
        ImageView iconFore;
        CheckBox check;
    }
}
