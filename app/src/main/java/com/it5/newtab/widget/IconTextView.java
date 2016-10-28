package com.it5.newtab.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import com.it5.newtab.R;

/**
 * Created by IT5 on 2016/10/27.
 */

public class IconTextView extends TextView {
    private final String ATTR_ICON_SRC = "iconSrc";
    private final String NAMESPACE = "http://www.myself.com";
    private Bitmap bitMap;
    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int resouceId = attrs.getAttributeResourceValue(NAMESPACE, ATTR_ICON_SRC, R.drawable.ic_launcher);
        bitMap = BitmapFactory.decodeResource(getResources(), resouceId);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(bitMap != null){
            //源 将图像截取部分内容，这里为整张图像
            Rect src = new Rect();
            src.top = 0;
            src.left = 0;
            src.right = bitMap.getWidth();
            src.bottom = bitMap.getHeight();

            int textHeight =  (int) getTextSize();
            Rect target = new Rect();
            target.left = 0;
            //  计算图像复制到目录区域的纵坐标。由于TextView中文本内容并不是从最顶端开始绘制的，因此，需要重新计算绘制图像的纵坐标
            target.top = (int) ((getMeasuredHeight() - getTextSize()) / 2) + 1;
            target.bottom = target.top + textHeight;
            //  为了保证图像不变形，需要根据图像高度重新计算图像的宽度
            target.right = (int) (textHeight * (bitMap.getWidth() / (float) bitMap
                    .getHeight()));
            canvas.drawBitmap(bitMap, src, target, getPaint());
            canvas.translate(target.right + 2, 0);
        }
        super.onDraw(canvas);
    }
}
