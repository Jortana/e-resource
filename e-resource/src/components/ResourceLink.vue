<template>
  <div class="resource-name" @click="viewResource(resource['id'])">
    <div :class="isHidden ? 'inline-resource-name' : ''">
      {{ resource['resourceName'] }}
    </div>
    <!-- 隐藏的a元素，用来在新窗口打开资源页面 -->
    <a
      v-show="false"
      ref="resourceTarget"
      class="resource-target"
      href=""
      target="_blank"
    ></a>
  </div>
</template>

<script>
export default {
  name: 'ResourceLink',
  props: {
    resource: Object,
    isHidden: {
      type: Boolean,
      default: false
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

<style scoped>
.inline-resource-name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
