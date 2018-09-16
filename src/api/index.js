import request from '../utils/request'

export function fetchConfigType() {
  return request({
    url: '/console/config-type',
    method: 'get'
  })
}

export function fetchInstanceMap() {
  return request({
    url: '/console/instance-map',
    method: 'get'
  })
}

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

export function fetchVersionClearByService(serviceId,pushType) {
  return request({
    url: '/console/version/clear-' + pushType + '/' + serviceId,
    method: 'post'
  })
}

export function fetchVersionUpdate(baseURL,pushType,version) {
  if(process.env.NODE_ENV==='development') {
    baseURL = '';
  }
  return request({
    baseURL: baseURL,
    url: '/version/update-'+pushType,
    method: 'post',
    version
  })
}

export function fetchVersionUpdateByService(serviceId,pushType,version) {
  return request({
    url: '/console/version/update-' + pushType + '/' + serviceId,
    method: 'post',
    version
  })
}

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

export function fetchConfigByGroup(group,serviceId) {
  return request({
    url: '/console/remote-config/view/' + group + '/' + serviceId,
    method: 'get'
  })
}

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

export function fetchConfigClearByGroup(group,serviceId) {
  return request({
    url: '/console/remote-config/clear/' + group + '/' + serviceId,
    method: 'post'
  })
}

export function fetchConfigClearByService(serviceId,pushType) {
  return request({
    url: '/console/version/clear-' + pushType + '/' + serviceId,
    method: 'post'
  })
}

export function fetchConfigUpdate(baseURL,pushType,config) {
  if(process.env.NODE_ENV==='development') {
    baseURL = '';
  }
  return request({
    baseURL: baseURL,
    url: '/config/update-'+pushType,
    method: 'post',
    config
  })
}

export function fetchConfigUpdateByGroup(group,serviceId,config) {
  return request({
    url: '/console/remote-config/update/' + group + '/' + serviceId,
    method: 'post',
    config
  })
}

export function fetchConfigUpdateByService(serviceId,pushType,config) {
  return request({
    url: '/console/config/update-' + pushType + '/' + serviceId,
    method: 'post',
    config
  })
}
