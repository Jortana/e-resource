<template>
<div class="menu">
  <div class="left">
    <logo></logo>
    <div class="search-container">
      <search class="search"></search>
    </div>
  </div>
  <avatar v-if="this.$store.state.user" class="avatar"></avatar>
  <el-button v-else class="avatar login-btn" @click="gotoLogin">登 录</el-button>
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
  },
  methods: {
    gotoLogin () {
      this.$router.push({
        path: '/login',
        query: { redirect: this.$route.fullPath }
      })
    }
  }
}
</script>

<style scoped>
.menu {
  height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.login-btn {
  height: 0;
  line-height: 0;
  padding: 18px 20px;
}
</style>
