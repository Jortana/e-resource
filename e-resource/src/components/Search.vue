<template>
  <el-input
    class="main-search"
    v-model="searchContent"
    @change="emitSearchContent">
    <i slot="prefix" class="el-input__icon el-icon-search"></i>
    <el-button class="search-btn phone-btn" slot="append" icon="el-icon-microphone"></el-button>
    <el-button class="search-btn" slot="append" icon="el-icon-search" @click="emitSearchContent"></el-button>
  </el-input>
</template>

<script>
export default {
  name: 'Search',
  data () {
    return {
      searchContent: ''
    }
  },
  methods: {
    emitSearchContent () {
      this.$emit('update:searchContent', this.searchContent)
    }
  }
}
</script>

<style scoped>
.main-search {
  width: 50vw;
  height: 3rem;
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
