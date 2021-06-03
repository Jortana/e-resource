<template>
  <div class="lg-container">
    <nav-menu :searchInfo.sync="searchInfo"></nav-menu>
    <div class="main-container resource-container">
      <div class="resources">
        <div class="flex-1">
          <!-- 知识卡片 -->
          <knowledge-card class="knowledge-card" :entityInfo="cardInfo" v-if="JSON.stringify(cardInfo) !== '{}'"></knowledge-card>
          <!-- 找到的实体和资源信息 -->
          <div class="resource">
            <div>
              <!-- 这里的v-for的数组实际上是只有一个元素的，后端问题，之前的想法和现在不一样，我发现这个问题的时候已经写了很多了，改起来太麻烦了，懒得改了 -->
              <div
              >
                <!-- 教学目标和教学重难点 -->
                <div v-if="goal.length > 0">
                  <div class="subtitle">教学目标</div>
                  <div
                    v-for="(goal, index) in goal"
                    :key="index"
                    v-if="index < 5"
                    :class="goal.content.length > 43 ? 'content-link overflow-content' : 'content-link'"
                    @click="viewResource(goal.resourceID)">
                    {{ goal.content }}
                  </div>
                </div>
                <div v-if="key.length > 0">
                  <div class="subtitle">教学重难点</div>
                  <div
                    v-for="(key, index) in key"
                    :key="'key' + index"
                    v-if="index < 5"
                    :class="key.content.length > 43 ? 'content-link overflow-content' : 'content-link'"
                    @click="viewResource(key.resourceID)">
                    {{ key.content }}
                  </div>
                </div>
                <!-- ----------------- -->
                <!-- ------------------------------ 筛选 ---------------------------------------- -->
                <el-tabs v-model="searchInfo.type" @tab-click="changeType">
                  <el-tab-pane
                    v-for="type in resourceTypes"
                    :key="type.code"
                    :label="type.label"
                    :name="String(type.code)">
                  </el-tab-pane>
                </el-tabs>
                <div class="sort">
                  <el-radio-group v-model="searchInfo.sort" size="mini" @change="changeSort">
                    <el-radio-button label="0">综合</el-radio-button>
                    <el-radio-button label="1">最热</el-radio-button>
                    <el-radio-button label="2">最新</el-radio-button>
                  </el-radio-group>
                </div>
                <!-- -------------------------------------------------------------------------------- -->
<!--                {{ resources.resources.resources }}-->
                <resource-list-with-info v-if="resources.resources.length > 0" :resources="resources.resources"></resource-list-with-info>
                <div v-else>{{ noResourceHint }}</div>
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
        <div class="graph">
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
import DownloadButton from '@/components/DownloadButton'
import ResourceListWithInfo from '@/components/ResourceListWithInfo'
import { record } from '@/api/record'
// import { recommendByUserEntity } from '@/api/recommend'
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
    ResourceLink,
    DownloadButton,
    ResourceListWithInfo
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
        console.log('sort', this.searchInfo.sort)
        console.log(this.$route.query.sort)
        this.resetResource()
        this.searchInfo.type = newQuery.type === undefined ? 0 : newQuery.type
        this.searchInfo.sort = newQuery.sort === undefined ? 0 : newQuery.sort
        this.searchInfo.content = newQuery.q === undefined ? 0 : newQuery.q
        this.pageInfo.page = newQuery.page === undefined ? 1 : newQuery.page
        this.goSearch()
        if (oldQuery === undefined || newQuery.q !== oldQuery.q) {
          this.resetCardInfo()
          this.resetEntity()
          this.resetGoalAndKey()
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
        type: this.$route.query.type === undefined ? 0 : this.$route.query.type,
        content: this.$route.query.q,
        sort: this.$route.query.sort === undefined ? 0 : this.$route.query.sort
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
        resources: {},
        total: 0,
        pages: 0
      },
      noResourceHint: '',
      entities: {
        entities: []
      },
      cardInfo: {},
      goal: [],
      key: [],
      activeEntity: ''
    }
  },
  methods: {
    changeType (tab) {
      this.searchInfo.type = tab.name
      this.$router.push({
        query: merge(this.$route.query, {
          'type': this.searchInfo.type,
          'sort': 0,
          'page': 1
        })
      })
    },
    goSearch () {
      this.noResourceHint = ''
      searchEntity({
        keyword: this.searchInfo.content,
        type: this.searchInfo.type,
        sort: this.searchInfo.sort,
        page: this.pageInfo.page,
        perPage: this.pageInfo.perPage
      }).then(response => {
        console.log(response.data)
        this.resetResource()
        if (response.data.code === 200) {
          console.log(response.data.data)
          let resource = response.data.data.resources[0]
          this.resources.total = response.data.data.total
          this.resources.pages = response.data.data.pages
          // 提取出教学目标和重难点以数组的形式存储
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
          this.goal = goal
          this.key = key
          // 找到和关键词一致的知识点
          if (resource.entityName === this.searchInfo.content) {
            this.cardInfo = resource.properties
          }
          this.resources.resources = resource.resources
          console.log(this.resources)
        }
        if (this.resources.resources.length === 0) {
          this.noResourceHint = '未查询到相关资源'
        }
      })
    },
    changeSort (sort) {
      this.$router.push({
        query: merge(this.$route.query, {
          'type': this.searchInfo.type,
          'page': 1,
          'sort': sort
        })
      })
    },
    resetResource () {
      this.resources.resources = []
      this.resources.total = 0
      this.resources.pages = 0
    },
    resetGoalAndKey () {
      this.goal = []
      this.key = []
    },
    resetCardInfo () {
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
    }
  }
}
</script>

<style scoped>
.sort {
  margin-top: -.5rem;
  margin-bottom: .5rem;
}

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

.resource {
  margin-bottom: 1rem;
}

.knowledge-card {
  margin-top: 1rem;
  margin-bottom: 1rem;
}

.entity:first-child {
  margin-left: -2px;
}

.pages {
  display: flex;
  justify-content: center;
  padding-bottom: 2rem;
}

.graph {
  padding-left: 2rem;
  width: 400px;
  height: 500px;
  min-height: 500px;
}

.graph button {
  margin-top: 10px;
}
</style>
