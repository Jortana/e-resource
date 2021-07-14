<template>
  <div>
    <el-card class="card" shadow="never">
      <div class="card-content">
        <div class="title bold">{{ title }}</div>
        <div
          v-if="contents[curIndex]"
          class="content"
          @mouseover="changeBtnShow(true)"
          @mouseleave="changeBtnShow(false)"
        >
          <div class="content-text">
            {{ contents[curIndex].content }}
          </div>
          <!-- 添加到备课的按钮 -->
          <add-to-package-button
            :ref="'btn'"
            :default-visible="false"
            class="x-mini-btn"
          ></add-to-package-button>
        </div>
        <div v-else class="content">
          暂无内容
        </div>
      </div>
      <div class="operations">
        <el-button
          :disabled="curIndex <= 0"
          icon="el-icon-arrow-left"
          circle
          size="mini"
          @click="changePage(-1)"
        ></el-button>
        <el-link :underline="false" @click="viewAllVisible = true">
          查看全部
        </el-link>
        <el-button
          :disabled="curIndex >= contents.length - 1"
          icon="el-icon-arrow-right"
          circle
          size="mini"
          @click="changePage(1)"
        ></el-button>
      </div>
    </el-card>
    <!-- 查看全部的对话框 -->
    <el-dialog :visible.sync="viewAllVisible" :title="title" width="350px">
      <ul class="content-list">
        <li v-for="content in contents" :key="content.resourceID">
          {{ content.content }}
        </li>
      </ul>
    </el-dialog>
  </div>
</template>

<script>
import AddToPackageButton from '@/components/Buttons/AddToPackageButton'
export default {
  name: 'GoalAndKeyCard',
  components: {
    AddToPackageButton
  },
  props: {
    list: Array,
    title: String
  },
  data() {
    return {
      contents: [],
      curIndex: 0,
      viewAllVisible: false // 控制查看全部对话框是否显示
    }
  },
  watch: {
    list: {
      handler() {
        this.curIndex = 0
        this.contents = [...this.list]
      },
      immediate: true
    }
  },
  methods: {
    // 切换条数
    changePage(change) {
      this.curIndex += change
    },
    // 控制添加到备课的按钮是否显示
    changeBtnShow(show) {
      const ref = 'btn'
      this.$refs[ref].changeVisible(show)
    }
  }
}
</script>

<style scoped>
.card {
  height: 100%;
}

.card >>> .el-card__body {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.card-content {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.title {
  font-size: 1rem;
  margin-bottom: 1rem;
}

.content {
  color: #606266;
}

.content-text {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  overflow: hidden;
  /* autoprefixer: ignore next */
  -webkit-box-orient: vertical;
}

.operations {
  display: flex;
  justify-content: space-between;
}

.operations .el-link {
  color: #909399;
}

.operations .el-link:hover {
  color: #b0b3bc;
}

.x-mini-btn >>> button {
  float: right;
  padding: 3px;
  font-size: 0.5rem;
  margin-top: 8px;
}

.content-list li {
  margin-bottom: 1rem;
}
</style>
