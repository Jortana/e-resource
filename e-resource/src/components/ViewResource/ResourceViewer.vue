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
<!--  <iframe-->
<!--    class="b-video"-->
<!--    src="//player.bilibili.com/player.html?aid=714853065&bvid=BV1tX4y1G795&cid=317189977&page=1"-->
<!--    scrolling="no"-->
<!--    border="0"-->
<!--    frameborder="no"-->
<!--    framespacing="0"-->
<!--    allowfullscreen="true">-->
<!--  </iframe>-->
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
  <!-- 打分和评论 -->
  <div class="comment">
    <h2>评论</h2>
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
    url: {
      type: String,
      required: true
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

.comment {
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #e4e7ed;
}

.comment h2 {
  font-weight: 500;
}
</style>
