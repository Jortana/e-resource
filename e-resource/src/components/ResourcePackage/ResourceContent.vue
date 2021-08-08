<template>
  <div @mouseover="mouseOver = true" @mouseleave="mouseOver = false">
    {{ content }}
    <el-popconfirm
      :visible-arrow="false"
      confirm-button-text="确定"
      cancel-button-text="取消"
      confirm-button-type="text"
      icon="el-icon-info"
      icon-color="#f56c6c"
      title="确定删除吗？"
      @confirm="deleteResource"
    >
      <i
        v-show="mouseOver"
        slot="reference"
        :class="btnMouseOver ? 'el-icon-error danger' : 'el-icon-error'"
        @mouseenter="btnMouseOver = true"
        @mouseleave="btnMouseOver = false"
      ></i>
    </el-popconfirm>
  </div>
</template>

<script>
export default {
  name: 'ResourceContent',
  props: {
    content: String
  },
  data() {
    return {
      mouseOver: false,
      btnMouseOver: false
    }
  }
}
</script>

<style>
.el-icon-error {
  margin-left: 0;
  cursor: pointer;
}
</style>
