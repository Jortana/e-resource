<template>
  <div class="resource-container">
    <div
      class="flex"
      v-for="resource in resourceList"
      :key="resource.id">
      <svg class="type-icon" aria-hidden="true">
        <use v-if="resource.suffix === 'doc' || resource.suffix === 'docx'" xlink:href="#e-resource-icon-word"></use>
        <use v-else-if="resource.suffix === 'ppt' || resource.suffix === 'pptx'" xlink:href="#e-resource-icon-ppt"></use>
        <use v-else-if="resource.suffix === 'pdf'" xlink:href="#e-resource-icon-pdf"></use>
        <use v-else xlink:href="#e-resource-icon-unknown"></use>
      </svg>
      <resource-link class="resource-name" :resource="resource" :browseTime="browseTime"></resource-link>
      <span v-if="browseTime === true" class="flex browse-time">{{ resource['browse'] }}</span>
    </div>
  </div>
</template>

<script>
import ResourceLink from '@/components/ResourceLink'
export default {
  name: 'ResourceList',
  components: {
    ResourceLink
  },
  props: {
    resourceList: Array,
    browseTime: Boolean
  }
}
</script>

<style scoped>
.type-icon {
  height: 1.2rem;
  width: 1.2rem;
  margin-right: .2rem;
}

.resource-container div {
  margin-bottom: .5rem;
}

.resource-name {
  font-size: 1rem;
  line-height: 1.5rem;
  color: #409eff;
  cursor: pointer;
}

.resource-name:hover {
  color: #66b1ff;
}

.browse-time {
  align-items: center;
  display: inline-block;
  margin-left: auto;
  color: #909399;
}
</style>
