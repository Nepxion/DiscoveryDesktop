<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    @closed="dialogClose(false)"
    width="400px">
    <el-form :model="form" label-width="80px">
      <el-form-item label="服务集群">
        <el-select v-model="form.groups" multiple :loading="dataLoading" placeholder="请选择服务集群" class="groups-select">
          <el-option v-for="(val, index) in groups" :key="index" :label="val" :value="val"></el-option>
        </el-select>
      </el-form-item>
      <el-alert
        title="*不选为全部服务集群"
        type="info"
        :closable="false" />
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogClose(false)">取 消</el-button>
      <el-button :loading="loading" type="primary" @click="dialogClose(true)">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import { mapGetters } from 'vuex';
  export default {
    name: "InstanceGroupDialog",
    data() {
      return {
        dialogVisible: this.visible,
        loading: false,
        dataLoading: true,
        form: {
          groups:''
        },
      }
    },
    computed: {
      ...mapGetters([
        'groups'
      ]),
    },
    props: {
      visible: Boolean,
      title: String,
    },
    watch: {
      visible(val) {
        this.loading=false;
        this.dialogVisible = val;
        if(val){
          this.dataLoading=true;
          this.$store.dispatch('GetGroups').then(this.dataLoading=false);
        }
      }
    },
    methods: {
      dialogClose(isok) {
        if(isok){
          this.loading=true;
        }
        this.$emit('dialogClose', false, isok, this.form.groups);
      }
    }
  }
</script>

<style scoped>
.groups-select{
  width: 100%;
}
</style>
