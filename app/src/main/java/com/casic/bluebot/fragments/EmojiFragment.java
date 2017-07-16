package com.casic.bluebot.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.casic.bluebot.R;
import com.casic.bluebot.common.MyImageGetter;
import com.casic.bluebot.view.EnterLayout;

import java.util.HashMap;


public class EmojiFragment extends Fragment {

    public enum Type {
        Small, Big
    }

    private LayoutInflater mInflater;
    private String[] mEmojiData;
    private MyImageGetter myImageGetter;
    private int deletePos;
    private EnterLayout mEnterLayout;

    private Type mType;
    private int mItemLayout;
    private int mGridViewColumns;

    public static HashMap<String, String> emojiMonkeyMap = new HashMap<String, String>();
    public static HashMap<String, String> textToMonkdyMap = new HashMap<String, String>();

    static {
        emojiMonkeyMap.put("coding_emoji_01", "����");
        emojiMonkeyMap.put("coding_emoji_02", "��");
        emojiMonkeyMap.put("coding_emoji_03", "ѹ��ɽ��");
        emojiMonkeyMap.put("coding_emoji_04", "����");
        emojiMonkeyMap.put("coding_emoji_05", "����");
        emojiMonkeyMap.put("coding_emoji_06", "��");
        emojiMonkeyMap.put("coding_emoji_07", "��");
        emojiMonkeyMap.put("coding_emoji_08", "��ҧ�Ұ�");
        emojiMonkeyMap.put("coding_emoji_09", "�ڼ�");
        emojiMonkeyMap.put("coding_emoji_10", "32����");
        emojiMonkeyMap.put("coding_emoji_11", "����");
        emojiMonkeyMap.put("coding_emoji_12", "����");
        emojiMonkeyMap.put("coding_emoji_13", "wow");
        emojiMonkeyMap.put("coding_emoji_14", "�����ɺ�");
        emojiMonkeyMap.put("coding_emoji_15", "NO!");
        emojiMonkeyMap.put("coding_emoji_16", "����");
        emojiMonkeyMap.put("coding_emoji_17", "Ү");
        emojiMonkeyMap.put("coding_emoji_18", "���տ���");
        emojiMonkeyMap.put("coding_emoji_19", "�����");
        emojiMonkeyMap.put("coding_emoji_20", "������");
        emojiMonkeyMap.put("coding_emoji_21", "˯��");
        emojiMonkeyMap.put("coding_emoji_22", "����");
        emojiMonkeyMap.put("coding_emoji_23", "Hi");
        emojiMonkeyMap.put("coding_emoji_24", "�򷢵㿩");
        emojiMonkeyMap.put("coding_emoji_25", "�Ǻ�");
        emojiMonkeyMap.put("coding_emoji_26", "��Ѫ");
        emojiMonkeyMap.put("coding_emoji_27", "Bug");
        emojiMonkeyMap.put("coding_emoji_28", "������");
        emojiMonkeyMap.put("coding_emoji_29", "����");
        emojiMonkeyMap.put("coding_emoji_30", "�Ҵ���Ŷ");
        emojiMonkeyMap.put("coding_emoji_31", "������");
        emojiMonkeyMap.put("coding_emoji_32", "�Ŷ���");
        emojiMonkeyMap.put("coding_emoji_33", "���");
        emojiMonkeyMap.put("coding_emoji_34", "ץư��");
        emojiMonkeyMap.put("coding_emoji_35", "�°�");
        emojiMonkeyMap.put("coding_emoji_36", "ð��");

        emojiMonkeyMap.put("coding_emoji_38", "2015");
        emojiMonkeyMap.put("coding_emoji_39", "����");
        emojiMonkeyMap.put("coding_emoji_40", "�����");
        emojiMonkeyMap.put("coding_emoji_41", "�ű���");
        emojiMonkeyMap.put("coding_emoji_42", "����");
        emojiMonkeyMap.put("coding_emoji_43", "�������");

        textToMonkdyMap.put("����", "coding_emoji_01");
        textToMonkdyMap.put("��", "coding_emoji_02");
        textToMonkdyMap.put("ѹ��ɽ��", "coding_emoji_03");
        textToMonkdyMap.put("����", "coding_emoji_04");
        textToMonkdyMap.put("����", "coding_emoji_05");
        textToMonkdyMap.put("��", "coding_emoji_06");
        textToMonkdyMap.put("��", "coding_emoji_07");
        textToMonkdyMap.put("��ҧ�Ұ�", "coding_emoji_08");
        textToMonkdyMap.put("�ڼ�", "coding_emoji_09");
        textToMonkdyMap.put("32����", "coding_emoji_10");
        textToMonkdyMap.put("����", "coding_emoji_11");
        textToMonkdyMap.put("����", "coding_emoji_12");
        textToMonkdyMap.put("wow", "coding_emoji_13");
        textToMonkdyMap.put("�����ɺ�", "coding_emoji_14");
        textToMonkdyMap.put("NO!", "coding_emoji_15");
        textToMonkdyMap.put("����", "coding_emoji_16");
        textToMonkdyMap.put("Ү", "coding_emoji_17");
        textToMonkdyMap.put("���տ���", "coding_emoji_18");
        textToMonkdyMap.put("�����", "coding_emoji_19");
        textToMonkdyMap.put("������", "coding_emoji_20");
        textToMonkdyMap.put("˯��", "coding_emoji_21");
        textToMonkdyMap.put("����", "coding_emoji_22");
        textToMonkdyMap.put("Hi", "coding_emoji_23");
        textToMonkdyMap.put("�򷢵㿩", "coding_emoji_24");
        textToMonkdyMap.put("�Ǻ�", "coding_emoji_25");
        textToMonkdyMap.put("��Ѫ", "coding_emoji_26");
        textToMonkdyMap.put("Bug", "coding_emoji_27");
        textToMonkdyMap.put("������", "coding_emoji_28");
        textToMonkdyMap.put("����", "coding_emoji_29");
        textToMonkdyMap.put("�Ҵ���Ŷ", "coding_emoji_30");
        textToMonkdyMap.put("������", "coding_emoji_31");
        textToMonkdyMap.put("�Ŷ���", "coding_emoji_32");
        textToMonkdyMap.put("���", "coding_emoji_33");
        textToMonkdyMap.put("ץư��", "coding_emoji_34");
        textToMonkdyMap.put("�°�", "coding_emoji_35");
        textToMonkdyMap.put("ð��", "coding_emoji_36");
        textToMonkdyMap.put("2015", "coding_emoji_38");
        textToMonkdyMap.put("����", "coding_emoji_39");
        textToMonkdyMap.put("�����", "coding_emoji_40");
        textToMonkdyMap.put("�ű���", "coding_emoji_41");
        textToMonkdyMap.put("����", "coding_emoji_42");
        textToMonkdyMap.put("�������", "coding_emoji_43");
    }

