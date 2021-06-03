<template>
<div class="lg-container">
  <nav-menu></nav-menu>
  <div class="main-container flex">
    <div class="resource-info flex-1">
      <div class="flex">
        <div class="basic-info flex-1">
          <h2 @click="addToCart(resource.id)">{{ resource['resourceName'] }}</h2>
          <div class="resource-name" v-if="resource['url'] !== undefined && resource['url'] !== null">{{ resource['url'].split('/').slice(-1)[0] }}</div>
        </div>
        <div class="operation flex flex-1">
          <div class="operation-button">
            <download-button :resourceID="Number(resourceID)"></download-button>
          </div>
          <div class="operation-button">
            <el-button class="full-width" size="medium" icon="el-icon-document-add">
              加入资源包
            </el-button>
          </div>
          <div class="operation-button">
            <el-button class="full-width" size="medium" icon="el-icon-star-off">
              收藏
            </el-button>
          </div>
        </div>
      </div>
      <!-- 资源展示组件 -->
      <div class="viewer">
        <resource-viewer :url="String(resource['viewUrl'])" :bInfo="{ aid: resource['aid'], bvid: resource['bvid'], cid: resource['cid'], page: 1 }"></resource-viewer>
      </div>
      <!-- 评分 -->
      <div class="rate">
        <el-rate
          v-model="resource['rate']"
          disabled
          show-score
          :colors="colors"
          score-template="{value}">
        </el-rate>
      </div>
      <div>
        <comment class="comment-container" :id="resource.id"></comment>
      </div>
      <!-- 相关资源 -->
      <div v-show="false" class="related-resource flex-1" v-if="relatedResources.length !== 0">
        <h2>相关资源</h2>
        <resource-list :resourceList="relatedResources"></resource-list>
      </div>
      <!-- ---------- -->
    </div>
    <div class="flex-1">
      <div class="graph">
        <k-g-chart :entities="entities.entities" ref="chart"></k-g-chart>
      </div>
      <!-- 推荐资源 -->
      <div class="recommend-container flex" v-if="recommendResources.length !== 0">
        <div class="flex-1">
          <h2>推荐资源</h2>
          <resource-list :resourceList="recommendResources"></resource-list>
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
import DownloadButton from '@/components/DownloadButton'
import ResourceList from '@/components/ResourceList'
import Comment from '@/components/Comment'
import { resourceInfo, related } from '@/api/resource'
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
    Comment
  },
  props: {
  },
  computed: {
    resourceID () {
      return this.$route.params.resourceID
    }
  },
  watch: {
    resourceID: {
      handler (resourceID) {
        // 获取资源信息
        resourceInfo(resourceID)
          .then(response => {
            if (response.data.code === 200) {
              this.resource = response.data.data
              console.log(response.data.data)
              // 先处理一下entityList
              let entityList = response.data.data['entityList']
              let keyword = ''
              entityList.forEach(entity => {
                keyword += entity + '#'
              })
              // 获取相关实体
              relatedEntity(keyword)
                .then(entityResponse => {
                  if (entityResponse.data.code === 200) {
                    console.log(entityResponse.data.data)
                    this.entities.entities = entityResponse.data.data
                  }
                })
            } else {
              console.log('无此资源')
            }
          })
        // 获取相关资源
        related(resourceID)
          .then(response => {
            // console.log(response)
            if (response.data.code === 200) {
              let resources = response.data.data
              resources.forEach(resource => {
                // console.log(resource)
                let suffix = resource.url.split('.').slice(-1)[0]
                resource.suffix = suffix
              })
              this.relatedResources = resources
            }
          })
        // 上传用户访问记录
        this.record()
        // 获取推荐资源
        this.getRecommendResources()
      },
      immediate: true
    }
  },
  data () {
    return {
      resource: {},
      entities: {
        entities: []
      },
      relatedResources: [],
      recommendResources: [],
      colors: ['#99A9BF', '#F7BA2A', '#FF9900']
    }
  },
  methods: {
    addToCart (resourceID) {
      authentication()
        .then(response => {
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
    record () {
      record({
        resourceID: Number(this.resourceID)
      })
    },
    // 获取推荐信息
    getRecommendResources () {
      authentication()
        .then(response => {
          if (response.data.code === 200) {
            recommendByResourceUser(Number(this.resourceID))
              .then(response => {
                if (response.data.code === 200) {
                  let resources = response.data.data
                  resources.forEach(resource => {
                    let suffix = resource.url.split('.').slice(-1)[0]
                    resource.suffix = suffix
                  })
                  this.recommendResources = resources
                }
              })
          }
        })
    }
  }
}
</script>

<style scoped>
.main-container {
  /*border-bottom: 1px solid #dcdfe6;*/
}

.operation {
  justify-content: flex-end;
}

.operation-button {
  margin-right: 1rem;
}

.graph {
  height: 100%;
}

.resource-info {
  position: relative;
  /*border-right: 1px solid #dcdfe6;*/
}

.resource-info >>> h2 {
  color: #222226;
}

.resource-name {
  color: #606266;
}

.viewer {
  margin-top: 1rem;
  padding-bottom: .4rem;
}

.related-resource,
.recommend-container {
  padding-top: 1rem;
  padding-bottom: 2rem;
}

.related-resource h2,
.recommend-container h2 {
  margin-bottom: 1rem;
}

.related-resource,
.recommend-container {
  border-top: 1px solid #dcdfe6;
}

.rate {
  margin-top: 1rem;
}

.comment-container {
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #e4e7ed;
  padding-bottom: 5rem;
}

/*.recommend-container {*/
/*  margin-left: -.5px;*/
/*  border-left: 1px solid #dcdfe6;*/
/*}*/

/*@media only screen and (max-width : 768px) {*/
/*  .main-container {*/
/*    flex-direction: column;*/
/*  }*/
/*}*/
</style>
