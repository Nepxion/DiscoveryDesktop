import Mock from 'mockjs';
import console from "../store/modules/console";

Mock.setup({
  timeout: 800, // 设置延迟响应，模拟向后端请求数据
});

Mock.mock('/console/config-type', 'get', 'Nacos');

Mock.mock('/console/instance-map11', 'get', {
  "springcloud-example-eureka": [
    {
      "serviceId": "springcloud-example-eureka",
      "version": null,
      "region": null,
      "host": "10.83.3.154",
      "port": 9528,
      "metadata": {
        "management.port": "9528",
        "jmx.port": "64672",
        "group": "example-eureka-group"
      }
    }
  ],
  "springcloud-example-console": [
    {
      "serviceId": "springcloud-example-console",
      "version": "1.0",
      "region": null,
      "host": "10.83.3.154",
      "port": 2222,
      "metadata": {
        "management.port": "3333",
        "jmx.port": "63080",
        "version": "1.0",
        "group": "example-console-group"
      }
    }
  ],
  "springcloud-example-zuul": [
    {
      "serviceId": "springcloud-example-zuul",
      "version": "1.0",
      "region": null,
      "host": "10.83.3.154",
      "port": 1400,
      "metadata": {
        "spring.application.config.rest.control.enabled": "true",
        "management.port": "5400",
        "jmx.port": "64724",
        "spring.application.context-path": "/",
        "spring.application.discovery.plugin": "Eureka Plugin",
        "version": "1.0",
        "spring.application.register.control.enabled": "true",
        "spring.application.group.key": "group",
        "spring.application.discovery.control.enabled": "true",
        "group": "example-service-group"
      }
    }
  ],
  "springcloud-example-a": [
    {
      "serviceId": "springcloud-example-a",
      "version": "1.1",
      "region": "qa",
      "host": "10.83.3.154",
      "port": 1101,
      "metadata": {
        "spring.application.config.rest.control.enabled": "true",
        "management.port": "5101",
        "jmx.port": "65243",
        "spring.application.context-path": "/",
        "spring.application.discovery.plugin": "Eureka Plugin",
        "version": "1.1",
        "spring.application.register.control.enabled": "true",
        "spring.application.group.key": "group",
        "region": "qa",
        "spring.application.discovery.control.enabled": "true",
        "group": "example-service-group"
      }
    },
    {
      "serviceId": "springcloud-example-a",
      "version": "1.0",
      "region": "dev",
      "host": "10.83.3.154",
      "port": 1100,
      "metadata": {
        "spring.application.config.rest.control.enabled": "true",
        "management.port": "5100",
        "jmx.port": "65095",
        "spring.application.context-path": "/",
        "spring.application.discovery.plugin": "Eureka Plugin",
        "version": "1.0",
        "spring.application.register.control.enabled": "true",
        "spring.application.group.key": "group",
        "region": "dev",
        "spring.application.discovery.control.enabled": "true",
        "group": "example-service-group"
      }
    }
  ],
  "springcloud-example-b": [
    {
      "serviceId": "springcloud-example-b",
      "version": "1.0",
      "region": "dev",
      "host": "10.83.3.154",
      "port": 1200,
      "metadata": {
        "spring.application.config.rest.control.enabled": "true",
        "management.port": "5200",
        "jmx.port": "65357",
        "spring.application.context-path": "/",
        "spring.application.discovery.plugin": "Eureka Plugin",
        "version": "1.0",
        "spring.application.register.control.enabled": "true",
        "spring.application.group.key": "group",
        "region": "dev",
        "spring.application.discovery.control.enabled": "true",
        "group": "example-service-group"
      }
    },
    {
      "serviceId": "springcloud-example-b",
      "version": "1.1",
      "region": "qa",
      "host": "10.83.3.154",
      "port": 1201,
      "metadata": {
        "spring.application.config.rest.control.enabled": "true",
        "management.port": "5201",
        "jmx.port": "65486",
        "spring.application.context-path": "/",
        "spring.application.discovery.plugin": "Eureka Plugin",
        "version": "1.1",
        "spring.application.register.control.enabled": "true",
        "spring.application.group.key": "group",
        "region": "qa",
        "spring.application.discovery.control.enabled": "true",
        "group": "example-service-group"
      }
    }
  ],
  "springcloud-example-c": [
    {
      "serviceId": "springcloud-example-c",
      "version": "1.0",
      "region": "dev",
      "host": "10.83.3.154",
      "port": 1300,
      "metadata": {
        "spring.application.config.rest.control.enabled": "true",
        "management.port": "5300",
        "jmx.port": "49274",
        "spring.application.context-path": "/",
        "spring.application.discovery.plugin": "Eureka Plugin",
        "version": "1.0",
        "spring.application.register.control.enabled": "true",
        "spring.application.group.key": "group",
        "region": "dev",
        "spring.application.discovery.control.enabled": "true",
        "group": "example-service-group"
      }
    },
    {
      "serviceId": "springcloud-example-c",
      "version": "1.1",
      "region": "qa",
      "host": "10.83.3.154",
      "port": 1301,
      "metadata": {
        "spring.application.config.rest.control.enabled": "true",
        "management.port": "5301",
        "jmx.port": "49450",
        "spring.application.context-path": "/",
        "spring.application.discovery.plugin": "Eureka Plugin",
        "version": "1.1",
        "spring.application.register.control.enabled": "true",
        "spring.application.group.key": "group",
        "region": "qa",
        "spring.application.discovery.control.enabled": "true",
        "group": "example-service-group"
      }
    },
    {
      "serviceId": "springcloud-example-c",
      "version": "1.2",
      "region": "qa",
      "host": "10.83.3.154",
      "port": 1302,
      "metadata": {
        "spring.application.config.rest.control.enabled": "true",
        "management.port": "5302",
        "jmx.port": "50228",
        "spring.application.context-path": "/",
        "spring.application.discovery.plugin": "Eureka Plugin",
        "version": "1.2",
        "spring.application.register.control.enabled": "true",
        "spring.application.group.key": "group",
        "region": "qa",
        "spring.application.discovery.control.enabled": "true",
        "group": "example-service-group"
      }
    }
  ]
});

