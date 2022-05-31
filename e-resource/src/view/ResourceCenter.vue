<template>
  <div>
    <nav-menu :searchInfo.sync="searchInfo"></nav-menu>
    <div class="lg-container">
      <div class="main-container resource-container">
        <div class="resources">
          <div class="flex-1">
            <!-- 知识卡片 -->
            <knowledge-card
              v-if="JSON.stringify(cardInfo) !== '{}'"
              :entityInfo="cardInfo"
              class="knowledge-card"
            ></knowledge-card>
            <!-- 找到的实体和资源信息 -->
            <div class="resource-content-container common-shadow">
              <div class="resource">
                <div>
                  <!-- 这里的v-for的数组实际上是只有一个元素的，后端问题，之前的想法和现在不一样，我发现这个问题的时候已经写了很多了，改起来太麻烦了，懒得改了 -->
                  <div>
                    <!-- 教学目标和教学重难点 -->
                    <div class="flex goal-and-key-container">
                      <goal-and-key-card
                        v-if="goal.length > 0"
                        :list="goal"
                        :title="'学习目标'"
                        class="goal-and-key-card"
                      />
                      <goal-and-key-card
                        v-if="key.length > 0"
                        :list="key"
                        :title="'学习重难点'"
                        class="goal-and-key-card"
                      />
                    </div>
                    <!-- ----------------- -->
                    <!-- ------------------------------ 筛选 ---------------------------------------- -->
                    <el-tabs v-model="searchInfo.type" @tab-click="changeType">
                      <el-tab-pane
                        v-for="type in resourceTypes"
                        :key="type.code"
                        :label="type.label"
                        :name="String(type.code)"
                      ></el-tab-pane>
                    </el-tabs>
                    <div class="sort">
                      <el-radio-group
                        v-model="searchInfo.sort"
                        size="mini"
                        @change="changeSort"
                      >
                        <el-radio-button label="0">综合</el-radio-button>
                        <el-radio-button label="1">最热</el-radio-button>
                        <el-radio-button label="2">最新</el-radio-button>
                      </el-radio-group>
                    </div>
                    <!-- -------------------------------------------------------------------------------- -->
                    <resource-list-with-info
                      v-if="resources.resources.length > 0"
                      :resources="resources.resources"
                    ></resource-list-with-info>
                    <div v-else>{{ noResourceHint }}</div>
                  </div>
                </div>
                <!-- 隐藏的a元素，用来在新窗口打开资源页面 -->
                <!-- 这个本来应该和资源名称一起封装在ResourceLink里了，但是教学重难点还需要使用，懒得封装了，也包括下面的ViewResource method -->
                <a
                  v-show="false"
                  ref="resourceTarget"
                  class="resource-target"
                  href=""
                  target="_blank"
                ></a>
                <!-- 触发下载测试 -->
                <a
                  v-show="false"
                  ref="download"
                  href=""
                  target="_blank"
                  download
                ></a>
              </div>
              <div v-if="resources.total > 10" class="pages">
                <el-pagination
                  :total="resources.total"
                  :current-page="Number(query.page)"
                  layout="prev, pager, next"
                  @current-change="changePage"
                ></el-pagination>
              </div>
            </div>
          </div>
          <!-- 知识图谱 -->
          <div class="graph common-shadow">
            <k-g-chart ref="chart" :entities="entities.entities"></k-g-chart>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavMenu from '@/components/NavMenu/'
