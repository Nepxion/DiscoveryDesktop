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
    GetGrayRoutes({ commit }, data) {
      return new Promise((resolve, reject) => {
        fetchRoutes(data.baseURL, data.serviceIds).then((data) => {
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
    ClearVersion({ commit, state }, data) {
      return new Promise((resolve, reject) => {
        fetchVersionClear(data.baseURL,data.pushType).then((data) => {
          //commit('setVersion',data);
          resolve(data);
        }).catch(error => {
          reject(error);
        });
      });
    },
    ClearConfig({ commit, state }, data) {
      return new Promise((resolve, reject) => {
        fetchConfigClear(data.baseURL,data.pushType).then((data) => {
          //commit('setVersion',data);
          resolve(data);
        }).catch(error => {
          reject(error);
        });
      });

    },
    UpdateVersion({ commit, state }, data) {
      return new Promise((resolve, reject) => {
        fetchVersionUpdate(data.baseURL,data.pushType,data.version).then((data) => {
          //commit('setVersion',data);
          resolve(data);
        }).catch(error => {
          reject(error);
        });
      });
    },
    UpdateConfig({ commit, state }, data) {
      return new Promise((resolve, reject) => {
        fetchConfigUpdate(data.baseURL,data.pushType,data.config).then((data) => {
          //commit('setConfig',data);
          resolve(data);
        }).catch(error => {
          reject(error);
        });
      });
    },
  }
};

export default service;
