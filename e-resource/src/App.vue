<template>
  <div id="app">
    <router-view />
    <footer-main />
  </div>
</template>

<script>
import FooterMain from '@/components/FooterMain'
export default {
  name: 'App',
  components: { FooterMain }
}
</script>

<style>
#app {
  /*font-family: 'Avenir', Helvetica, Arial, sans-serif;*/
  font-family: 'Helvetica Neue', Helvetica, Arial, 'Microsoft Yahei',
    'Hiragino Sans GB', 'Heiti SC', 'WenQuanYi Micro Hei', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /*text-align: center;*/
  /*color: #2c3e50;*/
  display: flex;
  flex-direction: column;
}

textarea {
  font-family: 'Helvetica Neue', Helvetica, Arial, 'Microsoft Yahei',
    'Hiragino Sans GB', 'Heiti SC', 'WenQuanYi Micro Hei', sans-serif;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.full-width {
  width: 100%;
}
</style>
