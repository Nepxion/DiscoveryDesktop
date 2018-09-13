import { fetchInstanceMap,fetchConfigType } from '../../api';
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
    }
  }
};

export default console;
