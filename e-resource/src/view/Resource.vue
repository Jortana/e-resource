<template>
  <div>
    <nav-menu></nav-menu>
    <div class="lg-container">
      <div class="main-container flex">
        <div class="resource-info left-side flex-1 common-shadow">
          <div class="flex">
            <div class="basic-info">
              <h2 @click="addToCart(resource.id)">
                {{ resource['resourceName'] }}
              </h2>
              <div
                v-if="resource['url'] !== undefined && resource['url'] !== null"
                class="resource-name"
              >
                {{ resource['url'].split('/').slice(-1)[0] }}
              </div>
            </div>
            <!-- 评分 -->
            <div class="rate">
              <el-rate
                v-model="resource['rate']"
                :colors="colors"
                disabled
                show-score
                score-template="{value}"
              ></el-rate>
            </div>
            <div class="operation flex flex-1">
              <div class="operation-button">
                <download-button
                  :resourceID="Number(resourceID)"
                ></download-button>
              </div>
              <div class="operation-button">
                <add-to-package-button
                  :resourceType="'resource'"
                  :resourceID="String(resourceID)"
                ></add-to-package-button>
              </div>
              <!--          <div class="operation-button">-->
              <!--            <el-button class="full-width" size="medium" icon="el-icon-star-off">-->
              <!--              收藏-->
              <!--            </el-button>-->
              <!--          </div>-->
            </div>
          </div>
          <!-- 资源展示组件 -->
          <div class="viewer">
            <resource-viewer
              :url="String(resource['viewUrl'])"
              :bInfo="{
                aid: resource['aid'],
                bvid: resource['bvid'],
                cid: resource['cid'],
                page: 1
              }"
            ></resource-viewer>
          </div>

          <div>
            <comment :id="resource.id" class="comment-container"></comment>
          </div>
          <!-- 相关资源 -->
          <div
            v-show="false"
            v-if="relatedResources.length !== 0"
            class="related-resource flex-1"
          >
            <h2>相关资源</h2>
            <resource-list :resourceList="relatedResources"></resource-list>
          </div>
          <!-- ---------- -->
        </div>
        <div class="right-side">
          <div class="graph common-shadow">
            <k-g-chart ref="chart" :entities="entities.entities"></k-g-chart>
          </div>
          <!-- 推荐资源 -->
          <div
            v-if="recommendResources.length !== 0"
            class="recommend-container flex common-shadow"
          >
            <div class="flex-1">
              <h2>推荐资源</h2>
              <resource-list :resourceList="recommendResources"></resource-list>
            </div>
          </div>
          <!--不会改 created by 买大米种水稻 2022/04/16-->
          <div v-else class="recommend-container flex common-shadow">
            <div class="flex-1">
              <h2>推荐资源</h2>
              <p>登录后获取推荐资源</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavMenu from '@/components/NavMenu'
import KGChart from '@/components/Chart/KGChart'
import ResourceViewer from '@/components/ViewResource/ResourceViewer'
import ResourceLink from '@/components/ResourceLink'
import DownloadButton from '@/components/Buttons/DownloadButton'
import AddToPackageButton from '@/components/Buttons/AddToPackageButton'
import ResourceList from '@/components/ResourceList'
import Comment from '@/components/Comment'
import { related, resourceInfo } from '@/api/resource'
import { relatedEntity } from '@/api/entity'
import { authentication } from '@/api/auth'
import { record } from '@/api/record'
import { recommendByResourceUser } from '@/api/recommend'

export default {
  name: 'Resource',
  components: {
    NavMenu,
    KGChart,
    ResourceViewer,
    ResourceLink,
    DownloadButton,
    ResourceList,
    Comment,
    AddToPackageButton
  },
  props: {},
  data() {
    return {
      resource: {},
      entities: {
        entities: []
      },
      relatedResources: [],
      recommendResources: [],
      colors: ['#99A9BF', '#F7BA2A', '#FF9900'],
      title: ''
    }
  },
  computed: {
    resourceID() {
      return this.$route.params.resourceID
    }
  },
  watch: {
    resourceID: {
      handler(resourceID) {
        // 获取资源信息
        resourceInfo(resourceID).then((response) => {
          if (response.data.code === 200) {
            this.resource = response.data.data
            // 先处理一下entityList
            const entityList = response.data.data['entityList']
            let keyword = ''
            entityList.forEach((entity) => {
              keyword += entity + '#'
            })
            // 获取相关实体
            relatedEntity(keyword).then((entityResponse) => {
              if (entityResponse.data.code === 200) {
                this.entities.entities = entityResponse.data.data
              }
            })
            this.title = `${this.resource.resourceName} - 智慧学伴`
          } else {
            // console.log('无此资源')
          }
        })
        // 获取相关资源
        related(resourceID).then((response) => {
          // console.log(response)
          if (response.data.code === 200) {
            this.relatedResources = response.data.data
          }
        })
        // 上传用户访问记录
        this.record()
        // 获取推荐资源
        this.getRecommendResources()
      },
      immediate: true
    },
    title: {
      handler(title) {
        document.title = title
      },
      immediate: true
    }
  },
  methods: {
    addToCart(resourceID) {
      authentication().then((response) => {
        if (response.data.code === 200) {
          this.$store.commit('addToCart', resourceID)
        } else {
          this.$message({
            message: '请先登录',
            type: 'warning',
            duration: 1500
          })
          this.$router.push({
            path: '/login',
            query: { redirect: this.$route.fullPath }
          })
        }
      })
    },
    record() {
      record({
        resourceID: Number(this.resourceID)
      })
    },
    // 获取推荐信息
    getRecommendResources() {
      authentication().then((response) => {
        if (response.data.code === 200) {
          recommendByResourceUser(Number(this.resourceID)).then((response) => {
            if (response.data.code === 200) {
              this.recommendResources = response.data.data
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.lg-container {
  background-color: #f7f5f4;
}

.right-side {
  margin-left: 1rem;
  margin-top: 1rem;
  width: 400px;
}

.operation {
  justify-content: flex-end;
}

.operation-button {
  margin-right: 1rem;
}

.graph {
  height: 500px;
  background-color: #fff;
  /*height: 100%;*/
}

.resource-info {
  position: relative;
  background-color: #fff;
  margin-top: 1rem;
  padding: 1.5rem;
  /*border-right: 1px solid #dcdfe6;*/
}

.resource-info >>> h2 {
  color: #222226;
}

.basic-info {
  margin-right: 1rem;
}

.resource-name {
  color: #606266;
}

.viewer {
  margin-top: 1rem;
  padding-bottom: 0.4rem;
}

.related-resource,
.recommend-container {
  padding: 1rem;
  background-color: #fff;
  margin-top: 1rem;
}

.related-resource h2,
.recommend-container h2 {
  margin-bottom: 1rem;
}

.rate {
  /*border-top: 1px solid #e4e7ed;*/
  padding-top: 1rem;
  margin-top: 1rem;
}

.comment-container {
  /*margin-top: 1rem;*/
  padding-top: 1rem;
  padding-bottom: 5rem;
}

/*@media only screen and (max-width : 768px) {*/
/*  .main-container {*/
/*    flex-direction: column;*/
/*  }*/
/*}*/
</style>
