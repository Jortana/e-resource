<template>
  <div>
    <nav-menu :searchInfo.sync="searchInfo"></nav-menu>
    <div class="main-container resource-container">
      <!-- ------------------------------暂不显示---------------------------------------- -->
      <el-tabs v-if="false" v-model="searchInfo.type" @tab-click="changeType">
        <el-tab-pane
          v-for="type in resourceTypes"
          :key="type.code"
          :label="type.label"
          :name="String(type.code)">
        </el-tab-pane>
      </el-tabs>
      <!-- -------------------------------------------------------------------------------- -->
      <div class="resources">
        <div class="flex-1">
          <!-- 知识卡片 -->
          <knowledge-card class="knowledge-card" :entityInfo="cardInfo" v-if="JSON.stringify(cardInfo) !== '{}'"></knowledge-card>
          <!-- 找到的实体和资源信息 -->
          <div class="resource">
            <div>
              <div
                v-for="(entity, index) in resources.resources"
                :key="index"
              >
                <!-- 教学目标和教学重难点 -->
                <div v-if="entity.goal.length > 0">
                  <div class="subtitle">教学目标</div>
                  <div
                    v-for="(goal, index) in entity.goal"
                    :key="index"
                    v-if="index < 5"
                    :class="goal.content.length > 43 ? 'content-link overflow-content' : 'content-link'"
                    @click="viewResource(goal.resourceID)">
                    {{ goal.content }}
                  </div>
                </div>
                <div v-if="entity.key.length > 0">
                  <div class="subtitle">教学重难点</div>
                  <div
                    v-for="(key, index) in entity.key"
                    :key="'key' + index"
                    v-if="index < 5"
                    :class="key.content.length > 43 ? 'content-link overflow-content' : 'content-link'"
                    @click="viewResource(key.resourceID)">
                    {{ key.content }}
                  </div>
                </div>
                <!-- ----------------- -->
                <div class="resource-list" v-if="entity.resources.length > 0">
                  <div
                    v-for="resource in entity.resources"
                    :key="resource.id"
                  >
                    <div class="resource-info">
                      <div class="info">
                        <resource-link :resource="resource"></resource-link>
<!--                        <span class="resource-name" @click="viewResource(resource['id'])">-->
<!--                          {{ resource['resourceName'] }}-->
<!--                        </span>-->
                        <div class="file-name">
                          {{ `${resource['resourceName']}.${resource['url'].split('.').slice(-1)}` }}
                        </div>
                        <div class="entity-list">
                          <div
                            v-for="entity in resource['entity'].split('#').slice(0, resource['entity'].split('#').length - 1)"
                            :key = entity
                          >
                            <el-button size="mini">
                              {{ entity }}
                            </el-button>
                          </div>
                        </div>
                        <div class="extra">
                          <div class="extra-info"><i class="el-icon-time"></i> {{ resource['updateTime'] }}</div>
                          <div class="extra-info"><i class="el-icon-download"></i> {{ resource['download'] }} 下载</div>
                        </div>
                      </div>
                      <div class="operation">
                        <div class="full-width">
                          <el-button class="full-width" type="primary" size="medium" icon="el-icon-download" @click="download(resource['url'])">
                            下载
                          </el-button>
                        </div>
                        <div class="full-width">
                          <el-button class="full-width" size="medium" icon="el-icon-document-add">
                            加入资源包
                          </el-button>
                        </div>
                        <div class="full-width">
                          <el-button class="full-width" size="medium" icon="el-icon-star-off">
                            收藏
                          </el-button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div v-else>无相关资源</div>
              </div>
            </div>
            <!-- 隐藏的a元素，用来在新窗口打开资源页面 -->
            <!-- 这个本来应该和资源名称一起封装在ResourceLink里了，但是教学重难点还需要使用，懒得封装了，也包括下面的ViewResource method -->
            <a class="resource-target" ref="resourceTarget" href="" target="_blank" v-show="false"></a>
            <!-- 触发下载测试 -->
            <a ref="download" href="" target="_blank" download v-show="false"></a>
          </div>
          <div class="pages" v-if="resources.total > 10">
            <el-pagination
              layout="prev, pager, next"
              :total="resources.total"
              :current-page="Number(query.page)"
              @current-change="changePage">
            </el-pagination>
          </div>
        </div>
        <!-- 知识图谱 -->
        <div class="graph flex-1">
            <k-g-chart :entities="entities.entities" ref="chart"></k-g-chart>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavMenu from '@/components/NavMenu'
