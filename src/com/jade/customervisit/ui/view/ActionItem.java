package com.jade.customervisit.ui.view;

import com.jade.customervisit.R;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * 菜单项包装类
 * 
 * @author huangzhongwen
 * 
 */
public class ActionItem {
	// 菜单项ID
	public int id;
	// 定义图标对象
	public Drawable mDrawable;
	// 定义文本对象
	public CharSequence mTitle;
	// 字体样式
	public int mTextSizeStyle;

	public ActionItem(Context context, int id, CharSequence title,
			Drawable drawable, int textSizeStyleId) {
		this.id = id;
		this.mTitle = title;
		this.mDrawable = drawable;
		this.mTextSizeStyle = textSizeStyleId;
	}

	public ActionItem(Context context, int id, int titleId, int drawableId,
			int textSizeStyleId) {
		this(context, id, context.getString(textSizeStyleId), context
				.getResources().getDrawable(drawableId), textSizeStyleId);
	}

	public ActionItem(Context context, int id, CharSequence title,
			int drawableId, int textSizeStyleId) {
		this(context, id, title,
				context.getResources().getDrawable(drawableId), textSizeStyleId);
	}

	public ActionItem(Context context, int id, CharSequence title,
			Drawable drawable) {
		this(context, id, title, drawable, R.dimen.textsize_middle);
	}

	public ActionItem(Context context, int id, int titleId, int drawableId) {
		this(context, id, context.getString(titleId), context.getResources()
				.getDrawable(drawableId), R.style.settingsTextNormal);
	}

	public ActionItem(Context context, int id, CharSequence title,
			int drawableId) {
		this(context, id, title,
				context.getResources().getDrawable(drawableId),
				R.style.settingsTextNormal);
	}

	public ActionItem(Context context, int id, int titleId) {
		this(context, id, context.getString(titleId), null,
				R.style.settingsTextNormal);
	}

	public ActionItem(Context context, int id, CharSequence title) {
		this(context, id, title, null, R.style.settingsTextNormal);
	}

	public void set(Context context, CharSequence title, int drawableId,
			int textSizeStyleId) {
		this.mTitle = title;
		this.mDrawable = context.getResources().getDrawable(drawableId);
		this.mTextSizeStyle = textSizeStyleId;
	}

	public void set(Context context, CharSequence title, int drawableId) {
		this.mTitle = title;
		this.mDrawable = context.getResources().getDrawable(drawableId);
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return (String) mTitle;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActionItem other = (ActionItem) obj;
		if (id != other.id)
			return false;
		return true;
	}

}