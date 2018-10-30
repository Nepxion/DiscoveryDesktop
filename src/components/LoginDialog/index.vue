<template>
  <el-dialog
    title="用户登陆"
    :show-close="false"
    :close-on-click-modal="false"
    :close-on-press-escape="false"

    :visible.sync="!this.$store.getters.status">
      <el-form :model="loginForm" ref="loginForm" label-width="100px">
        <el-form-item
          prop="baseURL"
          label="服务地址"
          :rules="{ required: true, message: '请输入服务地址（http://）！' }"
        >
          <el-input
            v-model="loginForm.baseURL"
            placeholder="请输入服务地址"
            name="baseURL"/>
        </el-form-item>
        <el-form-item
          prop="userId"
          label="用户名"
          :rules="{ required: true, message: '请输入用户名！' }"
        >
          <el-input
            v-model="loginForm.userId"
            placeholder="请输入用户名"
            name="userId"
            type="text"
            auto-complete="on"
          />
        </el-form-item>
        <el-form-item
          prop="password"
          label="密  码"
          :rules="{ required: true, message: '请输入密码！' }"
        >
          <el-input
            type="password"
            v-model="loginForm.password"
            placeholder="请输入密码"
            name="password"
            auto-complete="on"
            @keyup.enter.native="handleLogin"/>
        </el-form-item>
      </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button :loading="loading" type="primary" @click="handleLogin()">登 陆</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    name: "LoginDialog",
    data() {
      return {
        loading: false,
        loginForm: {
          baseURL: this.$store.getters.baseURL,
          userId: 'admin',
          password: 'admin'
        },
      }
    },
    mounted() {

    },
    methods: {
      handleLogin() {
        this.$refs.loginForm.validate((valid) => {
          if (valid) {
            this.loading = true;

            this.$store.dispatch('setBaseURL', this.loginForm.baseURL).then(() => {
              this.$store.dispatch('Login', this.loginForm).then((ret) => {
                this.loading = false;
                console.log(ret)
                if(ret){
                  this.$emit('dialogClose');
                }
              }).catch(() => {
                this.loading = false
              })
            })

          }
        });
      },
    },
  }
</script>

<style scoped>

</style>
