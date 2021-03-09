<template>
  <main>
    <search class="search" :searchContent.sync="searchContent"></search>
  </main>
</template>

<script>
import Search from '@/components/Search'
export default {
  name: 'HomePage',
  components: { Search },
  data () {
    return {
      searchContent: ''
    }
  },
  watch: {
    searchContent (content) {
      console.log(content)
      if (content !== '') {
        this.$router.push({
          path: '/search',
          query: {
            q: content
          }
        })
      }
    }
  }
}
</script>

<style scoped>
main {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.search {
  width: 50vw;
  height: 3rem;
  max-width: 580px;
  transform: translateY(-50%);
}

.main-search >>> .el-input__prefix,
.main-search >>> .el-input__suffix {
  top: 2px;
}
</style>
