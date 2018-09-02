import { fetchInstanceMap } from '../../api/console';

const console = {
  state: {
    instanceMap: []
  },

  mutations: {
    setInstanceMap(state, data) {
      state.instanceMap = data;
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
    }
  }
};

export default console;
