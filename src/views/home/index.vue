<template>
  <div class="wrapper">
    <el-container>
      <el-header height="42px">
        <el-row>
          <el-button type="text" @click="onDialogVisible(true)"><i class="el-iconfont-ecs"></i> 显示服务拓扑图</el-button>
          <span class="separator"></span>
          <el-button type="text" disabled><i class="el-iconfont-fabu"></i> 执行灰度发布</el-button>
          <span class="separator"></span>
          <el-button type="text" disabled><i class="el-iconfont-luyou"></i> 执行灰度路由</el-button>
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

    <instance-group-dialog
      title="服务集群选取"
      :visible="dialogVisible"
      :clusters="instanceMap"
      @dialogClose="onDialogVisible"
    >
    </instance-group-dialog>
  </div>
  <!-- 创建图容器 -->
</template>

<script>
  import { mapGetters } from 'vuex'

  import Graph from "@/components/D3/Graph"
  import Trace from "@/components/D3/Trace"

  import Screenfull from '@/components/Screenfull'
  import InstanceGroupDialog from "@/components/InstanceGroupDialog"

  import { filterGroups } from '@/utils'

  export default {
    name: "home",
    components: {
      Screenfull,
      InstanceGroupDialog
    },
    data() {
      return {
        dialogVisible: false
      }
    },
    computed: {
      ...mapGetters([
        'instanceMap'
      ])
    },
    mounted() {

      //this.initTrace();
    },
    methods: {
      initTrace: function () {
        let svg = new Trace("#graph");
        const data = {
          "title": "root",
          "children": [
            {
              "title": "parent A",
              "children": [
                { "title": "child A1" },
                { "title": "child A2" },
                { "title": "child A3" }
              ]
            }, {
              "title": "parent B",
              "children": [
                { "title": "child B1" },
                { "title": "child B2" }
              ]
            }
          ]
        };
        svg.loadData(data);

      },

      onDialogVisible: function (visible, isok, group) {
        this.dialogVisible = visible;
        if (visible) {
          this.$store.dispatch('GetInstanceMap');
        } else {
          if (isok) {
            const data = filterGroups(this.instanceMap, group);
            let svg = new Graph("#graph");
            svg.loadData(data);
          }
        }
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

</style>
