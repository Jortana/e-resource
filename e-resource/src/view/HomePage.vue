<template>
  <div>
    <nav-menu class="menu"></nav-menu>
    <main>
      <div @keyup.enter="search">
        <search class="search" :searchContent.sync="searchContent" @search="search"></search>
      </div>
    </main>
  </div>
</template>

<script>
import Search from '@/components/Search'
import NavMenu from '@/components/NavMenu'
export default {
  name: 'HomePage',
  components: { NavMenu, Search },
  data () {
    return {
      searchContent: ''
    }
  },
  watch: {
  },
  methods: {
    search () {
      let content = this.searchContent
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
  height: calc(100vh - 70px);
  display: flex;
  justify-content: center;
  align-items: center;
}

.search {
  width: 50vw;
  height: 3rem;
  max-width: 580px;
  transform: translateY(-80%);
}

.main-search >>> .el-input__prefix,
.main-search >>> .el-input__suffix {
  top: 2px;
}
</style>
