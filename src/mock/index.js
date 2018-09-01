import Mock from 'mockjs';

Mock.setup({
  timeout: 800, // 设置延迟响应，模拟向后端请求数据
});

Mock.mock('/console/instance-map', 'get', {
  "discovery-springcloud-example-eureka": [
    {
      "serviceId": "discovery-springcloud-example-eureka",
      "version": null,
      "host": "10.83.3.154",
      "port": 9528,
      "metaData": {
        "management.port": "9528",
        "jmx.port": "60812",
        "group": "example-eureka-group"
      }
    }
  ],
  "discovery-springcloud-example-console": [
    {
      "serviceId": "discovery-springcloud-example-console",
      "version": "1.0",
      "host": "10.83.3.154",
      "port": 2222,
      "metaData": {
        "management.port": "2222",
        "jmx.port": "60778",
        "version": "1.0",
        "group": "example-console-group"
      }
    }
  ],
  "discovery-springcloud-example-a": [
    {
      "serviceId": "discovery-springcloud-example-a",
      "version": "1.0.0",
      "host": "10.83.3.154",
      "port": 1100,
      "metaData": {
        "spring.application.config.rest.control.enabled": "true",
        "management.port": "1100",
        "jmx.port": "60879",
        "spring.application.discovery.plugin": "Eureka Plugin",
        "version": "1.0",
        "spring.application.register.control.enabled": "true",
        "spring.application.group.key": "group",
        "spring.application.discovery.control.enabled": "true",
        "group": "example-service-group"
      }
    },
    {
      "serviceId": "discovery-springcloud-example-a",
      "version": "1.1",
      "host": "10.83.3.154",
      "port": 1101,
      "metaData": {
        "spring.application.config.rest.control.enabled": "true",
        "management.port": "1101",
        "jmx.port": "60891",
        "spring.application.discovery.plugin": "Eureka Plugin",
        "version": "1.1",
        "spring.application.register.control.enabled": "true",
        "spring.application.group.key": "group",
        "spring.application.discovery.control.enabled": "true",
        "group": "example-service-group"
      }
    }
  ],
  "discovery-springcloud-example-gateway": [
    {
      "serviceId": "discovery-springcloud-example-gateway",
      "version": "1.0",
      "host": "10.83.3.154",
      "port": 1500,
      "metaData": {
        "spring.application.config.rest.control.enabled": "true",
        "management.port": "1500",
        "jmx.port": "60905",
        "spring.application.discovery.plugin": "Eureka Plugin",
        "version": "1.0",
        "spring.application.register.control.enabled": "true",
        "spring.application.group.key": "group",
        "spring.application.discovery.control.enabled": "true",
        "group": "example-service-group"
      }
    }
  ],
  "discovery-springcloud-example-b": [
    {
      "serviceId": "discovery-springcloud-example-b",
      "version": "1.0",
      "host": "10.83.3.154",
      "port": 1200,
      "metaData": {
        "spring.application.config.rest.control.enabled": "true",
        "management.port": "1200",
        "jmx.port": "62836",
        "spring.application.discovery.plugin": "Eureka Plugin",
        "version": "1.0",
        "spring.application.register.control.enabled": "true",
        "spring.application.group.key": "group",
        "spring.application.discovery.control.enabled": "true",
        "group": "example-service-group"
      }
    },
    {
      "serviceId": "discovery-springcloud-example-b",
      "version": "1.1",
      "host": "10.83.3.154",
      "port": 1201,
      "metaData": {
        "spring.application.config.rest.control.enabled": "true",
        "management.port": "1201",
        "jmx.port": "62865",
        "spring.application.discovery.plugin": "Eureka Plugin",
        "version": "1.1",
        "spring.application.register.control.enabled": "true",
        "spring.application.group.key": "group",
        "spring.application.discovery.control.enabled": "true",
        "group": "example-service-group"
      }
    }
  ],
  "discovery-springcloud-example-c": [
    {
      "serviceId": "discovery-springcloud-example-c",
      "version": "1.0",
      "host": "10.83.3.154",
      "port": 1300,
      "metaData": {
        "spring.application.config.rest.control.enabled": "true",
        "management.port": "1300",
        "jmx.port": "62901",
        "spring.application.discovery.plugin": "Eureka Plugin",
        "version": "1.0",
        "spring.application.register.control.enabled": "true",
        "spring.application.group.key": "group",
        "spring.application.discovery.control.enabled": "true",
        "group": "example-service-group"
      }
    }
  ],
  "discovery-springcloud-example-zuul": [
    {
      "serviceId": "discovery-springcloud-example-zuul",
      "version": "1.0",
      "host": "10.83.3.154",
      "port": 1400,
      "metaData": {
        "spring.application.config.rest.control.enabled": "true",
        "management.port": "1400",
        "jmx.port": "62947",
        "spring.application.discovery.plugin": "Eureka Plugin",
        "version": "1.0",
        "spring.application.register.control.enabled": "true",
        "spring.application.group.key": "group",
        "spring.application.discovery.control.enabled": "true",
        "group": "example-service-group"
      }
    }
  ]
});
