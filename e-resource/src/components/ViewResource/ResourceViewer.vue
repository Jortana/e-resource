<template>
<div class="viewer">
  <el-scrollbar :style="scrollerStyle">
    <pdf
      v-for="i in pdf.numPages"
      :key="i"
      :src="pdf.src"
      :page="i"
    ></pdf>
  </el-scrollbar>
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
      }
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
</style>
