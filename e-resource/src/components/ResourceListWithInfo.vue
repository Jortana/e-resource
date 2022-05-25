<template>
  <div v-if="resources.length > 0" class="resource-list">
    <div v-for="resource in resources" :key="resource.id">
      <div class="resource-info">
        <div class="thumbnail-container flex">
          <el-image
            :src="'http://222.192.6.62:8082' + resource.cover"
            fit="contain"
            class="thumbnail"
          ></el-image>
        </div>
        <div class="right-side flex">
          <div class="info">
            <resource-link :resource="resource"></resource-link>
            <div class="file-name">
              <span v-if="resource['url'] == null"></span>
              <span v-else>
                <span>{{ `${resource['resourceName']}` }}</span>
                <span>{{ `.${resource['url'].split('.').slice(-1)}` }}</span>
              </span>
            </div>
            <div v-if="resource['entityList'] !== null" class="entity-list">
              <div v-for="entity in resource['entityList']" :key="entity">
                <el-button
                  class="entity-btn"
                  type="text"
                  size="mini"
                  @click="searchEntity(entity)"
                >
                  {{ entity }}
                </el-button>
              </div>
            </div>
            <div class="extra">
              <div class="extra-info">
                <i class="el-icon-time"></i>
                {{ resource['updateTime'] }}
              </div>
              <div class="extra-info">
                <i class="el-icon-download"></i>
                {{ resource['download'] }} 下载
              </div>
            </div>
          </div>
          <div class="operations">
            <div class="full-width">
              <download-button
                :resourceID="resource['id']"
                :size="'medium'"
              ></download-button>
            </div>
            <div class="full-width">
              <add-to-package-button
                :resourceType="'resource'"
                :resourceID="String(resource.id)"
                :size="'medium'"
              ></add-to-package-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ResourceLink from '@/components/ResourceLink'
import DownloadButton from '@/components/Buttons/DownloadButton'
import AddToPackageButton from '@/components/Buttons/AddToPackageButton'
import merge from 'webpack-merge'

export default {
  name: 'ResourceListWithInfo',
  components: { ResourceLink, DownloadButton, AddToPackageButton },
  props: {
    resources: Array
  },
  methods: {
    searchEntity(entity) {
      this.$router.push({
        query: merge(this.$route.query, {
          q: entity,
          type: 0,
          page: 1,
          sort: 0
        })
      })
    }
  }
}
</script>

<style scoped>
.resource-info {
  /*height: 190px;*/
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
  /*height: 100%;*/
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
  justify-content: space-between;
  position: relative;
  margin-left: 1rem;
}

.entity-list {
  /* 这里的间距要减去按钮设置的间距，但不完全减 */
  margin-top: calc(0.5rem - 5px);
  display: flex;
  flex-wrap: wrap;
}

.entity-list div {
  margin-right: 10px;
  margin-top: 10px;
}

.extra {
  display: flex;
  margin-top: 0.8rem;
  /*position: absolute;*/
  /*bottom: 0;*/
  min-width: 220px;
}

.extra .extra-info {
  color: #909399;
  font-size: 0.9rem;
  line-height: 1rem;
  margin-right: 1rem;
}

.operations {
  display: flex;
  flex-direction: column;
  height: 100%;
  margin-left: 0.8rem;
  justify-content: center;
  align-items: center;
}

.operations button {
  margin-bottom: 12px;
}

.file-name {
  color: #909399;
}

.resource-name {
  font-size: 1.1rem;
  font-weight: bold;
  cursor: pointer;
}

.resource-name:hover {
  text-decoration: underline;
  text-underline-offset: 0.1rem;
}

.name-in-url {
  color: #555555;
  margin-bottom: 0.6rem;
}

.entity-btn {
  color: #555555;
}

/* 测试 */
.entity-list {
  margin-top: -0.6rem;
}
</style>
