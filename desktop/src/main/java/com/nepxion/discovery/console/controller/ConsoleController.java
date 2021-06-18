package com.nepxion.discovery.console.controller;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.entity.AuthenticationEntity;
import com.nepxion.discovery.common.entity.FormatType;
import com.nepxion.discovery.common.entity.InspectorEntity;
import com.nepxion.discovery.common.entity.InstanceEntityWrapper;
import com.nepxion.discovery.common.entity.ResultEntity;
import com.nepxion.discovery.common.entity.RouterEntity;
import com.nepxion.discovery.common.entity.UserEntity;
import com.nepxion.discovery.common.handler.DiscoveryResponseErrorHandler;
import com.nepxion.discovery.common.util.RestUtil;
import com.nepxion.discovery.common.util.UrlUtil;
import com.nepxion.discovery.console.desktop.common.context.ConsoleConstant;
import com.nepxion.discovery.console.desktop.common.context.ConsolePropertiesContext;
import com.nepxion.discovery.console.entity.Instance;

public class ConsoleController {
    public static RestTemplate restTemplate;

    private static String consoleUrl;
    private static String accessToken;

    static {
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DiscoveryResponseErrorHandler());
    }

    public static HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        if (StringUtils.isNotEmpty(accessToken)) {
            httpHeaders.add(DiscoveryConstant.ACCESS_TOKEN, accessToken);
        }

        return httpHeaders;
    }

    public static boolean authenticate(UserEntity userEntity) {
        String url = getUrl() + "authentication/authenticate";

        String result = restTemplate.postForEntity(url, userEntity, String.class).getBody();

        AuthenticationEntity authenticationEntity = RestUtil.fromJson(restTemplate, result, new TypeReference<AuthenticationEntity>() {
        });

        boolean isPassed = authenticationEntity.isPassed();
        accessToken = authenticationEntity.getToken();

        return isPassed;
    }

    public static String getDiscoveryType() {
        String url = getUrl() + "service/discovery-type";

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);

        String result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, new HashMap<String, String>()).getBody();

        String error = RestUtil.getError(restTemplate);
        if (StringUtils.isNotEmpty(error)) {
            result = error;
        }

        return result;
    }

    public static String getConfigType() {
        String url = getUrl() + "config/config-type";

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);

        String result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, new HashMap<String, String>()).getBody();

        String error = RestUtil.getError(restTemplate);
        if (StringUtils.isNotEmpty(error)) {
            result = error;
        }

        return result;
    }

    public static List<String> getGroups() {
        String url = getUrl() + "service/groups";

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);

        String result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, new HashMap<String, String>()).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<String>>() {
        });
    }

    public static List<String> getServices() {
        String url = getUrl() + "service/services";

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);

        String result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, new HashMap<String, String>()).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<String>>() {
        });
    }

    public static List<String> getGateways() {
        String url = getUrl() + "service/gateways";

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);

        String result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, new HashMap<String, String>()).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<String>>() {
        });
    }

    public static List<Instance> getInstanceList(String serviceId) {
        String url = getUrl() + "service/instance-list/" + serviceId;

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);

        String result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, new HashMap<String, String>()).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<Instance>>() {
        });
    }

    public static Map<String, List<Instance>> getInstanceMap(List<String> groups) {
        String url = getUrl() + "service/instance-map";

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<List<String>> httpEntity = new HttpEntity<List<String>>(groups, httpHeaders);

        String result = restTemplate.postForEntity(url, httpEntity, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<Map<String, List<Instance>>>() {
        });
    }

    public static List<String> getVersions(Instance instance) {
        String url = getUrl(instance) + "version/view";

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);

        String result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, new HashMap<String, String>()).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<String>>() {
        });
    }

    public static List<String> getRules(Instance instance) {
        String url = getUrl(instance) + "config/view";

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);

        String result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, new HashMap<String, String>()).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<String>>() {
        });
    }

    public static List<String> getRules(RouterEntity routerEntity) {
        String url = getUrl(routerEntity) + "config/view";

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);

        String result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, new HashMap<String, String>()).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<String>>() {
        });
    }

    public static RouterEntity routes(Instance instance, String routeServiceIds) {
        String url = getUrl(instance) + "router/routes";

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(routeServiceIds, httpHeaders);

        String result = restTemplate.postForEntity(url, httpEntity, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<RouterEntity>() {
        });
    }

    public static InspectorEntity inspect(String url, Map<String, String> headerMap, Map<String, String> parameterMap, Map<String, String> cookieMap, InspectorEntity inspectorEntity) {
        HttpHeaders httpHeaders = getHeaders();

        RestUtil.processHeader(httpHeaders, headerMap);

        url = RestUtil.processParameter(url, parameterMap);

        RestUtil.processCookie(httpHeaders, cookieMap);

        HttpEntity<InspectorEntity> httpEntity = new HttpEntity<InspectorEntity>(inspectorEntity, httpHeaders);

        String result = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<InspectorEntity>() {
        });
    }

    public static InspectorEntity inspectByHeader(String url, Map<String, String> headerMap, InspectorEntity inspectorEntity) {
        HttpHeaders httpHeaders = getHeaders();

        RestUtil.processHeader(httpHeaders, headerMap);

        HttpEntity<InspectorEntity> httpEntity = new HttpEntity<InspectorEntity>(inspectorEntity, httpHeaders);

        String result = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<InspectorEntity>() {
        });
    }

    public static InspectorEntity inspectByParameter(String url, Map<String, String> parameterMap, InspectorEntity inspectorEntity) {
        HttpHeaders httpHeaders = getHeaders();

        url = RestUtil.processParameter(url, parameterMap);

        HttpEntity<InspectorEntity> httpEntity = new HttpEntity<InspectorEntity>(inspectorEntity, httpHeaders);

        String result = restTemplate.postForEntity(url, httpEntity, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<InspectorEntity>() {
        });
    }

    public static InspectorEntity inspectByCookie(String url, Map<String, String> cookieMap, InspectorEntity inspectorEntity) {
        HttpHeaders httpHeaders = getHeaders();

        RestUtil.processCookie(httpHeaders, cookieMap);

        HttpEntity<InspectorEntity> httpEntity = new HttpEntity<InspectorEntity>(inspectorEntity, httpHeaders);

        String result = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<InspectorEntity>() {
        });
    }

    public static String remoteConfigUpdate(String group, String serviceId, String config, FormatType formatType) {
        String url = getUrl() + "config/remote/update/" + group + "/" + serviceId + "/" + formatType;

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(config, httpHeaders);

        String result = restTemplate.postForEntity(url, httpEntity, String.class).getBody();

        String error = RestUtil.getError(restTemplate);
        if (StringUtils.isNotEmpty(error)) {
            result = error;
        }

        return result;
    }

    public static String remoteConfigClear(String group, String serviceId) {
        String url = getUrl() + "config/remote/clear/" + group + "/" + serviceId;

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);

        String result = restTemplate.postForEntity(url, httpEntity, String.class).getBody();

        String error = RestUtil.getError(restTemplate);
        if (StringUtils.isNotEmpty(error)) {
            result = error;
        }

        return result;
    }

    public static String remoteConfigView(String group, String serviceId) {
        String url = getUrl() + "config/remote/view/" + group + "/" + serviceId;

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);

        String result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, new HashMap<String, String>()).getBody();

        return result;
    }

    public static List<ResultEntity> configUpdate(String serviceId, String config, boolean async) {
        String url = getUrl() + "config/update-" + getInvokeType(async) + "/" + serviceId;

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(config, httpHeaders);

        String result = restTemplate.postForEntity(url, httpEntity, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<ResultEntity>>() {
        });
    }

    public static String configUpdate(Instance instance, String config, boolean async) {
        String url = getUrl(instance) + "config/update-" + getInvokeType(async);

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(config, httpHeaders);

        String result = restTemplate.postForEntity(url, httpEntity, String.class).getBody();

        String error = RestUtil.getError(restTemplate);
        if (StringUtils.isNotEmpty(error)) {
            result = error;
        }

        return result;
    }

    public static List<ResultEntity> configClear(String serviceId, boolean async) {
        String url = getUrl() + "config/clear-" + getInvokeType(async) + "/" + serviceId;

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);

        String result = restTemplate.postForEntity(url, httpEntity, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<ResultEntity>>() {
        });
    }

    public static String configClear(Instance instance, boolean async) {
        String url = getUrl(instance) + "config/clear-" + getInvokeType(async);

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);

        String result = restTemplate.postForEntity(url, httpEntity, String.class).getBody();

        String error = RestUtil.getError(restTemplate);
        if (StringUtils.isNotEmpty(error)) {
            result = error;
        }

        return result;
    }

    public static List<ResultEntity> versionUpdate(String serviceId, String version, boolean async) {
        String url = getUrl() + "version/update-" + getInvokeType(async) + "/" + serviceId;

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(version, httpHeaders);

        String result = restTemplate.postForEntity(url, httpEntity, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<ResultEntity>>() {
        });
    }

    public static String versionUpdate(Instance instance, String version, boolean async) {
        String url = getUrl(instance) + "version/update-" + getInvokeType(async);

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(version, httpHeaders);

        String result = restTemplate.postForEntity(url, httpEntity, String.class).getBody();

        String error = RestUtil.getError(restTemplate);
        if (StringUtils.isNotEmpty(error)) {
            result = error;
        }

        return result;
    }

    public static List<ResultEntity> versionClear(String serviceId, boolean async) {
        String url = getUrl() + "version/clear-" + getInvokeType(async) + "/" + serviceId;

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);

        String result = restTemplate.postForEntity(url, httpEntity, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<ResultEntity>>() {
        });
    }

    public static String versionClear(Instance instance, boolean async) {
        String url = getUrl(instance) + "version/clear-" + getInvokeType(async);

        HttpHeaders httpHeaders = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);

        String result = restTemplate.postForEntity(url, httpEntity, String.class).getBody();

        String error = RestUtil.getError(restTemplate);
        if (StringUtils.isNotEmpty(error)) {
            result = error;
        }

        return result;
    }

    public static String getUrl() {
        String url = null;
        if (StringUtils.isNotEmpty(consoleUrl)) {
            url = consoleUrl;
        } else {
            url = ConsolePropertiesContext.getProperties().getString(ConsoleConstant.URL);
        }

        return UrlUtil.formatUrl(url);
    }

    public static void setUrl(String url) {
        consoleUrl = url;
    }

    private static String getUrl(Instance instance) {
        String url = InstanceEntityWrapper.getProtocol(instance) + "://" + instance.getHost() + ":" + instance.getPort() + InstanceEntityWrapper.getFormatContextPath(instance);

        return url;
    }

    private static String getUrl(RouterEntity routerEntity) {
        String url = routerEntity.getProtocol() + "://" + routerEntity.getHost() + ":" + routerEntity.getPort() + routerEntity.getContextPath();

        return url;
    }

    private static String getInvokeType(boolean async) {
        return async ? DiscoveryConstant.ASYNC : DiscoveryConstant.SYNC;
    }
}