<template>
  <div class="logo-container" @click="goHome">
    <div class="logo">
      <!--临时logo created by 买大米种水稻 2022/04/16-->
      <img src="../../assets/WDM.jpg" STYLE="width: 120%"/>
    </div>
    <div class="title"><h1>智慧学伴</h1></div>
  </div>
</template>

<script>
export default {
  name: 'Logo',
  methods: {
    goHome() {
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
  /*background-color: #aaa;*/
}

.title {
  height: 50px;
  line-height: 50px;
  font-size: 0.8rem;
  margin-left: 0.5rem;
  margin-right: 1rem;
}
</style>
