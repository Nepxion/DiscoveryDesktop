<template>
  <el-dialog
    :title="title"
    fullscreen
    :visible.sync="dialogVisible"
    @closed="dialogClose(false)">
    <el-card class="box-card" header="灰度版本操作">
      <el-tabs>
        <el-tab-pane label="灰度（动态）版本">
          <el-input v-model="newVersion" placeholder="请输入版本号"></el-input>
        </el-tab-pane>
        <el-tab-pane label="初始化（本地）版本">
          <el-input v-model="initVersion" placeholder="请输入版本号" :disabled="true"></el-input>
        </el-tab-pane>
      </el-tabs>

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
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="灰度（动态）规则" name="1"></el-tab-pane>
        <el-tab-pane label="初始化（本地）规则" name="2"></el-tab-pane>
      </el-tabs>

      <monaco-editor
        :value="value"
        @change="onChange"
        :readOnly="editorDisabled"
        class="editor"
        language="xml">
      </monaco-editor>

      <el-row class="editor-info">
        <el-col :span="24">
          <el-alert
            title="灰度规则，输入的格式为xml或json"
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
    name: "GrayReleaseDialog",
    data() {
      return {
        dialogVisible: this.visible,
        loading: false,
        value: this.initConfig,
        activeName: '1',
        initVersion: '',
        initConfig: '',
        newVersion: '',
        newConfig: '',
        editorDisabled: false,
        url: ''
      }
    },
    components: {
      MonacoEditor
    },
    computed: {
      ...mapGetters([
        'pushType',
      ]),
    },
    mounted() {
      if(this.dialogVisible){
        this.init();
      }
    },
    props: {
      visible: Boolean,
      title: String,
      selectedNode: Object,
    },
    watch: {
      visible(val) {
        this.dialogVisible = val;
        if(this.dialogVisible){
          this.init();
        }
      },
      newConfig(val) {
        this.newConfig = val;
        if(this.activeName!=="2"){
          this.value=this.newConfig;
        }else{
          this.value=this.initConfig;
        }
      }
    },
    methods: {
      init() {
        this.getUrl();
        this.$store.dispatch('GetVersion', this.url).then((data) => {
          try{
            this.initVersion=data[0];
            this.newVersion=data[1];

          } catch (e) {
            console.log(e);
          }
        }).catch(() => {
          this.$message.error('获取初始化（本地）版本失败！');
        });
        this.$store.dispatch('GetConfig', this.url).then((data) => {
          try{
            this.initConfig=data[0];
            this.newConfig=data[1];
          } catch (e) {
            console.log(e);
          }
        }).catch(() => {
          this.$message.error('获取初始化（本地）规则失败！');
        });
      },
      dialogClose(isok) {
        this.destroy();
        this.$emit('dialogClose', false, isok);
      },
      onChange(value) {
        this.newConfig = value;
      },
      destroy() {
        this.loading = false;
        this.editorDisabled = false;
        this.value = '';
        this.activeName = '1';
        this.initVersion = '';
        this.initConfig = '';
        this.newVersion = '';
        this.newConfig = '';
      },
      getUrl() {
        if(this.selectedNode&&this.selectedNode.host&&this.selectedNode.port){
          this.url="http://" + this.selectedNode.host + ":" + this.selectedNode.port;
        }
      },
      handleClick(tab, event) {
        if (tab.name === '2') {
          this.value=this.initConfig;
          this.editorDisabled = true;
        } else {
          this.editorDisabled = false;
          this.value=this.newConfig;
        }
      },
      handleClearVersion() {
        this.$store.dispatch('ClearVersion', {baseURL:this.url,pushType:this.pushType}).then((data) => {
          this.newVersion='';
        }).catch(() => {
          this.$message.error('清除初始化（本地）版本失败！');
        });
      },
      handleClearConfig() {
        this.$store.dispatch('ClearConfig', {baseURL:this.url,pushType:this.pushType}).then((data) => {
          this.newConfig='';
        }).catch(() => {
          this.$message.error('清除初始化（本地）版本失败！');
        });
      },
      handleUpdateVersion() {
        this.$store.dispatch('UpdateVersion', {baseURL:this.url,pushType:this.pushType, version:this.newVersion}).then((data) => {
          //this.newVersion='';
          this.$message({
            message: '更新成功！',
            type: 'success'
          });
        }).catch(() => {
          this.$message.error('更新灰度版本失败！');
        });
      },
      handleUpdateConfig() {
        this.$store.dispatch('UpdateConfig', {baseURL:this.url,pushType:this.pushType, config:this.newConfig}).then((data) => {
          //this.newConfig='';
          this.$message({
            message: '更新成功！',
            type: 'success'
          });
        }).catch(() => {
          this.$message.error('更新灰度规则失败！');
        });
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
