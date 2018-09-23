<template>
  <el-dialog
    :title="dialogTitle"
    fullscreen
    :visible.sync="dialogVisible"
    @closed="dialogClose(false)">
    <el-card class="box-card" header="灰度版本操作" v-if="!ruleToConfig">
      <el-row class="editor-info">
        <el-col :span="24">
          <el-input v-model="version" placeholder="请输入版本号"></el-input>
        </el-col>
      </el-row>

      <el-row class="editor-info">
        <el-col :span="24">
          <el-alert
            title="灰度版本，输入格式为[dynamicVersion]:[localVersion],例如1.1或者1.1；1.0，前者直接更新灰度版本为1.1，后者只是满足初始版本为1.0条件的服务更新灰度版本为1.1"
            :closable="false"
            type="info"
            show-icon />
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-button @click="handleUpdateVersion">更新灰度版本</el-button>
          <el-button @click="handleClearVersion">清除灰度版本</el-button>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="box-card" header="灰度规则操作">
      <monaco-editor
        :value="config"
        @change="onChange"
        class="editor"
        language="xml">
      </monaco-editor>

      <el-row class="editor-info">
        <el-col :span="24">
          <el-alert
            title="灰度规则，输入的格式为xml"
            :closable="false"
            type="info"
            show-icon />
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-button @click="handleUpdateConfig">更新灰度规则</el-button>
          <el-button @click="handleClearConfig">清除灰度规则</el-button>
        </el-col>
      </el-row>

    </el-card>

  </el-dialog>
</template>

<script>
  import { mapGetters } from 'vuex';

  import MonacoEditor from "../MonacoEditor";

  export default {
    name: "GrayClusterReleaseDialog",
    data() {
      return {
        dialogVisible: this.visible,
        loading: false,
        version: '',
        config: ''
      }
    },
    components: {
      MonacoEditor
    },
    computed: {
      ...mapGetters([
        'pushType',
        'ruleToConfig',
        'groups'
      ]),
      dialogTitle() {
        if(this.ruleToConfig){
          return this.title +'（远程配置中心）';
        } else {
          return this.title;
        }
      }
    },
    mounted() {
      if(this.dialogVisible){
        this.init();
      }
    },
    props: {
      visible: Boolean,
      title: String,
      selectedGroupNode: String,
    },
    watch: {
      visible(val) {
        this.dialogVisible = val;
        if(this.dialogVisible){
          this.init();
        }
      },
    },
    methods: {
      init() {
        if(this.ruleToConfig) {
          //需要考虑到的是this.groups 是否会多个存在
          this.$store.dispatch('GetConfigByGroup', {
            group: this.groups,
            serviceId: this.selectedGroupNode
          }).then((data) => {
            this.config = data;
          }).catch(() => {
            this.$message.error('获取远程配置中心规则失败！');
          });
        }
      },
      dialogClose(isok) {
        this.destroy();
        this.$emit('dialogClose', false, isok);
      },
      onChange(value) {
        this.config=value;
      },
      destroy() {
        this.loading = false;
        this.version = '';
        this.config = '';
      },
      handleClearVersion() {
        if(!this.ruleToConfig) {
          this.$store.dispatch('ClearVersionByService', {serviceId: this.selectedGroupNode,pushType:this.pushType}).then((data) => {
            this.version='';
          }).catch(() => {
            this.$message.error('清除初始化（本地）版本失败！');
          });
        }
      },
      handleClearConfig() {
        if(this.ruleToConfig){
          this.$store.dispatch('ClearConfigByGroup', {serviceId: this.selectedGroupNode,group: this.groups}).then((data) => {
            this.config='';
          }).catch(() => {
            this.$message.error('清除初始化（本地）版本失败！');
          });
        } else {
          this.$store.dispatch('ClearConfigByService', {serviceId: this.selectedGroupNode,pushType:this.pushType}).then((data) => {
            this.config='';
          }).catch(() => {
            this.$message.error('清除初始化（本地）版本失败！');
          });
        }
      },
      handleUpdateVersion() {
        if(!this.ruleToConfig) {
          this.$store.dispatch('UpdateVersionByService', {
            serviceId: this.selectedGroupNode,
            pushType: this.pushType,
            version: this.version
          }).then((data) => {
            this.$message({
              message: '更新成功！',
              type: 'success'
            });
          }).catch(() => {
            this.$message.error('更新灰度版本失败！');
          });
        }
      },
      handleUpdateConfig() {
        if(this.ruleToConfig){
          this.$store.dispatch('UpdateConfigByGroup', {serviceId: this.selectedGroupNode,group: this.groups, config:this.config}).then((data) => {
            this.$message({
              message: '更新成功！',
              type: 'success'
            });
          }).catch(() => {
            this.$message.error('更新灰度规则失败！');
          });
        } else {
          this.$store.dispatch('UpdateConfigByService', {serviceId: this.selectedGroupNode,pushType:this.pushType, config:this.config}).then((data) => {
            this.$message({
              message: '更新成功！',
              type: 'success'
            });
          }).catch(() => {
            this.$message.error('更新灰度规则失败！');
          });
        }
      },
    }
  }
</script>

<style scoped>
  .editor {
    border: #e4e7ed 2px solid;
    min-height: calc(100vh - 500px);
  }
  .editor-info {
    margin: 10px 0;
  }
</style>
