<template>
  <el-button
    :class="visible ? '' : 'none'"
    :type="type"
    :size="size"
    icon="el-icon-document-add"
    circle
  ></el-button>
</template>

<script>
export default {
  name: 'AddToPackageButton',
  props: {
    resourceID: Number,
    size: String,
    type: String,
    defaultVisible: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      visible: this.defaultVisible
    }
  },
  methods: {
    changeVisible(visible) {
      this.visible = visible
    }
  }
}
</script>

<style scoped></style>
