/**
 * CustomerVisit
 * QueryWorkflowParser
 * zhoushujie
 * 2016年12月14日 上午3:20:49
 */
package com.jade.customervisit.util.workflow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jade.customervisit.bean.Customer;
import com.jade.customervisit.bean.CustomerManager;
import com.jade.customervisit.bean.QueryWorkflowResult;
import com.jade.customervisit.bean.RequestResult;
import com.jade.customervisit.bean.Workflow;
import com.jade.customervisit.bll.ServiceManager.ResponseKey;
import com.jade.customervisit.network.IAsyncListener;
import com.jade.customervisit.util.AbsBaseParser;

/**
 * @author zhoushujie
 *
 */
public class QueryWorkflowParser extends AbsBaseParser<QueryWorkflowResult> {

    /**
     * @param listener
     */
    public QueryWorkflowParser(IAsyncListener<QueryWorkflowResult> listener) {
        super(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.jade.customervisit.network.IAsyncListener.ResultParser#parse(java.
     * lang.String)
     */
    @Override
    public QueryWorkflowResult parse(String response) {
        QueryWorkflowResult result = null;
        try {
            JSONObject json = new JSONObject(response);
            if (json != null) {
                String retcode = json.optString(RequestResult.RET_CODE_KEY);
                String retinfo = json.optString(RequestResult.RET_INFO_KEY);
                result = new QueryWorkflowResult(retcode, retinfo);
                int total = json.optInt(ResponseKey.TOTAL);
                result.setTotal(total);
                if (!json.isNull(ResponseKey.DATA_INFO)) {
                    JSONArray array = json.optJSONArray(ResponseKey.DATA_INFO);
                    int size = array.length();
                    for (int i = 0; i < size; i++) {
                        JSONObject obj = array.getJSONObject(i);
                        String id = obj.optString(ResponseKey.WORKFLOW_ID);
                        String customerName = obj
                                .optString(ResponseKey.CUSTOMER);
                        String problemFinder = obj
                                .optString(ResponseKey.PROBLEM_FINDER);
                        String receiveTime = obj
                                .optString(ResponseKey.RECEIVE_TIME);
                        String handleTime = obj
                                .optString(ResponseKey.HANDLE_TIME);
                        String brief = obj.optString(ResponseKey.BRIEF);
                        Workflow w = new Workflow();
                        w.setId(id);
                        Customer c = new Customer();
                        c.setName(customerName);
                        w.setCustomer(c);
                        CustomerManager pf = new CustomerManager();
                        pf.setName(problemFinder);
                        w.setProblemFinder(pf);
                        w.setReceiveTime(receiveTime);
                        w.setHandleTime(handleTime);
                        w.setBrief(brief);
                        result.getWofklowList().add(w);
                    }
                }
                result.setResponse(response);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            onFailure(e);
        }
        return result;
    }

}
