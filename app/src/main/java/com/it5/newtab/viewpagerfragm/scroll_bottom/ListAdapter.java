package com.it5.newtab.viewpagerfragm.scroll_bottom;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.it5.newtab.R;
import com.it5.newtab.util.StringUtils;
import com.it5.newtab.util.TDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IT5 on 2016/10/28.
 */

public class ListAdapter extends BaseAdapter {

    public static final int STATE_EMPTY_ITEM = 0;
    public static final int STATE_LOAD_MORE = 1;
    public static final int STATE_NO_MORE = 2;
    public static final int STATE_NO_DATA = 3;
    public static final int STATE_LESS_ONE_PAGE = 4;
    public static final int STATE_NETWORK_ERROR = 5;
    public static final int STATE_OTHER = 6;

    protected int state = STATE_LESS_ONE_PAGE;

    protected int _loadmoreText;
    protected int _loadFinishText;
    protected int _noDateText;
    protected int mScreenWidth;

    private LayoutInflater mInflater;

    protected LayoutInflater getLayoutInflate(Context context){
        if (mInflater==null) {
            mInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        return mInflater;
    }
    public void setScreenWidth(int width){
        mScreenWidth=width;
    }
    public void setState(int state){
        this.state=state;
    }
    public int getState(){
        return this.state;
    }
    protected ArrayList<String> mDatas=new ArrayList<String>();

    public ListAdapter(){
        _loadmoreText= R.string.loading;
        _loadFinishText=R.string.loading_no_more;
        _noDateText=R.string.error_view_no_data;
    }


    @Override
    public int getCount() {
        switch (getState()){
            case STATE_EMPTY_ITEM:
                return getDataSizePlus1();
            case STATE_NETWORK_ERROR:
            case STATE_LOAD_MORE:
                return getDataSizePlus1();
            case STATE_NO_DATA:
                return 1;
            case STATE_NO_MORE:
                return getDataSizePlus1();
            case STATE_LESS_ONE_PAGE:
                return getDataSize();
            default:
                break;
        }

        return getDataSize();
    }

    public int getDataSizePlus1(){
        if (hasFooterView()) {
            return getDataSize()+1;
        }
        return getDataSize();
    }
    public int getDataSize(){
        return mDatas.size();
    }

    public ArrayList<String> getData(){
        return mDatas==null?(mDatas=new ArrayList<>()):mDatas;
    }

    public void setData(ArrayList data){
        mDatas=data;
        notifyDataSetChanged();
    }
    public void addData(List data){
        if (mDatas!=null&& data!=null &&!data.isEmpty()) {
            mDatas.addAll(data);
        }
        notifyDataSetChanged();
    }
    public void addItem(String obj){
        if (mDatas!=null) {
            mDatas.add(obj);
        }
        notifyDataSetChanged();
    }

    public void addItem(int pos,String obj){
        if (mDatas!=null) {
            mDatas.add(pos,obj);
        }
        notifyDataSetChanged();
    }

    public void removeItem(Object obj){
        mDatas.remove(obj);
        notifyDataSetChanged();
    }
    public void clear(){
        mDatas.clear();
        notifyDataSetChanged();
    }
    public void setLoadmoreText(int loadmoreText){
        _loadmoreText=loadmoreText;
    }
    public void setLoadFinishText(int loadFinishText){
        _loadFinishText=loadFinishText;
    }

    public void setNoDataText(int noDataText){
        _noDateText=noDataText;
    }
    protected boolean loadMoreHasBg(){
        return true;
    }

    @Override
    public String getItem(int position) {
        if (mDatas.size()>position) {
            return mDatas.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected View getRealView(int position,View convertView,ViewGroup group){

        return null;
    }
    private LinearLayout mFooterView;
    protected  boolean hasFooterView(){
        return true;
    }
    public View getFootView(){
        return mFooterView;
    }

    protected void setText(TextView textView,String txt, boolean needGone){
        if (txt==null|| TextUtils.isEmpty(txt)) {
            if (needGone) {
                textView.setVisibility(View.GONE);
            }
        }else {
            textView.setText(txt);
        }
    }

    protected void setText(TextView textView, String text){
        setText(textView,text,false);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position==getCount()-1&&hasFooterView()) {
            if (getState()==STATE_LOAD_MORE||getState()==STATE_NO_MORE|| state == STATE_EMPTY_ITEM
                    || getState() == STATE_NETWORK_ERROR) {
                mFooterView= (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_footer,null);
                if (!loadMoreHasBg()) {
                    mFooterView.setBackgroundDrawable(null);
                }
                ProgressBar progressBar=getProgressBar(mFooterView);
                TextView textView=getTextView(mFooterView);
                switch (getState()){
                    case STATE_LOAD_MORE:
                        setFooterViewLoading();
                        break;
                    case STATE_NO_MORE:
                        mFooterView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        textView.setVisibility(View.VISIBLE);
                        textView.setText(_loadFinishText);
                        break;
                    case STATE_EMPTY_ITEM:
                        mFooterView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        textView.setText(_noDateText);
                        break;
                    case STATE_NETWORK_ERROR:
                        mFooterView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        textView.setVisibility(View.VISIBLE);
                        if (TDevice.hasInternet()) {
                            textView.setText("加载出错了");
                        }else {
                            textView.setText("没有可用的网络！");
                        }
                        break;
                    default:
                        mFooterView.setVisibility(View.GONE);
                        break;
                }
                return mFooterView;
            }
        }

        if (position<0) {
            position=0;
        }
        return getRealView(position,convertView,parent);
    }


    //设置底部 view的状态 如为空 提示成 加载中...
    public void setFooterViewLoading(String loadMsg){
        if (mFooterView==null) {
            return;
        }
        ProgressBar progressBar= (ProgressBar) mFooterView.findViewById(R.id.progressbar);
        TextView textView= (TextView) mFooterView.findViewById(R.id.text);
        mFooterView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        if (StringUtils.isEmail(loadMsg)) {
            textView.setText(_loadmoreText);
        }else {
            textView.setText(loadMsg);
        }

    }
    public void setFooterViewLoading(){
        setFooterViewLoading("");
    }

    //设置底部View 的文本
    public void setFooterViewText(String msg){
        ProgressBar progressBar= (ProgressBar) mFooterView.findViewById(R.id.progressbar);
        TextView textView= (TextView) mFooterView.findViewById(R.id.text);
        mFooterView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setText(msg);
    }

    public ProgressBar getProgressBar(View view){
        return (ProgressBar) view.findViewById(R.id.progressbar);
    }
    public TextView getTextView(View view){
        return (TextView) view.findViewById(R.id.text);
    }
}
