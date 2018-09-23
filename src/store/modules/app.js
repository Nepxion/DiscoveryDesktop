
const app = {
  state: {
    async: false,
    pushType: 'sync',
    baseURL: localStorage.getItem("baseURL") || process.env.SERVICE_API,
    ruleToConfig: false,//规则推送到远程配置中心
  },

  mutations: {
    SET_ASYNC(state, async) {
      state.async = async;
      state.pushType = async ? "async" : "sync";
    },
    SET_RULETOCONIFG(state, ruleToConfig) {
      state.ruleToConfig = ruleToConfig;
    },
    SET_BASEURL(state, baseURL) {
      state.baseURL = baseURL;
      localStorage.setItem("baseURL",baseURL);
    },
  },

  actions: {
    setAsync({ commit }, async) {
      commit('SET_ASYNC', async)
    },
    setRuleToConfig({ commit }, ruleToConfig) {
      commit('SET_RULETOCONIFG', ruleToConfig)
    },
    setBaseURL({ commit }, baseURL) {
      commit('SET_BASEURL', baseURL)
    },
  }
};

export default app;
