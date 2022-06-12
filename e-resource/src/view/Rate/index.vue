<template>
  <div>
    <nav-menu></nav-menu>
    <div class="main-container">
      <h1>人气排行</h1>
      <div class="rate-container"></div>
    </div>
  </div>
</template>

<script>
import NavMenu from '@/components/NavMenu'
import {
  hotResourceMore,
  newResourceMore,
  userRecommendResourceMore
} from '@/api/recommend'

export default {
  name: 'Rate',
  components: { NavMenu },
  created() {
    this.initData()
  },
  methods: {
    initData() {
      hotResourceMore().then((response) => {
        console.log(response)
      })
      newResourceMore().then((response) => {
        console.log(response)
      })
      userRecommendResourceMore().then((response) => {
        console.log(response)
      })
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
</style>
