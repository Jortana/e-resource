<template>
  <el-popover placement="right" trigger="click" @show="getFolders">
    <!-- 展示用户所有的资源包 -->
    <div v-if="loading">
      <i class="el-icon-loading"></i>
      <span>加载中</span>
    </div>
    <el-scrollbar v-else class="scroll" style="height: 150px;">
      <el-checkbox-group v-model="checked">
        <el-checkbox
          v-for="folder in folders"
          :key="folder.folderID"
          :label="folder.folderID"
          class="check-block"
        >
          {{ folder.folderName }}
        </el-checkbox>
      </el-checkbox-group>
    </el-scrollbar>
    <el-button
      slot="reference"
      :class="visible ? '' : 'none'"
      :type="type"
      :size="size"
      icon="el-icon-document-add"
      circle
    ></el-button>
  </el-popover>
</template>

<script>
import { getFolders } from '@/api/package'
export default {
  name: 'AddToPackageButton',
  props: {
    resourceID: Number,
    size: String,
    type: String,
    defaultVisible: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      visible: this.defaultVisible, // 控制按钮是否显示
      loading: false, // 是否在加载数据
      folders: [], // 存储所有的资源包
      checked: [] // 存储选中的资源包 ID
    }
  },
  methods: {
    changeVisible(visible) {
      this.visible = visible
    },
    getFolders() {
      this.loading = true
      getFolders().then((response) => {
        const { code, data: folders } = response.data
        if (code === 200) {
          this.folders = folders
        }
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>
/* 资源包选项 */
.check-block {
  display: block;
  margin-bottom: 0.5rem;
}

.scroll >>> .el-scrollbar__wrap {
  overflow-x: hidden;
}
</style>
