<template>
<span class="resource-name" @click="viewResource(resource['id'])">
  <span>{{ resource['resourceName'] }}</span>
  <!-- 隐藏的a元素，用来在新窗口打开资源页面 -->
  <a class="resource-target" ref="resourceTarget" href="" target="_blank" v-show="false"></a>
</span>
</template>

<script>
export default {
  name: 'ResourceLink',
  props: {
    resource: Object
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

</style>
