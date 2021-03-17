<template>
  <div>
    <nav-menu :searchInfo.sync="searchInfo"></nav-menu>
    <div class="resource-container">
      <el-tabs v-model="searchInfo.type" @tab-click="changeType">
        <el-tab-pane
          v-for="type in resourceTypes"
          :key="type.code"
          :label="type.label"
          :name="String(type.code)">
        </el-tab-pane>
      </el-tabs>
      <div class="resources">
        <div class="resource-list flex-1">
          <div
            class="resource"
            v-for="(resource, index) in resources.resources"
            :key="index"
          >
            <el-card shadow="never">
              <div class="resource-name"><span>{{ resource['resourceName'] }}</span></div>
              <div class="name-in-url"><span>{{ resource.url.split('/').slice(-1)[0] }}</span></div>
              <div class="entities">
                <el-button
                  class="entity"
                  round
                  size="mini"
                  v-for="entity in resource['entityList']"
                  :key="entity">
                  {{ entity }}
                </el-button>
              </div>
              <div class="resource-info flex">
                <div class="flex-1"><span>收藏：{{ resource['collection'] }}</span></div>
                <div class="flex-1"><span>下载：{{ resource['download'] }}</span></div>
                <div class="flex-1"><span>更新时间：{{ resource['updateTime'] }}</span></div>
              </div>
            </el-card>
          </div>
          <div class="pages">
            <el-pagination
              layout="prev, pager, next"
              :total="resources.total"
              :current-page="Number(query.page)"
              @current-change="changePage">
            </el-pagination>
          </div>
        </div>
<!--        <div class="flex-1" v-else>未查询到相关资源</div>-->
        <div class="graph flex-1">
          <el-button
            round
            v-for="entity in entities.entities"
            :key="entity['entityName']">
            {{ entity['entityName'] }}
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavMenu from '@/components/NavMenu'
import { resource } from '@/api/resource'
import { relatedEntity } from '@/api/entity'
import merge from 'webpack-merge'
export default {
  name: 'ResourceCenter',
  components: {
    NavMenu
  },
  mounted () {
    this.getResource()
  },
  computed: {
    query () {
      return this.$route.query
    }
  },
  watch: {
    query (query) {
      console.log('query changed')
      this.searchInfo.type = query.type === undefined ? 0 : query.type
      this.searchInfo.content = query.q === undefined ? 0 : query.q
      this.pageInfo.page = query.page === undefined ? 1 : query.page
      this.getResource()
    }
  },
  data () {
    console.log(this.$route.query)
    return {
      searchInfo: {
        type: this.$route.query.type === undefined ? 0 : this.$route.query.type,
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
      }
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
      this.getResource()
    },
    getResource () {
      console.log(this.pageInfo)
      resource({
        keyword: this.searchInfo.content,
        resourceType: this.searchInfo.type,
        page: this.pageInfo.page,
        perPage: this.pageInfo.perPage
      }).then(response => {
        console.log(response)
        this.resetResource()
        if (response.data.code === 200) {
          this.resources.resources = response.data.data.resources
          this.resources.total = response.data.data.total
          this.resources.pages = response.data.data.pages
          this.getRelatedEntity(this.searchInfo.content)
        } else {
          this.$message({
            message: response.data.message,
            type: 'warning',
            duration: 800
          })
        }
        console.log(this.resources)
      })
    },
    resetResource () {
      this.resources.resources = []
      this.resources.total = 0
      this.resources.pages = 0
      this.entities.entities = []
    },
    getRelatedEntity (keyword) {
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
    }
  }
}
</script>

<style scoped>
.resource-container {
  margin-left: 176px;
}

.resources {
  display: flex;
  /*justify-content: space-evenly;*/
}

.resource-name {
  color: #1a0dab;
  font-size: 1.2rem;
  cursor: pointer;
  margin-bottom: .2rem;
}

.resource-name:hover {
  text-decoration: underline;
  text-underline-offset: .2rem;
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

.resource-info {
  margin-top: .5rem;
  font-size: .9rem;
}

.pages {
  display: flex;
  justify-content: center;
}

.graph {
  padding-left: 2rem;
}
</style>
