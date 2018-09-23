import { fetchGroups,
  fetchInstanceMap,
  fetchConfigType,
  fetchDiscoveryType,
  fetchConfigByGroup,
  fetchConfigUpdateByGroup,
  fetchConfigClearByGroup,
  fetchConfigUpdateByService,
  fetchConfigClearByService,
  fetchVersionUpdateByService,
  fetchVersionClearByService } from '../../api';
import { getGroups } from '../../utils';

const console = {
  state: {
    instanceMap: {},
    groups:[],
    configType: undefined,
    discoveryType: undefined,
  },

  mutations: {
    setGroups(state, data) {
      state.groups = data;
    },
    setInstanceMap(state, data) {
      state.instanceMap = data;
    },
    setConfigType(state, configType) {
      state.configType = configType;
    },
    setDiscoveryType(state, discoveryType) {
      state.discoveryType = discoveryType;
    },
  },

  actions: {
    GetGroups({ commit }) {
      return new Promise((resolve, reject) => {
        fetchGroups().then((ret) => {
          commit('setGroups',ret);
          resolve(ret);
        }).catch(error => {
          reject(error);
        });
      });
    },
    GetInstanceMap({ commit }, groups) {
      return new Promise((resolve, reject) => {
        fetchInstanceMap(groups).then((ret) => {
          commit('setInstanceMap',ret);
          resolve(ret);
        }).catch(error => {
          reject(error);
        });
      });
    },
    GetConfigType({ commit }) {
      return new Promise((resolve, reject) => {
        fetchConfigType().then((ret) => {
          commit('setConfigType',ret);
          resolve(ret);
        }).catch(error => {
          reject(error);
        });
      });
    },
    GetDiscoveryType({ commit }) {
      return new Promise((resolve, reject) => {
        fetchDiscoveryType().then((ret) => {
          commit('setDiscoveryType',ret);
          resolve(ret);
        }).catch(error => {
          reject(error);
        });
      });
    },
    GetConfigByGroup({ commit }, data) {
      return new Promise((resolve, reject) => {
        fetchConfigByGroup(data.group, data.serviceId).then((ret) => {
          resolve(ret);
        }).catch(error => {
          reject(error);
        });
      });
    },
    UpdateConfigByGroup({ commit }, data) {
      return new Promise((resolve, reject) => {
        fetchConfigUpdateByGroup(data.group, data.serviceId, data.config).then((ret) => {
          resolve(ret);
        }).catch(error => {
          reject(error);
        });
      });
    },
    ClearConfigByGroup({ commit },data) {
      return new Promise((resolve, reject) => {
        fetchConfigClearByGroup(data.group,data.serviceId).then((ret) => {
          resolve(ret);
        }).catch(error => {
          reject(error);
        });
      });
    },
    UpdateConfigByService({ commit }, data) {
      return new Promise((resolve, reject) => {
        fetchConfigUpdateByService(data.pushType, data.serviceId, data.config).then((ret) => {
          resolve(ret);
        }).catch(error => {
          reject(error);
        });
      });
    },
    ClearConfigByService({ commit },data) {
      return new Promise((resolve, reject) => {
        fetchConfigClearByService(data.pushType,data.serviceId).then((ret) => {
          resolve(ret);
        }).catch(error => {
          reject(error);
        });
      });
    },
    UpdateVersionByService({ commit }, data) {
      return new Promise((resolve, reject) => {
        fetchVersionUpdateByService(data.pushType, data.serviceId, data.version).then((ret) => {
          resolve(ret);
        }).catch(error => {
          reject(error);
        });
      });
    },
    ClearVersionByService({ commit },data) {
      return new Promise((resolve, reject) => {
        fetchVersionClearByService(data.pushType,data.serviceId).then((ret) => {
          resolve(ret);
        }).catch(error => {
          reject(error);
        });
      });
    },
  }
};

export default console;
