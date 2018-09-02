import Vue from 'vue';
import Vuex from 'vuex';
import getters from './getters';
import console from './modules/console';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    console,
  },
  getters
})
