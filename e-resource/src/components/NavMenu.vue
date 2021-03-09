<template>
<div class="menu">
  <div class="left">
    <logo></logo>
    <div class="search-container">
      <search class="search"></search>
    </div>
  </div>
  <avatar class="avatar"></avatar>
</div>
</template>

<script>
import Search from '@/components/Search'
import Logo from '@/components/NavMenu/Logo'
import Avatar from '@/components/NavMenu/Avatar'
export default {
  name: 'NavMenu',
  components: {
    Search,
    Logo,
    Avatar
  },
  data () {
    return {
      search: {
        type: '0',
        searchContent: ''
      }
    }
  }
}
</script>

<style scoped>
.menu {
  height: 50px;
  display: flex;
  justify-content: space-between;
  margin-top: 1rem;
}

.left {
  display: flex;
}

.search {
  min-width: 700px;
  width: 100%;
  height: 80% !important;
}

.search-container {
  display: flex;
  align-items: center;
}

.search-container {
  width: 50%;
}

.search-container >>> .el-select {
  min-width: 80px;
}

.avatar {
  margin-right: 1rem;
}
</style>
