<template>
  <div>
    <el-card class="card" shadow="never">
      <div class="card-content">
        <div class="title bold">{{title}}</div>
        <div v-if="contents[curIndex]" class="content">
          {{contents[curIndex].content}}
        </div>
<!--        {{list}}-->
      </div>
      <div class="operations">
        <el-button icon="el-icon-arrow-left" circle size="mini"></el-button>
        <el-link :underline="false">查看全部</el-link>
        <el-button icon="el-icon-arrow-right" circle size="mini"></el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'GoalAndKeyCard',
  props: {
    list: Array,
    title: String
  },
  watch: {
    list: {
      handler () {
        this.contents = [...this.list]
      },
      immediate: true
    }
  },
  data () {
    return {
      contents: [],
      curIndex: 0
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

</style>
