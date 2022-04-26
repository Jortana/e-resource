<template>
  <div class="flex">
    <!-- 资源 -->
    <section class="left flex-1">
      <div v-if="resources.resources" class="section">
        <h4>资源列表</h4>
        <ul>
          <list-item
            v-for="resource in resources.resources"
            :key="resource.id"
            :resource="resource"
            :id="folderID"
            @updateResource="$emit('updateResource')"
          />
        </ul>
      </div>
    </section>
    <!-- 文字 -->
    <section class="right flex-1">
      <div v-if="resources.goal" class="section">
        <h4>学习目标</h4>
        <div
          v-for="(goal, index) in resources.goal"
          :key="index"
          class="content"
        >
          <resource-content
            :content="goal.text"
            :folderID="folderID"
            :id="goal.id"
            :type="'goal'"
            @updateResource="$emit('updateResource')"
          />
        </div>
      </div>
      <div v-if="resources.key" class="section">
        <h4>学习重难点</h4>
        <div v-for="(key, index) in resources.key" :key="index" class="content">
          <resource-content
            :content="key.text"
            :folderID="folderID"
            :id="key.id"
            :type="'key'"
            @updateResource="$emit('updateResource')"
          />
        </div>
      </div>
      <div v-if="resources.content" class="section">
        <h4>知识点</h4>
        <div
          v-for="(content, index) in resources.content"
          :key="index"
          class="content"
        >
          <resource-content
            :content="content"
            :folderID="folderID"
            :type="'content'"
            @updateResource="$emit('updateResource')"
          />
        </div>
      </div>
    </section>

    <!-- 隐藏的a元素，用来在新窗口打开资源页面 -->
    <a
      v-show="false"
      ref="resourceTarget"
      class="resource-target"
      href=""
      target="_blank"
    ></a>
    <!-- {{ resources }} -->
  </div>
</template>

<script>
import ListItem from '@/components/ResourcePackage/ListItem'
import ResourceContent from '@/components/ResourcePackage/ResourceContent'
export default {
  name: 'Resources',
  components: { ListItem, ResourceContent },
  props: {
    resourcesObj: {
      type: Object,
      default: null
    },
    folderID: String
  },
  data() {
    return {
      resources: null
    }
  },
  watch: {
    resourcesObj: {
      handler(resourcesObj) {
        this.resources = { ...resourcesObj }
      },
      immediate: true
    }
  },
  methods: {
    viewResource(resourceID) {
      const target = this.$refs.resourceTarget
      target.setAttribute(
        'href',
        `${window.location.origin}/e-resource/#/resource/${resourceID}`
      )
      target.click()
    }
  }
}
</script>

<style>
.section {
  color: #606266;
  margin-top: 1rem;
}

.section h4 {
  color: #303133;
}

.section .content {
  margin-top: 0.2rem;
}

.section ul li {
  margin-top: 0.5rem;
  display: flex;
  align-items: center;
}

.section ul li div {
  cursor: pointer;
}

.section ul li div:hover {
  text-decoration: underline;
  text-underline-offset: 0.2rem;
}

.type-icon {
  height: 1.2rem;
  width: 1.2rem;
  margin-right: 0.3rem;
}
</style>
