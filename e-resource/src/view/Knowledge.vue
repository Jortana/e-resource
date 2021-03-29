<template>
<div>
  <nav-menu></nav-menu>
  <div class="main-container flex">
    {{ entityName }}
  </div>
</div>
</template>

<script>
import NavMenu from '@/components/NavMenu'
export default {
  name: 'Knowledge',
  components: {
    NavMenu
  },
  computed: {
    entityName () {
      return this.$route.params.entityName
    }
  }
}
</script>

<style scoped>

</style>
