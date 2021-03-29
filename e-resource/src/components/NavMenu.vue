<template>
<div class="menu">
  <div class="left">
    <logo></logo>
    <div class="search-container" v-if="$route.fullPath !== '/'" @keyup.enter="search">
      <search class="search" :searchContent.sync="searchInfo.content" @search="search"></search>
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
  props: {
    searchInfo: Object
  },
  components: {
    Search,
    Logo,
    Avatar
  },
  data () {
    console.log(this.searchInfo)
    return {
    }
  },
  methods: {
    gotoLogin () {
      this.$router.push({
        path: '/login',
        query: { redirect: this.$route.fullPath }
      })
    },
    search () {
      if (this.searchInfo.content === '') {
        return
      }
      this.$router.push({
        path: '/search',
        query: {
          q: this.searchInfo.content,
          type: this.searchInfo.type
        }
      }).catch(() => {
        this.$router.go(0)
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
