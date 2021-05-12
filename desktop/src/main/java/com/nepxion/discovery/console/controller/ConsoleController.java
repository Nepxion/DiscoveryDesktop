package com.nepxion.discovery.console.controller;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

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

    static {
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DiscoveryResponseErrorHandler());
    }

    public static boolean authenticate(UserEntity userEntity) {
        String url = getUrl() + "console/authenticate";

        String result = restTemplate.postForEntity(url, userEntity, String.class).getBody();

        return Boolean.valueOf(result);
    }

    public static String getDiscoveryType() {
        String url = getUrl() + "console/discovery-type";

        String result = restTemplate.getForEntity(url, String.class).getBody();

        return result;
    }

    public static String getConfigType() {
        String url = getUrl() + "console/config-type";

        String result = restTemplate.getForEntity(url, String.class).getBody();

        return result;
    }

    public static List<String> getGroups() {
        String url = getUrl() + "console/groups";

        String result = restTemplate.getForEntity(url, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<String>>() {
        });
    }

    public static List<String> getServices() {
        String url = getUrl() + "console/services";

        String result = restTemplate.getForEntity(url, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<String>>() {
        });
    }

    public static List<String> getGateways() {
        String url = getUrl() + "console/gateways";

        String result = restTemplate.getForEntity(url, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<String>>() {
        });
    }

    public static List<Instance> getInstanceList(String serviceId) {
        String url = getUrl() + "console/instance-list/" + serviceId;

        String result = restTemplate.getForEntity(url, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<Instance>>() {
        });
    }

    public static Map<String, List<Instance>> getInstanceMap(List<String> groups) {
        String url = getUrl() + "console/instance-map";

        String result = restTemplate.postForEntity(url, groups, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<Map<String, List<Instance>>>() {
        });
    }

    public static List<String> getVersions(Instance instance) {
        String url = getUrl(instance) + "version/view";

        String result = restTemplate.getForEntity(url, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<String>>() {
        });
    }

    public static List<String> getRules(Instance instance) {
        String url = getUrl(instance) + "config/view";

        String result = restTemplate.getForEntity(url, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<String>>() {
        });
    }

    public static List<String> getRules(RouterEntity routerEntity) {
        String url = getUrl(routerEntity) + "config/view";

        String result = restTemplate.getForEntity(url, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<String>>() {
        });
    }

    public static RouterEntity routes(Instance instance, String routeServiceIds) {
        String url = getUrl(instance) + "router/routes";

        String result = restTemplate.postForEntity(url, routeServiceIds, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<RouterEntity>() {
        });
    }

    public static InspectorEntity inspect(String url, Map<String, String> headerMap, Map<String, String> parameterMap, Map<String, String> cookieMap, InspectorEntity inspectorEntity) {
        HttpHeaders httpHeaders = new HttpHeaders();

        RestUtil.processHeader(httpHeaders, headerMap);

        url = RestUtil.processParameter(url, parameterMap);

        RestUtil.processCookie(httpHeaders, cookieMap);

        HttpEntity<InspectorEntity> requestEntity = new HttpEntity<InspectorEntity>(inspectorEntity, httpHeaders);

        InspectorEntity resultInspectorEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, InspectorEntity.class).getBody();

        return resultInspectorEntity;
    }

    public static InspectorEntity inspectByHeader(String url, Map<String, String> headerMap, InspectorEntity inspectorEntity) {
        HttpHeaders httpHeaders = new HttpHeaders();

        RestUtil.processHeader(httpHeaders, headerMap);

        HttpEntity<InspectorEntity> requestEntity = new HttpEntity<InspectorEntity>(inspectorEntity, httpHeaders);

        InspectorEntity resultInspectorEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, InspectorEntity.class).getBody();

        return resultInspectorEntity;
    }

    public static InspectorEntity inspectByParameter(String url, Map<String, String> parameterMap, InspectorEntity inspectorEntity) {
        url = RestUtil.processParameter(url, parameterMap);

        InspectorEntity resultInspectorEntity = restTemplate.postForEntity(url, inspectorEntity, InspectorEntity.class).getBody();

        return resultInspectorEntity;
    }

    public static InspectorEntity inspectByCookie(String url, Map<String, String> cookieMap, InspectorEntity inspectorEntity) {
        HttpHeaders httpHeaders = new HttpHeaders();

        RestUtil.processCookie(httpHeaders, cookieMap);

        HttpEntity<InspectorEntity> requestEntity = new HttpEntity<InspectorEntity>(inspectorEntity, httpHeaders);

        InspectorEntity resultInspectorEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, InspectorEntity.class).getBody();

        return resultInspectorEntity;
    }

    public static String remoteConfigUpdate(String group, String serviceId, String config) {
        String url = getUrl() + "console/remote-config/update/" + group + "/" + serviceId;

        // 解决中文乱码
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(config, headers);

        String result = restTemplate.postForEntity(url, entity, String.class).getBody();

        String cause = RestUtil.getCause(restTemplate);
        if (StringUtils.isNotEmpty(cause)) {
            result = cause;
        }

        return result;
    }

    public static String remoteConfigClear(String group, String serviceId) {
        String url = getUrl() + "console/remote-config/clear/" + group + "/" + serviceId;

        String result = restTemplate.postForEntity(url, null, String.class).getBody();

        String cause = RestUtil.getCause(restTemplate);
        if (StringUtils.isNotEmpty(cause)) {
            result = cause;
        }

        return result;
    }

    public static String remoteConfigView(String group, String serviceId) {
        String url = getUrl() + "console/remote-config/view/" + group + "/" + serviceId;

        String result = restTemplate.getForEntity(url, String.class).getBody();

        return result;
    }

    public static List<ResultEntity> configUpdate(String serviceId, String config, boolean async) {
        String url = getUrl() + "console/config/update-" + getInvokeType(async) + "/" + serviceId;

        // 解决中文乱码
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(config, headers);

        String result = restTemplate.postForEntity(url, entity, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<ResultEntity>>() {
        });
    }

    public static String configUpdate(Instance instance, String config, boolean async) {
        String url = getUrl(instance) + "config/update-" + getInvokeType(async);

        // 解决中文乱码
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(config, headers);

        String result = restTemplate.postForEntity(url, entity, String.class).getBody();

        String cause = RestUtil.getCause(restTemplate);
        if (StringUtils.isNotEmpty(cause)) {
            result = cause;
        }

        return result;
    }

    public static List<ResultEntity> configClear(String serviceId, boolean async) {
        String url = getUrl() + "console/config/clear-" + getInvokeType(async) + "/" + serviceId;

        String result = restTemplate.postForEntity(url, null, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<ResultEntity>>() {
        });
    }

    public static String configClear(Instance instance, boolean async) {
        String url = getUrl(instance) + "config/clear-" + getInvokeType(async);

        String result = restTemplate.postForEntity(url, null, String.class).getBody();

        String cause = RestUtil.getCause(restTemplate);
        if (StringUtils.isNotEmpty(cause)) {
            result = cause;
        }

        return result;
    }

    public static List<ResultEntity> versionUpdate(String serviceId, String version, boolean async) {
        String url = getUrl() + "console/version/update-" + getInvokeType(async) + "/" + serviceId;

        String result = restTemplate.postForEntity(url, version, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<ResultEntity>>() {
        });
    }

    public static String versionUpdate(Instance instance, String version, boolean async) {
        String url = getUrl(instance) + "version/update-" + getInvokeType(async);

        String result = restTemplate.postForEntity(url, version, String.class).getBody();

        String cause = RestUtil.getCause(restTemplate);
        if (StringUtils.isNotEmpty(cause)) {
            result = cause;
        }

        return result;
    }

    public static List<ResultEntity> versionClear(String serviceId, boolean async) {
        String url = getUrl() + "console/version/clear-" + getInvokeType(async) + "/" + serviceId;

        String result = restTemplate.postForEntity(url, null, String.class).getBody();

        return RestUtil.fromJson(restTemplate, result, new TypeReference<List<ResultEntity>>() {
        });
    }

    public static String versionClear(Instance instance, boolean async) {
        String url = getUrl(instance) + "version/clear-" + getInvokeType(async);

        String result = restTemplate.postForEntity(url, null, String.class).getBody();

        String cause = RestUtil.getCause(restTemplate);
        if (StringUtils.isNotEmpty(cause)) {
            result = cause;
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