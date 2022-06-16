<template>
  <div class="main">
    <nav-menu></nav-menu>
    <main>
      <div class="row">
        <div class="card">
          <h2>热门资源</h2>
          <div class="list-container">
            <resource-list
              :resourceList="hotResources"
              :browseTime="true"
              :isHidden="true"
            ></resource-list>
          </div>
        </div>
        <div class="carousel">
          <el-carousel :interval="5000" trigger="click" height="400px">
            <el-carousel-item v-for="item in carouselData" :key="item.id">
              <div class="carousel-container">
                <el-image
                  :src="'http://202.102.89.244:8082' + item.cover"
                  fit="cover"
                  @click="viewResource(item.id)"
                ></el-image>
              </div>
            </el-carousel-item>
            <!-- 隐藏的a元素，用来在新窗口打开资源页面 -->
            <a
              v-show="false"
              ref="resourceTarget"
              class="resource-target"
              href=""
              target="_blank"
            ></a>
          </el-carousel>
        </div>
        <div class="card">
          <h2>最新资源</h2>
          <div class="list-container">
            <resource-list
              :resourceList="newResources"
              :browseTime="true"
              :isHidden="true"
            ></resource-list>
          </div>
        </div>
      </div>
      <div v-if="isLogin === true" class="row">
        <div class="kg-container">
          <h2>知识图谱</h2>
          <k-g-card></k-g-card>
        </div>
        <div class="card">
          <h2>推荐资源</h2>
          <div class="list-container">
            <resource-list
              :resourceList="recommendResources"
              :browseTime="true"
              :isHidden="true"
            ></resource-list>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="boutique-container">
          <h2>精选资源</h2>
          <div class="boutique-resource">
            <div
              v-for="resource in boutiqueResources"
              :key="resource.id"
              class="resource-info"
            >
              <el-image
                :src="'http://202.102.89.244:8082' + resource.cover"
                fit="cover"
                class="thumbnail"
                lazy
                @click="viewResource(resource.id)"
              ></el-image>
              <router-link
                :to="`/resource/${resource.id}`"
                target="_blank"
                class="resource-name"
              >
                {{ resource.resourceName }}
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- 导航目录 -->
    <div v-show="false" class="menu-container">
      <div class="white-container">
        <!-- 每一个目录 -->
        <div v-for="(menuObj, index) in menus" :key="index" class="flex">
          <div class="menu-condition">{{ menuObj.condition }}</div>
          <div>
            <div
              v-for="classification in menuObj['classification']"
              :key="classification.ID"
              class="menu-row"
            >
              <div class="menu-name">【{{ classification.name }}】</div>
              <div class="links-container">
                <!-- 每一个可选项 -->
                <div v-for="menu in classification.menu" :key="menu.menuID">
                  <el-link :underline="false" class="link">
                    {{ menu.menuName }}
                  </el-link>
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
import KGCard from '@/components/Chart/KGCard'
import { authentication } from '@/api/auth'
import { menus } from '@/api/menu'
import { carousel } from '@/api/recommend'
import {
  hotResource,
  newResource,
  userRecommendResource,
  boutiqueResource
} from '@/api/recommend'

export default {
  name: 'HomePage',
  components: { NavMenu, Search, ResourceList, KGCard },
  data() {
    return {
      searchContent: '',
      isLogin: false,
      hotResources: [],
      newResources: [],
      recommendResources: [],
      menus: [],
      carouselData: [],
      boutiqueResources: []
    }
  },
  watch: {},
  created() {
    authentication().then((response) => {
      if (response.data.code === 200) {
        this.isLogin = true
      }
      this.getRecommendResource()
    })
  },
  mounted() {
    this.getMenus()
    this.getCarousel()
    this.getBoutique()
  },
  methods: {
    search() {
      const content = this.searchContent
      if (content !== '') {
        this.$router.push({
          path: '/search',
          query: {
            q: content
          }
        })
      }
    },
    getRecommendResource() {
      hotResource().then((response) => {
        if (response.data.code === 200) {
          this.hotResources = response.data.data
        }
      })
      newResource().then((response) => {
        if (response.data.code === 200) {
          this.newResources = response.data.data
        }
      })
      if (this.isLogin === true) {
        userRecommendResource().then((response) => {
          this.recommendResources = response.data.data
        })
      }
    },
    getMenus() {
      menus().then((response) => {
        const {
          data: { code, data }
        } = response
        if (code === 200) {
          this.menus = data
        }
      })
    },
    getCarousel() {
      carousel().then((response) => {
        const { code, data } = response.data
        if (code === 200) {
          this.carouselData = data
        }
      })
    },
    viewResource(resourceID) {
      const target = this.$refs.resourceTarget
      target.setAttribute(
        'href',
        `${window.location.origin}/e-resource/#/resource/${resourceID}`
      )
      target.click()
    },
    getBoutique() {
      boutiqueResource().then((response) => {
        const { code, data } = response.data
        if (code === 200) {
          this.boutiqueResources = data
        } else {
          this.$message.warning('服务器错误，请稍后刷新重试')
        }
      })
    }
  }
}
</script>

