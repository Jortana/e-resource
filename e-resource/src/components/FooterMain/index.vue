<template>
  <div class="footer-container">
    <p>
      Copyright © 2010-2022 All rights reserved. 南京师范大学教育科学学院
      版权所有
    </p>
    <p>联系方式：025-83598571（随园） 025-85891035（仙林）</p>
  </div>
</template>

<script>
export default {
  name: 'FooterMain'
}
</script>

<style scoped>
.footer-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 80px;
  width: 100%;
  background-color: #3f3c40;
  color: #fff;
}

.footer-container p:first-child {
  margin-bottom: 0.5rem;
}
</style>
