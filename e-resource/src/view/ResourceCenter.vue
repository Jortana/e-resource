<template>
  <div>
    <nav-menu :searchInfo.sync="searchInfo"></nav-menu>
    <div class="main-container resource-container">
      <el-tabs v-if="false" v-model="searchInfo.type" @tab-click="changeType">
        <el-tab-pane
          v-for="type in resourceTypes"
          :key="type.code"
          :label="type.label"
          :name="String(type.code)">
        </el-tab-pane>
      </el-tabs>
      <div class="resources">
        <div class="resource-list flex-1">
          <div class="resource">
            <el-collapse v-model="activeEntity">
              <el-collapse-item
                v-for="(entity, index) in resources.resources"
                :key="index"
              >
                <template slot="title">
                  <div class="entity-title">{{ entity['entityName'] }}</div>
                </template>
                <div v-if="entity.resources.length > 0">
                  <div
                    v-for="resource in entity.resources"
                    :key="resource.id"
                  >
                    <div class="resource-info">
                      <span class="resource-name" @click="viewResource(resource['id'])">
                        {{ resource['resourceName'] }}
                      </span>
                    </div>
                  </div>
                </div>
                <div v-else>无相关资源</div>
              </el-collapse-item>
            </el-collapse>
            <!-- 隐藏的a元素，用来在新窗口打开资源页面 -->
            <a class="resource-target" ref="resourceTarget" href="" target="_blank" v-show="false"></a>
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
<!--        <div class="flex-1" v-else>未查询到相关知识点</div>-->
        <div class="graph flex-1">
            <k-g-chart :entities="entities.entities" ref="chart"></k-g-chart>
<!--          <el-button-->
<!--            round-->
<!--            v-for="entity in entities.entities"-->
<!--            :key="entity['entityName']">-->
<!--            {{ entity['entityName'] }}-->
<!--          </el-button>-->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavMenu from '@/components/NavMenu'
// import { resource } from '@/api/resource'
import { searchEntity, relatedEntity } from '@/api/entity'
import merge from 'webpack-merge'
import KGChart from '@/components/Chart/KGChart'
export default {
  name: 'ResourceCenter',
  components: {
    NavMenu,
    KGChart
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
        console.log(this.searchInfo)
        this.goSearch()
        if (oldQuery === undefined || newQuery.q !== oldQuery.q) {
          this.getRelatedEntity(newQuery.q)
        }
      },
      immediate: true
    },
    graphEntity: {
      handler () {
        console.log('aaa')
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
      activeEntity: ''
    }
  },
  methods: {
    changeType (tab) {
      console.log(tab.name)
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
        console.log(response)
        this.resetResource()
        if (response.data.code === 200) {
          this.resources.resources = response.data.data.resources
          this.resources.total = response.data.data.total
          this.resources.pages = response.data.data.pages
        }
      })
    },
    resetResource () {
      this.resources.resources = []
      this.resources.total = 0
      this.resources.pages = 0
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
      console.log(resourceID)
      let target = this.$refs.resourceTarget
      console.log(target)
      target.setAttribute('href', `${window.location.origin}/resource/${resourceID}`)
      target.click()
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

.resource-name {
  color: #1a0dab;
  font-size: 1rem;
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

.entity:first-child {
  margin-left: -2px;
}

.pages {
  display: flex;
  justify-content: center;
}

.graph {
  padding-left: 2rem;
  min-height: 500px;
}

.graph button {
  margin-top: 10px;
}
</style>
