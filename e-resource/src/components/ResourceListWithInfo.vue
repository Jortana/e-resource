<template>
  <div class="resource-list" v-if="resources.length > 0">
    <div
      v-for="resource in resources"
      :key="resource.id"
    >
      <div class="resource-info">
        <div class="info">
          <resource-link :resource="resource"></resource-link>
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
</template>

<script>
import ResourceLink from '@/components/ResourceLink'
import DownloadButton from '@/components/DownloadButton'

export default {
  name: 'ResourceListWithInfo',
  components: { ResourceLink, DownloadButton },
  props: {
    resources: Array
  }
}
</script>

<style scoped>

</style>
