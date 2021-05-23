<template>
<div class="menu">
  <div class="left">
    <logo></logo>
    <div class="search-container" v-if="$route.fullPath !== '/'" @keyup.enter="search">
      <search class="search" :searchContent.sync="searchInfo.content" @search="search"></search>
    </div>
  </div>
  <div class="user-info" v-if="this.$store.state.user">
    <div class="resource-package">
      <el-popover
        placement="bottom"
        trigger="click">
        <div>资源包是空的</div>
        <el-button slot="reference">我的资源包</el-button>
      </el-popover>
    </div>
    <avatar class="avatar"></avatar>
  </div>
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
    searchInfo: {
      type: Object,
      default () {
        return {
          content: ''
        }
      }
    }
  },
  components: {
    Search,
    Logo,
    Avatar
  },
  computed: {
    searchContent () {
      return this.searchInfo.content
    }
  },
  data () {
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
  padding-top: 1rem;
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

.user-info {
  display: flex;
  align-items: center;
}

.resource-package {
  margin-right: 1rem;
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
