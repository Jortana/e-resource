<template>
  <div class="main">
    <nav-menu class="menu"></nav-menu>
    <main>
      <div @keyup.enter="search">
        <search class="search" :searchContent.sync="searchContent" @search="search"></search>
      </div>
      <div class="recommend-container flex" :class="isLogin === true ? 'wide' : 'thin'">
        <div class="card flex-1">
          <h2>热门资源</h2>
          <div class="list-container">
            <resource-list :resourceList="hotResources" :browseTime="true" :isHidden="true"></resource-list>
          </div>
        </div>
        <div class="card flex-1">
          <h2>最新资源</h2>
          <div class="list-container">
            <resource-list :resourceList="newResources" :browseTime="true" :isHidden="true"></resource-list>
          </div>
        </div>
        <div class="card flex-1" v-if="isLogin === true">
          <h2>推荐资源</h2>
          <div class="list-container">
            <resource-list :resourceList="recommendResources" :browseTime="true" :isHidden="true"></resource-list>
          </div>
        </div>
      </div>
    </main>
    <!-- 导航目录 -->
    <div class="menu-container">
      <div class="white-container">
        <!-- 每一个目录 -->
        <div
          class="flex"
          v-for="(menuObj, index) in menus"
          :key="index"
        >
          <div class="menu-condition">{{ menuObj.condition }}</div>
          <div>
            <div
              class="menu-row"
              v-for="(classification, index) in menuObj['classification']"
              :key="index"
            >
              <div class="menu-name">【{{classification['periodName']}}】</div>
              <div class="links-container">
              <!-- 每一个可选项 -->
                <div
                  v-for="menu in classification['subject']"
                  :key="menu['periodID']"
                >
                  <el-link class="link" :underline="false">{{menu['subjectName']}}</el-link>
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
import NavMenu from '@/components/NavMenu'
import ResourceList from '@/components/ResourceList'
import { authentication } from '@/api/auth'
import { menus } from '@/api/menu'
import { hotResource, newResource, userRecommendResource } from '@/api/recommend'

export default {
  name: 'HomePage',
  components: { NavMenu, Search, ResourceList },
  data () {
    return {
      searchContent: '',
      isLogin: false,
      hotResources: [],
      newResources: [],
      recommendResources: [],
      menus: []
    }
  },
  created () {
    authentication()
      .then(response => {
        if (response.data.code === 200) {
          this.isLogin = true
        }
        this.getRecommendResource()
      })
  },
  mounted () {
    this.getMenus()
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
    },
    getRecommendResource () {
      hotResource()
        .then(response => {
          if (response.data.code === 200) {
            this.hotResources = response.data.data
          }
        })
      newResource()
        .then(response => {
          if (response.data.code === 200) {
            this.newResources = response.data.data
          }
        })
      if (this.isLogin === true) {
        userRecommendResource()
          .then(response => {
            this.recommendResources = response.data.data
          })
      }
    },
    getMenus () {
      menus()
        .then(response => {
          const { data: { code, data } } = response
          if (code === 200) {
            this.menus = data
          }
        })
    }
  }
}
</script>

<style scoped>
.main {
  /*background-image: url('~@/assets/background.jpg');*/
  background: linear-gradient(180deg, rgb(98, 207, 204) 30%, rgb(219, 229, 198) 70%);
  /*background-size:cover;*/
  min-height: 100vh;
  padding-bottom: 5rem;
  /*background-repeat:no-repeat;*/
}

main {
  height: calc(100vh - 50px);
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  align-items: center;
}

.search {
  width: 50vw;
  height: 3rem;
  max-width: 580px;
  /*transform: translateY(20%);*/
}

.main-search >>> .el-input__prefix,
.main-search >>> .el-input__suffix {
  top: 2px;
}

.recommend-container {
  min-height: 400px;
  padding: 2rem;
  background: linear-gradient(
    to right bottom,
    rgba(255, 255, 255, 0.7),
    rgba(255, 255, 255, 0.3)
  );
  border-radius: 1rem;
  backdrop-filter: blur(2rem);
  /*transform: translateY(15%);*/
}

.wide {
  width: 1200px;
}

.thin {
  width: 800px;
}

.card {
  margin-right: 1.5rem;
  padding: 1.5rem 1.2rem;
  background: linear-gradient(
    to left top,
    rgba(255, 255, 255, 1),
    rgba(255, 255, 255, 0.8)
  );
  border-radius: .6rem;
  box-shadow: 6px 6px 20px rgba(122, 122, 122, 0.1);
  overflow: hidden;
  /*text-overflow: ellipsis;*/
  /*white-space: nowrap;*/
}

.card:last-child {
  margin-right: 0;
}

.card h2 {
  color: #313437;
  margin-bottom: 1rem;
}

.list-container >>> .resource-name {
  color: #606266;
}

/*导航栏*/
.menu-container {
  /*display: flex;*/
  /*justify-content: center;*/
  width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  background: linear-gradient(
    to right bottom,
    rgba(255, 255, 255, 0.7),
    rgba(255, 255, 255, 0.3)
  );
  border-radius: 1rem;
  backdrop-filter: blur(2rem);
}

.menu-condition {
  font-size: 1.2rem;
  width: 50px;
}

.menu-name {
  color: #409EFF;
  width: 70px;
}

.menu-condition,
.menu-name {
  margin-right: 1rem;
}

/*每一个可选项外面的壳*/
.links-container {
  flex: 1;
  display: flex;
  flex-wrap: wrap;
}

.white-container {
  padding: 1.5rem 1.2rem;
  background: linear-gradient(
    to left top,
    rgba(255, 255, 255, 1),
    rgba(255, 255, 255, 0.8)
  );
  border-radius: .6rem;
  box-shadow: 6px 6px 20px rgba(122, 122, 122, 0.1);
  overflow: hidden;
}

.menu-row {
  display: flex;
  margin-bottom: .5rem;
}

.link {
  font-size: 1rem;
  margin-right: .7rem;
  margin-bottom: .5rem;
}
</style>
