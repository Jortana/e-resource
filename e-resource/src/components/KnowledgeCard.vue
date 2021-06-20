<template>
<div>
  <el-card
    shadow="never"
  >
    <div slot="header" class="clearfix">
      <span class="card-title">{{ entityName }}</span>
    </div>
    <div class="flex">
      <div class="flex-1">
        <div
          v-for="(value, name, index) in info"
          v-if="index % 2 === 0"
          :key="index">
          <div class="properties">
            <span class="bold">{{ name }}：</span>{{ value }}
          </div>
        </div>
      </div>
      <div class="flex-1">
        <div
          class="flex-1"
          v-for="(value, name, index) in info"
          v-if="index % 2 !== 0"
          :key="index">
          <div class="properties">
            <span class="bold">{{ name }}：</span>{{ value }}
          </div>
        </div>
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
        console.log(this.info)
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
  font-size: 1rem;
  margin-bottom: .3rem;
}

.more {
  margin-top: 1rem;
}
</style>
