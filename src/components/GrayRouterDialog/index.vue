<template>
  <el-dialog
    :title="title"
    fullscreen
    :visible.sync="dialogVisible"
    @closed="dialogClose(false)">
    <el-form :model="form" ref="routerForm" inline>
      <el-form-item
        prop="services"
        label="服务列表："
        label-width="100px"
        :rules="{ required: true, message: '服务列表不能为空' }"
      >
        <el-select v-model="form.services" multiple placeholder="请选择服务" class="services-select">
          <el-option v-for="val in serviceList" :key="val" :value="val"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button :loading="loading" type="primary" @click="handleRouter()">执行路由</el-button>
        <el-button @click="handleRouterReset()">清除路由</el-button>
      </el-form-item>
    </el-form>
    <el-main id="trace">

    </el-main>
  </el-dialog>
</template>

<script>
  import { convRoutes } from '../../utils'

  import Cluster from "../D3/Cluster"
  import Force from "../D3/Force"

  export default {
    name: "GrayRouterDialog",
    data() {
      return {
        dialogVisible: this.visible,
        loading: false,
        form: {
          services: undefined
        },
        svg: null
      }
    },
    mounted() {

    },
    props: {
      visible: Boolean,
      title: String,
      selectedNode: Object,
      serviceList: Array,
      showMode: String,
    },
    watch: {
      visible(val) {
        this.dialogVisible = val;
      }
    },
    methods: {
      dialogClose(isok) {
        this.destroy();
        this.$emit('dialogClose', false, isok);
      },
      handleRouter() {
        this.$refs.routerForm.validate((valid) => {
          if (valid) {
            this.loading = true;
            this.initTrace();
          } else {
            return false
          }
        });
      },
      handleRouterReset() {
        this.$refs.routerForm.resetFields();
        this.initTrace();

      },
      initTrace: function () {
        //if (!this.svg) {
        //this.svg = new Trace("#trace");
        //}
        if (this.showMode === 'force') {
          this.svg = new Force("#trace");

        } else {
          this.svg = new Cluster("#trace");
        }
        const url = this.getUrl();
        console.log(url);
        const serviceIds = this.form.services.join(";");
        //console.log(serviceIds)

        this.$store.dispatch('GetGrayRoutes', { baseURL: url, serviceIds: serviceIds }).then((data) => {
          this.loading = false;
          this.$message({
            message: '执行成功！',
            type: 'success'
          });
          try {
            const newData = convRoutes(data);

            this.svg && this.svg.loadData(newData);
          } catch (e) {
            console.log(e);
          }
        }).catch(() => {
          this.loading = false;
          this.$message.error('执行失败！');
        });
      },
      destroy: function () {
        if (this.svg) {
          this.svg.destroy();
        }
        this.$refs.routerForm.resetFields();
        // this.form = {
        //   services: ''
        // };
        this.svg = null;
        this.loading = false;
      },
      getUrl: function () {
        return "http://" + this.selectedNode.host + ":" + this.selectedNode.port;
      }
    }
  }
</script>

<style scoped>
  .services-select {
    min-width: 400px;
    width: calc(100vw - 450px);
  }

  #trace {
    border: #e9e9e9 1px solid;
    min-height: calc(100vh - 210px);
    overflow: hidden;
    padding-top: 20px;
  }
</style>
