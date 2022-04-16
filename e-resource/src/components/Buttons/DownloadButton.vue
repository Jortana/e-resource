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
      // download(this.resourceID)
      download(this.resourceID).then((response) => {
        const { code, data } = response.data
        if (code === 200) {
          window.location.href = data
        }
      })
    }
  }
}
</script>

<style scoped></style>
