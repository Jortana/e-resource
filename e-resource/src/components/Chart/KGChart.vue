<template>
  <div id="chart" class="chart"></div>
</template>

<script>
import * as echarts from 'echarts'
export default {
  name: 'KGChart',
  props: {
    entities: {
      type: Array,
      required: true
    }
  },
  mounted () {
    // this.initCharts()
    setTimeout(() => {
      window.onresize = () => {
        this.chart.resize()
      }
    }, 200)
  },
  watch: {
    entities: {
      handler (entities) {
        this.resetData()
        this.formatData(entities)
      }
    }
  },
  data () {
    return {
      chart: '',
      nodes: [],
      links: [],
      nodeBasicSize: 30,
      themeColor: ['#6f88fc', '#ff7582']
      // ['#d87c7c', '#919e8b', '#d7ab82', '#6e7074', '#61a0a8', '#efa18d', '#787464', '#cc7e63', '#724e58', '#4b565b']
    }
  },
  methods: {
    /**
     * 重置数据
     */
    resetData () {
      this.chart = ''
      this.nodes = []
      this.links = []
    },
    /**
     * 节点点击事件
     */
    nodeClick (node) {
      if (node.data.name === this.$route.query.q) {
        return
      }
      this.$router.push({
        path: '/search',
        query: {
          q: node.data.name
        }
      })
    },
    /**
     * 格式化数据为echarts需要的格式
     */
    formatData (entities) {
      let nodes = new Set()
      let links = new Set()
      let directEntities = []
      for (let entity of entities) {
        nodes.add(entity['entityName'])
        directEntities.push(entity['entityName'])
        for (let relEntity of entity['relatedEntity']) {
          nodes.add(relEntity)
          links.add({
            source: entity['entityName'],
            target: relEntity
          })
        }
      }
      for (let node of nodes) {
        if (directEntities.indexOf(node) !== -1) {
          this.nodes.push({
            name: node,
            symbolSize: this.nodeBasicSize * 1.5,
            itemStyle: {
              color: this.themeColor[0]
            }
          })
        } else {
          this.nodes.push({
            name: node,
            symbolSize: this.nodeBasicSize,
            itemStyle: {
              color: this.themeColor[1]
            }
          })
        }
      }
      this.links.push(...links)
      this.initCharts()
    },
    /**
     * 设置echarts配置项
     */
    initCharts () {
      if (!this.chart) {
        this.chart = echarts.init(document.getElementById('chart'))
        this.chart.on('click', (event) => {
          if (event.dataType === 'node') {
            // 判断点击的是图表的节点部分
            this.nodeClick(event)
          }
        })
      }
      let nodes = JSON.parse(JSON.stringify(this.nodes))
      let links = JSON.parse(JSON.stringify(this.links))
      // 指定图表的配置项和数据
      let option = {
        // 动画更新变化时间
        animationDurationUpdate: 500,
        animationEasingUpdate: 'quinticInOut',
        tooltip: {
          show: false
        },
        series: [
          {
            type: 'graph',
            layout: 'force',
            legendHoverLink: true, // 是否启用图例 hover(悬停) 时的联动高亮。
            focus: 'adjacency',
            force: {
              edgeLength: 50,
              repulsion: 200
            },
            label: {
              show: true,
              position: 'top'
            },
            // symbolSize: this.nodeBasicSize,
            roam: true,
            draggable: true, // 每个节点的拖拉
            nodes: nodes,
            links: links
          }
        ]
      }
      this.chart.setOption(option)
      this.chart.resize()
    }
  }
}
</script>

<style scoped>
.chart {
  height: 100%;
}
</style>
