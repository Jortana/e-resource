<template>
  <div>
    <nav-menu></nav-menu>
    <router-view></router-view>
  </div>
</template>

<script>
import NavMenu from '@/components/NavMenu'
export default {
  name: 'User',
  components: {
    NavMenu
  }
}
</script>

<style scoped></style>
