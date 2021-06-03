<template>
  <div class="resource-list" v-if="resources.length > 0">
    <div
      v-for="resource in resources"
      :key="resource.id"
    >
      <div class="resource-info">
        <div class="thumbnail-container flex">
          <el-image class="thumbnail">
          </el-image>
        </div>
        <div class="right-side flex">
          <div class="info">
            <resource-link :resource="resource"></resource-link>
            <div class="file-name">
              <span v-if="resource['url'] == null"></span>
              <span v-else>{{ `${resource['resourceName']}.${resource['url'].split('.').slice(-1)}` }}</span>
            </div>
            <div class="entity-list" v-if="resource['entityList'] !== null">
              <div
                v-for="entity in resource['entityList']"
                :key = entity
              >
                <el-button size="mini" @click="searchEntity(entity)">
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
              <download-button :resourceID="resource['id']"></download-button>
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
  </div>
</template>

<script>
import ResourceLink from '@/components/ResourceLink'
import DownloadButton from '@/components/DownloadButton'
import merge from 'webpack-merge'

export default {
  name: 'ResourceListWithInfo',
  components: { ResourceLink, DownloadButton },
  props: {
    resources: Array
  },
  methods: {
    searchEntity (entity) {
      this.$router.push({
        query: merge(this.$route.query, {
          'q': entity,
          'type': 0,
          'page': 1,
          'sort': 0
        })
      })
    }
  }
}
</script>

<style scoped>
.resource-info {
  height: 190px;
  display: flex;
  border-bottom: 1px solid #dcdfe6;
  padding-top: 1rem;
  padding-bottom: 1rem;
}

.resource-info .right-side {
  justify-content: space-between;
  width: 100%;
}

.thumbnail-container {
  height: 100%;
  align-items: center;
  justify-content: center;
}

.thumbnail {
  width: 100px;
  height: 100px;
}

.info {
  display: flex;
  flex-direction: column;
  position: relative;
  margin-left: 1rem;
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

.file-name {
  color: #909399;
}

.resource-name {
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
</style>
