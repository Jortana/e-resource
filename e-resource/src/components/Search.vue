<template>
  <el-input
    v-model="searchQ"
    class="main-search"
    @change="changeSearchContent">
    <i slot="prefix" class="el-input__icon el-icon-search"></i>
    <el-button slot="append" class="search-btn phone-btn" icon="el-icon-microphone"></el-button>
    <el-button slot="append" class="search-btn" icon="el-icon-search" @click="search"></el-button>
  </el-input>
</template>

<script>
export default {
  name: 'Search',
  props: {
    searchContent: String
  },
  data() {
    return {
      searchQ: this.searchContent
      // searchQ: this.$route.query.q === undefined ? '' : this.$route.query.q
    }
  },
  computed: {
    queryQ() {
      return this.$route.query.q
    }
  },
  watch: {
    queryQ() {
      this.searchQ = this.$route.query.q
    }
  },
  methods: {
    changeSearchContent() {
      this.$emit('update:searchContent', this.searchQ)
    },
    search() {
      this.$emit('search')
    },
    test() {
      console.log('a')
    }
  }
}
</script>

<style scoped>
.main-search {
  /*width: 50vw;*/
  height: 3rem;
}

.main-search >>> .el-input-group__append,
.main-search >>> input {
  background-color: #fff !important;
}

.main-search >>> input {
  height: 100%;
  border-top-left-radius: 2rem;
  border-bottom-left-radius: 2rem;
  border-right: none;
  padding-left: 40px;
  font-size: 16px;
}

.main-search >>> input:focus,
.main-search >>> input:hover {
  border-color: #DCDFE6;
}

.main-search >>> .el-input-group__append {
  font-size: 16px;
  border-top-right-radius: 2rem;
  border-bottom-right-radius: 2rem;
  background: none;
}

.main-search >>> .el-input__prefix,
.main-search >>> .el-input__suffix {
  font-size: 16px;
  color: #9aa0a6;
}

.main-search >>> .el-icon-microphone {
  font-size: 18px;
}

.main-search >>> .el-input__prefix {
  left: 10px;
}

.search-btn {
  padding: 12px 16px;
  position: relative;
}

.search-btn:hover {
  color: #409EFF;
}

.phone-btn::after {
  content: "";
  height: 70%;
  position: absolute;
  top: 50%;
  left: 0;
  transform: translateY(-50%);
  border-left: 1px solid #bcc4cb;
}
</style>
