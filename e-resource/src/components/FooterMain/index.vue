<template>
  <div class="footer-container">
    <div class="footer-content">
      <p>
        系统为国家社会科学基金教育学一般课题
      </p>
      <p>
        “基础教育知识图谱构建方法与应用模型研究”（课题编号:BCA190087）研究成果之一
      </p>
      <p>
        Copyright © 2021-2022 All rights reserved. 南京师范大学教育科学学院
        版权所有
      </p>
      <p>联系方式：18362971103</p>
    </div>
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
  justify-content: center;
  height: 150px;
  width: 100%;
  background-color: #3f3c40;
  color: #fff;
  text-align: center;
}

.footer-content {
  margin: 1rem auto;
  width: 1200px;
}

.footer-content p {
  margin-top: 0.5rem;
}
</style>
