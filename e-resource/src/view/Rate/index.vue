<template>
  <div>
    <nav-menu></nav-menu>
    <div class="main-container">
      <h1>人气排行</h1>
      <div class="rate-container">
        <div class="card">
          <div class="title-container">
            <h2>热门资源</h2>
            <el-select
              v-show="subjectReady"
              v-model="hotCurSubject"
              size="mini"
              class="subject-select"
              @change="getHot"
            >
              <el-option
                v-for="subject in $store.state.subjects"
                :key="subject.ID"
                :label="subject.name"
                :value="subject.ID"
                :size="'mini'"
              ></el-option>
            </el-select>
          </div>

          <div class="list-container">
            <resource-list
              :resourceList="hotResources.slice(0, 13)"
              :browseTime="true"
              :isHidden="true"
            ></resource-list>
          </div>
        </div>
        <div class="card">
          <div class="title-container">
            <h2>最新资源</h2>
            <el-select
              v-show="subjectReady"
              v-model="newCurSubject"
              size="mini"
              class="subject-select"
              @change="getNew"
            >
              <el-option
                v-for="subject in $store.state.subjects"
                :key="subject.ID"
                :label="subject.name"
                :value="subject.ID"
                :size="'mini'"
              ></el-option>
            </el-select>
          </div>
          <div class="list-container">
            <resource-list
              :resourceList="newResources.slice(0, 13)"
              :browseTime="true"
              :isHidden="true"
            ></resource-list>
          </div>
        </div>
        <div class="card">
          <div class="title-container">
            <h2>最多下载</h2>
            <el-select
              v-show="subjectReady"
              v-model="downloadCurSubject"
              size="mini"
              class="subject-select"
              @change="getDownload"
            >
              <el-option
                v-for="subject in $store.state.subjects"
                :key="subject.ID"
                :label="subject.name"
                :value="subject.ID"
                :size="'mini'"
              ></el-option>
            </el-select>
          </div>
          <div class="list-container">
            <resource-list
              :resourceList="downloadResources.slice(0, 13)"
              :browseTime="true"
              :isHidden="true"
            ></resource-list>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavMenu from '@/components/NavMenu'
import ResourceList from '@/components/ResourceList'
import {
  hotResourceMore,
  newResourceMore,
  downloadResourceMore
  // userRecommendResourceMore
} from '@/api/recommend'
import { getSubjects } from '@/api/menu'

export default {
  name: 'Rate',
  components: {
    NavMenu,
    ResourceList
  },
  data() {
    return {
      hotResources: [],
      newResources: [],
      downloadResources: [],
      userRecommendResources: [],
      subjectReady: false,
      hotCurSubject: '',
      newCurSubject: '',
      downloadCurSubject: '',
      subjects: []
    }
  },
  created() {
    this.initData()
  },
  methods: {
    initData() {
      this.getHot()
      this.getNew()
      this.getDownload()
      this.getSubjects()
    },
    getHot() {
      hotResourceMore(this.hotCurSubject).then((response) => {
        const { code, data } = response.data
        if (code === 200) {
          this.hotResources = data
        } else {
          this.$message.warning('服务器错误，请稍后刷新重试')
        }
      })
    },
    getNew() {
      newResourceMore(this.newCurSubject).then((response) => {
        const { code, data } = response.data
        if (code === 200) {
          this.newResources = data
        } else {
          this.$message.warning('服务器错误，请稍后刷新重试')
        }
      })
    },
    getDownload() {
      downloadResourceMore(this.downloadCurSubject).then((response) => {
        const { code, data } = response.data
        if (code === 200) {
          this.downloadResources = data
        } else {
          this.$message.warning('服务器错误，请稍后刷新重试')
        }
      })
    },
    getSubjects() {
      if (this.$store.state.subjects.length === 0) {
        getSubjects().then((response) => {
          const { code, data } = response.data
          if (code === 200) {
            this.$store.commit('initSubjects', data)
            this.subjects = data
            this.subjectReady = true
          }
        })
      } else {
        this.subjectReady = true
      }
    }
  }
}
</script>

<style scoped>
.main-container {
  min-height: calc(100vh - 202px);
  padding: 2rem 0;
}

.main-container h1 {
  padding-left: 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid #dcdfe6;
}

.rate-container {
  display: flex;
  padding: 1.5rem 1rem 0;
}

.card {
  background-color: #fdfbfb;
  box-shadow: 1px 3px 6px rgb(122 122 122 / 0.3);
  overflow: hidden;
  height: 600px;
  flex: 1;
  /*text-overflow: ellipsis;*/
  /*white-space: nowrap;*/
}

.card {
  margin-right: 2rem;
}

.card:last-child {
  margin-right: 0;
}

.card h2 {
  font-size: 1.5rem;
}

.card .title-container {
  padding: 0.8rem;
  display: flex;
  align-items: center;
  border-bottom: solid 1px #a5a3a3;
  justify-content: space-between;
}

.card .title-container .subject-select {
  width: 80px;
}

.list-container {
  padding: 1rem;
}
</style>
