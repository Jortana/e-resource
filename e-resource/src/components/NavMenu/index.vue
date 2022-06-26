<template>
  <div>
    <div class="top-menu">
      <logo></logo>
      <div class="search-container" @keyup.enter="search">
        <search
          :searchContent.sync="searchInfo.content"
          class="search"
          @search="search"
        ></search>
      </div>
      <div v-if="this.$store.state.user" class="user-info">
        <avatar class="avatar"></avatar>
      </div>
      <el-button v-else class="avatar login-btn" @click="gotoLogin">
        登 录
      </el-button>
    </div>
    <div class="menu-container">
      <div class="nav-menu">
        <a
          class="menu-item"
          href=""
          @mouseover="catogoryVisible = true"
          @mouseleave="catogoryVisible = false"
        >
          <svg
            t="1652337674709"
            class="icon-fold"
            viewBox="0 0 1024 1024"
            version="1.1"
            xmlns="http://www.w3.org/2000/svg"
            p-id="2352"
            width="18"
            height="18"
          >
            <path
              d="M126 192.5h772M898 227.5H126c-19.3 0-35-15.7-35-35s15.7-35 35-35h772c19.3 0 35 15.7 35 35s-15.7 35-35 35zM126 511.5h772M898 546.5H126c-19.3 0-35-15.7-35-35s15.7-35 35-35h772c19.3 0 35 15.7 35 35s-15.7 35-35 35z"
              p-id="2353"
              fill="#ffffff"
            ></path>
            <path
              d="M126 830.5h772M898 865.5H126c-19.3 0-35-15.7-35-35s15.7-35 35-35h772c19.3 0 35 15.7 35 35s-15.7 35-35 35z"
              p-id="2354"
              fill="#ffffff"
            ></path>
          </svg>
          学科分类
        </a>
        <router-link :to="'/'" class="menu-item">首页</router-link>
        <router-link :to="'/rate'" class="menu-item">排行</router-link>
        <router-link :to="'/search?q=小学'" class="menu-item">
          小学资源库
        </router-link>
        <router-link :to="'/search?q=初中'" class="menu-item">
          初中资源库
        </router-link>
        <router-link :to="'/search?q=高中'" class="menu-item">
          高中资源库
        </router-link>
      </div>
      <div class="catogory-wrap">
        <div
          v-show="catogoryVisible"
          class="catogory-container"
          @mouseover="catogoryVisible = true"
          @mouseleave="catogoryVisible = false"
        >
          <div v-for="(menuObj, index) in menus" :key="index">
            <div
              v-for="classification in menuObj['classification']"
              :key="classification.ID"
              class="menu-row"
            >
              <div class="menu-name">【{{ classification.name }}】</div>
              <div class="link-container">
                <!-- 每一个可选项 -->
                <div v-for="menu in classification.menu" :key="menu.menuID">
                  <a class="link" href="">{{ menu.menuName }}</a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Search from '@/components/Search'
import Logo from '@/components/NavMenu/Logo'
import Avatar from '@/components/NavMenu/Avatar'
import { authentication } from '@/api/auth'
import { menus } from '@/api/menu'
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
    return {
      menus: [],
      catogoryVisible: false
    }
  },
  computed: {
    searchContent() {
      return this.searchInfo.content
    }
  },
  created() {
    this.authLogin()
    this.getMenus()
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
      // 清空知识图谱记录
      this.$store.commit('clearHistorty')
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
    },
    getMenus() {
      if (this.$store.state.menus.length === 0) {
        menus().then((response) => {
          const {
            data: { code, data }
          } = response
          if (code === 200) {
            this.menus = data
            this.$store.commit('initMenus', data)
          }
        })
      } else {
        this.menus = this.$store.state.menus
      }
    }
  }
}
</script>

<style scoped>
.top-menu {
  width: 1200px;
  height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 1rem auto;
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
  display: flex;
  align-self: center;
}

.login-btn {
  height: 0;
  line-height: 0;
  padding: 18px 20px;
  display: flex;
  align-self: center;
}

.menu-container {
  width: 100%;
  background-color: #3f3c40;
}

.nav-menu {
  display: flex;
  width: 1200px;
  height: 2.5rem;
  margin: 0 auto;
  align-items: center;
  position: relative;
}

.nav-menu a {
  display: inline-flex;
  padding: 0 1rem;
  width: 240px;
  height: 100%;
  align-items: center;
  color: #fff;
  text-decoration: none;
}

.nav-menu a:hover {
  background-color: #323033;
}

.icon-fold {
  margin-right: 0.5rem;
}

/* 学科分类下拉框 */
.catogory-wrap {
  position: relative;
  width: 1200px;
  margin: 0 auto;
}

.catogory-container {
  position: absolute;
  width: 1200px;
  background-color: #fdfbfb;
  z-index: 10;
  box-shadow: 6px 6px 10px rgba(39, 39, 39, 0.3);
  padding: 1rem 1.5rem;
}

.menu-row {
  display: flex;
  padding: 1.2rem 0;
  border-top: 1px solid #dcdfe6;
}

.menu-row:first-child {
  border: none;
}

.menu-name {
  display: inline-flex;
  justify-content: center;
  margin-right: 0.5rem;
}

.link-container {
  display: flex;
  flex: 1;
  justify-content: space-between;
}

.link {
  text-decoration: none;
  color: #606266;
}

.link:hover {
  color: #52a8ff;
}
/* ************* */
</style>
