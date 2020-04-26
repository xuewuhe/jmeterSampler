package com.test.sampler;

import com.alibaba.fastjson.JSONObject;
import com.bkjk.nuus.sdk.client.oauth.OAuthProxyClient;
import com.test.common.HttpClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class MyGTokenClient extends AbstractJavaSamplerClient {

    private OAuthProxyClient client;

    @Override
    public Arguments getDefaultParameters() {//设置参数及默认值
        Arguments arguments = new Arguments();
        arguments.addArgument("serverUrl", "http://api.qq.com");
        arguments.addArgument("scope", "singleton");
        arguments.addArgument("clientId", "bmskjk");
        arguments.addArgument("clientSecret", "Kdu7uqnQiW3vBtqEIG");
        arguments.addArgument("grantType", "client_credentials");
        return arguments;
    }

    @Override
    public void setupTest(JavaSamplerContext context) {//运行前方法，同一线程只运行一次
        String scope = context.getParameter("scope");
        String url = context.getParameter("serverUrl");
        if (StringUtils.isNotEmpty(scope)){
            if ("singleton".equalsIgnoreCase(scope)){
                client = HttpClient.getInstence();
            }else {
                client = new OAuthProxyClient(url);
            }
        }
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {//运行后方法，同一线程只运行一次

    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {//测试方法
        SampleResult sampleResult = new SampleResult();
        String serverUrl = context.getParameter("serverUrl");
        String clientId = context.getParameter("clientId");
        String clientSecret = context.getParameter("clientSecret");
        String grantType = context.getParameter("grantType");
        JSONObject json = new JSONObject();
        json.put("clientId", clientId);
        json.put("clientSecret", clientSecret);
        json.put("grantType", grantType);
        sampleResult.setSampleLabel("getGToken");
        String result = null;
        sampleResult.sampleStart();//测试开始
        try {
            result = client.userToken(JSONObject.toJSONString(json));
        } catch (Exception e) {
            //e.printStackTrace();
            sampleResult.setSuccessful(false);
        }finally {
            sampleResult.sampleEnd();//测试结束
            sampleResult.setResponseMessage(result);
            //System.out.println(result);
            boolean b = result.contains("\"code\":200");
            if (Boolean.compare(true, b) == 0){
                sampleResult.setSuccessful(true);//设置断言成功与否
            }else {
                sampleResult.setSuccessful(false);//设置断言成功与否
            }
        }
        return sampleResult;
    }

    //public static void main(String[] args) {
    //    MyGTokenClient myClient = new MyGTokenClient();
    //    Arguments defaultParameters = myClient.getDefaultParameters();
    //    JavaSamplerContext javaSamplerContext = new JavaSamplerContext(defaultParameters);
    //    myClient.runTest(javaSamplerContext);
    //}
}
