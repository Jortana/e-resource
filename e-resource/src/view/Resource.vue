<template>
<div>
  <nav-menu></nav-menu>
  <div class="main-container flex">
    <div class="resource-info flex-1">
      <h2>{{ resource['resourceName'] }}</h2>
      <div class="resource-name" v-if="resource['url'] !== undefined">{{ resource['url'].split('/').slice(-1)[0] }}</div>
      <div class="related-resource">
        <h2>相关资源</h2>
        <div
          v-for="resource in relatedResources"
          :key="resource.id">
          {{ resource['resourceName'] }}
        </div>
      </div>
    </div>
    <div class="graph flex-1">
      <k-g-chart :entities="entities.entities" ref="chart"></k-g-chart>
    </div>
  </div>
</div>
</template>

<script>
import NavMenu from '@/components/NavMenu'
import KGChart from '@/components/Chart/KGChart'
import { resourceInfo, related } from '@/api/resource'
import { relatedEntity } from '@/api/entity'

export default {
  name: 'Resource',
  components: {
    NavMenu,
    KGChart
  },
  props: {
  },
  computed: {
    resourceID () {
      return this.$route.params.resourceID
    }
  },
  watch: {
    resourceID: {
      handler (resourceID) {
        // 获取资源信息
        resourceInfo(resourceID)
          .then(response => {
            if (response.data.code === 200) {
              this.resource = response.data.data
              // 获取相关实体
              console.log(response.data.data['entity'])
              relatedEntity(response.data.data['entity'])
                .then(entityResponse => {
                  console.log(entityResponse)
                  if (entityResponse.data.code === 200) {
                    this.entities.entities = entityResponse.data.data
                  }
                })
            } else {
              console.log('无此资源')
            }
          })
        // 获取相关资源
        related(resourceID)
          .then(response => {
            console.log(response)
            if (response.data.code === 200) {
              this.relatedResources = response.data.data
              console.log(this.relatedResources)
            }
          })
      },
      immediate: true
    }
  },
  data () {
    return {
      resource: {},
      entities: {
        entities: []
      },
      relatedResources: []
    }
  }
}
</script>

<style scoped>
.main-container {
  height: calc(100vh - 70px);
}

.graph {
  height: 100%;
}

.resource-info {
  margin-top: 1rem;
  position: relative;
}

.resource-info >>> h2 {
  color: #222226;
}

.resource-info .resource-name {
  color: #606266;
}

.related-resource {
  position: absolute;
  left: 0;
  bottom: 2rem;
}
</style>
