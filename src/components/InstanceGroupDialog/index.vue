<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    @closed="dialogClose(false)"
    width="400px">
    <el-form :model="form" label-width="80px">
      <el-form-item label="服务集群">
        <el-select v-model="form.cluster" placeholder="请选择服务集群" class="cluster-select">
          <el-option label="全部服务集群" value=""></el-option>
          <el-option v-for="(val, index) in groups" :key="index" :label="val" :value="val"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogClose(false)">取 消</el-button>
      <el-button type="primary" @click="dialogClose(true)">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import { getGroups } from '../../utils'
  export default {
    name: "InstanceGroupDialog",
    data() {
      return {
        dialogVisible: this.visible,
        form: {
          cluster:''
        },
      }
    },
    props: {
      visible: Boolean,
      title: String,
      groups: Array,
    },
    watch: {
      visible(val) {
        this.dialogVisible = val;
      }
    },
    methods: {
      dialogClose(isok) {
        this.$emit('dialogClose', false, isok, this.form.cluster);
      }
    }
  }
</script>

<style scoped>
.cluster-select{
  width: 100%;
}
</style>
