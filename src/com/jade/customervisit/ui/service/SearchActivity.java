package com.jade.customervisit.ui.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.jade.customervisit.ui.BaseActivity;
import com.jade.customervisit.ui.view.SearchView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 搜索主界面
 * 
 * @author huangzhongwen
 * 
 */
public class SearchActivity extends BaseActivity implements OnClickListener,
        OnEnterListener {

    public static final String TAG = SearchActivity.class.getName();

    /** 搜索View */
    @ViewInject(R.id.searchview)
    SearchView mSearchView;

    Context mContext;

    /**
     * 搜索类型（本地、远端）
     */
    SearchType mSearchType = SearchType.SCREEN;

    int curPage = 1;

    /**
     * 搜索条件
     */
    SearchScreen mScreen = new SearchScreen();

    /**
     * 搜索用户结果适配器
     */
    UserGroupAdapter mUserResultAdapter;

    /**
     * 用于按群组搜索用户的数据源
     */
    public static UserGroupAdapter userGroupsAdapter;

    /**
     * 用于按部门搜索用户的数据源
     */
    public static UserGroupAdapter departmentUsersAdapter;

    /**
     * 用于按类别搜索用户的数据源
     */
    public static TypeGroupAdapter typeUsersAdapter;

    /**
     * 用于部门搜索的数据源
     */
    public static DepartmentAdapter departAdapter;

    /**
     * 部门搜索结果适配器
     */
    public DepartmentGroupAdapter departResultAdapter;

    /**
     * 用于职位搜索的数据源
     */
    public static PositionAdapter postAdapter;

    /**
     * 部门搜索结果适配器
     */
    public PositionGroupAdapter postResultAdapter;

    /**
     * 当前列表中的结果数量
     */
    private int currentChildrenCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        // 背景透明
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.activity_search);
        ViewUtils.inject(this);

        SearchScreen screen = (SearchScreen) getIntent().getSerializableExtra(
                Consts.OPT_DATA);
        if (screen != null) {
            mScreen.setParam(screen.getParam());
            mScreen.setModule(screen.getModule());
            mScreen.setTimeType(screen.getTimeType());
            mScreen.setKeyword(screen.getKeyword());
            mScreen.setTimeInterval(screen.getTimeInterval());
            mScreen.setParam(screen.getParam());
        }

        mSearchType = (SearchType) getIntent().getSerializableExtra(
                Consts.OPT_TYPE);
        if (mSearchType == null) {
            mSearchType = SearchType.SCREEN;
        }

        initView();
        initListener();
        initData();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        // mAadvancedLayout.setVisibility(View.GONE);
        // mResultLv.setPullRefreshEnable(false);
        mResultLv.setPullLoadEnable(false);

        switch (mSearchType) {
        case DEPART:
        case POST:
        case USERS:
            mResultLv.setPullLoadEnable(false);
            mResultLv.setPullRefreshEnable(false);
            mSearchView.getCancelBtn().setText("返回");
        case NO_SCREEN:
            mSearchView.hideScreenBtn();
            break;
        case SCREEN:
            mSearchView.showScreenBtn();
            break;

        default:
            break;
        }

        LogUtils.i("group=" + userGroupsAdapter);
        LogUtils.i("typeUsers=" + typeUsersAdapter);
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        mSearchView.setOnEnterListener(this);
        mSearchView.setOnScreenClickListener(this);
        mSearchView.setOnSearchClickListener(this);
        mResultLv.setOnRefreshListener(this);
        mResultLv.setOnLoadMoreListener(this);
        mResultLv.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                return SearchActivity.this.onChildClick(parent, v,
                        groupPosition, childPosition, id);
            }
        });
    }

    private boolean onChildClick(ExpandableListView parent, View v,
            int groupPosition, int childPosition, long id) {

        if (mSearchType == SearchType.USERS) {
            // 选择搜索用户
            BaseHR hr = mUserResultAdapter.getChild(groupPosition,
                    childPosition);
            if (mUserResultAdapter.isSelect(groupPosition, childPosition)) {
                mUserResultAdapter.deselect(groupPosition, childPosition);
                if (typeUsersAdapter != null) {
                    typeUsersAdapter.selectedSet.remove(hr);
                    typeUsersAdapter.notifyDataSetChanged();
                }
                if (userGroupsAdapter != null) {
                    userGroupsAdapter.selectedSet.remove(hr);
                    userGroupsAdapter.notifyDataSetChanged();
                }
            } else {
                if (mUserResultAdapter != null) {
                    mUserResultAdapter.select(groupPosition, childPosition);
                }
                if (typeUsersAdapter != null) {
                    if (typeUsersAdapter.isSingleSelection()) {
                        typeUsersAdapter.selectedSet.clear();
                    }
                    typeUsersAdapter.selectedSet.add(hr);
                    typeUsersAdapter.notifyDataSetChanged();
                }
                if (userGroupsAdapter != null) {
                    if (userGroupsAdapter.isSingleSelection()) {
                        userGroupsAdapter.selectedSet.clear();
                    }
                    userGroupsAdapter.selectedSet.add(hr);
                    userGroupsAdapter.notifyDataSetChanged();
                }
            }
            if (typeUsersAdapter != null
                    && typeUsersAdapter.isSingleSelection()) {
                UserActivity.searchFlag = true;
                mSearchView.getCancelBtn().performClick();
            }
            return true;
        }

        if (mSearchType == SearchType.DEPART) {
            // 选择搜索部门
            BaseHR hr = departResultAdapter.getChild(groupPosition,
                    childPosition);
            if (departResultAdapter.isSelect(groupPosition, childPosition)) {
                departResultAdapter.deselect(groupPosition, childPosition);
                if (departAdapter != null) {
                    departAdapter.selectedSet.remove(hr);
                    departAdapter.notifyDataSetChanged();
                }
            } else {
                departResultAdapter.select(groupPosition, childPosition);
                if (departAdapter != null) {
                    departAdapter.selectedSet.add(hr);
                    departAdapter.notifyDataSetChanged();
                }
            }
            if (departAdapter.isSingleSelection()) {
                DepartmentActivity.searchFlag = true;
                mSearchView.getCancelBtn().performClick();
            }
            return true;
        }

        if (mSearchType == SearchType.POST) {
            // 选择搜索职位
            BaseHR hr = postResultAdapter
                    .getChild(groupPosition, childPosition);
            if (postResultAdapter.isSelect(groupPosition, childPosition)) {
                postResultAdapter.deselect(groupPosition, childPosition);
                if (postAdapter != null) {
                    postAdapter.selectedSet.remove(hr);
                    postAdapter.notifyDataSetChanged();
                }
            } else {
                postResultAdapter.select(groupPosition, childPosition);
                if (postAdapter != null) {
                    postAdapter.selectedSet.add(hr);
                    postAdapter.notifyDataSetChanged();
                }
            }
            return true;
        }

        // 搜索其他
        SearchGroup group = (SearchGroup) mAdapter.getGroup(groupPosition);
        Serializable sr = mAdapter.getChild(groupPosition, childPosition);
        if (sr instanceof Notice) {
            Notice c = (Notice) sr;
            NoticeDetailsActivity.start(mContext, c);
        } else if (sr instanceof Email) {
            Email c = (Email) sr;
            if (Child.MAIL_DRAFTS.equals(group.getType())) {
                c.setType(EmailType.DRAFTS);
                EmailEditActivity.start(mContext, OptType.WRITE, c);
            } else if (Child.MAIL_INBOX.equals(group.getType())) {
                c.setType(EmailType.INBOX);
                EmailDetailsActivity.start(mContext, c, null);
            } else if (Child.MAIL_OUTBOX.equals(group.getType())) {
                c.setType(EmailType.OUTBOX);
                EmailDetailsActivity.start(mContext, c, null);
            } else if (Child.MAIL_TRASH.equals(group.getType())) {
                c.setType(EmailType.TRASH);
                EmailDetailsActivity.start(mContext, c, null);
            }
        } else if (sr instanceof Workflow) {
            Workflow c = (Workflow) sr;
            if (Child.INCOMING_DONE.equals(group.getType())) {
                c.setType(Type.INCOMING);
                WorkflowDetailsActivity2.start(mContext, c);
            } else if (Child.INCOMING_TODO.equals(group.getType())) {
                c.setType(Type.INCOMING);
                WorkflowHandleActivity2.start(mContext, c);
            } else if (Child.DISPATCH_DONE.equals(group.getType())) {
                c.setType(Type.DISPATCH);
                WorkflowDetailsActivity2.start(mContext, c);
            } else if (Child.DISPATCH_TODO.equals(group.getType())) {
                c.setType(Type.DISPATCH);
                WorkflowHandleActivity2.start(mContext, c);
            } else if (Child.APPROVE_DONE.equals(group.getType())) {
                c.setType(Type.APPROVE);
                WorkflowDetailsActivity2.start(mContext, c);
            } else if (Child.APPROVE_TODO.equals(group.getType())) {
                c.setType(Type.APPROVE);
                WorkflowHandleActivity2.start(mContext, c);
            }
        } else if (sr instanceof FavoritesResult) {
            FavoritesResult fav = (FavoritesResult) sr;
            CommonManager.openFav(fav, mContext, loadingDialog);
        }
        return true;
    }

    /**
     * 初始化显示数据
     */
    private void initData() {
        mAdapter = new SearchResultAdapter(mContext, listGroup, mResultLv);
        mResultLv.setAdapter(mAdapter);
    }

    /**
     * 启动Activity
     * 
     * @param activity
     * @param screen
     *            搜索条件
     */
    public static void startForResult(Activity activity, SearchScreen screen,
            SearchType type) {
        Intent intent = new Intent(activity, SearchActivity.class);
        intent.putExtra(Consts.OPT_DATA, screen);
        intent.putExtra(Consts.OPT_TYPE, type);
        activity.startActivityForResult(intent,
                CommonManager.SEARCH_REQUEST_CODE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.search_screen_btn:
            SearchScreenActivity.startForResult(this, mScreen, SCREEN_CODE);
            break;

        case R.id.search_search_btn:
            onEnter(mSearchView.getKeyword());
            break;

        default:
            break;
        }
    }

    @Override
    public void onEnter(String keyword) {
        switch (mSearchType) {
        case SCREEN:
            if (mScreen != null) {
                mScreen.setKeyword(keyword);
            }
            CommonManager.getSearchList(mScreen, curPage,
                    new RequestListener<GetSearchListResult>() {

                        @Override
                        public void onSuccess(int stateCode,
                                GetSearchListResult result) {
                            if (result != null) {
                                if (result.getResultList().size() == 0) {
                                    if (curPage == 1) {
                                        MsgDialog.show(mContext, "搜索",
                                                "无搜索结果！", BtnType.ONE);
                                        mAdapter.changeData(result
                                                .getResultList());
                                    } else {
                                        MsgDialog.show(mContext, "搜索",
                                                "无更多结果！", BtnType.ONE);
                                        mAdapter.addGroup(result
                                                .getResultList());
                                    }
                                    return;
                                }
                                setData(result.getResultList());
                                mResultLv.allExpand();
                            } else {
                                ToastUtil.showShort(mContext,
                                        R.string.connect_exception);
                            }
                        }

                        @Override
                        public void onStart() {
                            loadingDialog.show(R.string.searching);
                        }

                        @Override
                        public void onFailure(Exception e, String content) {
                            loadingDialog.dismiss();
                            ToastUtil.showShort(mContext,
                                    R.string.connect_exception);
                        }

                        @Override
                        public void onStopped() {
                            loadingDialog.dismiss();
                            mResultLv.stopLoadMore();
                            mResultLv.stopRefresh();
                            mResultLv.setPullLoadEnable(mAdapter
                                    .getGroupCount() > 0);
                        }
                    });
            break;
        case NO_SCREEN:
            Intent intent = new Intent();
            intent.putExtra(Consts.OPT_DATA, keyword);
            setResult(RESULT_OK, intent);
            finish();
            break;
        case USERS:
            new SearchHrTask(new RequestListener<List<IListGroup<BaseHR>>>() {
                @Override
                public void onStart() {
                    loadingDialog.show("正在搜索用户…");
                }

                @Override
                public void onSuccess(int stateCode,
                        List<IListGroup<BaseHR>> result) {
                    mUserResultAdapter = new UserGroupAdapter(mContext, result,
                            mResultLv);
                    mResultLv.setAdapter(mUserResultAdapter);
                    mResultLv.allExpand();
                    LogUtils.i(mUserResultAdapter.getGroups().toString());
                    if (mUserResultAdapter.getChildrenCount(0) == 0) {
                        MsgDialog.show(mContext, "搜索", "无搜索结果！", BtnType.ONE);
                    }
                }

                @Override
                public void onStopped() {
                    loadingDialog.dismiss();
                }
            }, keyword).execute();
            break;
        case DEPART:
            new SearchHrTask(new RequestListener<List<IListGroup<BaseHR>>>() {
                @Override
                public void onStart() {
                    loadingDialog.show("正在搜索部门…");
                }

                @Override
                public void onSuccess(int stateCode,
                        List<IListGroup<BaseHR>> result) {
                    departResultAdapter = new DepartmentGroupAdapter(mContext,
                            result, mResultLv);
                    mResultLv.setAdapter(departResultAdapter);
                    mResultLv.allExpand();
                    LogUtils.i(departResultAdapter.getGroups().toString());
                    if (departResultAdapter.getChildrenCount(0) == 0) {
                        MsgDialog.show(mContext, "搜索", "无搜索结果！", BtnType.ONE);
                    }
                }

                @Override
                public void onStopped() {
                    loadingDialog.dismiss();
                }
            }, keyword).execute();
            break;
        case POST:
            new SearchHrTask(new RequestListener<List<IListGroup<BaseHR>>>() {
                @Override
                public void onStart() {
                    loadingDialog.show("正在搜索部门…");
                }

                @Override
                public void onSuccess(int stateCode,
                        List<IListGroup<BaseHR>> result) {
                    postResultAdapter = new PositionGroupAdapter(mContext,
                            result, mResultLv);
                    mResultLv.setAdapter(postResultAdapter);
                    mResultLv.allExpand();
                    LogUtils.i(postResultAdapter.getGroups().toString());
                    if (postResultAdapter.getChildrenCount(0) == 0) {
                        MsgDialog.show(mContext, "搜索", "无搜索结果！", BtnType.ONE);
                    }
                }

                @Override
                public void onStopped() {
                    loadingDialog.dismiss();
                }
            }, keyword).execute();
            break;
        default:
            break;
        }
    }

    /**
     * 
     * @param resultList
     */
    protected void setData(List<IListGroup<Serializable>> resultList) {
        if (resultList == null) {
            return;
        }
        boolean hasMore = checkHasMore(resultList);
        if (curPage == 1) {
            mAdapter.changeData(resultList);
            mResultLv.setHasMore(hasMore);
        } else {
            mAdapter.addGroup(resultList);
            if (mAdapter.getAllChildrenCount() == currentChildrenCount) {
                ToastUtil.showShort(mContext, "已显示全部内容。");
                curPage--;
                mResultLv.setHasMore(false);
            } else {
                mResultLv.setHasMore(hasMore);
            }
        }
    }

    private boolean checkHasMore(List<IListGroup<Serializable>> resultList) {
        int length = resultList.size();
        for (int i = 0; i < length; i++) {
            IListGroup<Serializable> group = resultList.get(i);
            List<Serializable> list = group.getChildren();
            if (list != null && list.size() >= CommonManager.LIMIT) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRefresh() {
        curPage = 1;
        onEnter(mSearchView.getKeyword());
    }

    @Override
    public void onLoadMore() {
        if (!mResultLv.isHasMore()) {
            ToastUtil.showShort(mContext, "已显示全部内容。");
            return;
        }
        currentChildrenCount = mAdapter.getAllChildrenCount();
        curPage++;
        onEnter(mSearchView.getKeyword());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
        case RESULT_OK:
            mScreen = (SearchScreen) data.getSerializableExtra(Consts.OPT_DATA);
            LogUtils.i(mScreen.toString());
            // ToastUtil.showShort(mContext, mScreen.toString());
            curPage = 1;
            onEnter(mSearchView.getKeyword());
            break;

        default:
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class SearchHrTask extends BaseAsyncTask<List<IListGroup<BaseHR>>> {

        String keyword = "";

        public SearchHrTask(RequestListener<List<IListGroup<BaseHR>>> callback,
                String keyword) {
            super(callback);
            this.keyword = keyword;
        }

        @Override
        protected List<IListGroup<BaseHR>> doInBackground(Object... params) {
            List<IListGroup<BaseHR>> result = new ArrayList<IListGroup<BaseHR>>();
            Group group = new Group("0", "搜索结果");
            switch (mSearchType) {
            case USERS:
                searchUsers(group);
                break;
            case DEPART:
                searchDepart(group);
                break;
            case POST:
                searchPost(group);
                break;

            default:
                break;
            }
            result.add(group);
            return result;
        }

        private void searchPost(Group group) {
            if (postAdapter != null) {
                int count = postAdapter.getList().size();
                for (int j = 0; j < count; j++) {
                    BaseHR hr = postAdapter.getList().get(j);
                    String name = hr.getName().toLowerCase();
                    if (name.contains(keyword.toLowerCase())) {
                        if (!group.getChildren().contains(hr)) {
                            group.getChildren().add(hr);
                        }
                    }
                }
            }
        }

        private void searchDepart(Group group) {
            if (departAdapter != null) {
                int count = departAdapter.getList().size();
                for (int j = 0; j < count; j++) {
                    BaseHR hr = departAdapter.getList().get(j);
                    String name = hr.getName().toLowerCase();
                    if (name.contains(keyword.toLowerCase())) {
                        if (!group.getChildren().contains(hr)) {
                            group.getChildren().add(hr);
                        }
                    }
                }
            }
        }

        private void searchUsers(Group group) {
            // 按部门搜索用户
            if (departmentUsersAdapter != null) {
                int size = departmentUsersAdapter.getGroups().size();
                for (int i = 0; i < size; i++) {
                    List<BaseHR> users = departmentUsersAdapter.getGroups().get(i).getChildren();
                    int count = users.size();
                    for (int j = 0; j < count; j++) {
                        BaseHR hr = users.get(j);
                        String name = hr.getName().toLowerCase();
                        if (name.contains(keyword.toLowerCase())) {
                            if (!group.getChildren().contains(hr)) {
                                group.getChildren().add(hr);
                            }
                        }
                    }
                }
            }
            // 按群组搜索用户
            if (userGroupsAdapter != null) {
                int size = userGroupsAdapter.getGroups().size();
                for (int i = 0; i < size; i++) {
                    List<BaseHR> users = userGroupsAdapter.getGroups().get(i)
                            .getChildren();
                    int count = users.size();
                    for (int j = 0; j < count; j++) {
                        BaseHR hr = users.get(j);
                        String name = hr.getName().toLowerCase();
                        if (name.contains(keyword.toLowerCase())) {
                            if (!group.getChildren().contains(hr)) {
                                group.getChildren().add(hr);
                            }
                        }
                    }
                }
            }
            // 按类别搜索用户
            if (typeUsersAdapter != null) {
                List<com.sini.zsdx.mobileoa.model.hr.Type> types = typeUsersAdapter
                        .getTypes();
                if (types != null) {
                    int count = types.size();
                    for (int i = 0; i < count; i++) {
                        com.sini.zsdx.mobileoa.model.hr.Type type = types.get(i);
                        if (type != null) {
                            List<BaseHR> users = type.getUsers();
                            if (users != null) {
                                int size = users.size();
                                for (int j = 0; j < size; j++) {
                                    BaseHR hr = users.get(j);
                                    String name = hr.getName().toLowerCase();
                                    if (name.contains(keyword.toLowerCase())) {
                                        if (!group.getChildren().contains(hr)) {
                                            group.getChildren().add(hr);
                                        }
                                    }
                                }
                            }
                            List<Group> groups = type.getGroups();
                            if (groups != null) {
                                int size = groups.size();
                                for (int j = 0; j < size; j++) {
                                    users = groups.get(j).getUsers();
                                    if (users != null) {
                                        int ss = users.size();
                                        for (int k = 0; k < ss; k++) {
                                            BaseHR hr = users.get(k);
                                            String name = hr.getName().toLowerCase();
                                            if (name.contains(keyword.toLowerCase())) {
                                                if (!group.getChildren().contains(hr)) {
                                                    group.getChildren().add(hr);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}
