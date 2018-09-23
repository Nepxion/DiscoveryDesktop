<template>
  <div class="wrapper">
    <el-container>
      <el-header height="42px">
        <el-row>
          <el-button type="text" @click="onDialogVisible(true)"><i class="el-iconfont-ecs"></i> 显示服务拓扑图</el-button>
          <span class="separator"></span>
          <el-button type="text" @click="onGrayReleaseDialogVisible(true)" :disabled="grayReleaseDisabled"><i class="el-iconfont-fabu"></i> 执行灰度发布</el-button>
          <span class="separator"></span>
          <el-button type="text" @click="onGrayRouterDialogVisible(true)" :disabled="grayRouterDisabled"><i class="el-iconfont-luyou"></i> 执行灰度路由</el-button>
          <span class="separator"></span>
          <el-button type="text" @click="onGlobalReleaseDialogVisible(true)"><i class="el-iconfont-luyou"></i> 全链路灰度发布</el-button>
          <span class="separator"></span>
          <el-dropdown trigger="click" @command="handleCommand">
            <span>
              <i class="el-iconfont-luyou"></i> 推送模式设置<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="async|true"><div class="dropdownSpan"><i class="el-icon-check" v-if="async"></i></div> 异步推送</el-dropdown-item>
              <el-dropdown-item command="async|false"><div class="dropdownSpan"><i class="el-icon-check" v-if="!async"></i></div> 同步推送</el-dropdown-item>

              <el-dropdown-item command="ruleToConfig|true" divided><div class="dropdownSpan"><i class="el-icon-check" v-if="ruleToConfig"></i></div> 规则推送到远程配置中心</el-dropdown-item>
              <el-dropdown-item command="ruleToConfig|false"><div class="dropdownSpan"><i class="el-icon-check" v-if="!ruleToConfig"></i></div> 规则推送到服务或服务集群</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <span class="toolbar">
            <el-tooltip content="全屏">
              <screenfull />
            </el-tooltip>
          </span>
        </el-row>
      </el-header>
      <el-main id="graph">

      </el-main>
    </el-container>

    <login-dialog
      @dialogClose="onLoginDialogClose"
    >
    </login-dialog>
    <instance-group-dialog
      title="服务集群选取"
      :visible="dialogVisible"
      @dialogClose="onDialogVisible"
    >
    </instance-group-dialog>
    <gray-router-dialog
      title="执行灰度路由"
      :visible="grayRouterDialogVisible"
      :selectedNode="selectedNode"
      :serviceList="serviceList"
      @dialogClose="onGrayRouterDialogVisible"
    >
    </gray-router-dialog>
    <gray-release-dialog
      title="执行灰度发布"
      :visible="grayReleaseDialogVisible"
      :selectedNode="selectedNode"
      @dialogClose="onGrayReleaseDialogVisible"
    >
    </gray-release-dialog>

    <global-release-dialog
      title="全链路灰度发布"
      :visible="globalReleaseDialogVisible"
      :groups="groups"
      @dialogClose="onGlobalReleaseDialogVisible"
    >
    </global-release-dialog>


    <gray-cluster-release-dialog
      title="服务集群灰度发布"
      :visible="grayClusterReleaseDialogVisible"
      :selectedGroupNode="selectedGroupNode"
      @dialogClose="onGrayReleaseDialogVisible"
    >
    </gray-cluster-release-dialog>
  </div>
  <!-- 创建图容器 -->
</template>

