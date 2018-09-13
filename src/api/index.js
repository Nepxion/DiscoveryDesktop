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
