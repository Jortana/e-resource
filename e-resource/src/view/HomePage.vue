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
            <resource-list :resourceList="hotResources" :browseTime="true"></resource-list>
          </div>
        </div>
        <div class="card flex-1">
          <h2>最新资源</h2>
          <div class="list-container">
            <resource-list :resourceList="newResources" :browseTime="true"></resource-list>
          </div>
        </div>
        <div class="card flex-1" v-if="isLogin === true">
          <h2>推荐资源</h2>
          <div class="list-container">
            <resource-list :resourceList="recommendResources" :browseTime="true"></resource-list>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import Search from '@/components/Search'
import NavMenu from '@/components/NavMenu'
import ResourceList from '@/components/ResourceList'
import {authentication} from '@/api/auth'
import {hotResource, newResource, userRecommendResource} from '@/api/recommend'

export default {
  name: 'HomePage',
  components: { NavMenu, Search, ResourceList },
  data () {
    return {
      searchContent: '',
      isLogin: false,
      hotResources: [],
      newResources: [],
      recommendResources: []
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
    }
  }
}
</script>

<style scoped>
.main {
  background-image: url('~@/assets/background.jpg');
  background-size:cover;
  height: 100vh;
  background-repeat:no-repeat;
}

main {
  height: calc(100vh - 70px);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.search {
  width: 50vw;
  height: 3rem;
  max-width: 580px;
  transform: translateY(20%);
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
  transform: translateY(15%);
}

.wide {
  width: 1100px;
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
</style>
