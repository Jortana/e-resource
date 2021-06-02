<template>
  <div>
    <nav-menu></nav-menu>
    <div class="container">
      <div class="fav-container flex">
        <div class="folder-list">
          <h3>我的收藏夹</h3>
          <div class="new-folder flex">
            <i class="el-icon-circle-plus fav-icon"></i>
            <span>新建收藏夹</span>
          </div>
          <fav-folder
            v-for="favFolder in favFolders"
            :key="favFolder.id"
            :curID="curID"
            :favFolder="favFolder"
          >
          </fav-folder>
        </div>
        <div class="fav-list"></div>
      </div>
    </div>
  </div>
</template>

<script>
import NavMenu from '@/components/NavMenu'
import FavFolder from '@/components/Favourite/FavFolder'

export default {
  name: 'Favourite',
  components: { NavMenu, FavFolder },
  data () {
    return {
      favFolders: [],
      resource: [],
      curID: ''
    }
  },
  computed: {
    query () {
      return this.$route.query
    }
  },
  mounted () {
    this.getFavFolders()
  },
  watch: {
    query: {
      handler (newQuery, oldQuery) {
        this.getResources(newQuery.id)
        this.curID = newQuery.id
      },
      immediate: true
    }
  },
  methods: {
    getResources (favID) {
      this.resource = [1, 20, 30]
    },
    getFavFolders () {
      this.favFolders = [
        {
          id: 1,
          name: '收藏1'
        },
        {
          id: 2,
          name: '收藏2'
        }
      ]
      if (this.query.id === undefined) {
        this.curID = 1
      }
    }
  }
}
</script>

<style scoped>
.container {
  background-color: #F4F5F7;
  margin-top: 1rem;
  /*空出nav-menu的位置*/
  height: calc(100vh - 70px);
  padding-top: 1rem;
}

.fav-container {
  margin: 0 auto;
  border-top-left-radius: .5rem;
  border-top-right-radius: .5rem;
  border: 1px solid #DCDFE6;
  width: 60%;
  min-width: 1000px;
  height: 100%;
  background-color: #fff;
}

.folder-list {
  width: 200px;
  /*background-color: #53a8ff;*/
  border-right: 1px solid #E4E7ED;
  /*padding: 1.5rem;*/
}

.folder-list h3 {
  font-weight: 500;
  color: #515151;
  font-size: 1rem;
  margin: 1.5rem;
}

.new-folder,
.fav-folder {
  align-items: center;
  cursor: pointer;
  height: 2rem;
  padding: 1.5rem;
}

.fav-icon {
  color: #CCD0D8;
  font-size: 1.5rem;
  margin-right: .5rem;
}

.fav-list {
}

</style>
