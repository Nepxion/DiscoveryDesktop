import { fetchGroups,fetchInstanceMap,fetchConfigType,fetchDiscoveryType,fetchConfigUpdateByGroup,fetchConfigClearByGroup } from '../../api';
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
        fetchGroups().then((data) => {
          commit('setGroups',data);
          resolve(data);
        }).catch(error => {
          reject(error);
        });
      });
    },
    GetInstanceMap({ commit }, groups) {
      return new Promise((resolve, reject) => {
        fetchInstanceMap(groups).then((data) => {
          commit('setInstanceMap',data);
          resolve(data);
        }).catch(error => {
          reject(error);
        });
      });
    },
    GetConfigType({ commit }) {
      return new Promise((resolve, reject) => {
        fetchConfigType().then((data) => {
          commit('setConfigType',data);
          resolve(data);
        }).catch(error => {
          reject(error);
        });
      });
    },
    GetDiscoveryType({ commit }) {
      return new Promise((resolve, reject) => {
        fetchDiscoveryType().then((data) => {
          commit('setDiscoveryType',data);
          resolve(data);
        }).catch(error => {
          reject(error);
        });
      });
    },
    UpdateConfigByGroup({ commit }, data) {
      return new Promise((resolve, reject) => {
        fetchConfigUpdateByGroup(data.group, data.serviceId, data.config).then((data) => {
          resolve(data);
        }).catch(error => {
          reject(error);
        });
      });
    },
    ClearConfigByGroup({ commit },data) {
      return new Promise((resolve, reject) => {
        fetchConfigClearByGroup(data.group,data.serviceId).then((data) => {
          resolve(data);
        }).catch(error => {
          reject(error);
        });
      });
    },
  }
};

export default console;
