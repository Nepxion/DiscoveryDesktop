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
      <el-button :loading="loading" type="primary" @click="dialogClose(true)">更新灰度规则</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import MonacoEditor from "../MonacoEditor";

  export default {
    name: "GlobalReleaseDialog",
    data() {
      return {
        dialogVisible: this.visible,
        loading: false,
        form: {
          value: '',
          cluster: undefined
        },
      }
    },
    components: {
      MonacoEditor
    },
    mounted() {

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
        if(isok) {
          this.$refs.editorForm.validate((valid) => {
            if (valid) {
              this.loading = true;

              this.onClose(isok);
            } else {
              //this.$message.error('更新失败！');
              return false;
            }
          });
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
      destroy: function () {
        this.$refs.editorForm.resetFields();
        this.loading = false;
        //this.value = '';
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
