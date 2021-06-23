<template>
  <div class="menu">
    <div class="left">
      <logo></logo>
      <div
        v-if="$route.fullPath !== '/'"
        class="search-container"
        @keyup.enter="search"
      >
        <search
          :searchContent.sync="searchInfo.content"
          class="search"
          @search="search"
        ></search>
      </div>
    </div>
    <div v-if="this.$store.state.user" class="user-info">
      <avatar class="avatar"></avatar>
    </div>
    <el-button v-else class="avatar login-btn" @click="gotoLogin">
      登 录
    </el-button>
  </div>
</template>

<script>
import Search from '@/components/Search'
import Logo from '@/components/NavMenu/Logo'
import Avatar from '@/components/NavMenu/Avatar'
import { authentication } from '@/api/auth'
export default {
  name: 'NavMenu',
  components: {
    Search,
    Logo,
    Avatar
  },
  props: {
    searchInfo: {
      type: Object,
      default() {
        return {
          content: ''
        }
      }
    }
  },
  data() {
    return {}
  },
  computed: {
    searchContent() {
      return this.searchInfo.content
    }
  },
  created() {
    this.authLogin()
  },
  methods: {
    gotoLogin() {
      this.$router.push({
        path: '/login',
        query: { redirect: this.$route.fullPath }
      })
    },
    search() {
      if (this.searchInfo.content === '') {
        return
      }
      this.$router
        .push({
          path: '/search',
          query: {
            q: this.searchInfo.content,
            type: this.searchInfo.type
          }
        })
        .catch(() => {
          this.$router.go(0)
        })
    },
    authLogin() {
      authentication().then((response) => {
        const {
          data: { code }
        } = response
        const { user } = this.$store.state
        if (code === 400 && user !== '') {
          // 服务器端未登录而且 state 中存了用户信息
          // 这里可以在 state 中存储用户密码，遇到这种情况直接发送一个登录请求
          this.$store.commit('logout')
        }
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
  max-width: 50%;
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
