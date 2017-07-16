package com.casic.bluebot.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.casic.bluebot.MyPushReceiver;
import com.casic.bluebot.R;
import com.casic.bluebot.SensorHubApplication;
import com.casic.bluebot.bean.AccountInfo;
import com.casic.bluebot.bean.Message;
import com.casic.bluebot.bean.RestResponse;
import com.casic.bluebot.bean.UserObject;
import com.casic.bluebot.common.ClickSmallImage;
import com.casic.bluebot.common.FootUpdate;
import com.casic.bluebot.common.Global;
import com.casic.bluebot.common.GlobalSetting;
import com.casic.bluebot.common.HtmlContent;
import com.casic.bluebot.common.ImageLoadTool;
import com.casic.bluebot.common.MyImageGetter;
import com.casic.bluebot.common.PageInfo;
import com.casic.bluebot.common.PhotoOperate;
import com.casic.bluebot.common.StartActivity;
import com.casic.bluebot.common.htmltext.URLSpanNoUnderline;
import com.casic.bluebot.common.widget.CustomDialog;
import com.casic.bluebot.http.ApiClent;
import com.casic.bluebot.http.NetUrl;
import com.casic.bluebot.view.BlankViewDisplay;
import com.casic.bluebot.view.ContentArea;
import com.casic.bluebot.view.EnterEmojiLayout;
import com.casic.bluebot.view.EnterLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@EActivity(R.layout.activity_message_list)
public class MessageListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, FootUpdate.LoadMore, StartActivity, EnterLayout.CameraAndPhoto {

   /* private static final int RESULT_REQUEST_FOLLOW = 1002;
    private static final int RESULT_REQUEST_PICK_PHOTO = 1003;
    private static final int RESULT_REQUEST_PHOTO = 1005;
    final String HOST_MESSAGE_SEND = Global.HOST_API + "/message/send?";
    final String hostDeleteMessage = Global.HOST_API + "/message/%s";
    final String TAG_SEND_IMAGE = "TAG_SEND_IMAGE";
    final String HOST_MESSAGE_LAST = Global.HOST_API + "/message/conversations/%s/last?id=%s";
    final String HOST_USER_INFO = Global.HOST_API + "/user/key/";
    private final int REFRUSH_TIME = 3 * 1000;*/
   private static final int RESULT_REQUEST_PICK_PHOTO = 1003;
    private final int REFRUSH_TIME = 3 * 1000;
    @Extra
    UserObject mUserObject;

    @Extra
    String mGlobalKey;
    ArrayList<Message.MessageObject> mData = new ArrayList<Message.MessageObject>();
    String url = "";

    ClickSmallImage clickImage = new ClickSmallImage(this);

    @ViewById
    ListView listView;
    @ViewById
    View blankLayout;
    MyImageGetter myImageGetter = new MyImageGetter(this);

    EnterEmojiLayout mEnterLayout;

    ImageLoadTool imageLoadTool = new ImageLoadTool();

    private PhotoOperate photoOperate = new PhotoOperate(this);

    RefrushHanlder mHandler;

    public HashMap<String, PageInfo> mPages = new HashMap<>();
    public final int pageSize = 20;

