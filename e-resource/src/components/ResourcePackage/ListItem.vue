<template>
  <div
    class="flex item-container"
    @mouseover="mouseOver = true"
    @mouseleave="mouseOver = false"
  >
    <el-checkbox
      v-model="checked"
      class="checkbox"
      @change="changeHandler"
    ></el-checkbox>
    <svg class="type-icon" aria-hidden="true">
      <use
        v-if="resource['fileType'] === 'doc' || resource['fileType'] === 'docx'"
        xlink:href="#e-resource-icon-word"
      ></use>
      <use
        v-else-if="
          resource['fileType'] === 'ppt' || resource['fileType'] === 'pptx'
        "
        xlink:href="#e-resource-icon-ppt"
      ></use>
      <use
        v-else-if="resource['fileType'] === 'pdf'"
        xlink:href="#e-resource-icon-pdf"
      ></use>
      <use
        v-else-if="resource['fileType'] === 'video'"
        xlink:href="#e-resource-icon-video"
      ></use>
      <use v-else xlink:href="#e-resource-icon-unknown"></use>
    </svg>
    <div class="resource-name" @click="viewResource(resource.id)">
      {{ resource['resourceName'] }}
    </div>
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
    <!-- <div
      class="icon-more el-icon-more"
      @mouseover="operationVisible = true"
      @mouseleave="operationVisible = false"
    ></div> -->
    <!-- 删除图标 -->
    <div v-if="false">
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

    <!-- 隐藏的a元素，用来在新窗口打开资源页面 -->
    <a
      v-show="false"
      ref="resourceTarget"
      class="resource-target"
      href=""
      target="_blank"
    ></a>

    <!-- 隐藏的a元素，用来在新窗口下载 -->
    <a v-show="false" ref="downloadTarget" href="" target="_blank"></a>

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
import { download } from '@/api/resource'

export default {
  name: 'ListItem',
  props: {
    resource: Object,
    id: String
  },
  data() {
    return {
      mouseOver: false,
      btnMouseOver: false,
      checked: false,
      operationVisible: false,
      deleteVisible: false
    }
  },
  methods: {
    viewResource(resourceID) {
      const target = this.$refs.resourceTarget
      target.setAttribute(
        'href',
        `${window.location.origin}/e-resource/#/resource/${resourceID}`
      )
      target.click()
    },
    deleteResource() {
      const info = {
        folderID: this.id,
        resourceID: this.resource.id
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
    changeHandler(isChecked) {
      console.log(this.resource)
      if (isChecked === true) {
        this.$emit(
          'addListItem',
          'checkedResources',
          this.resource.collectionId
        )
      } else {
        this.$emit(
          'deleteListItem',
          'checkedResources',
          this.resource.collectionId
        )
      }
    },
    changeCheck() {
      this.checked = !this.checked
    },
    // 请求生成资源包，获取地址
    download() {
      // download(this.resourceID)
      download(this.resourceID).then((response) => {
        // const { code, data } = response.data
        // if (code === 200) {
        //   window.location.href = data
        // }
        console.log(response)
        if (!response) return
        const blob = new Blob([response.data], { type: response.data.type }) // 构造一个blob对象来处理数据，并设置文件类型

        if (window.navigator.msSaveOrOpenBlob) {
          // 兼容IE10
          navigator.msSaveBlob(blob, this.filename)
        } else {
          const href = URL.createObjectURL(blob) // 创建新的URL表示指定的blob对象
          const a = document.createElement('a') // 创建a标签
          a.style.display = 'none'
          a.href = href // 指定下载链接
          // a.download = '文件名' // 指定下载文件名
          a.click() // 触发下载
          URL.revokeObjectURL(a.href) // 释放URL对象
        }
      })
    }
  }
}
</script>

<style>
.item-container {
  align-items: center;
}

.resource-name {
  text-overflow: ellipsis;
  /* 元素的宽度: 超出时， 有一个确切的计算值 */
  /* 元素宽度： 不只可以是元素的width。max-width，以及flex 布局都是可以的。 */
  max-width: 100%;
  /* overflow：计算值非visible */
  /*  overflow确实是非visible， 但是计算值并不是设定值， 因为css有个叫inhert的关键字  */
  overflow: hidden;
  /* 不折行:white-space: pre || nowrap */
  /* pre也是可以的， 因为这个属性的设置主要就是为了不折行。  */
  white-space: nowrap;
  cursor: pointer;
}

.el-icon-error {
  margin-left: 5px;
  color: #c0c4cc;
}

.danger {
  color: #f56c6c;
}

.checkbox {
  margin-right: 0.5rem !important;
}

.operation-popover {
  margin-left: auto;
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
  letter-spacing: 1px;
}

.operation-btn:hover {
  background-color: #f0f5fa;
}
</style>