    public EmojiFragment() {
        super();
    }

    public void init(String[] emojis, MyImageGetter imageGetter, EnterLayout enterLayout, Type type) {
        mEmojiData = emojis;
        myImageGetter = imageGetter;
        deletePos = emojis.length - 1;
        mEnterLayout = enterLayout;

        mType = type;
        if (type == Type.Small) {
            mItemLayout = R.layout.gridview_emotion_emoji;
            mGridViewColumns = 7;
        } else {
            mItemLayout = R.layout.gridview_emotion_big;
            mGridViewColumns = 4;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray("mEmojiData", mEmojiData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        View v = inflater.inflate(R.layout.emoji_gridview, container, false);

        if (savedInstanceState != null) {
            mEmojiData = savedInstanceState.getStringArray("mEmojiData");
        }

        GridView gridView = (GridView) v.findViewById(R.id.gridView);
        gridView.setNumColumns(mGridViewColumns);

        gridView.setAdapter(adapterIcon);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mType == Type.Small) {
                    int realPos = (int) id;
                    if (realPos == deletePos) {
                        mEnterLayout.deleteOneChar();
                    } else {
                        String name = (String) adapterIcon.getItem((int) id);

                        if (name.equals("my100")) {
                            name = "100";
                        } else if (name.equals("a00001")) {
                            name = "+1";
                        } else if (name.equals("a00002")) {
                            name = "-1";
                        }

                        mEnterLayout.insertEmoji(name);
                    }
                } else {
                    String potoName = (String) adapterIcon.getItem((int) id);
                    String editName = emojiMonkeyMap.get(potoName);
                    mEnterLayout.insertEmoji(editName);
                }
            }
        });
        return v;
    }

    BaseAdapter adapterIcon = new BaseAdapter() {

        @Override
        public int getCount() {
            return mEmojiData.length;
        }

        @Override
        public Object getItem(int position) {
            return mEmojiData[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(mItemLayout, parent, false);
                holder = new ViewHolder();
                holder.icon = convertView.findViewById(R.id.icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String iconName = mEmojiData[position];
            holder.icon.setBackgroundDrawable(myImageGetter.getDrawable(iconName));

            return convertView;
        }

        class ViewHolder {
            public View icon;
        }
    };

}
