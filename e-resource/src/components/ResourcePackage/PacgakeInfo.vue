<template>
  <el-dialog
    :visible.sync="visible"
    :before-close="close"
    :close-on-click-modal="false"
    custom-class="package-info-dialog"
    title="收藏夹信息"
    width="400px"
    center
  >
    <!-- 资源包名和简介 -->
    <div>
      <el-form
        ref="infoForm"
        :show-message="false"
        :model="packageInfo"
        :rules="rules"
        size="medium"
        label-width="100px"
        label-position="top"
      >
        <el-form-item label="资源包名称" prop="name">
          <el-input
            :maxlength="20"
            :show-word-limit="true"
            v-model="packageInfo.name"
            placeholder="资源包名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="简介" prop="intro">
          <el-input
            :rows="5"
            :maxlength="200"
            :show-word-limit="true"
            v-model="packageInfo.intro"
            placeholder="可填写简介"
            type="textarea"
            resize="none"
          ></el-input>
        </el-form-item>
      </el-form>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button type="primary" size="medium" @click="validateForm('infoForm')">
        提 交
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'PackageInfo',
  components: {},
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      packageInfo: {
        name: '',
        intro: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入资源包名称', trigger: 'blur' },
          { max: 20, message: '长度在 20 个字符以内', trigger: 'blur' }
        ],
        intro: [{ max: 200, message: '长度在 200 个字符以内', trigger: 'blur' }]
      }
    }
  },
  computed: {},
  watch: {},
  mounted() {},
  methods: {
    // 要通过事件的方式改变传进来的 visible 的值
    close() {
      this.$emit('update:visible', false)
    },
    // 验证表单
    validateForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          console.log('valid')
        } else {
          console.log('invalid')
          return false
        }
      })
    },
    // 提交创建/修改信息
    submit() {
      console.log('submit')
    }
  }
}
</script>
<style scoped></style>

<style>
/* 对话框的样式 */
/* header */
.package-info-dialog > div:nth-child(1) {
  border-bottom: 1px solid #e5e9ef;
}

/* body */
.package-info-dialog > div:nth-child(2) {
  padding: 20px 25px 0;
}

.package-info-dialog label {
  padding: 0 !important;
}
</style>