Mock.mock('/console/instance-map', 'get', function() {
  let data = {};
  for (let i = 0; i < 100; i++) {
    let services = [];
    const m = Math.round(Math.random() * 10);
    const key = 'springcloud-example-' + i;
    for (let j = 0; j < m; j++) {
      services.push({
        "serviceId": key,
        "version": "1."+j,
        "region": "qa",
        "host": "10.83." + i + ".10" + j,
        "port": 110 + j,
        "metadata": {
          "spring.application.config.rest.control.enabled": "true",
          "management.port": "5101",
          "jmx.port": "65243",
          "spring.application.context-path": "/",
          "spring.application.discovery.plugin": "Eureka Plugin",
          "version": "1.1",
          "spring.application.register.control.enabled": "true",
          "spring.application.group.key": "group",
          "region": "qa",
          "spring.application.discovery.control.enabled": "true",
          "group": "example-service-group"
        }
      })
    }
    data[key] = services;
  }
  return data
});

//springcloud-example-zuul;springcloud-example-a;springcloud-example-b;springcloud-example-c
Mock.mock('/router/routes', 'post', {
  "serviceId": "springcloud-example-zuul",
  "version": "1.0",
  "region": null,
  "host": "10.83.3.154",
  "port": 1400,
  "weight": -1,
  "customMap": null,
  "contextPath": "/",
  "nexts": [
    {
      "serviceId": "springcloud-example-a",
      "version": "1.0",
      "region": "dev",
      "host": "10.83.3.154",
      "port": 1100,
      "weight": -1,
      "customMap": null,
      "contextPath": "/",
      "nexts": [
        {
          "serviceId": "springcloud-example-b",
          "version": "1.0",
          "region": "dev",
          "host": "10.83.3.154",
          "port": 1200,
          "weight": -1,
          "customMap": null,
          "contextPath": "/",
          "nexts": [
            {
              "serviceId": "springcloud-example-c",
              "version": "1.1",
              "region": "qa",
              "host": "10.83.3.154",
              "port": 1301,
              "weight": 10,
              "customMap": {
                "database": "prod"
              },
              "contextPath": "/",
              "nexts": []
            },
            {
              "serviceId": "springcloud-example-c",
              "version": "1.0",
              "region": "dev",
              "host": "10.83.3.154",
              "port": 1300,
              "weight": 90,
              "customMap": {
                "database": "prod"
              },
              "contextPath": "/",
              "nexts": []
            }
          ]
        }
      ]
    }
  ]
});

