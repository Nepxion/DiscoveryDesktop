const getters = {
  async: state => state.app.async,
  pushType: state => state.app.pushType,
  baseURL: state => state.app.baseURL,
  ruleToConfig: state => state.app.ruleToConfig,
  userId: state => state.user.userId,
  status: state => state.user.status,
  configType: state => state.console.configType,
  discoveryType: state => state.console.discoveryType,
  instanceMap: state => state.console.instanceMap,
  groups: state => state.console.groups,
  grayRoutes: state => state.service.grayRoutes,
  version: state => state.service.version,
  config: state => state.service.config
}
export default getters
