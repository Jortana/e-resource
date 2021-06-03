<template>
<div class="resource-name" @click="viewResource(resource['id'])">
  <div :class="isHidden ? 'inline-resource-name' : ''">{{ resource['resourceName'] }}</div>
  <!-- 隐藏的a元素，用来在新窗口打开资源页面 -->
  <a class="resource-target" ref="resourceTarget" href="" target="_blank" v-show="false"></a>
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
    viewResource (resourceID) {
      let target = this.$refs.resourceTarget
      target.setAttribute('href', `${window.location.origin}/resource/${resourceID}`)
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
