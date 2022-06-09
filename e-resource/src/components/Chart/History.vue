<template>
  <div class="node-container">
    <span v-for="(node, index) in nodes" :key="index">
      <span class="node-name" @click="nodeClickHandler(node)">{{ node }}</span>
      <span v-if="index < nodes.length - 1">＞</span>
    </span>
  </div>
</template>

<script>
export default {
  name: 'History',
  data() {
    return {
      nodes: []
    }
  },
  computed: {
    query() {
      return this.$route.query
    }
  },
  watch: {
    query: {
      handler() {
        this.getHistory()
      },
      immediate: true
    }
  },
  methods: {
    getHistory() {
      this.nodes = [...this.$store.state.graphHistory]
      if (this.nodes.length === 0) {
        this.nodes.push(this.query.q)
      }
    },
    nodeClickHandler(node) {
      this.$store.commit('backHistory', node)
      this.getHistory()
      if (node !== this.$route.query.q) {
        this.$router.push({
          path: '/search',
          query: {
            q: node
          }
        })
      }
    }
  }
}
</script>

<style scoped>
.node-container {
  font-size: 0.9rem;
}

.node-name {
  cursor: pointer;
  height: 1.3rem;
  display: inline-block;
}

.node-name:hover {
  color: #409eff;
  text-decoration: underline;
  text-underline-offset: 2px;
}
</style>
