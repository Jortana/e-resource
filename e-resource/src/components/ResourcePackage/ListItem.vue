<template>
  <li @mouseover="mouseOver = true" @mouseleave="mouseOver = false">
    <!-- {{ resource['fileType'] }} -->
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
    <!-- 删除图标 -->
    <div>
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
  </li>
</template>

<script>
export default {
  name: 'ListItem',
  props: {
    resource: Object
  },
  data() {
    return {
      mouseOver: false,
      btnMouseOver: false
    }
  },
  methods: {
    viewResource(resourceID) {
      const target = this.$refs.resourceTarget
      target.setAttribute(
        'href',
        `${window.location.origin}/resource/${resourceID}`
      )
      target.click()
    },
    deleteResource() {
      console.log('dedele')
    }
  }
}
</script>

<style>
.resource-name {
  text-overflow: ellipsis;
  /* 元素的宽度: 超出时， 有一个确切的计算值 */
  /* 元素宽度： 不只可以是元素的width。max-width，以及flex 布局都是可以的。 */
  max-width: 300px;
  /* overflow：计算值非visible */
  /*  overflow确实是非visible， 但是计算值并不是设定值， 因为css有个叫inhert的关键字  */
  overflow: hidden;
  /* 不折行:white-space: pre || nowrap */
  /* pre也是可以的， 因为这个属性的设置主要就是为了不折行。  */
  white-space: nowrap;
}

.el-icon-error {
  margin-left: 0.5rem;
  color: #c0c4cc;
}

.danger {
  color: #f56c6c;
}
</style>
