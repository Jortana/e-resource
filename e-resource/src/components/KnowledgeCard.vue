<template>
  <div>
    <el-card shadow="never">
      <div slot="header" class="clearfix">
        <span class="card-title">{{ entityName }}</span>
      </div>
      <div class="flex">
        <div class="flex-1">
          <div v-for="(value, name, index) in info" :key="index">
            <div
              v-if="index % 2 === 0"
              class="properties flex"
              @mouseover="changeBtnShow(index, true)"
              @mouseleave="changeBtnShow(index, false)"
            >
              <span class="bold">{{ name }}：</span>
              {{ value }}
              <!-- 添加到资源包的按钮 -->
              <add-to-package-button
                :ref="'btn' + index"
                :default-visible="false"
                :resourceType="'content'"
                :content="`${entityName}：${name}：${value}`"
                class="x-mini-btn"
              ></add-to-package-button>
            </div>
          </div>
        </div>
        <div class="flex-1">
          <div v-for="(value, name, index) in info" :key="index" class="flex-1">
            <div
              v-if="index % 2 !== 0"
              class="properties flex"
              @mouseover="changeBtnShow(index, true)"
              @mouseleave="changeBtnShow(index, false)"
            >
              <span class="bold">{{ name }}：</span>
              {{ value }}
              <!-- 添加到资源包的按钮 -->
              <add-to-package-button
                :ref="'btn' + index"
                :default-visible="false"
                :resourceType="'content'"
                :content="`${entityName}：${name}：${value}`"
                class="x-mini-btn"
              ></add-to-package-button>
            </div>
          </div>
        </div>
      </div>
      <!-- <el-link class="more" @click="gotoKnowledge">查看完整信息</el-link> -->
      <!-- 隐藏的a元素，用来在新窗口打开资源页面 -->
      <a
        v-show="false"
        ref="knowledgeTarget"
        class="knowledge-target"
        href=""
        target="_blank"
      ></a>
    </el-card>
  </div>
</template>

<script>
import AddToPackageButton from '@/components/Buttons/AddToPackageButton'
export default {
  name: 'KnowledgeCard',
  components: {
    AddToPackageButton
  },
  props: {
    entityInfo: Object
  },
  data() {
    return {
      entityName: '',
      info: {}
    }
  },
  watch: {
    entityInfo: {
      handler(info) {
        this.entityName = info.name
        delete info.name
        this.info = info
      },
      immediate: true
    }
  },
  methods: {
    gotoKnowledge() {
      const target = this.$refs.knowledgeTarget
      target.setAttribute(
        'href',
        `${window.location.origin}/knowledge/${this.entityName}`
      )
      target.click()
    },
    changeBtnShow(index, show) {
      const ref = 'btn' + index
      this.$refs[ref][0].changeVisible(show)
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
  margin-bottom: 0.3rem;
  align-items: center;
}

.more {
  margin-top: 1rem;
}

.x-mini-btn >>> button {
  padding: 3px;
  font-size: 0.5rem;
  margin-left: 5px;
}
</style>
