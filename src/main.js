// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import App from './App';
import router from './router';
import store from './store'
import ElementUI from 'element-ui';
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';
import 'normalize.css';
import 'element-ui/lib/theme-chalk/index.css';
import '@/assets/iconfont/iconfont.css';
// global css
import '@/styles/index.scss';

import './mock';

Vue.use(ElementUI);

router.beforeEach((to, from, next) => {
  NProgress.start(); // start progress bar
  next(()=>{
    NProgress.done();// finish progress bar
  });
});

router.afterEach(() => {
  NProgress.done();// finish progress bar
});

Vue.config.productionTip = false;

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
});
