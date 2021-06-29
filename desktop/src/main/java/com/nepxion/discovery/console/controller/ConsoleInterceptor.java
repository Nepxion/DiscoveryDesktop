package com.nepxion.discovery.console.controller;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @author Fengfeng Li
 * @version 1.0
 */

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.console.cache.ConsoleCache;

public class ConsoleInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = execution.execute(request, body);

        List<String> headers = response.getHeaders().get(DiscoveryConstant.N_D_ACCESS_TOKEN);
        if (CollectionUtils.isNotEmpty(headers)) {
            String accessToken = headers.get(0);

            ConsoleCache.setAccessToken(accessToken);
        }

        return response;
    }
}