import KnowledgeCard from '@/components/KnowledgeCard'
import ResourceLink from '@/components/ResourceLink'
import { record } from '@/api/record'
// import { download } from '@/api/resource'
import { searchEntity, relatedEntity } from '@/api/entity'
import merge from 'webpack-merge'
import KGChart from '@/components/Chart/KGChart'
export default {
  name: 'ResourceCenter',
  components: {
    NavMenu,
    KGChart,
    KnowledgeCard,
    ResourceLink
  },
  mounted () {
    this.goSearch()
  },
  computed: {
    query () {
      return this.$route.query
    },
    graphEntity () {
      return this.entities.entities
    }
  },
  watch: {
    query: {
      handler (newQuery, oldQuery) {
        console.log('query changed')
        this.resetResource()
        this.resetEntity()
        // this.searchInfo.type = newQuery.type === undefined ? 0 : newQuery.type
        this.searchInfo.content = newQuery.q === undefined ? 0 : newQuery.q
        this.pageInfo.page = newQuery.page === undefined ? 1 : newQuery.page
        this.goSearch()
        if (oldQuery === undefined || newQuery.q !== oldQuery.q) {
          this.getRelatedEntity(newQuery.q)
        }
      },
      immediate: true
    },
    graphEntity: {
      handler () {
        // this.$refs.chart.initCharts()
      }
    }
  },
  data () {
    console.log(this.$route.query)
    return {
      searchInfo: {
        // type: this.$route.query.type === undefined ? 0 : this.$route.query.type,
        content: this.$route.query.q
      },
      pageInfo: {
        page: this.$route.query.page === undefined ? 1 : this.$route.query.page,
        perPage: 10
      },
      resourceTypes: [{
        label: '全部',
        code: 0
      }, {
        label: '视频',
        code: 1
      }, {
        label: '试题',
        code: 2
      }, {
        label: '试卷',
        code: 3
      }, {
        label: '课件',
        code: 4
      }, {
        label: '文献资料',
        code: 5
      }, {
        label: '教学案例',
        code: 6
      }],
      resources: {
        resources: [],
        total: 0,
        pages: 0
      },
      entities: {
        entities: []
      },
      cardInfo: {},
      activeEntity: ''
    }
  },
  methods: {
    changeType (tab) {
      this.searchInfo.type = tab.name
      this.$router.push({
        path: '/search',
        query: {
          q: this.searchInfo.content,
          type: this.searchInfo.type
        }
      }).catch(() => {
        this.$router.go(0)
      })
      // this.goSearch()
    },
    goSearch () {
      searchEntity({
        keyword: this.searchInfo.content,
        page: this.pageInfo.page,
        perPage: this.pageInfo.perPage
      }).then(response => {
        console.log(response.data)
        this.resetResource()
        if (response.data.code === 200) {
          console.log(response.data.data)
          this.resources.resources = response.data.data.resources
          this.resources.total = response.data.data.total
          this.resources.pages = response.data.data.pages
          // 提取出教学目标和重难点以数组的形式存储
          this.resources.resources.forEach(resource => {
            let goal = []
            let key = []
            resource['goalAndKey'].forEach(goalAndKey => {
              if (goalAndKey['objectives'] !== null) {
                goal.push({
                  content: goalAndKey['objectives'],
                  resourceID: goalAndKey['resourceID']
                })
              }
              if (goalAndKey['keyPoint'] !== null) {
                key.push({
                  content: goalAndKey['keyPoint'],
                  resourceID: goalAndKey['resourceID']
                })
              }
            })
            resource.goal = goal
            resource.key = key
          })
          // 找到和关键词一致的知识点
          response.data.data.resources.forEach(resource => {
            if (resource.entityName === this.searchInfo.content) {
              this.cardInfo = resource.properties
            }
          })
        }
      })
    },
    resetResource () {
      this.resources.resources = []
      this.resources.total = 0
      this.resources.pages = 0
      this.cardInfo = {}
    },
    resetEntity () {
      this.entities.entities = []
    },
    getRelatedEntity (keyword) {
      this.resetEntity()
      console.log(keyword)
      relatedEntity(keyword)
        .then(response => {
          console.log(response)
          if (response.data.code === 200) {
            this.entities.entities = response.data.data
          }
        })
    },
    changePage (curPage) {
      this.$router.push({
        query: merge(this.$route.query, {
          'page': curPage
        })
      })
    },
    viewResource (resourceID) {
      let target = this.$refs.resourceTarget
      record({
        resourceID: resourceID
      })
      target.setAttribute('href', `${window.location.origin}/resource/${resourceID}`)
      target.click()
    },
    download (url) {
      console.log('download')
      // download(url)
      //   .then(response => {
      //     console.log(response)
      //   })
      // let target = this.$refs.download
      // target.setAttribute('href', `http://223.2.50.241:8082${url}`)
      // target.click()
    }
  }
}
</script>

