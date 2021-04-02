<template>
<div>
  <el-card
    shadow="never"
  >
    <div slot="header" class="clearfix">
      <span class="card-title">{{ entityName }}</span>
    </div>
    <div
      v-for="(value, name, index) in info"
      :key="index">
      <div class="properties" v-if="index < 5">
        <span class="bold">{{ name }}：</span>{{ value }}
      </div>
    </div>
    <el-link class="more" @click="gotoKnowledge">查看完整信息</el-link>
    <!-- 隐藏的a元素，用来在新窗口打开资源页面 -->
    <a class="knowledge-target" ref="knowledgeTarget" href="" target="_blank" v-show="false"></a>
  </el-card>
</div>
</template>

<script>
export default {
  name: 'KnowledgeCard',
  props: {
    entityInfo: Object
  },
  watch: {
    entityInfo: {
      handler (info) {
        this.entityName = info.name
        delete info.name
        this.info = info
      },
      immediate: true
    }
  },
  data () {
    return {
      entityName: '',
      info: {}
    }
  },
  methods: {
    gotoKnowledge () {
      let target = this.$refs.knowledgeTarget
      target.setAttribute('href', `${window.location.origin}/knowledge/${this.entityName}`)
      target.click()
    }
  }
}
</script>

<style scoped>
.card-title {
  font-size: 1.5rem;
}

.properties {
  font-size: .9rem;
  margin-bottom: .3rem;
}

.more {
  margin-top: 1rem;
}
</style>
