/**
 * CustomerVisit
 * ServiceManager
 * Administrator
 * 2016-4-25 下午6:15:41
 */
package com.jade.customervisit.bll;

import java.io.File;
import java.util.List;

import org.xutils.common.Callback.Cancelable;
import org.xutils.http.RequestParams;

import com.jade.customervisit.CVApplication;
import com.jade.customervisit.bean.GetVisitInfoResult;
import com.jade.customervisit.bean.QueryContentResult;
import com.jade.customervisit.bean.QueryServiceContentResult;
import com.jade.customervisit.bean.QueryServiceStatusResult;
import com.jade.customervisit.bean.QueryWorkflowResult;
import com.jade.customervisit.bean.SubmitResult;
import com.jade.customervisit.network.RequestListener;
import com.jade.customervisit.network.WebService;
import com.jade.customervisit.util.CommonUtils;
import com.jade.customervisit.util.service.GetVisitInfoParser;
import com.jade.customervisit.util.service.QueryContentParser;
import com.jade.customervisit.util.service.QueryServiceContentParser;
import com.jade.customervisit.util.service.QueryServiceStatusParser;
import com.jade.customervisit.util.service.SubmitParser;
import com.jade.customervisit.util.workflow.QueryWorkflowParser;

/**
 * @author Administrator
 * 
 */
public class ServiceManager {

    /**  */
    public static final String TAG = ServiceManager.class.getName();

    public static final int LIMIT = 20;

    /**
     * 请求参数Key
     */
    public interface RequestKey {

        /** 用户id KEY */
        String USER_ID = "userId";

        /**
         * 服务类型，0=未完成，1=已完成，其他=全部
         */
        String TYPE = "type";

        String PAGE = "page";

        String LIMIT = "limit";

        String KEYWORD = "keyword";

        String START_TIME = "startTime";

        String END_TIME = "endTime";

        String SERVICE_ID = "serviceId";

        String CONTENT_IDS = "ids";

        String CODE = "code";

        String FLAG = "flag";

        String LAT = "lat";

        String LNG = "lng";

        String CITY = "city";

        String FILE = "file";
    }

    /**
     * 响应参数Key
     */
    public interface ResponseKey {

        /** 总数量KEY */
        String TOTAL = "total";

        /**
         * 服务内容表数据
         */
        String DATA_INFO = "dataInfo";

        String SERVICE_ID = "serviceId";

        String NAME = "name";

        String TITLE = "title";

        String CREATE_TIME = "createTime";

        /**
         * 服务类型，0=未完成，1=已完成，其他=全部
         */
        String TYPE = "type";

        String CODE_SIGN_FLAG = "codeSignFlag";

        String CODE_EXIT_FLAG = "codeExitFlag";

        String CONTENT_FLAG = "contentFlag";

        String PRAISE_FLAG = "praiseFlag";

        String TAKE_PHOTO_FLAG = "takePhotoFlag";

        String CUSTOMER_ID = "userId";

        String CUSTOMER_NAME = "username";

        String ARRIVE_TIME = "arriveTime";

        String LEAVE_TIME = "leaveTime";

        String CITY = "city";

        String PRAISE = "praise";

        String IMAGE_LIST = "imageList";

        String IMAGE_URL = "imageUrl";

        String CONTENT_ID = "id";

        String CONTENT_NAME = "name";

        String WORKFLOW_ID = "id";

        String CUSTOMER = "customer";

        String PROBLEM_FINDER = "problemFinder";

        String RECEIVE_TIME = "receiveTime";

        String HANDLE_TIME = "handleTime";

        String BRIEF = "brief";
    }

    /**
     * 登录地址
     */
    public interface Url {
        /** 查询服务内容列表 */
        String QUERY_SERVICE_CONTENT = "/queryServiceContent.do?";

        String QUERY_CONTENT = "/queryContent.do?";

        String QUERY_SERVICE_STATUS = "/queryServiceStatus.do?";

        String SUBMIT_SERVICE_CONTENT = "/submitServiceContent.do?";

        String CODE_SIGN = "/codeSign.do?";

        String PRAISE = "/praise.do?";

        String SUBMIT_SERVICE_RESULT = "/submitServiceResult.do?";

        /** 获取拜访信息 */
        String GET_VISIT_INFO = "/getVisitInfo.do?";

        String DEAL_WORKFLOW_LIST = "/dealWorkflowList.do?";

        String VIEW_WORKFLOW_LIST = "/viewWorkflowList.do?";

        String DEAL_WORKFLOW = "/dealWorkflow.do?";

        String VIEW_WORKFLOW = "/viewWorkflow.do?";

        String NEW_WORKFLOW_SUBMIT = "/newWorkflowSubmit.do?";

        String DEAL_WORKFLOW_SUBMIT = "/dealWorkflowSubmit.do?";
    }

