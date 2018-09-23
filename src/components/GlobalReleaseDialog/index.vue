<template>
  <el-dialog
    :title="title"
    fullscreen
    :visible.sync="dialogVisible"
    @closed="dialogClose(false)">
    <el-card class="box-card">

      <el-form :model="form" ref="editorForm" label-width="100px">
        <el-form-item
          prop="cluster"
          label="服务集群组"
          :rules="{ required: true, message: '请选择服务集群组' }"
        >
          <el-select v-model="form.cluster" placeholder="请选择服务集群组" class="cluster-select">
            <el-option v-for="(val, index) in groups" :key="index" :label="val" :value="val"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          prop="value"
          label="灰度规则"
          :rules="{ required: true, message: '请填写灰度规则' }"
        >
          <monaco-editor
            v-model="form.value"
            @change="onChange"
            class="editor"
            language="xml">
          </monaco-editor>
        </el-form-item>

        <el-form-item>
          <el-alert
            title="灰度规则，输入的格式为xml"
            :closable="false"
            type="info"
            show-icon />
        </el-form-item>
      </el-form>

    </el-card>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogClose(false)">取 消</el-button>
      <el-button type="danger" @click="onClear()">清除灰度规则</el-button>
      <el-button :loading="loading" type="primary" @click="onOK()">更新灰度规则</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import { mapGetters } from 'vuex';
  import MonacoEditor from "../MonacoEditor";

  export default {
    name: "GlobalReleaseDialog",
    data() {
      return {
        dialogVisible: this.visible,
        loading: false,
        form: {
          value: '',
          cluster: ''
        },
      }
    },
    components: {
      MonacoEditor
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
        this.dialogVisible = val;
        if (val && !this.groups) {
          this.$store.dispatch('GetGroups');
        }
      }
    },
    methods: {
      init() {

      },
      dialogClose(isok) {
        if(isok) {
        } else{
          this.onClose(isok);
        }
      },
      onClose(isok) {
        this.destroy();
        this.$emit('dialogClose', false, isok);
      },
      onChange(value) {
        //this.value = value;
      },
      destroy() {
        this.$refs.editorForm.resetFields();
        this.loading = false;
        //this.value = '';
      },
      onOK() {
        this.$refs.editorForm.validate((valid) => {
          if (valid) {
            this.loading = true;

            this.$store.dispatch('UpdateConfigByGroup', {group:this.form.cluster, serviceId:this.form.cluster, config:this.form.value}).then((data) => {
              this.$message({
                message: '更新成功！',
                type: 'success'
              });
            }).catch(() => {
              this.$message.error('更新灰度规则失败！');
            });

            //this.onClose(isok);
          } else {
            //this.$message.error('更新失败！');
            return false;
          }
        });
      },
      onClear() {
        if (this.form.cluster) {
          this.$store.dispatch('ClearConfigByGroup', {
            group: this.form.cluster,
            serviceId: this.form.cluster
          }).then((data) => {
            this.$refs.editorForm.resetFields();
          }).catch(() => {
            this.$message.error('清除失败！');
          });
        } else {
          this.$message.error('请选择服务集群组！');
        }
      },
    }
  }
</script>

<style scoped>
  .editor {
    border: #e4e7ed 2px solid;
    min-height: calc(100vh - 400px);
  }
  .cluster-select{
    width: 100%;
  }
</style>
