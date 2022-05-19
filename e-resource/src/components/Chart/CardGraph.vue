<template>
  <!-- <div> -->
  <!-- {{ info }} -->
  <div ref="chart" class="chart"></div>
  <!-- </div> -->
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'CardGraph',
  props: {
    info: Object
  },
  data() {
    return {
      chart: '',
      nodes: [],
      links: [],
      hiddenNodes: {},
      nodeBasicSize: 30,
      themeColor: ['#6f88fc', '#ff7582']
      // ['#d87c7c', '#919e8b', '#d7ab82', '#6e7074', '#61a0a8', '#efa18d', '#787464', '#cc7e63', '#724e58', '#4b565b']
    }
  },
  watch: {
    info: {
      handler(newInfo) {
        if (newInfo !== {}) {
          this.resetData()
          this.formatData(this.info)
        }
      }
    }
  },
  mounted() {
    // 当 DOM 大小改变的时候 resize 图谱
    setTimeout(() => {
      window.onresize = () => {
        this.chart.resize()
      }
    }, 200)
  },
  methods: {
    /**
     * 重置数据
     */
    resetData() {
      this.chart = ''
      this.nodes = []
      this.links = []
    },
    /**
     * 格式化数据为 echarts 需要的格式
     */
    formatData(info) {
      if (info === {}) {
        return
      }
      const { node: entities } = info
      // 存储节点的 Set
      const nodes = new Set()
      // 存储边的 Set
      const links = new Set()
      // const directEntities = []
      // 筛选出节点和节点关系
      for (const entity of entities) {
        nodes.add(entity['entityName'])
        // 相连的默认直接显示
        for (const relNode of entity['connect']) {
          nodes.add(relNode)
          links.add({
            source: entity['entityName'],
            target: relNode
          })
        }
        // 推荐的节点默认不显示
        this.hiddenNodes[entity['entityName']] = []
        for (const relNode of entity['disconnect']) {
          this.hiddenNodes[entity['entityName']] = [
            ...this.hiddenNodes[entity['entityName']],
            {
              name: relNode,
              symbolSize: this.nodeBasicSize,
              itemStyle: {
                color: this.themeColor[1]
              },
              hidden: true
            }
          ]
          // 关系全部加上，节点和连线会同步显示
          links.add({
            source: entity['entityName'],
            target: relNode
          })
        }
      }
      // 给节点设置属性
      for (const node of nodes) {
        this.nodes.push({
          name: node,
          symbolSize: this.nodeBasicSize * 1.2,
          itemStyle: {
            color: this.themeColor[0]
          }
        })
      }
      this.links = [...links]
      this.initCharts()
    },
    /**
     * 点击节点展开
     */
    nodeClick(node) {
      const nodeName = node.name
      let nodes = this.hiddenNodes[nodeName]
      // 为空则不做操作
      if (nodes === undefined || nodes === []) {
        return
      }
      const setHidden = !nodes[0].hidden
      // 添加到显示的节点中，并设置 hidden 属性为 false
      nodes.forEach((node) => {
        node.hidden = setHidden
      })
      // 如果是隐藏变为显示则添加，否则删除
      if (setHidden === false) {
        // 已有的节点名称
        const nodeNames = []
        this.nodes.forEach((node) => {
          nodeNames.push(node.name)
        })
        // 避免重复添加节点
        nodes = nodes.filter((node) => {
          return nodeNames.indexOf(node.name) === -1
        })
        this.nodes = [...this.nodes, ...nodes]
      } else {
        // 需要删除的节点的 name
        const nodeNames = []
        nodes.forEach((node) => {
          nodeNames.push(node.name)
        })
        this.nodes = this.nodes.filter((node) => {
          return nodeNames.indexOf(node.name) === -1
        })
      }

      const option = this.chart.getOption()
      option.series[0].nodes = [...this.nodes]
      this.chart.setOption(option)
    },
    /**
     * 双击节点跳转搜索页面
     */
    nodeDblClick(node) {
      this.$router.push({
        path: '/search',
        query: {
          q: node.data.name
        }
      })
    },
    /**
     * 设置echarts配置项
     */
    initCharts() {
      if (!this.chart) {
        this.chart = echarts.init(this.$refs.chart)
        // 设置点击事件
        this.chart.on('click', (event) => {
          if (event.dataType === 'node') {
            // 判断点击的是图表的节点部分
            this.nodeClick(event)
          }
        })
        // 设置双击事件
        this.chart.on('dblclick', (event) => {
          if (event.dataType === 'node') {
            // 判断点击的是图表的节点部分
            this.nodeDblClick(event)
          }
        })
      }
      // const nodes = JSON.parse(JSON.stringify(this.nodes))
      const nodes = [...this.nodes]
      const links = [...this.links]
      // const { subject: title } = this.info
      // 指定图表的配置项和数据
      const option = {
        animationDelayUpdate: 500,
        // 动画更新变化时间
        animationDurationUpdate: 500,
        animationEasingUpdate: 'quinticInOut',
        tooltip: {
          show: false
        },
        // 标题
        title: {
          // text: title
        },
        // 图谱
        series: [
          {
            type: 'graph',
            layout: 'force',
            legendHoverLink: true, // 是否启用图例 hover(悬停) 时的联动高亮。
            focus: 'adjacency',
            force: {
              edgeLength: 60,
              repulsion: 80
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

<style>
.chart {
  width: 100%;
  height: 100%;
}
</style>
