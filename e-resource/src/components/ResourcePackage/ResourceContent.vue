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
        :class="
          btnMouseOver
            ? 'el-icon-error danger content-btn'
            : 'el-icon-error content-btn'
        "
        @mouseenter="btnMouseOver = true"
        @mouseleave="btnMouseOver = false"
      ></i>
    </el-popconfirm>
  </div>
</template>

<script>
import { deleteResource } from '@/api/package'

export default {
  name: 'ResourceContent',
  props: {
    type: String,
    content: String,
    id: Number,
    folderID: String
  },
  data() {
    return {
      mouseOver: false,
      btnMouseOver: false
    }
  },
  methods: {
    deleteResource() {
      const info = {
        folderID: this.folderID
      }
      switch (this.type) {
        case 'goal':
          info.goal = this.id
          break
        case 'key':
          info.key = this.id
          break
        case 'content':
          info.content = this.content
          break
        default:
          break
      }
      deleteResource(info).then((response) => {
        const { code } = response.data
        if (code === 200) {
          this.$emit('updateResource')
        } else {
          this.$message.error('删除失败，请稍后再试')
        }
      })
    }
  }
}
</script>

<style>
.content-btn {
  margin-left: 0;
  cursor: pointer;
}
</style>
