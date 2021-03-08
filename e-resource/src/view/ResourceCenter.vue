<template>
  <div>
    <nav-menu></nav-menu>
  </div>
</template>

<script>
import NavMenu from '@/components/NavMenu'
export default {
  name: 'ResourceCenter',
  components: {
    NavMenu
  },
  data () {
    console.log(this.$route.query)
    return {
      searchContent: ''
    }
  }
}
</script>

<style scoped>

</style>
