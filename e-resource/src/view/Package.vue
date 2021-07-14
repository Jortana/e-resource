<template>
  <div>
    <nav-menu class="lg-container"></nav-menu>
    <div class="container">
      <div class="package-container flex">
        <!-- 左边栏 -->
        <div class="folder-list">
          <h3>我的资源包</h3>
          <div class="new-folder flex" @click="showInfoDialog">
            <i class="el-icon-circle-plus package-icon"></i>
            <span>新建资源包</span>
          </div>
          <div
            v-for="folder in packageFolders"
            :key="folder.folderID"
            @click="changeFolder(folder.folderID)"
          >
            <package-folder
              :curID="curID"
              :folder="folder"
              @confirmDelete="confirmDelete"
              @editFolderInfo="editFolderInfo"
            ></package-folder>
          </div>
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
              <el-button size="medium" type="primary" @click="deleteFolder">
                确 定
              </el-button>
            </span>
          </el-dialog>
          <!-- 创建收藏夹和修改收藏夹信息的对话框 -->
          <package-info
            v-if="infoVisible"
            :visible.sync="infoVisible"
            :originInfo="originInfo"
            @clearInfo="clearInfo"
            @updatePackages="getPackageFolders"
          ></package-info>
        </div>
        <!-- 资源包内容展示区域 -->
        <div class="list flex-1">
          <!-- 资源包名、简介等基本信息 -->
          <div v-if="curFolderInfo" class="basic-info">
            <div>{{ curFolderInfo.folderName }}</div>
            <div class="intro">{{ curFolderInfo.resourceNum }} 个内容</div>
            <div class="intro">
              {{ curFolderInfo.introduction }}
            </div>
          </div>
          <!-- 资源包中包含资源的展示区域 -->
          <resources :resourcesObj.sync="resources" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavMenu from '@/components/NavMenu'
import PackageFolder from '@/components/ResourcePackage/PackageFolder'
import PackageInfo from '@/components/ResourcePackage/PacgakeInfo'
import Resources from '@/components/ResourcePackage/Resources'
import merge from 'webpack-merge'
import { getFolders, getResources, deleteFolder } from '@/api/package'

export default {
  name: 'Package',
  components: { NavMenu, PackageFolder, PackageInfo, Resources },
  data() {
    return {
      packageFolders: [],
      packageInfo: '',
      curID: '', // 当前选中的资源包的 ID
      curFolderInfo: null, // 当前选中的资源包的信息
      resources: null, // 当前选中的资源包中包含的资源信息
      infoVisible: false, // 控制创建和修改资源包信息的对话框是否显示
      originInfo: null, // 待修改的资源包的原始信息
      deleteID: '', // 待删除的文件夹的 ID
      deleteVisible: false // 确认删除的对话框是否显示
    }
  },
  computed: {
    query() {
      return this.$route.query
    }
  },
  watch: {
    query: {
      handler(query) {
        const { id } = query
        this.getPackageResources(id)
        this.curID = id
      },
      immediate: true
    },
    curID: {
      handler(curID) {
        const folders = this.packageFolders
        const curFolderInfo = folders.find((folder) => {
          return folder.folderID === curID
        })
        this.curFolderInfo = curFolderInfo
      },
      immediate: true
    }
  },
  mounted() {
    this.getPackageFolders()
  },
  methods: {
    // 获取当前 ID 资源包中所包含的资源
    getPackageResources(id) {
      getResources(id).then((response) => {
        const { code, data } = response.data
        if (code === 200) {
          console.log(data)
          this.resources = { ...data }
        }
      })
    },
    // 获取所有资源包
    getPackageFolders() {
      const { id } = this.query
      getFolders().then((response) => {
        const { code, data: folders, message } = response.data
        console.log(response.data)
        if (code === 200) {
          this.packageFolders = folders
          // 设置当前选中的资源包
          if (id === undefined) {
            this.curID = folders[0].folderID
          }
          const curFolderInfo = folders.find((folder) => {
            return folder.folderID === this.curID
          })
          this.curFolderInfo = curFolderInfo
        } else {
          this.$message.error(message)
        }
      })
    },
    // 通过路由改变当前所选的文件夹
    changeFolder(id) {
      // 如果点击的文件夹的 id 和当前已经选中的 curID 相同则不进行操作
      if (id !== this.curID) {
        this.$router.push({
          query: merge(this.$route.query, { id })
        })
      }
    },
    // 展示创建和修改资源包的对话框
    showInfoDialog() {
      this.infoVisible = true
    },
    // 确认删除
    confirmDelete(id) {
      // 弹出确认删除的对话框
      this.deleteVisible = true
      // 设置待删除的资源包 ID
      this.deleteID = id
    },
    // 删除资源包
    deleteFolder() {
      const { deleteID, curID } = this
      deleteFolder(deleteID).then((response) => {
        const {
          data: { code }
        } = response
        if (code === 200) {
          this.$message.success('删除成功')
          // 删除的资源包是当前选中的
          if (deleteID === curID) {
            console.log('aa')
            this.$router.push('/package')
          }
          this.getPackageFolders()
        }
      })
      this.deleteVisible = false
    },
    // 接收修改文件夹的信息
    editFolderInfo(folderInfo) {
      // console.log(folderInfo)
      this.originInfo = folderInfo
      this.infoVisible = true
    },
    // 清除待修改文件夹的信息
    clearInfo() {
      this.originInfo = null
    }
  }
}
</script>

<style scoped>
.container {
  background-color: #f4f5f7;
  margin-top: 1rem;
  /*空出nav-menu的位置*/
  height: calc(100vh - 70px);
  padding-top: 1rem;
}

.package-container {
  margin: 0 auto;
  border-top-left-radius: 0.5rem;
  border-top-right-radius: 0.5rem;
  border: 1px solid #dcdfe6;
  border-bottom: none;
  width: 1000px;
  min-height: 100%;
  background-color: #fff;
}

.folder-list {
  width: 200px;
  /*background-color: #53a8ff;*/
  border-right: 1px solid #e4e7ed;
  /*padding: 1.5rem;*/
}

.folder-list h3 {
  font-weight: 500;
  color: #515151;
  font-size: 1rem;
  margin: 1.5rem;
}

.new-folder,
.folder {
  align-items: center;
  cursor: pointer;
  height: 2rem;
  padding: 1.5rem;
  transition: all 0.2s;
}

.package-icon {
  color: #ccd0d8;
  font-size: 1.5rem;
  margin-right: 0.5rem;
}

/* 资源包内容展示区域 */
.list {
  padding: 1.2rem;
}

.basic-info {
  border-bottom: 1px solid #e4e7ed;
}

.intro {
  color: #909399;
  margin-top: 0.5rem;
}

.intro:last-child {
  margin-bottom: 1rem;
}
</style>