    /*
    RefrushHanlder mHandler;
    int mLastId = 0;
    */
    int mLastId = 0;
    View.OnClickListener onClickRetry = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRefresh();
        }
    };
    /*
    String HOST_INSERT_IMAGE = Global.HOST_API + "/tweet/insert_image";
    */
    //提交消息回调函数
    private ApiClent.ClientCallback msgSendCallback = new ApiClent.ClientCallback() {
        @Override
        public void onSuccess(Object data) {

            String sData = data.toString();
            RestResponse resp = RestResponse.parseJson(sData);
            String message=resp.getMessage();
            if (resp.isSuccess()) {

            Message.MessageObject item = Message.MessageObject.parseJson(message);

                for (int i = mData.size() - 1; i >= 0; --i) {
                    Message.MessageObject temp = mData.get(i);
                    if (temp.getId() < item.getId()) {
                        mData.add(i, item);
                        break;
                    }
                }

                for (int i = mData.size() - 1; i >= 0; --i) {
                    Object singleItem = mData.get(i);
                    if (singleItem instanceof MyMessage) {
                        MyMessage tempMsg = (MyMessage) singleItem;
                        if (tempMsg.getCreateTime() == item.created_at) {
                            mData.remove(i);
                            break;
                        }
                    }
                }
                mEnterLayout.clearContent();
                AccountInfo.saveMessages(MessageListActivity.this, mUserObject.global_key, mData);

            }
            else{

                for (int i = mData.size() - 1; i >= 0; --i) {
                    Object singleItem = mData.get(i);
                    if (singleItem instanceof MyMessage) {
                        MyMessage tempMsg = (MyMessage) singleItem;
                        if (tempMsg.getCreateTime() == Long.parseLong(message)) {
                            tempMsg.myStyle = MyMessage.STYLE_RESEND;
                            break;
                        }
                    }
                }
                showButtomToast(message);
            }

        }
        @Override
        public void onFailure(String message) {

            for (int i = mData.size() - 1; i >= 0; --i) {
                Object singleItem = mData.get(i);
                if (singleItem instanceof MyMessage) {
                    MyMessage tempMsg = (MyMessage) singleItem;
                    if (tempMsg.getCreateTime() == Long.parseLong(message)) {
                        tempMsg.myStyle = MyMessage.STYLE_RESEND;
                        break;
                    }
                }
            }
            showButtomToast("服务器连接失败");
        }
        @Override
        public void onError(Exception e) {

        }
    };

    //提交消息回调函数
    private ApiClent.ClientCallback imageSendCallback = new ApiClent.ClientCallback() {
        @Override
        public void onSuccess(Object data) {

            //TODO LIST:图片上传成功后，再发送一个图片发送的消息
            String sData = data.toString();
            RestResponse resp = RestResponse.parseJson(sData);
            String message=resp.getMessage();
            if (resp.isSuccess()) {
                String imageUrl = message;
                String content = String.format(" ![图片](%s) ", imageUrl);
                MyMessage temp = new MyMessage(MyMessage.REQUEST_TEXT,mUserObject);
                temp.content = content;
                ApiClent.sendMsg(MessageListActivity.this, content, mUserObject.global_key,
                        SensorHubApplication.sUserObject.global_key, temp.getCreateTime(),
                        msgSendCallback);
            }
            else{
                for (int i = mData.size() - 1; i >= 0; --i) {
                    Object singleItem = mData.get(i);
                    if (singleItem instanceof MyMessage) {
                        MyMessage tempMsg = (MyMessage) singleItem;
                        if (tempMsg.getCreateTime() == Long.parseLong(message)) {
                            tempMsg.myStyle = MyMessage.STYLE_RESEND;
                            break;
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                showButtomToast("服务器连接失败");
            }

        }
        @Override
        public void onFailure(String message) {

            for (int i = mData.size() - 1; i >= 0; --i) {
                Object singleItem = mData.get(i);
                if (singleItem instanceof MyMessage) {
                    MyMessage tempMsg = (MyMessage) singleItem;
                    if (tempMsg.getCreateTime() == Long.parseLong(message)) {
                        tempMsg.myStyle = MyMessage.STYLE_RESEND;
                        break;
                    }
                }
            }
            showButtomToast("服务器连接失败");

        }
        @Override
        public void onError(Exception e) {

        }
    };

    //用户信息获取回调函数
    private ApiClent.ClientCallback userInfoFetchCallback = new ApiClent.ClientCallback() {
        @Override
        public void onSuccess(Object data) {

            String sData = data.toString();
            RestResponse resp = RestResponse.parseJson(sData);
            String message=resp.getMessage();

            if (resp.isSuccess()) {
                mUserObject = UserObject.parseJson(message);
                initControl();
            }
            else
            {
                hideProgressDialog();
                showProgressBar(false);
                showButtomToast("friend信息获取失败");
            }

        }
        @Override
        public void onFailure(String message) {

        }
        @Override
        public void onError(Exception e) {

            hideProgressDialog();
            showProgressBar(false);
            showButtomToast("服务器连接失败");
        }
    };

    //删除用户信息回调
    private ApiClent.ClientCallback delMsgCallback = new ApiClent.ClientCallback() {
        @Override
        public void onSuccess(Object data) {

            String sData = data.toString();
            RestResponse resp = RestResponse.parseJson(sData);
            String message=resp.getMessage();

            if (resp.isSuccess()) {

                deleteItem(Integer.parseInt(message));
                AccountInfo.saveMessages(MessageListActivity.this, mUserObject.global_key, mData);
            }
            else
            {
                showButtomToast("删除信息失败");
            }

        }
        @Override
        public void onFailure(String message) {

        }
        @Override
        public void onError(Exception e) {

            showButtomToast("网络连接失败");

        }
    };

    //翻页查询信息
    private ApiClent.ClientCallback pageQueryCallback = new ApiClent.ClientCallback() {
        @Override
        public void onSuccess(Object data) {

            hideProgressDialog();
            showProgressBar(false);

            String sData = data.toString();
            RestResponse resp = RestResponse.parseJson(sData);
            String message=resp.getMessage();

            if (resp.isSuccess()) {

                if (isLoadingFirstPage(url)) {
                    mData.clear();
                    // 标记信息某个人的信息已读
                    ApiClent.markMsg(MessageListActivity.this,mGlobalKey,markMSGCallback);
                    mHandler.sendEmptyMessageDelayed(0, REFRUSH_TIME);
                }

                //TODO LIST:解析服务器返回的message数据
                List<Message.MessageObject> messageObjectList = Message.MessageObject.parseJsons(message);
                for (int i = 0; i < messageObjectList.size(); ++i) {
                    mData.add(0, messageObjectList.get(i));
                }

                AccountInfo.saveMessages(MessageListActivity.this, mUserObject.global_key, mData);

                adapter.notifyDataSetChanged();

                if (mData.size() == messageObjectList.size()) {
                    listView.setSelection(mData.size() - 1);
                } else {
                    final int index = messageObjectList.size();
                    listView.setSelection(index + 1);
                }

                BlankViewDisplay.setBlank(mData.size(), this, true, blankLayout, onClickRetry);

            }

        }
        @Override
        public void onFailure(String message) {

        }
        @Override
        public void onError(Exception e) {

            hideProgressDialog();
            showProgressBar(false);

            BlankViewDisplay.setBlank(mData.size(), this, false, blankLayout, onClickRetry);
            showButtomToast("网络连接失败");
           // mFootUpdate.updateState(code, isLoadingLastPage(tag), mData.size());

        }
    };

    //MARK MSG回到函数
    private ApiClent.ClientCallback markMSGCallback = new ApiClent.ClientCallback() {
        @Override
        public void onSuccess(Object data) {

            String sData = data.toString();
            RestResponse resp = RestResponse.parseJson(sData);
            String message=resp.getMessage();

            if (resp.isSuccess()) {

            }

        }
        @Override
        public void onFailure(String message) {

        }
        @Override
        public void onError(Exception e) {

        }
    };



    View.OnClickListener mOnClickSendText = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String s = mEnterLayout.getContent();
          /*  if (EmojiFilter.containsEmptyEmoji(v.getContext(), s)) {
                return;
            }*/
            MyMessage temp = new MyMessage(MyMessage.REQUEST_TEXT,mUserObject);
            temp.content = s;
            mData.add(temp);

            ApiClent.sendMsg(MessageListActivity.this, s, mUserObject.global_key,
                    SensorHubApplication.sUserObject.global_key, temp.getCreateTime(),
                    msgSendCallback);

            adapter.notifyDataSetChanged();

            mEnterLayout.clearContent();
        }
    };
    /*
    private Uri fileUri;
    */
    private int mPxImageWidth = 0;
    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            Message.MessageObject item = (Message.MessageObject) getItem(position);
            if (item.sender.id == (item.friend.id)) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Message.MessageObject item = (Message.MessageObject) getItem(position);
            ViewHolder holder;
            if (convertView == null) {
                int res = getItemViewType(position) == 0 ? R.layout.message_list_list_item_left : R.layout.message_list_list_item_right;
                convertView = mInflater.inflate(res, parent, false);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                //TODO LIST:点击用户图片响应时间
                //holder.icon.setOnClickListener(mOnClickUser);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.contentArea = new ContentArea(convertView, null, clickImage, myImageGetter, imageLoadTool, mPxImageWidth);
                holder.contentArea.clearConentLongClick();
                holder.resend = convertView.findViewById(R.id.resend);
                holder.sending = convertView.findViewById(R.id.sending);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            imageLoadTool.loadImage(holder.icon,item.sender.avatar);
            holder.icon.setTag(item.sender.global_key);

            long lastTime = 0;
            if (position > 0) {
                lastTime = ((Message.MessageObject) getItem(position - 1)).created_at;
            }
            long selfTime = item.created_at;
            if (lessThanStandard(selfTime, lastTime)) {
                holder.time.setVisibility(View.GONE);
            } else {
                holder.time.setVisibility(View.VISIBLE);
                holder.time.setText(Global.getTimeDetail(selfTime));
            }

            if (position == 0) {
                //TODO LIST:默认显示创建的时候刷新页面，从服务器端获取数据
                /*if (!isLoadingLastPage(url)) {
                    onRefresh();
                }*/
            }

            if (item instanceof MyMessage) {
                final MyMessage myMessage = (MyMessage) item;
                if (myMessage.myStyle == MyMessage.STYLE_RESEND) {
                    holder.resend.setVisibility(View.VISIBLE);
                    holder.sending.setVisibility(View.INVISIBLE);
                    holder.resend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (myMessage.myRequestType == MyMessage.REQUEST_TEXT) {

                                ApiClent.sendMsg(MessageListActivity.this, myMessage.content, myMessage.friend.global_key,
                                        SensorHubApplication.sUserObject.global_key,myMessage.getCreateTime(),msgSendCallback);

                            } else {
                                //TODO LIST:重传图片
                                ApiClent.sendImage(MessageListActivity.this,myMessage.imageFile,myMessage.friend.global_key,
                                        SensorHubApplication.sUserObject.global_key,
                                        myMessage.getCreateTime(),imageSendCallback);
                            }
                            myMessage.myStyle = MyMessage.STYLE_SENDING;
                            adapter.notifyDataSetChanged();
                        }
                    });

                } else {
                    holder.resend.setVisibility(View.INVISIBLE);
                    holder.sending.setVisibility(View.VISIBLE);
                }

            } else {
                holder.resend.setVisibility(View.INVISIBLE);
                holder.sending.setVisibility(View.INVISIBLE);
            }

            holder.contentArea.setData(item.content);

            return convertView;
        }

        private boolean lessThanStandard(long selfTime, long lastTime) {
            return (selfTime - lastTime) < (30 * 60 * 1000);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();

            BlankViewDisplay.setBlank(mData.size(), this, true, blankLayout, onClickRetry);
        }
    };
    private int mPxImageDivide = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new RefrushHanlder();
        mHandler.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeMessages(0);
            mHandler = null;
        }
        super.onDestroy();
    }

    @AfterViews
    void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEnterLayout = new EnterEmojiLayout(this, mOnClickSendText);
        // 72 photo 3 photo 3 photo 72
        final int divide = 3;
        mPxImageWidth = Global.dpToPx(SensorHubApplication.sWidthDp - 72 * 2 - divide * 2) / 3;
        mPxImageDivide = Global.dpToPx(divide);

        if (mUserObject == null) {
            //TODO LIST:获取朋友的用户信息
            ApiClent.getUserInfo(MessageListActivity.this,mGlobalKey,userInfoFetchCallback);
        } else {
            mGlobalKey = mUserObject.global_key;
            initControl();
        }

        //清除通知栏目中对应的消息提醒
        MyPushReceiver.closeNotify(this, URLSpanNoUnderline.createMessageUrl(mGlobalKey));
        GlobalSetting.getInstance().setMessageNoNotify(mGlobalKey);

        String lastInput = AccountInfo.loadMessageDraft(this, mGlobalKey);
        mEnterLayout.setText(lastInput);
    }

    void initControl() {
        mData = AccountInfo.loadMessages(this, mUserObject.global_key);
        if (mData.isEmpty()) {
            showDialogLoading();
        }

        //TODO LIST:响应注册的输入事件响应
        //mEnterLayout.content.addTextChangedListener(new TextWatcherAt(this, this, RESULT_REQUEST_FOLLOW));
        url = String.format(NetUrl.HOST + "/message/conversations/%s?pageSize=20", mUserObject.global_key);

        getSupportActionBar().setTitle(mUserObject.name);
        mFootUpdate.initToHead(listView, mInflater, this);
        listView.setAdapter(adapter);
        listView.setSelection(mData.size());
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mEnterLayout.hideKeyboard();
                }
                return false;
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int ppp, long id) {
                final Message.MessageObject msg = mData.get((int) id);
                //TODO LIST:
                Global.MessageParse msgParse = HtmlContent.parseMessage(msg.content);

                AlertDialog.Builder builder = new AlertDialog.Builder(MessageListActivity.this);
                if (msgParse.text.isEmpty()) {
                    builder.setItems(R.array.message_action_image, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO LIST:删除消息
                            deleteMessage(msg);
                        }
                    });

                } else {
                    builder.setItems(R.array.message_action_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                Global.copy(MessageListActivity.this, msg.content);
                                showButtomToast("已复制");
                            } else if (which == 1) {
                                deleteMessage(msg);
                            }
                        }
                    });
                }

                AlertDialog dialog = builder.show();
                CustomDialog.dialogTitleLineColor(MessageListActivity.this, dialog);

                return true;
            }
        });

        //TODO LIST:翻页查找对应的消息记录
