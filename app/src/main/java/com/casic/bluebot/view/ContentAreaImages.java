package com.casic.bluebot.view;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.casic.bluebot.R;
import com.casic.bluebot.common.ImageLoadTool;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

/**
 * Created by chenchao on 15/3/31.
 * 有6张图片的控件，比如任务的评论
 */
public class ContentAreaImages extends ContentAreaBase {

    private static final int[] itemImages = new int[]{
            R.id.image0,
            R.id.image1,
            R.id.image2,
            R.id.image3,
            R.id.image4,
            R.id.image5
    };
    private static final int itemImagesMaxCount = itemImages.length;
    protected ImageLoadTool imageLoad;
    protected View imageLayout0;
    protected View imageLayout1;
    protected DisplayImageOptions imageOptions = new DisplayImageOptions
            .Builder()
            .showImageOnLoading(R.drawable.ic_default_image)
            .showImageForEmptyUri(R.drawable.ic_default_image)
            .showImageOnFail(R.drawable.ic_default_image)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();
    private int contentMarginBottom = 0;
    private ImageView images[] = new ImageView[itemImagesMaxCount];


    public ContentAreaImages(View convertView, View.OnClickListener onClickContent, View.OnClickListener onclickImage, Html.ImageGetter imageGetterParamer, ImageLoadTool loadParams, int pxImageWidth) {
        super(convertView, onClickContent, imageGetterParamer);

        imageLoad = loadParams;

        imageLayout0 = convertView.findViewById(R.id.imagesLayout0);
        imageLayout1 = convertView.findViewById(R.id.imagesLayout1);

        for (int i = 0; i < itemImagesMaxCount; ++i) {
            images[i] = (ImageView) convertView.findViewById(itemImages[i]);
            images[i].setOnClickListener(onclickImage);
            images[i].setFocusable(false);
            images[i].setLongClickable(true);
            ViewGroup.LayoutParams lp = images[i].getLayoutParams();
            lp.width = pxImageWidth;
            lp.height = pxImageWidth;
        }

        contentMarginBottom = convertView.getResources().getDimensionPixelSize(R.dimen.message_text_margin_bottom);
    }

    protected void setImageUrl(ArrayList<String> uris) {
       /* if (uris.size() == 0) {
            imageLayout0.setVisibility(View.GONE);
            imageLayout1.setVisibility(View.GONE);
        } else if (uris.size() < 3) {
            imageLayout0.setVisibility(View.VISIBLE);
            imageLayout1.setVisibility(View.GONE);
        } else {
            imageLayout0.setVisibility(View.VISIBLE);
            imageLayout1.setVisibility(View.VISIBLE);
        }

        int min = Math.min(uris.size(), images.length);
        int i = 0;
        for (; i < min; ++i) {
            images[i].setVisibility(View.VISIBLE);
            images[i].setTag(new MaopaoListFragment.ClickImageParam(uris, i, false));
            if (images[i] instanceof GifMarkImageView) {
                ((GifMarkImageView) images[i]).showGifFlag(uris.get(i));
            }

            imageLoad.loadImage(images[i], uris.get(i), imageOptions);
        }

        for (; i < itemImagesMaxCount; ++i) {
            images[i].setVisibility(View.GONE);
        }*/
    }

}
