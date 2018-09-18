import request from '../utils/request'

/**
 * 获取注册发现中心类型
 */
export function fetchDiscoveryType() {
  return request({
    url: '/console/discovery-type',
    method: 'get'
  })
}

/**
 * 获取配置中心类型
 */
export function fetchConfigType() {
  return request({
    url: '/console/config-type',
    method: 'get'
  })
}

/**
 * 获取服务注册中心的服务组名列表
 */
export function fetchGroups() {
  return request({
    url: '/console/groups',
    method: 'get'
  })
}

/**
 * 获取服务注册中心的服务实例的Map（精简数据）
 */
export function fetchInstanceMap(groups) {
  if (groups === undefined) {
    groups = [];
  }
  return request({
    url: '/console/instance-map',
    method: 'post',
    data: groups
  })
}

/**
 * 获取全路径的路由信息树
 * @param baseURL 单个服務地址
 * @param data 按调用服务名的前后次序排列，起始节点的服务名不能加上去。如果多个用“;”分隔，不允许出现空格（例如：service-a;service-b）
 */
export function fetchRoutes(baseURL, data) {
  if(process.env.NODE_ENV==='development') {
    baseURL = '';
  }
  return request({
    baseURL: baseURL,
    url: '/router/routes',
    method: 'post',
    data
  })
}

/**
 * 查看服务的动态版本
 * @param baseURL 单个服務地址
 */
export function fetchVersion(baseURL) {
  if(process.env.NODE_ENV==='development') {
    baseURL = '';
  }
  return request({
    baseURL: baseURL,
    url: '/version/view',
    method: 'get'
  })
}

/**
 * 清除服务的动态版本
 * @param baseURL 单个服務地址
 * @param pushType 异步|同步(sync|async)
 */
export function fetchVersionClear(baseURL,pushType) {
  if(process.env.NODE_ENV==='development') {
    baseURL = '';
  }
  return request({
    baseURL: baseURL,
    url: '/version/clear-'+pushType,
    method: 'post'
  })
}

/**
 * 批量清除服务的动态版本
 * @param serviceId 服务名
 * @param pushType 异步|同步(sync|async)
 */
export function fetchVersionClearByService(serviceId,pushType) {
  return request({
    url: '/console/version/clear-' + pushType + '/' + serviceId,
    method: 'post'
  })
}

/**
 * 更新服务的动态版本
 * @param baseURL 单个服務地址
 * @param pushType 异步|同步(sync|async)
 * @param version 版本号，格式为[dynamicVersion]或者[dynamicVersion];[localVersion]
 */
export function fetchVersionUpdate(baseURL,pushType,version) {
  if(process.env.NODE_ENV==='development') {
    baseURL = '';
  }
  return request({
    baseURL: baseURL,
    url: '/version/update-'+pushType,
    method: 'post',
    data: version
  })
}

/**
 * 批量更新服务的动态版本
 * @param serviceId 服务名
 * @param pushType 异步|同步(sync|async)
 * @param version 版本号，格式为[dynamicVersion]或者[dynamicVersion];[localVersion]
 */
export function fetchVersionUpdateByService(serviceId,pushType,version) {
  return request({
    url: '/console/version/update-' + pushType + '/' + serviceId,
    method: 'post',
    data: version
  })
}

/**
 * 查看规则配置信息
 * @param baseURL 单个服務地址
 */
export function fetchConfig(baseURL) {
  if(process.env.NODE_ENV==='development') {
    baseURL = '';
  }
  return request({
    baseURL: baseURL,
    url: '/config/view',
    method: 'get'
  })
}

/**
 * 查看远程配置中心的规则配置信息
 * @param group 组名
 * @param serviceId 服务名
 */
export function fetchConfigByGroup(group,serviceId) {
  return request({
    url: '/console/remote-config/view/' + group + '/' + serviceId,
    method: 'get'
  })
}

/**
 * 清除更新的规则配置信息
 * @param baseURL 单个服務地址
 * @param pushType  异步|同步(sync|async)
 */
export function fetchConfigClear(baseURL,pushType) {
  if(process.env.NODE_ENV==='development') {
    baseURL = '';
  }
  return request({
    baseURL: baseURL,
    url: '/config/clear-'+pushType,
    method: 'post'
  })
}

/**
 * 清除规则配置信息到远程配置中心
 * @param group 组名
 * @param serviceId 服务名
 */
export function fetchConfigClearByGroup(group,serviceId) {
  return request({
    url: '/console/remote-config/clear/' + group + '/' + serviceId,
    method: 'post'
  })
}

/**
 * 批量清除更新的规则配置信息
 * @param serviceId 服务名
 * @param pushType 异步|同步(sync|async)
 */
export function fetchConfigClearByService(serviceId,pushType) {
  return request({
    url: '/console/config/clear-' + pushType + '/' + serviceId,
    method: 'post'
  })
}

/**
 * 更新规则配置信息
 * @param baseURL 单个服務地址
 * @param pushType 异步|同步(sync|async)
 * @param config 规则配置内容，XML格式
 */
export function fetchConfigUpdate(baseURL,pushType,config) {
  if(process.env.NODE_ENV==='development') {
    baseURL = '';
  }
  return request({
    baseURL: baseURL,
    url: '/config/update-'+pushType,
    method: 'post',
    data: config
  })
}

/**
 * 推送更新规则配置信息到远程配置中心
 * @param group 组名
 * @param serviceId 服务名
 * @param config 规则配置内容，XML格式
 */
export function fetchConfigUpdateByGroup(group,serviceId,config) {
  return request({
    url: '/console/remote-config/update/' + group + '/' + serviceId,
    method: 'post',
    data: config
  })
}

/**
 * 批量推送更新规则配置信息
 * @param serviceId 服务名
 * @param pushType 异步|同步(sync|async)
 * @param config 规则配置内容，XML格式
 */
export function fetchConfigUpdateByService(serviceId,pushType,config) {
  return request({
    url: '/console/config/update-' + pushType + '/' + serviceId,
    method: 'post',
    data: config
  })
}
