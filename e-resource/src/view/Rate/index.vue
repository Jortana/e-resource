<template>
  <div>
    <nav-menu></nav-menu>
    <div class="main-container">
      <h1>人气排行</h1>
      <div class="rate-container">
        <div class="card">
          <h2>热门资源</h2>
          <div class="list-container">
            <resource-list
              :resourceList="hotResources.slice(0, 13)"
              :browseTime="true"
              :isHidden="true"
            ></resource-list>
          </div>
        </div>
        <div class="card">
          <h2>最新资源</h2>
          <div class="list-container">
            <resource-list
              :resourceList="newResources.slice(0, 13)"
              :browseTime="true"
              :isHidden="true"
            ></resource-list>
          </div>
        </div>
        <div class="card">
          <h2>最多下载</h2>
          <div class="list-container">
            <resource-list
              :resourceList="[]"
              :browseTime="true"
              :isHidden="true"
            ></resource-list>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavMenu from '@/components/NavMenu'
import ResourceList from '@/components/ResourceList'
import {
  hotResourceMore,
  newResourceMore,
  userRecommendResourceMore
} from '@/api/recommend'

export default {
  name: 'Rate',
  components: {
    NavMenu,
    ResourceList
  },
  data() {
    return {
      hotResources: [],
      newResources: [],
      userRecommendResources: []
    }
  },
  created() {
    this.initData()
  },
  methods: {
    initData() {
      hotResourceMore().then((response) => {
        const { code, data } = response.data
        if (code === 200) {
          this.hotResources = data
        } else {
          this.$message.warning('服务器错误，请稍后刷新重试')
        }
      })
      newResourceMore().then((response) => {
        const { code, data } = response.data
        if (code === 200) {
          this.newResources = data
        } else {
          this.$message.warning('服务器错误，请稍后刷新重试')
        }
      })
      userRecommendResourceMore().then((response) => {
        console.log(response)
      })
    }
  }
}
</script>

<style scoped>
.main-container {
  min-height: calc(100vh - 202px);
  padding: 2rem 0;
}

.main-container h1 {
  padding-left: 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid #dcdfe6;
}

.rate-container {
  display: flex;
  padding: 1.5rem 1rem 0;
}

.card {
  background-color: #fdfbfb;
  box-shadow: 1px 3px 6px rgb(122 122 122 / 0.3);
  overflow: hidden;
  height: 600px;
  flex: 1;
  /*text-overflow: ellipsis;*/
  /*white-space: nowrap;*/
}

.card {
  margin-right: 2rem;
}

.card:last-child {
  margin-right: 0;
}

.card h2 {
  font-size: 1.5rem;
  padding: 0.8rem;
  border-bottom: solid 1px #a5a3a3;
}

.list-container {
  padding: 1rem;
}
</style>
