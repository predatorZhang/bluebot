package com.casic.bluebot.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.casic.bluebot.R;
import com.casic.bluebot.bean.HCHCDevice;

import java.util.List;


public class DeviceAdapter extends BaseAdapter{

    private Context context;

    private List<HCHCDevice> mList;

    public DeviceAdapter(List<HCHCDevice> l,Context context)
    {
        this.mList = l;
        this.context=context;
    }
    private final void click(final HCHCDevice dev)
    {
        //TODO LIST:根据点击设备项目，做出相应的操作
       // ((DeviceListActivity)this.context).showDevice(dev);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final HCHCDevice device = mList.get(position);
        ViewHolder holder = null;
        if(convertView == null){
            convertView  = LayoutInflater.from(this.context).inflate(R.layout.list_item_device, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_ip = (TextView) convertView.findViewById(R.id.tv_ip);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(device.getName());
        holder.tv_ip.setText(device.getIP());

        convertView.setOnClickListener((View.OnClickListener) new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                DeviceAdapter.this.click(device);
            }
        });
        return convertView;
    }

    class ViewHolder{
        public TextView tv_name;
        public TextView tv_ip;
    }

}
