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
      <div class="resources flex-1">
        <div class="resource-list">
          <div
            class="resource"
            v-if="resources !== []"
            v-for="(resource, index) in resources"
            :key="index"
          >
            <el-card shadow="never">
              <div class="resource-name"><span>{{ resource.name }}</span></div>
              <div class="name-in-url"><span>{{ resource.url.split('/').slice(-1)[0] }}</span></div>
              <div class="resource-info flex">
                <div class="flex-1"><span>收藏：{{ resource['collection'] }}</span></div>
                <div class="flex-1"><span>下载：{{ resource['download'] }}</span></div>
                <div class="flex-1"><span>更新时间：{{ resource['updateTime'] }}</span></div>
              </div>
            </el-card>
          </div>
          <div v-else>未查询到相关资源</div>
        </div>
        <div class="graph flex-1">node</div>
      </div>
    </div>
  </div>
</template>

<script>
import NavMenu from '@/components/NavMenu'
import { resource } from '@/api/resource'
export default {
  name: 'ResourceCenter',
  components: {
    NavMenu
  },
  created () {
    this.getResource()
  },
  data () {
    console.log(this.$route.query.type)
    return {
      searchInfo: {
        type: this.$route.query.type === undefined ? 0 : this.$route.query.type,
        content: this.$route.query.q
      },
      pageInfo: {
        page: 1,
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
      resources: []
    }
  },
  methods: {
    changeType (tab) {
      console.log(tab.name)
      this.searchInfo.type = tab.name
      this.resources = []
      this.getResource()
    },
    getResource () {
      resource({
        keyword: this.searchInfo.content,
        resourceType: this.searchInfo.type,
        page: this.pageInfo.page,
        perPage: this.pageInfo.perPage
      }).then(response => {
        console.log(response)
        if (response.data.code === 200) {
          this.resources = response.data.data !== null ? response.data.data.resources : []
        }
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

.resource-list,
.graph {
  flex: 1;
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
  margin-bottom: 1rem;
}
</style>
