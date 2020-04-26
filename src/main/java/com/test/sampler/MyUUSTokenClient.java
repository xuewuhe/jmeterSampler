package com.test.sampler;

import com.bkjk.nuus.sdk.client.oauth.OAuthProxyClient;
import com.test.common.HttpClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class MyUUSTokenClient extends AbstractJavaSamplerClient {
    private OAuthProxyClient client;
    @Override
    public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();
        arguments.addArgument("serverUrl", "http://api.qq.com");
        arguments.addArgument("scope", "singleton");
        arguments.addArgument("clientId", "bsmskjk");
        arguments.addArgument("clientSecret", "iKdu7uqnQiW3vBtqEIG");
        arguments.addArgument("accountName", "13500000001");
        arguments.addArgument("partnerUid", "7000001");
        return arguments;
    }

    @Override
    public void setupTest(JavaSamplerContext context) {
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
    public void teardownTest(JavaSamplerContext context) {

    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult sampleResult = new SampleResult();
        String serverUrl = context.getParameter("serverUrl");
        String clientId = context.getParameter("clientId");
        String clientSecret = context.getParameter("clientSecret");
        String accountName = context.getParameter("accountName");
        String partnerUid = context.getParameter("partnerUid");
        String json = "{\n" +
                "    \"clientId\":\"${clientId}\",\n" +
                "    \"clientSecret\":\"${clientSecret}\",\n" +
                "    \"grantType\":\"password\",\n" +
                "    \"loginType\":\"SILENT-REGISTER\",\n" +
                "     \"partnerKey\": \"APLUS\",\n" +
                "    \"userInfo\":{\n" +
                "     \"accountName\":\"${accountName}\",\n" +
                "        \"accountType\":\"PHONE\"\n" +
                "    },\n" +
                "   \"linkedAccounts\": {\n" +
                "    \"partnerKey\": \"APLUS\",\n" +
                "    \"partnerUid\": \"${partnerUid}\"\n" +
                "  }\n" +
                "}";
        String body = json.replace("${clientId}", clientId).replace("${clientSecret}", clientSecret).replace("${accountName}", accountName).replace("${partnerUid}", partnerUid);
        sampleResult.setSampleLabel("getUusToken");
        sampleResult.sampleStart();
        String result = null;
        try {
            result = client.userToken(body);
        } catch (Exception e) {
            //e.printStackTrace();
            sampleResult.setSuccessful(false);
        }finally {
            sampleResult.sampleEnd();
            sampleResult.setResponseMessage(result);
            //System.out.println(result);
            boolean b = result.contains("\"code\":200");
            if (Boolean.compare(true, b) == 0){
                sampleResult.setSuccessful(true);
            }else {
                sampleResult.setSuccessful(false);
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