Mock.mock('/version/view', 'get', ["1.0",""]);

Mock.mock(/\/version\/clear-[\s\S]*?/, 'post', 'ok');

Mock.mock(/\/version\/update-[\s\S]*?/, 'post', 'ok');//-sync -async

Mock.mock('/config/view', 'get', ["<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<rule>\n    <!-- 如果不想开启相关功能，只需要把相关节点删除即可，例如不想要黑名单功能，把blacklist节点删除 -->\n    <register>\n        <!-- 服务注册的黑/白名单注册过滤，只在服务启动的时候生效。白名单表示只允许指定IP地址前缀注册，黑名单表示不允许指定IP地址前缀注册。每个服务只能同时开启要么白名单，要么黑名单 -->\n        <!-- filter-type，可选值blacklist/whitelist，表示白名单或者黑名单 -->\n        <!-- service-name，表示服务名 -->\n        <!-- filter-value，表示黑/白名单的IP地址列表。IP地址一般用前缀来表示，如果多个用“;”分隔，不允许出现空格 -->\n        <!-- 表示下面所有服务，不允许10.10和11.11为前缀的IP地址注册（全局过滤） -->\n        <blacklist filter-value=\"10.10;11.11\">\n            <!-- 表示下面服务，不允许172.16和10.10和11.11为前缀的IP地址注册 -->\n            <service service-name=\"springcloud-example-a\" filter-value=\"172.16\"/>\n        </blacklist>\n\n        <!-- <whitelist filter-value=\"\">\n            <service service-name=\"\" filter-value=\"\"/>\n        </whitelist>  -->\n\n        <!-- 服务注册的数目限制注册过滤，只在服务启动的时候生效。当某个服务的实例注册达到指定数目时候，更多的实例将无法注册 -->\n        <!-- service-name，表示服务名 -->\n        <!-- filter-value，表示最大实例注册数 -->\n        <!-- 表示下面所有服务，最大实例注册数为10000（全局配置） -->\n        <count filter-value=\"10000\">\n            <!-- 表示下面服务，最大实例注册数为5000，全局配置值10000将不起作用，以局部配置值为准 -->\n            <service service-name=\"springcloud-example-a\" filter-value=\"5000\"/>\n        </count>\n    </register>\n\n    <discovery>\n        <!-- 服务发现的黑/白名单发现过滤，使用方式跟“服务注册的黑/白名单过滤”一致 -->\n        <!-- 表示下面所有服务，不允许10.10和11.11为前缀的IP地址被发现（全局过滤） -->\n        <blacklist filter-value=\"10.10;11.11\">\n            <!-- 表示下面服务，不允许172.16和10.10和11.11为前缀的IP地址被发现 -->\n            <service service-name=\"springcloud-example-b\" filter-value=\"172.16\"/>\n        </blacklist>\n\n        <!-- 服务发现的多版本灰度访问控制 -->\n        <!-- service-name，表示服务名 -->\n        <!-- version-value，表示可供访问的版本，如果多个用“;”分隔，不允许出现空格 -->\n        <!-- 版本策略介绍 -->\n        <!-- 1. 标准配置，举例如下 -->\n        <!--    <service consumer-service-name=\"a\" provider-service-name=\"b\" consumer-version-value=\"1.0\" provider-version-value=\"1.0;1.1\"/> 表示消费端1.0版本，允许访问提供端1.0和1.1版本 -->\n        <!-- 2. 版本值不配置，举例如下 -->\n        <!--    <service consumer-service-name=\"a\" provider-service-name=\"b\" provider-version-value=\"1.0;1.1\"/> 表示消费端任何版本，允许访问提供端1.0和1.1版本 -->\n        <!--    <service consumer-service-name=\"a\" provider-service-name=\"b\" consumer-version-value=\"1.0\"/> 表示消费端1.0版本，允许访问提供端任何版本 -->\n        <!--    <service consumer-service-name=\"a\" provider-service-name=\"b\"/> 表示消费端任何版本，允许访问提供端任何版本 -->\n        <!-- 3. 版本值空字符串，举例如下 -->\n        <!--    <service consumer-service-name=\"a\" provider-service-name=\"b\" consumer-version-value=\"\" provider-version-value=\"1.0;1.1\"/> 表示消费端任何版本，允许访问提供端1.0和1.1版本 -->\n        <!--    <service consumer-service-name=\"a\" provider-service-name=\"b\" consumer-version-value=\"1.0\" provider-version-value=\"\"/> 表示消费端1.0版本，允许访问提供端任何版本 -->\n        <!--    <service consumer-service-name=\"a\" provider-service-name=\"b\" consumer-version-value=\"\" provider-version-value=\"\"/> 表示消费端任何版本，允许访问提供端任何版本 -->\n        <!-- 4. 版本对应关系未定义，默认消费端任何版本，允许访问提供端任何版本 -->\n        <!-- 特殊情况处理，在使用上需要极力避免该情况发生 -->\n        <!-- 1. 消费端的application.properties未定义版本号，则该消费端可以访问提供端任何版本 -->\n        <!-- 2. 提供端的application.properties未定义版本号，当消费端在xml里不做任何版本配置，才可以访问该提供端 -->\n        <version>\n            <!-- 表示网关z的1.0，允许访问提供端服务a的1.0版本 -->\n            <service consumer-service-name=\"springcloud-example-zuul\" provider-service-name=\"springcloud-example-a\" consumer-version-value=\"1.0\" provider-version-value=\"1.0\"/>\n            <!-- 表示网关z的1.1，允许访问提供端服务a的1.1版本 -->\n            <service consumer-service-name=\"springcloud-example-zuul\" provider-service-name=\"springcloud-example-a\" consumer-version-value=\"1.1\" provider-version-value=\"1.1\"/>\n            <!-- 表示消费端服务a的1.0，允许访问提供端服务b的1.0版本 -->\n            <service consumer-service-name=\"springcloud-example-a\" provider-service-name=\"springcloud-example-b\" consumer-version-value=\"1.0\" provider-version-value=\"1.0\"/>\n            <!-- 表示消费端服务a的1.1，允许访问提供端服务b的1.1版本 -->\n            <service consumer-service-name=\"springcloud-example-a\" provider-service-name=\"springcloud-example-b\" consumer-version-value=\"1.1\" provider-version-value=\"1.1\"/>\n            <!-- 表示消费端服务b的1.0，允许访问提供端服务c的1.0和1.1版本 -->\n            <service consumer-service-name=\"springcloud-example-b\" provider-service-name=\"springcloud-example-c\" consumer-version-value=\"1.0\" provider-version-value=\"1.0;1.1\"/>\n            <!-- 表示消费端服务b的1.1，允许访问提供端服务c的1.2版本 -->\n            <service consumer-service-name=\"springcloud-example-b\" provider-service-name=\"springcloud-example-c\" consumer-version-value=\"1.1\" provider-version-value=\"1.2\"/>\n        </version>\n    </discovery>\n</rule>",""]);

Mock.mock(/\/config\/clear-[\s\S]*?/, 'post', 'ok');

Mock.mock(/\/config\/update-[\s\S]*?/, 'post', 'ok');

Mock.mock(/\/console\/\/remote-config\/\/clear\/[\s\S]*?/, 'post', 'ok');

Mock.mock(/\/console\/\/remote-config\/\/update\/[\s\S]*?/, 'post', 'ok');
