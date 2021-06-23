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
          {{ contents[curIndex].content }}
          <!-- 添加到备课的按钮 -->
          <add-to-package-button
            :ref="'btn'"
            :default-visible="false"
            class="x-mini-btn"
          ></add-to-package-button>
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
        <el-link :underline="false">查看全部</el-link>
        <el-button
          :disabled="curIndex >= contents.length - 1"
          icon="el-icon-arrow-right"
          circle
          size="mini"
          @click="changePage(1)"
        ></el-button>
      </div>
    </el-card>
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
      curIndex: 0
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

.x-mini-btn {
  padding: 3px;
  font-size: 0.5rem;
  margin-left: 5px;
}
</style>
