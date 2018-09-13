import { fetchInstanceMap,fetchConfigType,fetchConfigUpdateByGroup,fetchConfigClearByGroup } from '../../api';
import { getGroups } from '../../utils';

const console = {
  state: {
    instanceMap: {},
    groups:[],
    configType: undefined,
  },

  mutations: {
    setInstanceMap(state, data) {
      state.instanceMap = data;
      state.groups = getGroups(data);
    },
    setConfigType(state, configType) {
      state.configType = configType;
    }
  },

  actions: {
    GetInstanceMap({ commit }) {
      return new Promise((resolve, reject) => {
        fetchInstanceMap().then((data) => {
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