/*
        getNextPageNetwork(url, url);
*/

        listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int last;

            @Override
            public void onGlobalLayout() {
                int current = listView.getHeight();
                if (last > current) {
                    listView.setSelection(mData.size());
                }

                last = current;
            }
        });
    }

    public void getNextPageNetwork(String url, final String tag) {
        PageInfo pageInfo = mPages.get(tag);
        if (pageInfo == null) {
            pageInfo = new PageInfo();
            mPages.put(tag, pageInfo);
        }

        if (pageInfo.isLoadingLastPage()) {
            return;
        }

        String pageIndex = pageInfo.pageIndex+"";
        String pageSize = this.pageSize + "";
        ApiClent.pageQueryMsg(MessageListActivity.this,
                mUserObject.global_key, pageIndex,
                pageSize, pageQueryCallback);
    }

    private void updatePage(JSONObject json, final String tag) throws JSONException {

        PageInfo pageInfo = mPages.get(tag);
        JSONObject jsonData = json.optJSONObject("page");
        if (jsonData != null) {
            if(jsonData.has("totalPage")&&jsonData.has("pageIndex")) {
                pageInfo.pageAll = jsonData.getInt("totalPage");
                pageInfo.pageIndex = jsonData.getInt("pageIndex");
            }
        } else {
            pageInfo.pageIndex = 0;
            pageInfo.pageAll = 0;
        }
    }

    // 是否需要刷新所有数据
    public boolean isLoadingFirstPage(String tag) {
        PageInfo info = mPages.get(tag);
        return info == null || info.isNewRequest;
    }

    private void deleteMessage(Message.MessageObject msg) {
        if (msg instanceof MyMessage) {
            //TODO LIST：为什么不与服务器同步呢？
            mData.remove(msg);
            adapter.notifyDataSetChanged();
        } else {
            //TODO LIST:提交删除消息的代码到服务器
            ApiClent.deleteMsg(MessageListActivity.this,msg.getId()+"",delMsgCallback);
        }
    }

    public void photo() {
        Intent intent = new Intent(this, PhotoPickActivity.class);
        startActivityForResult(intent, RESULT_REQUEST_PICK_PHOTO);
    }

    @OptionsItem
    void action_refresh() {
        showProgressBar(true);
        loadMore();
    }

    @Override
    public void loadMore() {
        onRefresh();
    }

    void deleteItem(int itemId) {
        for (int i = 0; i < mData.size(); ++i) {
            if (mData.get(i).getId() == itemId) {
                mData.remove(i);
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void onRefresh() {
     /*   if (url == null || url.isEmpty()) {
            getNetwork(HOST_USER_INFO + mGlobalKey, HOST_USER_INFO);
            return;
        }

        getNextPageNetwork(url, url);*/
        //TODO LIST:刷新页面
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /*if (requestCode == RESULT_REQUEST_PICK_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {

                try {
                    ArrayList<ImageInfo> pickPhots = (ArrayList<ImageInfo>) data.getSerializableExtra("data");
                    for (ImageInfo item : pickPhots) {
                        Uri uri = Uri.parse(item.path);
                        sendPhotoPre(uri);
                    }
                } catch (Exception e) {
                    showMiddleToast("缩放图片失败");
                    Global.errorLog(e);
                }
                adapter.notifyDataSetChanged();
            }
        } else if (requestCode == RESULT_REQUEST_PHOTO) {
            if (resultCode == RESULT_OK) {
                try {
                    sendPhotoPre(fileUri);
                } catch (Exception e) {
                    showMiddleToast("缩放图片失败");
                    Global.errorLog(e);
                }

                adapter.notifyDataSetChanged();
            }
        } else if (requestCode == RESULT_REQUEST_FOLLOW) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra("name");
                mEnterLayout.insertText(name);
            }

        } else*/
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendPhotoPre(Uri uri) throws Exception {

        //TODO LIST:创建图片的一个message，同时提交到服务器
        File scaleFile = photoOperate.scal(uri);
        MyMessage myMessage = new MyMessage(MyMessage.REQUEST_IMAGE, mUserObject);
        String imageLink = "<div class='message-image-box'><a href='%s' target='_blank'><img class='message-image' src='%s'/?></a></div>";
        myMessage.content = String.format(imageLink, uri.toString(), uri.toString());
        myMessage.imageFile = scaleFile;
        mData.add(myMessage);

        ApiClent.sendImage(MessageListActivity.this,scaleFile,mUserObject.global_key,
                SensorHubApplication.sUserObject.global_key,
                myMessage.getCreateTime(),imageSendCallback);
    }

    @OptionsItem(android.R.id.home)
    void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (mEnterLayout != null && mEnterLayout.isEmojiKeyboardShowing()) {
            mEnterLayout.closeEmojiKeyboard();
            return;
        }

        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();

        Message.MessageObject item = null;
        if (mData.size() > 0) {
            item = mData.get(mData.size() - 1);
        }

        //TODO LIST:
/*
        UsersListFragment.ReadedUserId.setReadedUser(mGlobalKey, item);
*/
    }

    @Override
    protected void onStop() {

        String input = mEnterLayout.getContent();
        AccountInfo.saveMessageDraft(this, input, mGlobalKey);

        super.onStop();
    }

    public void refrushData() {
        //TODO LIST:刷新数据
        int lastId = mLastId;
        if (lastId == 0) {
            if (mData.size() > 0) {
                for (int i = mData.size() - 1; i >= 0; --i) {
                    Message.MessageObject item = mData.get(i);
                    if (!(item instanceof MyMessage)) {
                        lastId = item.getId();
                        break;
                    }
                }
            }
        }

       /* String url = String.format(HOST_MESSAGE_LAST, mGlobalKey, lastId);
        getNetwork(url, HOST_MESSAGE_LAST);*/
    }

    public static class MyMessage extends Message.MessageObject implements Serializable {

        public static final int STYLE_SENDING = 0;
        public static final int STYLE_RESEND = 1;

        public static final int REQUEST_TEXT = 0;
        public static final int REQUEST_IMAGE = 1;

        public int myStyle = 0;
        public int myRequestType = 0;
        public File imageFile;

        public MyMessage(int requestType,UserObject friendUser) {

            myStyle = STYLE_SENDING;
            myRequestType = requestType;

            friend = friendUser;
            sender = SensorHubApplication.sUserObject;

            created_at = Calendar.getInstance().getTimeInMillis();
        }

        public long getCreateTime() {
            return created_at;
        }
    }

    static class ViewHolder {
        TextView time;
        ImageView icon;
        ContentArea contentArea;

        View resend;
        View sending;
    }

    static class RefrushHanlder extends Handler {

        private WeakReference<MessageListActivity> mRef;

        public void addActivity(Object activity) {
            if (activity instanceof MessageListActivity) {
                mRef = new WeakReference<>((MessageListActivity) activity);
            }
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            if (mRef == null) {
                return;
            }

            MessageListActivity activity = mRef.get();
            if (activity == null) {
                return;
            }

            activity.refrushData();
        }
    }
}