    public static Cancelable queryServiceContent(int type, int page,
            String keyword,
            final RequestListener<QueryServiceContentResult> listener) {
        String[] keys = { RequestKey.USER_ID, RequestKey.TYPE, RequestKey.PAGE,
                RequestKey.LIMIT, RequestKey.KEYWORD };
        String[] values = { CVApplication.cvApplication.getUserid(),
                String.valueOf(type), String.valueOf(page),
                String.valueOf(LIMIT), keyword };
        return WebService.post(Url.QUERY_SERVICE_CONTENT, keys, values,
                listener, new QueryServiceContentParser(listener));
    }

    public static Cancelable queryContent(String serviceId,
            final RequestListener<QueryContentResult> listener) {
        String[] keys = { RequestKey.USER_ID, RequestKey.SERVICE_ID };
        String[] values = { CVApplication.cvApplication.getUserid(),
                serviceId };
        return WebService.post(Url.QUERY_CONTENT, keys, values, listener,
                new QueryContentParser(listener));
    }

    public static Cancelable queryServiceStatus(String serviceId,
            final RequestListener<QueryServiceStatusResult> listener) {
        String[] keys = { RequestKey.USER_ID, RequestKey.SERVICE_ID };
        String[] values = { CVApplication.cvApplication.getUserid(),
                serviceId };
        return WebService.post(Url.QUERY_SERVICE_STATUS, keys, values, listener,
                new QueryServiceStatusParser(listener));
    }

    public static Cancelable getVisitInfo(int type, int page, String keyword,
            final RequestListener<GetVisitInfoResult> listener) {
        String[] keys = { RequestKey.USER_ID, RequestKey.TYPE, RequestKey.PAGE,
                RequestKey.LIMIT, RequestKey.KEYWORD };
        String[] values = { CVApplication.cvApplication.getUserid(),
                String.valueOf(type), String.valueOf(page),
                String.valueOf(LIMIT), keyword };
        return WebService.post(Url.GET_VISIT_INFO, keys, values, listener,
                new GetVisitInfoParser(listener));
    }

    public static Cancelable submitContent(String ids, String serviceId,
            final RequestListener<SubmitResult> listener) {
        String[] keys = { RequestKey.USER_ID, RequestKey.SERVICE_ID,
                RequestKey.CONTENT_IDS };
        String[] values = { CVApplication.cvApplication.getUserid(), serviceId,
                ids };
        return WebService.post(Url.SUBMIT_SERVICE_CONTENT, keys, values,
                listener, new SubmitParser(listener));
    }

    public static Cancelable codeSign(String serviceId, String code,
            String flag, String lat, String lon, String city,
            final RequestListener<SubmitResult> listener) {
        String[] keys = { RequestKey.USER_ID, RequestKey.SERVICE_ID,
                RequestKey.CODE, RequestKey.FLAG, RequestKey.LAT,
                RequestKey.LNG, RequestKey.CITY };
        String[] values = { CVApplication.cvApplication.getUserid(), serviceId,
                code, flag, lat, lon, city };
        return WebService.post(Url.CODE_SIGN, keys, values, listener,
                new SubmitParser(listener));
    }

    public static Cancelable praise(String serviceId, String code,
            final RequestListener<SubmitResult> listener) {
        String[] keys = { RequestKey.USER_ID, RequestKey.SERVICE_ID,
                RequestKey.CODE };
        String[] values = { CVApplication.cvApplication.getUserid(), serviceId,
                code };
        return WebService.post(Url.PRAISE, keys, values, listener,
                new SubmitParser(listener));
    }

    public static Cancelable photoSign(String serviceId, List<File> files,
            final RequestListener<SubmitResult> listener) {
        String[] keys = { RequestKey.USER_ID, RequestKey.SERVICE_ID };
        String[] values = { CVApplication.cvApplication.getUserid(),
                serviceId };
        RequestParams params = CommonUtils.createParams(keys, values,
                WebService.URL + Url.SUBMIT_SERVICE_RESULT);
        params.setMultipart(true);
        for (File file : files) {
            params.addBodyParameter(RequestKey.FILE, file);
        }
        return WebService.upload(params, listener, new SubmitParser(listener));
    }

    public static Cancelable dealWorkflowList(int page, String keyword,
            final RequestListener<QueryWorkflowResult> listener) {
        String[] keys = { RequestKey.USER_ID, RequestKey.PAGE, RequestKey.LIMIT,
                RequestKey.KEYWORD };
        String[] values = { CVApplication.cvApplication.getUserid(),
                String.valueOf(page), String.valueOf(LIMIT), keyword };
        return WebService.post(Url.DEAL_WORKFLOW_LIST, keys, values, listener,
                new QueryWorkflowParser(listener));
    }

    public static Cancelable viewWorkflowList(int page, String keyword,
            final RequestListener<QueryWorkflowResult> listener) {
        String[] keys = { RequestKey.USER_ID, RequestKey.PAGE, RequestKey.LIMIT,
                RequestKey.KEYWORD };
        String[] values = { CVApplication.cvApplication.getUserid(),
                String.valueOf(page), String.valueOf(LIMIT), keyword };
        return WebService.post(Url.VIEW_WORKFLOW_LIST, keys, values, listener,
                new QueryWorkflowParser(listener));
    }
}
