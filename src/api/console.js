import request from '../utils/request'

export function fetchInstanceMap() {
  return request({
    url: '/console/instance-map',
    method: 'get'
  })
}
