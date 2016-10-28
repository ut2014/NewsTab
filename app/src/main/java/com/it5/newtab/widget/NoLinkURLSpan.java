package com.it5.newtab.widget;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.View;

@SuppressLint("ParcelCreator")
public class NoLinkURLSpan extends URLSpan {

	public NoLinkURLSpan(Parcel src) {
		super(src);
		// TODO Auto-generated constructor stub
	}

	public NoLinkURLSpan(String url) {
		super(url);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		ds.setColor(Color.BLACK);
		ds.setUnderlineText(false); // 去掉下划线
	}

	@Override
	public void onClick(View widget) {
		// doNothing...
	}
}
