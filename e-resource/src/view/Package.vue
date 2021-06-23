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
            :key="folder.id"
            @click="changeFolder(folder.id)"
          >
            <package-folder :curID="curID" :folder="folder"></package-folder>
          </div>
          <!-- 创建收藏夹和修改收藏夹信息的对话框 -->
          <package-info
            v-if="infoVisible"
            :visible.sync="infoVisible"
          ></package-info>
        </div>
        <!-- 资源包内容展示区域 -->
        <div class="list flex-1">
          <!-- 资源包名、简介等基本信息 -->
          <div class="basic-info">
            <div>资源包1</div>
            <div class="intro">100 个内容</div>
            <div class="intro">
              Lorem ipsum dolor sit amet consectetur adipisicing elit. Eius,
              cumque error quos natus similique, ut ipsa doloremque perspiciatis
              nihil necessitatibus esse assumenda aut minima quibusdam expedita
              libero ipsam fuga? Nostrum.
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavMenu from '@/components/NavMenu'
import PackageFolder from '@/components/ResourcePackage/PackageFolder'
import PackageInfo from '@/components/ResourcePackage/PacgakeInfo'
import merge from 'webpack-merge'

export default {
  name: 'Package',
  components: { NavMenu, PackageFolder, PackageInfo },
  data() {
    return {
      packageFolders: [],
      packageInfo: '',
      curID: '',
      infoVisible: false // 控制创建和修改资源包信息的对话框是否显示
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
        this.getPackageInfo(id)
        this.curID = id
      },
      immediate: true
    }
  },
  mounted() {
    this.getPackageFolders()
  },
  methods: {
    getPackageInfo(id) {
      console.log(id)
    },
    getPackageFolders() {
      const { id } = this.query
      this.packageFolders = [
        {
          id: '1',
          name: '资源包1'
        },
        {
          id: '2',
          name: '资源包2'
        }
      ]
      if (id === undefined) {
        this.curID = '1'
      }
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
  width: 1000px;
  height: 100%;
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
  padding-bottom: 1rem;
  border-bottom: 1px solid #e4e7ed;
}

.intro {
  color: #909399;
  margin-top: 0.5rem;
}
</style>
