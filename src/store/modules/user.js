import { fetchLogin } from '../../api';

const user = {
  state: {
    userId: '',
    status: false
  },

  mutations: {
    SET_USERID: (state, userId) => {
      state.userId = userId
    },
    SET_STATUS: (state, status) => {
      state.status = status
    }
  },

  actions: {
    // 用户名登录
    Login({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        fetchLogin(userInfo.userId, userInfo.password).then(response => {
          const result = eval(response);
          if(result){
            commit('SET_USERID', userInfo.userId)
            commit('SET_STATUS', result)
          }
          resolve(result)
        }).catch(error => {
          reject(error)
        })
      })
    },
  }
}

export default user
