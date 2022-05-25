<template>
  <div
    class="content-row"
    @mouseover="mouseOver = true"
    @mouseleave="mouseOver = false"
  >
    <el-checkbox
      v-model="checked"
      class="checkbox"
      @change="changeHandler"
    ></el-checkbox>
    <span class="content">{{ content }}</span>
    <el-popover
      :close-delay="100"
      class="operation-popover"
      trigger="hover"
      transition="el-zoom-in-top"
      width="150"
    >
      <div class="operation-container">
        <div class="operation-btn flex" @click="download">下载</div>
        <div class="operation-btn flex" @click="deleteVisible = true">删除</div>
      </div>
      <div
        slot="reference"
        class="el-icon-more"
        @mouseover="operationVisible = true"
        @mouseleave="operationVisible = false"
      ></div>
    </el-popover>

    <!-- 确认删除的对话框 -->
    <el-dialog
      :visible.sync="deleteVisible"
      title="确认删除"
      width="350px"
      center
    >
      <span>确认删除吗？</span>
      <span slot="footer" class="dialog-footer">
        <el-button size="medium" @click="deleteVisible = false">
          取 消
        </el-button>
        <el-button size="medium" type="primary" @click="deleteResource">
          确 定
        </el-button>
      </span>
    </el-dialog>
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
      btnMouseOver: false,
      checked: false,
      deleteVisible: false
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
    },
    changeCheck(checked) {
      this.checked = checked
    },
    download() {},
    changeHandler(isChecked) {
      let listName
      switch (this.type) {
        case 'goal':
          listName = 'checkedGoals'
          isChecked === true
            ? this.$emit('addListItem', listName, this.id)
            : this.$emit('deleteListItem', listName, this.id)
          break
        case 'key':
          listName = 'checkedKeys'
          isChecked === true
            ? this.$emit('addListItem', listName, this.id)
            : this.$emit('deleteListItem', listName, this.id)
          break
        case 'content':
          listName = 'checkedEntities'
          isChecked === true
            ? this.$emit('addListItem', listName, this.content)
            : this.$emit('deleteListItem', listName, this.content)
          break
        default:
          break
      }
    }
  }
}
</script>

<style>
.content-row {
  display: flex;
  align-items: center;
}

.content-btn {
  margin-left: 0;
  cursor: pointer;
}

.content {
  color: #606266;
}

.operation-popover {
  margin-left: auto;
}
</style>
