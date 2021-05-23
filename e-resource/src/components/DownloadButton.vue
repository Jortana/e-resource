<template>
  <el-button class="full-width" type="primary" size="medium" icon="el-icon-download" @click="download()">
    下载
  </el-button>
</template>

<script>
import { download } from '@/api/resource'

export default {
  name: 'DownloadButton',
  props: {
    resourceID: Number
  },
  methods: {
    download () {
      download(this.resourceID)
    }
  }
}
</script>

<style scoped>

</style>
