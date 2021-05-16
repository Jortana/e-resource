<template>
<div class="viewer">
  <el-scrollbar style="height: 80vh">
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
    }
  },
  watch: {
    suffix: {
      handler (suffix) {
        if (suffix === 'pdf') {
          this.pdf.src = pdf.createLoadingTask(this.viewUrl)
          // console.log(this.pdf.src)
        }
      },
      immediate: true
    },
    url: {
      handler (url) {
        if (url !== 'undefined') {
          this.pdf.src.promise.then(pdf => {
            this.pdf.numPages = pdf.numPages
          })
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
