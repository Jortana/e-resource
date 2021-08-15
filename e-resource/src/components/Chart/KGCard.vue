<template>
  <div class="graph-container">
    <div v-for="(info, index) in graphInfos" :key="index" class="graph-card">
      <card-graph :info="info"></card-graph>
    </div>
  </div>
</template>

<script>
import { recommend } from '@/api/entity'
import CardGraph from '@/components/Chart/CardGraph'
export default {
  name: 'KGCard',
  components: { CardGraph },
  data() {
    return {
      graphInfos: []
    }
  },
  mounted() {
    this.getInfo()
  },
  methods: {
    getInfo() {
      recommend().then((response) => {
        const { data } = response.data
        this.graphInfos = data
        console.log(this.graphInfos)
      })
    }
  }
}
</script>

<style>
.graph-container {
  display: flex;
  justify-content: space-between;
  flex-flow: row wrap;
}

.graph-card {
  flex: 0 0 32%;
  height: 300px;
  padding: 1rem;
  margin-bottom: 1.2rem;
  background: linear-gradient(
    to left top,
    rgba(255, 255, 255, 1),
    rgba(255, 255, 255, 0.8)
  );
  border-radius: 0.6rem;
  box-shadow: 6px 6px 20px rgba(122, 122, 122, 0.1);
  overflow: hidden;
}
</style>
