import Vue from 'vue';
import Vuex from 'vuex';
import getters from './getters';
import app from './modules/app';
import console from './modules/console';
import service from './modules/service';
import user from './modules/user';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    app,
    console,
    service,
    user,
  },
  getters
})
