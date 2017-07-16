package com.casic.bluebot.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.casic.bluebot.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends android.support.v4.app.Fragment {

	public static final String EXTRA_ARG = "extra.arg";
	private static final String LOG_TAG = "StatisticsFragment";	
	private static StatisticsFragment singleton = null;
	private static final int XAXIS_LABEL_TYPE_DAY = 1;
	private static final int XAXIS_LABEL_TYPE_WEEK = 2;
	private static final int XAXIS_LABEL_TYPE_MONTH = 3;
	private static final String[] WEEK = new String[]{"Mon","Tue", "Wed", "Thu","Fri","Sat","Sun"};

	private ListView mListView;
	private LineChartListAdapter mAdapter;
	private List<LineData> list_day;
	private List<LineData> list_week;
	private List<LineData> list_month;
	private View rootView;

			
	public StatisticsFragment(){
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	
		if(rootView == null){
			rootView = inflater.inflate(R.layout.fragment_statistics, container, false);

			mListView = (ListView) rootView.findViewById(R.id.lv_charts);

			int type = getArguments().getInt(EXTRA_ARG);
			switch(type){
			case 0:
				LineData d1 = generateDataLine("5月1日", 24, 100, getXLabels(XAXIS_LABEL_TYPE_DAY), Color.rgb(192, 255, 140));
				LineData d2 = generateDataLine("5月2日", 24, 100, getXLabels(XAXIS_LABEL_TYPE_DAY), Color.rgb(255, 247, 140));
				LineData d3 = generateDataLine("5月3日", 24, 100, getXLabels(XAXIS_LABEL_TYPE_DAY), Color.rgb(192, 255, 140));
				LineData d4 = generateDataLine("5月4日", 24, 100, getXLabels(XAXIS_LABEL_TYPE_DAY), Color.rgb(255, 247, 140));
				LineData d5 = generateDataLine("5月5日", 24, 100, getXLabels(XAXIS_LABEL_TYPE_DAY), Color.rgb(192, 255, 140));
				
				list_day = new ArrayList<LineData>();
				list_day.add(d5);
				list_day.add(d4);
				list_day.add(d3);
				list_day.add(d2);
				list_day.add(d1);
				mAdapter = new LineChartListAdapter(StatisticsFragment.this.getActivity().getApplicationContext(), list_day);
				
				break;
			case 1:
				LineData w1 = generateDataLine("5月第1周", 7, 100, getXLabels(XAXIS_LABEL_TYPE_WEEK), Color.rgb(192, 255, 140));
				LineData w2 = generateDataLine("5月第2周", 7, 100, getXLabels(XAXIS_LABEL_TYPE_WEEK), Color.rgb(255, 247, 140));
				LineData w3 = generateDataLine("5月第3周", 7, 100, getXLabels(XAXIS_LABEL_TYPE_WEEK), Color.rgb(192, 255, 140));
				list_week = new ArrayList<LineData>();
				list_week.add(w3);
				list_week.add(w2);
				list_week.add(w1);
				mAdapter = new LineChartListAdapter(StatisticsFragment.this.getActivity().getApplicationContext(), list_week);
				break;
			case 2:
				LineData m1 = generateDataLine("2015年5月", 31, 100, getXLabels(XAXIS_LABEL_TYPE_MONTH), Color.rgb(192, 255, 140));
				list_month = new ArrayList<LineData>();
				list_month.add(m1);
				mAdapter = new LineChartListAdapter(StatisticsFragment.this.getActivity().getApplicationContext(), list_month);
				break;
			}
			mListView.setAdapter(mAdapter);

		}
		return rootView;
	}

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}


	private LineData generateDataLine(String label, int xCount, int yMax, List<String> xList, int color) {

	        ArrayList<Entry> entries = new ArrayList<Entry>();

	        for (int i = 0; i < xCount; i++) {
	            entries.add(new Entry((int) (Math.random() * yMax), i));
	        }

	        LineDataSet dataSet = new LineDataSet(entries, label);
	        dataSet.setLineWidth(2.5f);
	        dataSet.setCircleSize(3f);		        
	        dataSet.setCircleColor(Color.WHITE);
	        dataSet.setHighLightColor(Color.WHITE);
	        dataSet.setColor(Color.WHITE);
	        dataSet.setDrawValues(false);
	      
	        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
	        sets.add(dataSet);	       
	        LineData cd = new LineData(xList, sets);
	        return cd;
	    }

	private List<String> getXLabels( int type ){
		List<String> labels = new ArrayList<String>();
		switch(type){
		case XAXIS_LABEL_TYPE_DAY:{
			for(int i = 0; i < 24; i++){
				labels.add(String.valueOf(i));
			}
		}			
			break;
		case XAXIS_LABEL_TYPE_WEEK:
			for(int i = 0; i<7; i++){
				labels.add(WEEK[i]);
			}
			break;
		case XAXIS_LABEL_TYPE_MONTH:
			for(int i = 0; i < 31; i++){
				labels.add(String.valueOf(i));
			}
			break;
		}
		
		return labels;
	}

	class LineChartListAdapter extends ArrayAdapter<LineData>{
		private List<LineData> dataList;
		private List<View> views;
		private int[] mColors = new int[] {
				Color.rgb(255, 102, 0), 
	            Color.rgb(240, 240, 30), 
	            Color.rgb(89, 199, 250),
	            Color.rgb(250, 104, 104)
	    };
		public LineChartListAdapter(Context c, List<LineData> l){
			super(c,0, l);
			dataList = l;
			
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataList.size();
		}

		@Override
		public LineData getItem(int position) {
			// TODO Auto-generated method stub
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		public void setList( List<LineData> l){
			dataList = l;
			notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			  LineData data = dataList.get(position);

	            ViewHolder holder = null;

	            if (convertView == null) {

	                holder = new ViewHolder();
	                convertView = LayoutInflater.from(getContext()).inflate(
	                        R.layout.list_item_linechart, null);               	
					
					holder.chart = (LineChart) convertView.findViewById(R.id.bc_statistics);	
	                convertView.setTag(holder);	               
		            
	            } else {
	                holder = (ViewHolder) convertView.getTag();
	            }
	            // apply styling
	            holder.chart.setDescription("");
	            holder.chart.setDrawGridBackground(false);
	            holder.chart.setBackgroundColor(mColors[position%4]);
	            Legend l = holder.chart.getLegend();
	            l.setTextSize(12f);
	            l.setForm(LegendForm.CIRCLE);
	            l.setFormSize(6f);
	            l.setTextColor(Color.WHITE);	            
	            data.setValueTextColor(Color.WHITE);

	            XAxis xAxis = holder.chart.getXAxis();
	            xAxis.setPosition(XAxisPosition.BOTTOM);	        
	            xAxis.setDrawGridLines(false);
	            xAxis.setTextColor(Color.WHITE);
	            xAxis.setTextSize(12f);
	            
	            YAxis leftAxis = holder.chart.getAxisLeft();	      
	            leftAxis.setLabelCount(5);
	            leftAxis.setSpaceTop(15f); 
	            leftAxis.setTextColor(Color.WHITE);
	            leftAxis.setTextSize(12f);
	            
	            YAxis rightAxis = holder.chart.getAxisRight();
	            rightAxis.setTextColor(Color.TRANSPARENT);
			    holder.chart.setData(data);
	            holder.chart.animateX(700);

			return convertView;
		}
		
		 private class ViewHolder {

	            LineChart chart;
	        }
		
	}
}