import KnowledgeCard from '@/components/KnowledgeCard'
import ResourceLink from '@/components/ResourceLink'
import DownloadButton from '@/components/Buttons/DownloadButton'
import ResourceListWithInfo from '@/components/ResourceListWithInfo'
import GoalAndKeyCard from '@/components/GoalAndKeyCard'
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
    ResourceListWithInfo,
    GoalAndKeyCard
  },
  data() {
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
      resourceTypes: [
        {
          label: '全部',
          code: 0
        },
        {
          label: '视频',
          code: 1
        },
        {
          label: '试题',
          code: 2
        },
        {
          label: '试卷',
          code: 3
        },
        {
          label: '课件',
          code: 4
        },
        {
          label: '文献资料',
          code: 5
        },
        {
          label: '教学案例',
          code: 6
        }
      ],
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
      activeEntity: '',
      title: ''
    }
  },
  computed: {
    query() {
      return this.$route.query
    },
    graphEntity() {
      return this.entities.entities
    }
  },
  watch: {
    query: {
      handler(newQuery, oldQuery) {
        // console.log(this.searchInfo)
        this.resetResource()
        this.searchInfo.type = newQuery.type === undefined ? 0 : newQuery.type
        this.searchInfo.sort = newQuery.sort === undefined ? 0 : newQuery.sort
        this.searchInfo.content = newQuery.q === undefined ? 0 : newQuery.q
        this.pageInfo.page = newQuery.page === undefined ? 1 : newQuery.page
        // console.log(this.searchInfo)
        this.goSearch()
        if (oldQuery === undefined || newQuery.q !== oldQuery.q) {
          this.resetCardInfo()
          this.resetEntity()
          this.resetGoalAndKey()
          this.getRelatedEntity(newQuery.q)
        }
        this.title = `${newQuery.q} - 智慧学伴`
      },
      immediate: true
    },
    graphEntity: {
      handler() {
        // this.$refs.chart.initCharts()
      }
    },
    title: {
      handler(title) {
        document.title = title
      },
      immediate: true
    }
  },
  mounted() {
    // this.goSearch()
  },
  methods: {
    changeType(tab) {
      this.searchInfo.type = tab.name
      this.$router.push({
        query: merge(this.$route.query, {
          type: this.searchInfo.type,
          sort: 0,
          page: 1
        })
      })
    },
    goSearch() {
      this.noResourceHint = ''
      searchEntity({
        keyword: this.searchInfo.content,
        type: this.searchInfo.type,
        sort: this.searchInfo.sort,
        page: this.pageInfo.page,
        perPage: this.pageInfo.perPage
      }).then((response) => {
        this.resetResource()
        console.log(response)
        if (response.data.code === 200) {
          const resource = response.data.data.resources[0]
          this.resources.total = response.data.data.total
          this.resources.pages = response.data.data.pages
          // 提取出教学目标和重难点以数组的形式存储
          const goal = []
          const key = []
          resource['goalAndKey'].forEach((goalAndKey) => {
            if (goalAndKey['objectives'] !== null) {
              goal.push({
                content: goalAndKey['objectives'],
                resourceID: goalAndKey['resourceID']
              })
            }
            if (goalAndKey['keyPoint'] !== null) {
              key.push({
                content: goalAndKey['objectives'],
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
        }
        if (this.resources.resources.length === 0) {
          this.noResourceHint = '未查询到相关资源'
        }
      })
    },
    changeSort(sort) {
      this.$router.push({
        query: merge(this.$route.query, {
          type: this.searchInfo.type,
          page: 1,
          sort: sort
        })
      })
    },
    resetResource() {
      this.resources.resources = []
      this.resources.total = 0
      this.resources.pages = 0
    },
    resetGoalAndKey() {
      this.goal = []
      this.key = []
    },
    resetCardInfo() {
      this.cardInfo = {}
    },
    resetEntity() {
      this.entities.entities = []
    },
    getRelatedEntity(keyword) {
      this.resetEntity()
      relatedEntity(keyword).then((response) => {
        if (response.data.code === 200) {
          this.entities.entities = response.data.data
        }
      })
    },
    changePage(curPage) {
      this.$router.push({
        query: merge(this.$route.query, {
          page: curPage
        })
      })
    },
    viewResource(resourceID) {
      const target = this.$refs.resourceTarget
      record({
        resourceID: resourceID
      })
      target.setAttribute(
        'href',
        `${window.location.origin}/e-resource/#/resource/${resourceID}`
      )
      target.click()
    }
  }
}
</script>

<style scoped>
.lg-container {
  background-color: #f7f5f4;
  min-height: calc(100vh - 130px);
  padding-bottom: 2rem;
}

.sort {
  margin-top: -0.5rem;
  margin-bottom: 0.5rem;
}

.resources {
  display: flex;
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
}

.content-link {
  overflow: hidden;
  position: relative;
  cursor: pointer;
  text-align: justify;
  width: 100%;
  font-size: 0.8rem;
  line-height: 1.2rem;
  height: 1.2rem;
  color: #909399;
}

.content-link:hover {
  color: #53a8ff;
  text-decoration: underline;
  text-underline-offset: 0.2rem;
}

/*省略号*/
.overflow-content::after {
  content: '...';
  position: absolute;
  bottom: 0;
  right: 0;
  /*将省略号的大小设置为1个字体大小*/
  width: 1em;
  /*设置背景，将最后一个字覆盖掉*/
  background: #fff;
}

.resource-content-container {
  background-color: #fff;
  margin-top: 1rem;
  padding: 0.5rem 1.2rem 1.2rem;
}

.resource {
  margin-bottom: 1rem;
}

.knowledge-card {
  margin-top: 1rem;
  box-shadow: 1px 3px 6px rgb(122 122 122 / 0.2);
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
  padding-left: 1rem;
  padding-right: 1rem;
  margin-top: 1rem;
  margin-left: 1rem;
  width: 400px;
  height: 420px;
  min-height: 400px;
  background-color: white;
}

.graph button {
  margin-top: 10px;
}

/*教学目标和教学重难点卡片*/
.goal-and-key-container {
  justify-content: space-between;
}

.goal-and-key-card {
  width: calc(50% - 10px);
  height: 190px;
}
</style>
