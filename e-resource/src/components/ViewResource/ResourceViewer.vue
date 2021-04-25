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
      return 'pdf'
      // return this.url.split('.').slice(-1)[0]
    }
  },
  watch: {
    suffix: {
      handler (suffix) {
        if (suffix === 'pdf') {
          this.pdf.src = pdf.createLoadingTask('http://127.0.0.1:9000/e-resource/api/file/resource/03 化学/2教案/doc/09 结构式文摘写作要求.pdf')
          console.log(this.pdf.src)
        }
      },
      immediate: true
    }
  },
  mounted () {
    this.pdf.src.promise.then(pdf => {
      this.pdf.numPages = pdf.numPages
    })
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
