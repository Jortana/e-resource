<template>
  <el-button
    :size="size"
    type="primary"
    icon="el-icon-download"
    circle
    @click="download()"
  ></el-button>
</template>

<script>
import { download } from '@/api/resource'

export default {
  name: 'DownloadButton',
  props: {
    resourceID: Number,
    size: String
  },
  methods: {
    download() {
      download(this.resourceID)
    }
  }
}
</script>

<style scoped></style>
