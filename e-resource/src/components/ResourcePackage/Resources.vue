<template>
  <div class="content-container">
    <el-tabs v-model="activeTab">
      <!-- 资源 -->
      <el-tab-pane label="资源列表" name="list">
        <div v-if="resources.resources" class="section">
          <div class="list">
            <list-item
              v-for="resource in resources.resources"
              ref="listItem"
              :key="resource.id"
              :resource="resource"
              :id="folderID"
              @updateResource="$emit('updateResource')"
              @addListItem="addListItem"
              @deleteListItem="deleteListItem"
            />
          </div>
          <div
            v-if="resources.resources && resources.resources.length"
            class="batch-operation"
          >
            <el-checkbox
              v-model="checkAllResources"
              @change="changeAllResources(checkAllResources)"
            >
              全选
            </el-checkbox>
            <el-button
              class="x-mini-btn"
              type="danger"
              plain
              size="mini"
              @click="deleteConfirm(checkedResources)"
            >
              删除
            </el-button>
          </div>
          <div v-else class="empty">暂未收藏资源</div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="知识点" name="entity">
        <div
          v-for="(content, index) in resources.content"
          :key="index"
          class="content-row-container"
        >
          <resource-content
            ref="entityItem"
            :content="content.content"
            :collectionId="content.collectionId"
            :folderID="folderID"
            :type="'content'"
            @updateResource="$emit('updateResource')"
            @addListItem="addListItem"
            @deleteListItem="deleteListItem"
          />
        </div>
        <div
          v-if="resources.content && resources.content.length"
          class="batch-operation"
        >
          <el-checkbox
            v-model="checkAllEntities"
            @change="changeAllEntities(checkAllEntities)"
          >
            全选
          </el-checkbox>
          <el-button class="x-mini-btn" type="danger" plain size="mini">
            删除
          </el-button>
        </div>
        <div v-else class="empty">暂未收藏知识点</div>
      </el-tab-pane>
      <el-tab-pane label="学习目标" name="goal">
        <div
          v-for="(goal, index) in resources.goal"
          :key="index"
          class="content-row-container"
        >
          <resource-content
            ref="goalItem"
            :content="goal.text"
            :folderID="folderID"
            :id="goal.id"
            :type="'goal'"
            @updateResource="$emit('updateResource')"
          />
        </div>
        <div
          v-if="resources.goal && resources.goal.length"
          class="batch-operation"
        >
          <el-checkbox v-model="checkAllGoals" @change="changeAllGoals">
            全选
          </el-checkbox>
          <el-button class="x-mini-btn" type="danger" plain size="mini">
            删除
          </el-button>
        </div>
        <div v-else class="empty">暂未收藏学习目标</div>
      </el-tab-pane>
      <el-tab-pane label="学习重难点" name="key">
        <div
          v-for="(key, index) in resources.key"
          :key="index"
          class="content-row-container"
        >
          <resource-content
            ref="keyItem"
            :content="key.text"
            :folderID="folderID"
            :id="key.id"
            :type="'key'"
            @updateResource="$emit('updateResource')"
          />
        </div>
        <div
          v-if="resources.key && resources.key.length"
          class="batch-operation"
        >
          <el-checkbox v-model="checkAllKeys" @change="changeAllKeys">
            全选
          </el-checkbox>
          <el-button class="x-mini-btn" type="danger" plain size="mini">
            删除
          </el-button>
        </div>
        <div v-else class="empty">暂未收藏学习重难点</div>
      </el-tab-pane>
    </el-tabs>

    <!-- 隐藏的a元素，用来在新窗口打开资源页面 -->
    <a
      v-show="false"
      ref="resourceTarget"
      class="resource-target"
      href=""
      target="_blank"
    ></a>
    <!-- {{ resources }} -->

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
        <el-button size="medium" type="primary" @click="deleteMulti">
          确 定
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import ListItem from '@/components/ResourcePackage/ListItem'
import ResourceContent from '@/components/ResourcePackage/ResourceContent'
import { deleteMulti } from '@/api/package'
export default {
  name: 'Resources',
  components: { ListItem, ResourceContent },
  props: {
    resourcesObj: {
      type: Object,
      default: null
    },
    folderID: String
  },
  data() {
    return {
      resources: null,
      activeTab: 'list',
      checkedResources: [],
      checkedEntities: [],
      checkedGoals: [],
      checkedKeys: [],
      checkAllResources: false,
      checkAllEntities: false,
      checkAllGoals: false,
      checkAllKeys: false,
      deleteVisible: false,
      deleteName: ''
    }
  },
  watch: {
    resourcesObj: {
      handler(resourcesObj) {
        this.resources = { ...resourcesObj }
      },
      immediate: true
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
    addListItem(listName, content) {
      this[listName].push(content)
    },
    deleteListItem(listName, content) {
      const index = this[listName].indexOf(content)
      this[listName].splice(index, 1)
    },
    changeAllResources(checked) {
      this.$refs.listItem.forEach((item) => {
        item.changeCheck(checked)
      })
    },
    changeAllEntities(checked) {
      this.$refs.entityItem.forEach((item) => {
        item.changeCheck(checked)
      })
    },
    changeAllGoals(checked) {
      this.$refs.goalItem.forEach((item) => {
        item.changeCheck(checked)
      })
    },
    changeAllKeys(checked) {
      this.$refs.keyItem.forEach((item) => {
        item.changeCheck(checked)
      })
    },
    deleteMulti() {
      console.log(this.deleteName)
      const resourceIDs = this[this.deleteName]
      console.log(resourceIDs)
      return
      // eslint-disable-next-line no-unreachable
      deleteMulti({
        folderID: this.folderID,
        resourceIDs
      }).then((response) => {
        console.log(response)
      })
    },
    deleteConfirm(chechedName) {
      this.deleteVisible = true
      this.deleteName = chechedName
    }
  }
}
</script>

<style>
.content-container {
  margin-top: 0.5rem;
}

.el-tabs__item {
  font-size: 1rem;
}

.section,
.empty {
  color: #606266;
}

.section h4 {
  color: #303133;
}

.section .list > div,
.content-row-container {
  margin-top: 0.8rem;
}

.section .list > div:first-child,
.content-row-container:first-child {
  margin-top: 0;
}

.section .list li div:hover {
  text-decoration: underline;
  text-underline-offset: 0.2rem;
}

.type-icon {
  height: 1.2rem;
  width: 1.2rem;
  margin-right: 0.3rem;
}

.batch-operation {
  display: flex;
  align-items: center;
  margin-top: 1rem;
}

.x-mini-btn {
  margin-left: 1rem;
  padding: 4px 6px;
}
</style>
