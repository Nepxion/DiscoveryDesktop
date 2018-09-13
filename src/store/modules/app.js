
const app = {
  state: {
    async: false,
    pushType: 'sync',
  },

  mutations: {
    SET_ASYNC(state, async) {
      state.async = async;
      state.pushType = async ? "async" : "sync";
    }
  },

  actions: {
    setAsync({ commit }, async) {
      commit('SET_ASYNC', async)
    }
  }
};

export default app;
