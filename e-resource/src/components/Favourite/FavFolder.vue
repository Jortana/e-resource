<template>
  <div
    :class="active ? 'active' : 'no-active'"
    class="fav-folder flex"
    @mouseover="hover = true"
    @mouseleave="hover = false"
  >
    <i class="el-icon-folder-opened fav-icon"></i>
    <span>{{ favFolder.name }}</span>
    <el-popover
      :visible-arrow="false"
      :close-delay="100"
      trigger="hover"
      transition="el-zoom-in-top"
      width="150"
    >
      <div class="operation-container">
        <div class="operation-btn flex">编辑</div>
        <div class="operation-btn flex">删除</div>
      </div>
      <div v-show="hover" slot="reference" class="operation-dot"></div>
    </el-popover>
  </div>
</template>

<script>
export default {
  name: 'FavFolder',
  props: {
    favFolder: Object,
    curID: Number
  },
  data() {
    return {
      hover: false
    }
  },
  computed: {
    active() {
      return this.favFolder.id === this.curID
    }
  }
}
</script>

<style scoped>
.fav-folder {
  position: relative;
}

.no-active:hover {
  background-color: #f0f5fa;
  color: #79bbff;
}

.active {
  color: #fff;
  background-color: #66b1ff;
}

.active .fav-icon {
  color: #fff;
}

.fav-icon {
  color: #ccd0d8;
  font-size: 1.5rem;
  margin-right: 0.5rem;
}

.operation-dot {
  position: absolute;
  right: 1.2rem;
}

.operation-dot::after {
  content: '•••';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%) rotate(90deg);
  font-size: 1rem;
  letter-spacing: 1px;
  margin-top: 2px;
}

.operation-container {
  margin: -7px -12px;
}

.operation-btn {
  font-size: 1rem;
  height: 2rem;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.operation-btn:hover {
  background-color: #f0f5fa;
}
</style>