<style scoped>
.main {
  /*background-image: url('~@/assets/background.jpg');*/
  /*background-size:cover;*/
  background-color: #fffeff;
  min-height: calc(100vh - 80px);
  /*background-repeat:no-repeat;*/
}

main {
  width: 1200px;
  margin: 0 auto;
}

.row {
  display: flex;
  margin-top: 1.5rem;
  justify-content: space-between;
  margin-bottom: 1.5rem;
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

.card,
.kg-container {
  background-color: #fdfbfb;
  box-shadow: 1px 3px 6px rgb(122 122 122 / 0.3);
  overflow: hidden;
  height: 400px;
}

.card {
  /* background: linear-gradient(
    to left top,
    rgba(255, 255, 255, 1),
    rgba(255, 255, 255, 0.8)
  ); */
  background-color: #fdfbfb;
  box-shadow: 1px 3px 6px rgb(122 122 122 / 0.3);
  overflow: hidden;
  height: 400px;
  width: 24%;
  /*text-overflow: ellipsis;*/
  /*white-space: nowrap;*/
}

.boutique-container {
  background-color: #fdfbfb;
  box-shadow: 1px 3px 6px rgb(122 122 122 / 0.3);
  overflow: hidden;
  width: 100%;
}

.kg-container {
  width: 75%;
}

.card h2,
.card > div,
.kg-container h2,
.kg-container > div,
.boutique-container h2 {
  padding-left: 1.2rem;
  padding-right: 1.2rem;
}

.card h2,
.kg-container h2,
.boutique-container h2 {
  font-size: 1.3rem;
  margin-top: 0.8rem;
  padding-bottom: 0.5rem;
  border-bottom: solid 1px #a5a3a3;
}

.carousel {
  width: 50%;
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

.menu-condition {
  font-size: 1.2rem;
  width: 100px;
}

.menu-name {
  color: #409eff;
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
  border-radius: 0.6rem;
  box-shadow: 6px 6px 20px rgba(122, 122, 122, 0.1);
  overflow: hidden;
}

.menu-row {
  display: flex;
  margin-bottom: 0.5rem;
}

.link {
  font-size: 1rem;
  margin-right: 0.7rem;
  margin-bottom: 0.5rem;
}

.tip {
  font-size: 0.8rem;
  color: #909399;
  margin-top: -0.5rem;
}

/* 轮播图测试 */
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}

.carousel-container {
  height: 100%;
  cursor: pointer;
}

.carousel-container div {
  height: 100%;
}

/* 精选资源的样式 */
.boutique-resource {
  display: flex;
  flex-wrap: wrap;
  padding: 1rem 1rem 0;
  justify-content: space-between;
}

.boutique-resource .resource-info {
  display: flex;
  flex-direction: column;
  width: 19%;
  margin-bottom: 1.5rem;
}

.boutique-resource .resource-info .thumbnail {
  height: 120px;
  box-shadow: 1px 3px 6px rgb(122 122 122 / 0.2);
  cursor: pointer;
}

.boutique-resource .resource-info .resource-name {
  margin-top: 0.5rem;
  text-decoration: none;
  color: #606266;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.boutique-resource .resource-info .resource-name:hover {
  text-decoration: underline;
  text-underline-offset: 2px;
}
</style>
