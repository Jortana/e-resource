<template>
  <div class="logo-container" @click="goHome">
    <div class="logo"></div>
    <div class="title"><h1>智能学伴</h1></div>
  </div>
</template>

<script>
export default {
  name: 'Logo',
  methods: {
    goHome () {
      this.$router.push({
        path: '/'
      })
    }
  }
}
</script>

<style scoped>
.logo-container {
  display: flex;
  cursor: pointer;
}

.logo {
  width: 50px;
  height: 50px;
  background-color: #aaa;
}

.title {
  height: 50px;
  line-height: 50px;
  font-size: 0.8rem;
  margin-left: 0.5rem;
  margin-right: 1rem;
}
</style>
