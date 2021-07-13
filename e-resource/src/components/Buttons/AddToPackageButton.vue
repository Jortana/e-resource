<template>
  <el-popover placement="right" trigger="click" @show="getFolders">
    <!-- 展示用户所有的资源包 -->
    <div v-if="loading">
      <i class="el-icon-loading"></i>
      <span>加载中</span>
    </div>
    <el-scrollbar class="scroll" style="height: 150px;">
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
    <!-- 确定按钮 -->
    <el-button
      class="full-width"
      type="primary"
      size="mini"
      @click="addToPackage"
    >
      完成
    </el-button>
    <!-- 添加到资源包的圆形按钮 -->
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
import { authentication } from '@/api/auth'
import { getFolders } from '@/api/package'
export default {
  name: 'AddToPackageButton',
  props: {
    resourceType: String, // 用于说明添加的资源类型：resource/content/lGoal/lKey等
    resourceID: String, // 如果添加的是资源，则传递资源 ID
    content: String, // 如果添加的是文字内容，则传递内容
    lGoal: Object, // 学习目标
    lKey: Object, // 学习重难点
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
      // 如果未登录则提示信息
      authentication().then((response) => {
        const {
          data: { code }
        } = response
        // 验判断是否登录
        if (code !== 200) {
          // 未登录
          this.$message.warning('请先登录')
          this.$router.push({
            path: '/login',
            query: { redirect: this.$route.fullPath }
          })
          return
        }
      })
      // 已登录
      this.loading = true
      getFolders().then((response) => {
        const { code, data: folders } = response.data
        if (code === 200) {
          this.folders = folders
        }
        this.loading = false
      })
    },
    // 将所选资源添加到资源包
    addToPackage() {
      const { resourceType: type } = this
      switch (type) {
        case 'resource':
          console.log(this.resourceID)
          break
        case 'content':
          console.log(this.content)
          break
        default:
          break
      }
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

/* 滚动条 */
.scroll {
  margin-bottom: 0.5rem;
}

.scroll >>> .el-scrollbar__wrap {
  overflow-x: hidden;
}
</style>
