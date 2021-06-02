<template>
<div class="viewer">
  <div v-if="suffix === 'pdf'">
    <el-scrollbar :style="scrollerStyle">
      <pdf
        v-for="i in pdf.numPages"
        :key="i"
        :src="pdf.src"
        :page="i"
      ></pdf>
    </el-scrollbar>
  </div>
  <iframe
    v-else-if="bInfo.bvid !== null"
    class="b-video"
    :src=bSrc
    scrolling="no"
    border="0"
    frameborder="no"
    framespacing="0"
    allowfullscreen="true">
  </iframe>
<!--  <div v-else>-->
<!--    该资源暂不支持预览，请下载后进行学习-->
<!--  </div>-->
  <!-- 评分 -->
  <div class="rate">
    <el-rate
      v-model="rate"
      disabled
      show-score
      text-color="#ff9900"
      score-template="{value}">
    </el-rate>
  </div>

</div>
</template>

<script>
import pdf from 'vue-pdf'
const baseUrl = 'http://223.2.50.241:8082'
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
  computed: {
    suffix () {
      // return 'pdf'
      return this.url.split('.').slice(-1)[0]
    },
    viewUrl () {
      return baseUrl + this.url
    },
    scrollerStyle () {
      if (this.suffix === 'pdf') {
        return 'height: calc(100vh - 150px - 2rem);'
      }
      return ''
    },
    bSrc () {
      return `//player.bilibili.com/player.html?aid=${this.bInfo.aid}&bvid=${this.bInfo.bvid}&cid=${this.bInfo.cid}&page=${this.bInfo.page}`
    }
  },
  watch: {
    suffix: {
      handler (suffix) {
        if (suffix === 'pdf') {
          console.log(this.viewUrl)
          this.pdf.src = pdf.createLoadingTask(this.viewUrl)
          // console.log(this.pdf.src)
        }
      },
      immediate: true
    },
    url: {
      handler (url) {
        console.log(url)
        if (url !== 'undefined') {
          if (this.suffix === 'pdf') {
            this.pdf.src.promise.then(pdf => {
              this.pdf.numPages = pdf.numPages
            })
          }
        }
      },
      immediate: true
    }
  },
  mounted () {
  },
  data () {
    return {
      pdf: {
        src: '',
        numPages: undefined
      },
      comment: {
        rate: '',
        content: ''
      },
      rate: 4.5
    }
  },
  methods: {
  }
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

.rate {
  margin-top: 1rem;
}
</style>
