package com.jade.customervisit.ui.view;

import java.util.ArrayList;
import java.util.List;

import com.jade.customervisit.CVApplication;
import com.jade.customervisit.R;
import com.jade.customervisit.adapter.AbsListAdapter;
import com.jade.customervisit.util.CommonUtils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 弹出菜单
 */
public class PopupMenuView extends PopupWindow implements OnKeyListener {

	private Context mContext;

	/** 弹窗子类项选中时的监听 */
	private OnItemClickListener mOnItemClickListener;

	/** 定义列表对象 */
	private ListView mListView;

	/** 定义弹窗子类项列表 */
	private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();

	/**
	 * 适配器
	 */
	private MenuAdapter mAdapter;

	/**
	 * 弹出菜单
	 * 
	 * @param context
	 */
	public PopupMenuView(Context context) {
		// 设置布局的参数
		this(context, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	public PopupMenuView(Context context, int width, int height) {
		super(LayoutInflater.from(context).inflate(R.layout.view_popupmenu,
				null), width, height);
		this.mContext = context;

		// 设置可以获得焦点
		setFocusable(true);
		// 设置弹窗内可点击
		setTouchable(true);
		// 设置弹窗外可点击
		setOutsideTouchable(true);

		setBackgroundDrawable(new ColorDrawable());

		mAdapter = new MenuAdapter(mContext, mActionItems);
		initUI();
	}

	/**
	 * 初始化弹窗列表
	 */
	private void initUI() {
		mListView = (ListView) getContentView().findViewById(R.id.title_list);

		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				// 点击子类项后，弹窗消失
				dismiss();

				if (mOnItemClickListener != null) {
					mOnItemClickListener.onItemClick(adapterView, view,
							position, id);
				}

			}
		});
		mListView.setOnKeyListener(this);