<style scoped>
.resources {
  display: flex;
  margin-top: 1rem;
  /*justify-content: space-evenly;*/
}

.entity-title {
  font-size: 1.2rem;
  line-height: 1.2rem;
}

.resource-list {
  margin-top: 1rem;
  border-top: 1px solid #dcdfe6;
}

.resource .subtitle {
  color: #606266;
  margin-top: .5rem;
}

.content-link {
  overflow: hidden;
  position: relative;
  cursor: pointer;
  text-align: justify;
  width: 100%;
  font-size: .8rem;
  line-height: 1.2rem;
  height: 1.2rem;
  color: #909399;
}

.content-link:hover {
  color: #53a8ff;
  text-decoration: underline;
  text-underline-offset: .2rem;
}

/*省略号*/
.overflow-content::after {
  content: "...";
  position: absolute;
  bottom: 0;
  right: 0;
  /*将省略号的大小设置为1个字体大小*/
  width: 1em;
  /*设置背景，将最后一个字覆盖掉*/
  background: #fff;
}

.resource-info {
  height: 190px;
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid #dcdfe6;
  padding-top: 1rem;
  padding-bottom: 1rem;
}

.info {
  display: flex;
  flex-direction: column;
  position: relative;
}

.entity-list {
  /* 这里的间距要减去按钮设置的间距，但不完全减 */
  margin-top: calc(.5rem - 5px);
  display: flex;
  flex-wrap: wrap;
}

.entity-list div {
  margin-right: 10px;
  margin-top: 10px;
}

.extra {
  display: flex;
  margin-top: .8rem;
  position: absolute;
  bottom: 0;
  min-width: 220px;
}

.extra .extra-info {
  color: #909399;
  font-size: .9rem;
  line-height: 1rem;
  margin-right: 1rem;
}

.operation {
  display: flex;
  flex-direction: column;
  height: 100%;
  margin-left: .8rem;
  justify-content: center;
  align-items: center;
}

.operation button {
  margin-bottom: 10px;
}

.resource-name {
  /*color: #1a0dab;*/
  font-size: 1.2rem;
  font-weight: bold;
  cursor: pointer;
}

.resource-name:hover {
  text-decoration: underline;
  text-underline-offset: .1rem;
}

.name-in-url {
  color: #555555;
  margin-bottom: .6rem;
}

.resource {
  margin-bottom: 1rem;
}

.knowledge-card {
  margin-bottom: 1rem;
}

.entity:first-child {
  margin-left: -2px;
}

.pages {
  display: flex;
  justify-content: center;
}

.graph {
  padding-left: 2rem;
  height: 500px;
  min-height: 500px;
}

.graph button {
  margin-top: 10px;
}
</style>
