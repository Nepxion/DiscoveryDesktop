<template>
  <div>
    <div class="wrapper">
      <el-container>
        <el-header height="42px">
          <el-row>
            <i class="el-iconfont-ecs"></i>
            <span class="separator"></span>
            <span class="toolbar">
              <i class="el-iconfont-fullscreen"></i>
            </span>
          </el-row>
        </el-header>
        <el-main>{{ msg }}</el-main>
      </el-container>
    </div>
    <!-- 创建图容器 -->

  </div>
</template>

<script>

  export default {
    name: "home",
    data() {
      return {
        msg: 'Welcome to Your Vue.js App'
      }
    },
    mounted() {

    }
  }
</script>

<style scoped>
  .separator {
    margin: 4px;
    border-left: 1px solid #E9E9E9;
  }

  .toolbar {
    float: right;
  }
</style>