<script>
  import { mapGetters } from 'vuex'
  import { Loading } from 'element-ui';

  import LoginDialog from '@/components/LoginDialog'

  import Graph from "@/components/D3/Graph"

  import Screenfull from '@/components/Screenfull'
  import InstanceGroupDialog from "@/components/InstanceGroupDialog"
  import GrayRouterDialog from "@/components/GrayRouterDialog"
  import GrayReleaseDialog from "@/components/GrayReleaseDialog"
  import GlobalReleaseDialog from "@/components/GlobalReleaseDialog"
  import GrayClusterReleaseDialog from "@/components/GrayClusterReleaseDialog"

  import { filterGroups,getPluginService } from '@/utils'

  export default {
    name: "home",
    components: {
      LoginDialog,
      Screenfull,
      InstanceGroupDialog,
      GrayRouterDialog,
      GrayReleaseDialog,
      GlobalReleaseDialog,
      GrayClusterReleaseDialog,
    },
    data() {
      return {
        dialogVisible: false,
        grayRouterDialogVisible: false,
        grayRouterDisabled: true,
        grayReleaseDialogVisible: false,
        grayReleaseDisabled: true,
        globalReleaseDialogVisible: false,
        grayClusterReleaseDialogVisible: false,
        selectedGroupNode: null,
        selectedNode: null,
        serviceList:[],
      }
    },
    computed: {
      ...mapGetters([
        'status',
        'async',
        'configType',
        'ruleToConfig',
        'discoveryType',
        'instanceMap',
        'groups'
      ]),
    },
    created() {
      //this.init(document.title);
    },
    methods: {
      init:function (title) {
        this.$store.dispatch('GetDiscoveryType').then(()=>{
          this.showTitle(title);
        }).catch(() => {
          this.$message.error('获取DiscoveryType失败！');
        });
        this.$store.dispatch('GetConfigType').then(()=>{
          this.showTitle(title);
        }).catch(() => {
          this.$message.error('获取ConfigType失败！');
        });
      },
      showTitle:function(title){
        if (this.discoveryType) {
          title += '[' + this.discoveryType + '注册发现中心]';
        }
        if (this.configType) {
          title += '[' + this.configType + '远程配置中心]';
        }

        document.title = title;
      },
      showNodes(data){
        return new Promise((resolve, reject) => {
          let svg = new Graph("#graph");
          svg.onNodeChecked = this.onNodeChecked;
          svg.onGroupChecked = this.onGroupChecked;
          svg.loadData(data);
        });
      },
      onNodeChecked(node) {
        this.selectedNode = node;
        this.selectedGroupNode = undefined;

        this.grayRouterDisabled=false;
        this.grayReleaseDisabled=false;


        this.serviceList=getPluginService(this.instanceMap,node.serviceId);
      },
      onGroupChecked(g) {
        this.selectedNode = undefined;
        this.selectedGroupNode = g;

        this.grayRouterDisabled=true;
        this.grayReleaseDisabled=false;

        this.serviceList=[];
      },

      onDialogVisible(visible, isok, groups) {
        this.dialogVisible = visible;
        if (!visible) {
          if (isok) {
            let loadingInstance = Loading.service({ fullscreen: true });
              this.$store.dispatch('GetInstanceMap',groups).then((data)=>{
                this.showNodes(data).then(loadingInstance.close()).catch((e)=>{
                  console.log(e);
                  loadingInstance.close();
                });
              }).catch((e)=>{
                loadingInstance.close();
              });
          }
        }
      },

      onGrayRouterDialogVisible(visible, isok) {
        this.grayRouterDialogVisible = visible;
      },

      onGrayReleaseDialogVisible(visible, isok) {
        if(this.selectedNode){
          this.grayReleaseDialogVisible = visible;
        } else {
          this.grayClusterReleaseDialogVisible = visible;
        }
      },

      onGlobalReleaseDialogVisible(visible, isok) {
        this.globalReleaseDialogVisible = visible;
      },

      handleCommand(command) {
        const strs = command.split('|');
        if (strs.length == 2) {
          if (strs[0] === 'async') {
            if (strs[1] === 'true') {
              this.$store.dispatch('setAsync', true);
            } else {
              this.$store.dispatch('setAsync', false);
            }
          } else {
            if (strs[1] === 'true') {
              this.$store.dispatch('setRuleToConfig', true);
            } else {
              this.$store.dispatch('setRuleToConfig', false);
            }
          }
        }
      },

      onLoginDialogClose() {
        this.init(document.title);
      }
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

  .dropdownSpan {
    display: inline-block;
    width: 20px;
  }
</style>
