<template>
<div class="lg-container">
  <nav-menu></nav-menu>
  <div class="main-container flex">
    <div class="resource-info flex-1">
      <div class="flex">
        <div class="basic-info flex-1">
          <h2 @click="addToCart(resource.id)">{{ resource['resourceName'] }}</h2>
          <div class="resource-name" v-if="resource['url'] !== undefined">{{ resource['url'].split('/').slice(-1)[0] }}</div>
        </div>
        <div class="operation flex flex-1">
          <div class="operation-button">
            <el-button class="full-width" type="primary" size="medium" icon="el-icon-download" @click="download(resource['url'])">
              下载
            </el-button>
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
        <resource-viewer :url="String(resource['viewUrl'])"></resource-viewer>
      </div>
      <!-- ---------- -->
    </div>
    <div class="graph flex-1">
      <k-g-chart :entities="entities.entities" ref="chart"></k-g-chart>
    </div>
  </div>
  <!-- 推荐资源 -->
  <div class="out-first-screen flex">
    <div class="related-resource flex-1">
      <h2>相关资源</h2>
      <div
        class="flex"
        v-for="resource in relatedResources"
        :key="resource.id">
        <svg class="type-icon" aria-hidden="true">
          <use v-if="resource.suffix === 'doc' || resource.suffix === 'docx'" xlink:href="#e-resource-icon-word"></use>
          <use v-else-if="resource.suffix === 'ppt' || resource.suffix === 'pptx'" xlink:href="#e-resource-icon-ppt"></use>
          <use v-else-if="resource.suffix === 'pdf'" xlink:href="#e-resource-icon-pdf"></use>
          <use v-else xlink:href="#e-resource-icon-unknown"></use>
        </svg>
        <resource-link class="related-resource-name" :resource="resource"></resource-link>
      </div>
    </div>
    <div class="flex-1">
      <h2>推荐资源</h2>
    </div>
  </div>
</div>
</template>

<script>
import NavMenu from '@/components/NavMenu'
import KGChart from '@/components/Chart/KGChart'
import ResourceViewer from '@/components/ViewResource/ResourceViewer'
import ResourceLink from '@/components/ResourceLink'
import { resourceInfo, related } from '@/api/resource'
import { relatedEntity } from '@/api/entity'
import { authentication } from '@/api/auth'
import { record } from '@/api/record'

export default {
  name: 'Resource',
  components: {
    NavMenu,
    KGChart,
    ResourceViewer,
    ResourceLink
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
              // 获取相关实体
              relatedEntity(response.data.data['entity'])
                .then(entityResponse => {
                  if (entityResponse.data.code === 200) {
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
      relatedResources: []
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
    }
  }
}
</script>

<style scoped>
.main-container {
  margin-bottom: 1rem;
  border-bottom: 1px solid #dcdfe6;
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
  border-right: 1px solid #DCDFE6;
}

.resource-info >>> h2 {
  color: #222226;
}

.resource-info .resource-name {
  color: #606266;
}

.viewer {
  margin-top: 1rem;
  margin-bottom: 2rem;
}

.out-first-screen {
}

.out-first-screen h2 {
  margin-bottom: 1rem;
}

.related-resource {
  margin-bottom: 2rem;
}

.related-resource div {
  margin-bottom: .5rem;
}

.related-resource-name {
  font-size: 1rem;
  line-height: 1.5rem;
  color: #409eff;
  cursor: pointer;
}

.type-icon {
  height: 1.2rem;
  width: 1.2rem;
  margin-right: .2rem;
}

.related-resource-name:hover {
  color: #66b1ff;
}

/*@media only screen and (max-width : 768px) {*/
/*  .main-container {*/
/*    flex-direction: column;*/
/*  }*/
/*}*/
</style>