		((View) mListView.getParent())
				.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						dismiss();
						return false;
					}
				});
	}

	/**
	 * 设置指定菜单项的文字及图标
	 * 
	 * @param id
	 * @param title
	 * @param drawableId
	 */
	public void set(int id, CharSequence title, int drawableId) {
		getAction(id).set(mContext, title, drawableId);
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 获取菜单项数量
	 * 
	 * @return
	 */
	public int getItemCount() {
		return mActionItems.size();
	}

	/**
	 * 添加菜单项
	 * 
	 * @param action
	 */
	public void addAction(ActionItem action) {
		if (action != null) {
			((MenuAdapter) mAdapter).add(action);
		}
	}

	/**
	 * 添加多个菜单项
	 * 
	 * @param actions
	 */
	public void addAction(List<ActionItem> actions) {
		if (actions != null) {
			((MenuAdapter) mAdapter).addAll(actions);
		}
	}

	/**
	 * 设置子项显示数据
	 * 
	 * @param actions
	 */
	public void setActions(List<ActionItem> actions) {
		if (actions != null) {
			((MenuAdapter) mAdapter).clear();
			((MenuAdapter) mAdapter).addAll(actions);
		}
	}

	/**
	 * 清除子类项
	 */
	public void cleanAction() {
		if (!mActionItems.isEmpty()) {
			((MenuAdapter) mAdapter).clear();
		}
	}

	/**
	 * 根据位置得到子类项
	 */
	public ActionItem getAction(int id) {
		int size = mActionItems.size();
		for (int i = 0; i < size; i++) {
			if (mActionItems.get(i).id == id) {
				return mActionItems.get(i);
			}
		}
		return null;
	}

	/**
	 * 设置监听事件
	 */
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.mOnItemClickListener = onItemClickListener;
	}

	public void setAdapter(BaseAdapter adapter) {
	}

	/**
	 * 显示在正下方
	 * 
	 * @param anchor
	 * @param shadeTv
	 *            遮罩层
	 */
	public void showAsDown(View anchor, final TextView shadeTv) {
		if (shadeTv != null) {
			shadeTv.setAnimation(AnimationUtils.loadAnimation(mContext,
					android.R.anim.fade_in));
			shadeTv.setVisibility(View.VISIBLE);
		}
		showAsDown(anchor);
		setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				if (shadeTv != null) {
					shadeTv.setAnimation(AnimationUtils.loadAnimation(mContext,
							android.R.anim.fade_out));
					shadeTv.setVisibility(View.GONE);
				}
			}
		});
	}

	/**
	 * 显示正下方
	 * 
	 * @param anchor
	 * @param shadeTv
	 */
	public void showAsDropDown(View anchor, final TextView shadeTv) {
		if (shadeTv != null) {
			shadeTv.setAnimation(AnimationUtils.loadAnimation(mContext,
					android.R.anim.fade_in));
			shadeTv.setVisibility(View.VISIBLE);
		}
		super.showAsDropDown(anchor);
		setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				if (shadeTv != null) {
					shadeTv.setAnimation(AnimationUtils.loadAnimation(mContext,
							android.R.anim.fade_out));
					shadeTv.setVisibility(View.GONE);
				}
			}
		});
	}

	/**
	 * 显示在正下方
	 * 
	 * @param anchor
	 */
	public void showAsDown(View anchor) {
		int menuWidth = mContext.getResources().getDimensionPixelSize(
				R.dimen.titlebar_menu_view_width);
		showAsDropDown(anchor, -menuWidth / 2 + CommonUtils.dip2px(mContext, 5), 0);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU
				&& KeyEvent.ACTION_UP == event.getAction()) {
			dismiss();
			return true;
		}
		return false;
	}

	public ActionItem select(int position) {
		if (mAdapter == null) {
			return null;
		}
		ActionItem item = mAdapter.getItem(position);
		select(item);
		return item;
	}

	public ActionItem select(ActionItem item) {
		if (mAdapter == null) {
			return null;
		}
		mAdapter.select(item);
		return item;
	}

	public void notifyDataSetChanged() {
		if (mAdapter == null) {
			return;
		}
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 菜单适配器
	 * 
	 * @author huangzhongwen 2014-7-25 上午8:29:50
	 */
	class MenuAdapter extends AbsListAdapter<ActionItem> {

		private ActionItem selectItem;

		public MenuAdapter(Context context, List<ActionItem> mList) {
			super(context, mList);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder hd = null;
			if (convertView == null) {
				hd = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_popupmenu, null);
				hd.iv = (ImageView) convertView
						.findViewById(R.id.item_leader_iv);
				hd.tv = (TextView) convertView
						.findViewById(R.id.item_leader_tv);
				convertView.setTag(hd);
			} else {
				hd = (ViewHolder) convertView.getTag();
			}

			ActionItem item = mActionItems.get(position);
			hd.tv.setTextAppearance(mContext,
					CVApplication.cvApplication.getSettingsTextStyle());
			// 设置文本文字
			hd.tv.setText(item.mTitle);
			// 设置在文字的左边放一个图标
			if (item.mDrawable == null) {
				hd.iv.setVisibility(View.GONE);
				hd.tv.setPadding(
						mContext.getResources().getDimensionPixelSize(
								R.dimen.popupmenu_padingleft), 0, 0, 0);
			} else {
				hd.iv.setVisibility(View.VISIBLE);
				hd.iv.setImageDrawable(item.mDrawable);
				hd.tv.setPadding(0, 0, 0, 0);
			}
			if (item.equals(selectItem)) {
				hd.tv.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						R.drawable.ic_selected, 0);
				hd.tv.setTextColor(mContext.getResources().getColor(
						R.color.blue_text));
			} else {
				hd.tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				hd.tv.setTextColor(mContext.getResources().getColor(
						R.color.black_text));
			}
			return convertView;
		}

		public void select(ActionItem selectItem) {
			this.selectItem = selectItem;
			notifyDataSetChanged();
		}

		class ViewHolder {
			ImageView iv;
			TextView tv;
		}

	}

	public ActionItem getItem(int position) {
		if (mAdapter == null) {
			return null;
		}
		return mAdapter.getItem(position);
	}

}
