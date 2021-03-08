<template>
<div class="menu">
  <div class="left">
    <div class="logo-container">
      <div class="logo"></div>
      <div class="title"><h1>智能学伴系统</h1></div>
    </div>
    <div class="search-container">
      <search class="search"></search>
    </div>
  </div>
  <div class="avatar">
    <el-avatar icon="el-icon-user-solid"></el-avatar>
  </div>
</div>
</template>

<script>
import Search from '@/components/Search'
export default {
  name: 'NavMenu',
  components: {
    Search
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

.logo-container {
  display: flex;
}

.logo {
  width: 50px;
  height: 50px;
  background-color: #aaa;
}

.title {
  height: 50px;
  line-height: 50px;
  font-size: 0.8rem;
  margin-left: 0.5rem;
  margin-right: 1rem;
}

.search {
  min-width: 700px;
  width: 100%;
}

.search-container,
.avatar {
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
  cursor: pointer;
}
</style>
