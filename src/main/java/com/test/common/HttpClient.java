package com.test.common;

import com.bkjk.nuus.sdk.client.oauth.OAuthProxyClient;

public class HttpClient {
    private static final OAuthProxyClient client = new OAuthProxyClient("http://api.stage.bkjk.cn");
    private HttpClient(String url){
        new OAuthProxyClient(url);
    }

    public static OAuthProxyClient getInstence(){
        return client;
    }

    public static void main(String[] args) {
        OAuthProxyClient client1 = HttpClient.getInstence();
        OAuthProxyClient client2 = HttpClient.getInstence();
        System.out.println(client1 == client2);
    }
}
