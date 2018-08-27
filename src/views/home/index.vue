<template>
  <div class="wrapper">
    <el-container>
      <el-header height="42px">
        <el-row>
          <el-button type="text"><i class="el-iconfont-ecs"></i> 显示服务拓扑图</el-button>
          <span class="separator"></span>
          <el-button type="text" disabled><i class="el-iconfont-fabu"></i> 执行灰度发布</el-button>
          <span class="separator"></span>
          <el-button type="text" disabled><i class="el-iconfont-luyou"></i> 执行灰度路由</el-button>
          <span class="toolbar">
          <el-button type="text" title="全屏"><i class="el-iconfont-fullscreen"></i></el-button>
          </span>
        </el-row>
      </el-header>
      <el-main id="graph">

      </el-main>
    </el-container>
  </div>
  <!-- 创建图容器 -->
</template>

<script>

  import Graph from "@/components/d3/graph"
  import Trace from "@/components/d3/trace"
  import Tree from "@/components/d3/tree"

  export default {
    name: "home",
    data() {
      return {}
    },
    mounted() {
      //this.initGraph();
      this.initTrace();
    },
    methods: {
      initGraph: function () {
        let svg = new Graph("#graph");
        svg.addNode({title: "服务a"});
        svg.addNode({title: "服务b"});
        svg.addNode({title: "服务c"});
        svg.addNode({title: "服务d"});
        svg.addNode({title: "服务d"});

        svg.addGroup({title: "服务群组a", child: [{title: "服务a1"}, {title: "服务a2"}]});
        svg.addGroup({title: "服务群组b", child: [{title: "服务b1"}, {title: "服务b2"}]});
        svg.addGroup({title: "服务群组c", child: [{title: "服务c1"}, {title: "服务c2"}]});
      },
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
