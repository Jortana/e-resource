<template>
  <div class="viewer">
    <div v-if="suffix === 'pdf'">
      <el-scrollbar :style="scrollerStyle">
        <pdf v-for="i in pdf.numPages" :key="i" :src="pdf.src" :page="i"></pdf>
      </el-scrollbar>
    </div>
    <div v-else-if="suffix === 'mp4'">
      <video controls="controls" class="videoDemo">
        <source :src="videoUrl">
      </video>
    </div>
    <iframe
      v-else-if="bInfo.bvid !== null"
      :src="bSrc"
      class="b-video"
      scrolling="no"
      border="0"
      frameborder="no"
      framespacing="0"
      allowfullscreen="true"
    ></iframe>
    <div v-else>
      该资源暂不支持预览
    </div>
  </div>
</template>

<script>
import pdf from 'vue-pdf'
// const baseUrl = 'http://202.102.89.244:8082'
const baseUrl = 'http://222.192.6.62:8082'
export default {
  name: 'ResourceViewer',
  components: {
    pdf
  },
  props: {
    id: Number,
    url: {
      type: String
    },
    bInfo: {
      type: Object
    }
  },
  data() {
    return {
      pdf: {
        src: '',
        numPages: undefined
      },
      comment: {
        rate: '',
        content: ''
      },
      videoUrl: ''
    }
  },
  computed: {
    suffix() {
      // return 'pdf'
      return this.url.split('.').slice(-1)[0]
    },
    viewUrl() {
      return baseUrl + this.url
    },
    scrollerStyle() {
      if (this.suffix === 'pdf') {
        return 'height: calc(100vh - 150px - 2rem);'
      }
      return ''
    },
    bSrc() {
      return `//player.bilibili.com/player.html?aid=${this.bInfo.aid}&bvid=${this.bInfo.bvid}&cid=${this.bInfo.cid}&page=${this.bInfo.page}`
    }
  },
  watch: {
    suffix: {
      handler(suffix) {
        if (suffix === 'pdf') {
          console.log(this.viewUrl)
          this.pdf.src = pdf.createLoadingTask(this.viewUrl)
          // console.log(this.pdf.src)
        } else if (suffix === 'mp4') {
          console.log(this.viewUrl)
          this.videoUrl = this.viewUrl
        }
      },
      immediate: true
    },
    url: {
      handler(url) {
        console.log(url)
        if (url !== 'undefined') {
          if (this.suffix === 'pdf') {
            this.pdf.src.promise.then((pdf) => {
              this.pdf.numPages = pdf.numPages
            })
          }
        }
      },
      immediate: true
    }
  },
  mounted() {},
  methods: {}
}
</script>

<style scoped>
.viewer >>> .el-scrollbar__wrap {
  overflow-x: hidden;
}

.b-video {
  width: 100%;
  height: 500px;
}
.videoDemo{
  width: 100%;
  height: 500px;
}
</style>
