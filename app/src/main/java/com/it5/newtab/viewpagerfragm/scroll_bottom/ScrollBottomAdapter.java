package com.it5.newtab.viewpagerfragm.scroll_bottom;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by IT5 on 2016/10/28.
 */

public class ScrollBottomAdapter extends ListAdapter{
    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh=null;
        if (convertView==null) {
            convertView=getLayoutInflate(parent.getContext()).inflate(android.R.layout.simple_list_item_1,null);
            vh=new ViewHolder(convertView);
            convertView.setTag(vh);
        }else {
            vh= (ViewHolder) convertView.getTag();
        }
        vh.mTextView.setText(mDatas.get(position));
        return convertView;
    }


    static class ViewHolder{
        TextView mTextView;
        public ViewHolder(View view){
            mTextView= (TextView) view.findViewById(android.R.id.text1);
        }
    }
}
