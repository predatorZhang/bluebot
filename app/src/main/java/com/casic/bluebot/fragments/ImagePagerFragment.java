package com.casic.bluebot.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.casic.bluebot.R;
import com.casic.bluebot.SensorHubApplication;
import com.casic.bluebot.bean.ImageInfo;
import com.casic.bluebot.common.Global;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.io.File;

import pl.droidsonroids.gif.GifImageView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by chaochen on 2014-9-7.
 */
@EFragment(R.layout.activity_image_pager_item)
public class ImagePagerFragment extends BaseFragment {

    public static final int HTTP_CODE_FILE_NOT_EXIST = 1304;
    public static DisplayImageOptions optionsImage = new DisplayImageOptions
            .Builder()
            .showImageForEmptyUri(R.drawable.image_not_exist)
            .showImageOnFail(R.drawable.image_not_exist)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheOnDisk(true)
            .resetViewBeforeLoading(true)
            .cacheInMemory(false)
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();
    private final View.OnClickListener onClickImageClose = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().onBackPressed();
        }
    };
    private final PhotoViewAttacher.OnPhotoTapListener onPhotoTapClose = new PhotoViewAttacher.OnPhotoTapListener() {
        @Override
        public void onPhotoTap(View view, float v, float v2) {
            getActivity().onBackPressed();
        }
    };
    private final PhotoViewAttacher.OnViewTapListener onViewTapListener = new PhotoViewAttacher.OnViewTapListener() {
        @Override
        public void onViewTap(View view, float v, float v1) {
            getActivity().onBackPressed();
        }
    };
    @ViewById
    DonutProgress circleLoading;
    @ViewById
    View imageLoadFail;
    @ViewById
    ViewGroup rootLayout;

    @ViewById
    View blankLayout;

    View image;

    @FragmentArg
    String uri;
    @FragmentArg
    String fileId;
    @FragmentArg
    int mProjectObjectId;


    public void setData(String uriString) {
        uri = uriString;
    }

    public void setData(String fileId, int mProjectObjectId) {
        this.fileId = fileId;
        this.mProjectObjectId = mProjectObjectId;
    }

    @AfterViews
    void init() {
        setHasOptionsMenu(true);
        circleLoading.setVisibility(View.INVISIBLE);
        showPhoto();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_empty, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Click
    protected final void rootLayout() {
        getActivity().onBackPressed();
    }

    @Override
    public void onDestroyView() {
        if (image != null) {
            if (image instanceof GifImageView) {
                ((GifImageView) image).setImageURI(null);
            } else if (image instanceof PhotoView) {
                try {
                    ((BitmapDrawable) ((PhotoView) image).getDrawable()).getBitmap().recycle();
                } catch (Exception e) {
                    Global.errorLog(e);
                }
            }
        }

        super.onDestroyView();
    }

    private void showPhoto() {
        if (!isAdded()) {
            return;
        }

        ImageSize size = new ImageSize(SensorHubApplication.sWidthPix, SensorHubApplication.sHeightPix);
        getImageLoad().imageLoader.loadImage(uri, size, optionsImage, new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        circleLoading.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        if (!isAdded()) {
                            return;
                        }

                        circleLoading.setVisibility(View.GONE);
                        imageLoadFail.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingComplete(final String imageUri, View view, Bitmap loadedImage) {
                        if (!isAdded()) {
                            return;
                        }

                        circleLoading.setVisibility(View.GONE);

                        File file;
                        if (ImageInfo.isLocalFile(uri)) {
                            file = ImageInfo.getLocalFile(uri);
                        } else {
                            file = getImageLoad().imageLoader.getDiskCache().get(imageUri);
                        }
                        if (Global.isGifByFile(file)) {
                            image = getActivity().getLayoutInflater().inflate(R.layout.imageview_gif, null);
                            rootLayout.addView(image);
                            image.setOnClickListener(onClickImageClose);
                        } else {
                            PhotoView photoView = (PhotoView) getActivity().getLayoutInflater().inflate(R.layout.imageview_touch, null);
                            image = photoView;
                            rootLayout.addView(image);
                            photoView.setOnPhotoTapListener(onPhotoTapClose);
                            photoView.setOnViewTapListener(onViewTapListener);
                        }

                        try {
                            if (image instanceof GifImageView) {
                                Uri uri1 = Uri.fromFile(file);
                                ((GifImageView) image).setImageURI(uri1);
                            } else if (image instanceof PhotoView) {
                                ((PhotoView) image).setImageBitmap(loadedImage);

                            }
                        } catch (Exception e) {
                            Global.errorLog(e);
                        }
                    }
                },
                new ImageLoadingProgressListener() {

                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
                        if (!isAdded()) {
                            return;
                        }

                        int progress = current * 100 / total;
                        circleLoading.setProgress(progress);
                    }
                });

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }


}
