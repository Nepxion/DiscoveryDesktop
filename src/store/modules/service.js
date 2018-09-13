import { fetchRoutes,fetchVersion,fetchConfig,fetchVersionClear,fetchConfigClear,fetchVersionUpdate,fetchConfigUpdate } from '../../api';

const service = {
  state: {
    grayRoutes: {},
    version: [],
    config: [],
  },

  mutations: {
    setGrayRoutes(state, data) {
      state.grayRoutes = data;
    },
    setVersion(state, data) {
      state.version = data;
    },
    setConfig(state, data) {
      state.config = data;
    },
  },

  actions: {
    GetGrayRoutes({ commit }, baseURL, serviceIds) {
      return new Promise((resolve, reject) => {
        fetchRoutes(baseURL, serviceIds).then((data) => {
          commit('setGrayRoutes',data);
          resolve(data);
        }).catch(error => {
          reject(error);
        });
      });
    },
    GetVersion({ commit }, baseURL) {
      return new Promise((resolve, reject) => {
        fetchVersion(baseURL).then((data) => {
          commit('setVersion',data);
          resolve(data);
        }).catch(error => {
          reject(error);
        });
      });
    },
    GetConfig({ commit }, baseURL) {
      return new Promise((resolve, reject) => {
        fetchConfig(baseURL).then((data) => {
          commit('setConfig',data);
          resolve(data);
        }).catch(error => {
          reject(error);
        });
      });
    },
    ClearVersion({ commit },pushType, baseURL) {
      return new Promise((resolve, reject) => {
        fetchVersionClear(baseURL,pushType).then((data) => {
          //commit('setVersion',data);
          resolve(data);
        }).catch(error => {
          reject(error);
        });
      });
    },
    ClearConfig({ commit },pushType, baseURL) {
      return new Promise((resolve, reject) => {
        fetchConfigClear(baseURL,pushType).then((data) => {
          //commit('setVersion',data);
          resolve(data);
        }).catch(error => {
          reject(error);
        });
      });

    },
    UpdateVersion({ commit },pushType, baseURL,version) {
      return new Promise((resolve, reject) => {
        fetchVersionUpdate(baseURL,pushType,version).then((data) => {
          //commit('setVersion',data);
          resolve(data);
        }).catch(error => {
          reject(error);
        });
      });

    },
    UpdateConfig({ commit },pushType, baseURL,config) {
      return new Promise((resolve, reject) => {
        fetchConfigUpdate(baseURL,pushType,config).then((data) => {
          //commit('setVersion',data);
          resolve(data);
        }).catch(error => {
          reject(error);
        });
      });

    },
  }
};

export default service;
