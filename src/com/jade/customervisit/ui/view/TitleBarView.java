package com.jade.customervisit.ui.view;

import com.jade.customervisit.CVApplication;
import com.jade.customervisit.R;
import com.jade.customervisit.util.ToastUtil;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View.OnClickListener;

import android.view.View.OnLongClickListener;

/**
 * 自定义标题栏
 */
public class TitleBarView extends LinearLayout implements OnLongClickListener,
        OnClickListener {

    public static final String TAG = TitleBarView.class.getName();

    /** 返回按钮 */
    private LinearLayout mBackBtn;

    private TextView mBackTv;

    private LinearLayout mLeftLine1;

    /** 标题 */
    private TextView mTitleTv;

    /** 加载进度条 */
    private ProgressBar mTitlePb;

    private LinearLayout mRightLine2;

    /** 更多按钮 */
    private ImageButton mMoreBtn;

    private LinearLayout mRightLine1;

    /** 搜索按钮 */
    private View mSearchBtn;

    /** 菜单View */
    private PopupMenuView mPopupMenu;

    /** 黑色透明遮罩层 */
    private TextView mShadeTv;

    private String title;

    public TitleBarView(Context context) {
        this(context, null);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArr = context.obtainStyledAttributes(attrs,
                R.styleable.pref_view);
        title = typedArr.getString(R.styleable.pref_view_custom_title);
        initView();
        initListener();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_titlebar, this);
        mBackBtn = (LinearLayout) findViewById(R.id.titlebar_back_btn);
        mBackTv = (TextView) findViewById(R.id.title_back_tv);
        mLeftLine1 = (LinearLayout) findViewById(R.id.title_left_line1);
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mTitleTv.setClickable(true);
        mTitlePb = (ProgressBar) findViewById(R.id.titlebar_pb);
        mRightLine2 = (LinearLayout) findViewById(R.id.title_right_line2);
        mMoreBtn = (ImageButton) findViewById(R.id.titlebar_more_btn);
        mRightLine1 = (LinearLayout) findViewById(R.id.title_right_line1);
        mSearchBtn = findViewById(R.id.titlebar_search_btn);
        mPopupMenu = new PopupMenuView(getContext());
        mTitleTv.setText(title);

        updateTextSize();
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        mMoreBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mSearchBtn.setOnClickListener(this);
        mTitleTv.setOnClickListener(this);

        mMoreBtn.setOnLongClickListener(this);
        mSearchBtn.setOnLongClickListener(this);
    }

    /**
     * 显示ProgressBar
     */
    public TitleBarView showProgressBar() {
        mTitlePb.setVisibility(VISIBLE);
        return this;
    }

    /**
     * 隐藏ProgressBar
     */
    public TitleBarView hideProgressBar() {
        // mTitlePb.setAnimation(AnimationUtils.loadAnimation(getContext(),
        // android.R.anim.fade_out));
        mTitlePb.setVisibility(GONE);
        return this;
    }

    /**
     * 设置显示标题
     * 
     * @param title
     */
    public TitleBarView setTitle(String title) {
        if (title == null) {
            title = "";
        }
        this.title = title;
        // mTitleTv.setText(Html.fromHtml("<b>" + title + "</b>"));
        mTitleTv.setText(title);
        return this;
    }

    /**
     * 设置标题
     * 
     * @param resId
     *            string资源ID
     */
    public TitleBarView setTitle(int resId) {
        title = getContext().getString(resId);
        // mTitleTv.setText(Html.fromHtml("<b>" + title + "</b>"));
        mTitleTv.setText(title);
        return this;
    }

    /**
     * 获取标题TextView对象
     * 
     * @return
     */
    public TextView getTitleTv() {
        return mTitleTv;
    }

    /**
     * 返回按钮点击监听
     * 
     * @param listener
     * @return
     */
    public TitleBarView setOnBackClickListener(OnClickListener listener) {
        mBackBtn.setOnClickListener(listener);
        return this;
    }

    /**
     * 搜索按钮点击监听
     * 
     * @param listener
     * @return
     */
    public TitleBarView setOnSearchClickListener(OnClickListener listener) {
        mSearchBtn.setOnClickListener(listener);
        return this;
    }

    /**
     * 菜单项的监听器
     * 
     * @param listener
     * @return
     */
    public TitleBarView setOnMenuItemClickListener(OnItemClickListener listener) {
        mPopupMenu.setOnItemClickListener(listener);
        return this;
    }

    /**
     * 添加更多按钮点击监听
     * 
     * @param listener
     * @return
     */
    public TitleBarView setOnMoreClickListener(OnClickListener listener) {
        mMoreBtn.setOnClickListener(listener);
        return this;
    }

    /**
     * 显示返回按钮
     */
    public TitleBarView showBackBtn() {
        mBackBtn.setVisibility(VISIBLE);
        mLeftLine1.setVisibility(VISIBLE);
        return this;
    }

    /**
     * 不显示返回按钮
     */
    public TitleBarView hideBackBtn() {
        mBackBtn.setVisibility(GONE);
        mLeftLine1.setVisibility(GONE);
        return this;
    }

    /**
     * 显示更多按钮
     */
    public TitleBarView showMoreBtn() {
        mMoreBtn.setVisibility(VISIBLE);
        mRightLine2.setVisibility(VISIBLE);
        return this;
    }

    /**
     * 不显示更多按钮
     */
    public TitleBarView hideMoreBtn() {
        mMoreBtn.setVisibility(GONE);
        mRightLine2.setVisibility(GONE);
        return this;
    }

    /**
     * 显示搜索按钮
     */
    public TitleBarView showSearchBtn() {
        mSearchBtn.setVisibility(VISIBLE);
        mRightLine1.setVisibility(VISIBLE);
        return this;
    }

    /**
     * 不显示搜索按钮
     */
    public TitleBarView hideSearchBtn() {
        mSearchBtn.setVisibility(GONE);
        mRightLine1.setVisibility(GONE);
        return this;
    }

    /**
     * 添加菜单项
     * 
     * @param item
     */
    public TitleBarView addMenuItem(ActionItem item) {
        mPopupMenu.addAction(item);
        return this;
    }

    public void notifyMenuChanged() {
        if (mPopupMenu == null) {
            return;
        }
        mPopupMenu.notifyDataSetChanged();
    }

    public TitleBarView select(ActionItem item) {
        return select(item, false);
    }

    /**
     * 
     * @param item
     * @param showTitle
     *            true=标题栏显示item名称，false=标题栏不变
     * @return
     */
    public TitleBarView select(ActionItem item, boolean showTitle) {
        if (mPopupMenu == null || item == null) {
            return this;
        }
        mPopupMenu.select(item);
        if (showTitle) {
            setTitle(item.getTitle());
        }
        return this;
    }

    /**
     * 
     * @param position
     * @param showTitle
     *            true=标题栏显示item名称，false=标题栏不变
     * @return
     */
    public TitleBarView select(int position, boolean showTitle) {
        if (mPopupMenu == null || position < 0
                || position >= mPopupMenu.getItemCount()) {
            return this;
        }
        ActionItem item = mPopupMenu.select(position);
        if (item != null) {
            if (showTitle) {
                setTitle(item.getTitle());
            }
        }
        return this;
    }

    public TitleBarView select(int position) {
        return select(position, false);
    }

    /**
     * 执行返回按钮的点击
     */
    public void backPerformClick() {
        mBackBtn.performClick();
    }

    /**
     * 执行添加更多按钮的点击
     */
    public void addMorePerformClick() {
        mMoreBtn.performClick();
    }

    /**
     * 执行搜索按钮的点击
     */
    public void searchPerformClick() {
        mSearchBtn.performClick();
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
        case R.id.titlebar_more_btn:
            showHint("更多", v);
            break;
        case R.id.titlebar_search_btn: {
            String hint = mSearchBtn.getContentDescription().toString();
            showHint(hint, v);
            break;
        }

        default:
            break;
        }
        return true;
    }

    /**
     * 显示按钮提示
     * 
     * @param hint
     * @param v
     */
    private void showHint(String hint, View v) {
        ToastUtil.showLong(getContext(), hint, Gravity.TOP | Gravity.LEFT,
                (int) v.getLeft() - v.getWidth(),
                (int) v.getTop() + v.getHeight());
    }

    /**
     * 设置进度条
     * 
     * @param progressBar
     * @return
     */
    public TitleBarView setProgressBar(ProgressBar progressBar) {
        mTitlePb = progressBar;
        return this;
    }

    /**
     * 设置遮罩层
     * 
     * @param shadeTv
     * @return
     */
    public TitleBarView setShade(TextView shadeTv) {
        mShadeTv = shadeTv;
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.titlebar_more_btn:
            // 弹出操作菜单 PopupWindow
            if (mPopupMenu.getItemCount() == 0) {
                return;
            }
            mPopupMenu.showAsDropDown(v, mShadeTv);
            break;
        case R.id.titlebar_back_btn:
            ((Activity) getContext()).onBackPressed();
            break;
        case R.id.titlebar_search_btn:
            break;
        default:
            break;
        }
    }

    /**
     * 设置字号大小
     * 
     * @param style
     */
    public void updateTextSize() {
        if (isInEditMode()) {
            return;
        }
        ((TextView) mBackBtn.findViewById(R.id.title_back_tv))
                .setTextAppearance(getContext(),
                        CVApplication.cvApplication.getModuleTextStyle());
        mTitleTv.setTextAppearance(getContext(),
                CVApplication.cvApplication.getTitleTextStyle());
    }

    public View getSearchBtn() {
        return mSearchBtn;
    }

    public ImageButton getMoreBtn() {
        return mMoreBtn;
    }

    public ActionItem getMenuItem(int position) {
        if (mPopupMenu == null) {
            return null;
        }
        return mPopupMenu.getItem(position);
    }

    public void setBackText(String text) {
        mBackTv.setText(text);
    }

}
