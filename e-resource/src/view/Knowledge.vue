<template>
<div>
  <nav-menu></nav-menu>
  <div class="main-container flex">
    <div class="entity-info flex-1">
      <h2>{{ entityInfo['entityName'] }}</h2>
      <div class="flex">
        <div class="properties flex-1">
          <div
            class="flex prop-row"
            v-for="(value, name, index) in entityInfo.properties"
            :key="index"
            v-if="index < Object.keys(entityInfo.properties).length / 2">
            <div class="flex-1 prop-name">
              {{ name }}
            </div>
            <div class="flex-1">
              {{ value }}
            </div>
<!--            <dl class="inline-flex">-->
<!--              <dt>{{ name }}</dt>-->
<!--              <dd>{{ value }}</dd>-->
<!--            </dl>-->
          </div>
        </div>
        <div class="properties flex-1">
          <div
            class="flex prop-row"
            v-for="(value, name, index) in entityInfo.properties"
            :key="index"
            v-if="index >= Object.keys(entityInfo.properties).length / 2">
            <div class="flex-1 prop-name">
              {{ name }}
            </div>
            <div class="flex-1">
              {{ value }}
            </div>
          </div>
        </div>
      </div>
<!--      <h3>相关资源</h3>-->
    </div>
    <div class="graph flex-1">
      <k-g-chart :entities="relatedEntities" ref="chart"></k-g-chart>
    </div>
  </div>
</div>
</template>

<script>
import NavMenu from '@/components/NavMenu'
import KGChart from '@/components/Chart/KGChart'
import { properties, relatedEntity } from '@/api/entity'
import { record } from '@/api/record'

export default {
  name: 'Knowledge',
  components: {
    NavMenu,
    KGChart
  },
  computed: {
    entityName () {
      return this.$route.params.entityName
    }
  },
  watch: {
    entityName: {
      handler (entityName) {
        // 获取知识属性
        properties(entityName)
          .then(response => {
            if (response.data.code === 200) {
              this.entityInfo = response.data.data
              delete this.entityInfo.properties.name
              console.log(this.entityInfo)
            }
          })
        // 获取相关实体
        relatedEntity(entityName)
          .then(response => {
            if (response.data.code === 200) {
              response.data.data.forEach(entity => {
                if (entity.entityName === entityName) {
                  this.relatedEntities = [entity]
                }
              })
            }
          })
        // 上传用户记录
        this.record()
      },
      immediate: true
    }
  },
  data () {
    return {
      entityInfo: {},
      relatedEntities: []
    }
  },
  methods: {
    record () {
      record({
        entityName: String(this.entityName)
      })
    }
  }
}
</script>

<style scoped>
.entity-info >>> h2 {
  color: #222226;
}

.properties {
  margin-top: 1rem;
}

.properties:first-child {
  margin-right: 2rem;
}

.prop-row {
  font-size: .8rem;
  line-height: 1rem;
  margin-bottom: .3rem;
}

.prop-row .prop-name {
  color: #909399;
  font-weight: bold;
}

.main-container h3 {
  margin-top: 3rem;
}
</style>
