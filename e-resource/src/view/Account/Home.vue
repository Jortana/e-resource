<template>
<div class="main-container">
  <h2>个人中心</h2>
  <div class="user-info">
    <div class="show-basic-infos">
      <div class="avatar"><el-avatar :size="100" :src="userinfo.avatar"></el-avatar></div>
      <div class="basic-info">
        <h2>
          {{ userinfo.username }}
        </h2>
        <div class="email">
          {{ userinfo.user_email }}
        </div>
      </div>
    </div>
    <div class="infos-container">
      <h2>账户</h2>
      <div class="infos flex-1">
        <div class="info">
          <h3>用户名</h3>
          <div class="info-content" v-if="isEdit.basic === false">{{ userinfo.username }}</div>
          <el-input class="info-content" v-else v-model="editInfo.username"></el-input>
        </div>
      </div>
      <div class="infos flex-1">
        <div class="info">
          <h3>密码</h3>
          <el-button class="change-password-btn" type="text">更改密码</el-button>
        </div>
      </div>
      <div class="edit">
        <el-button type="primary" size="medium" plain v-if="isEdit.basic === false">编辑</el-button>
        <el-button type="primary" size="medium" v-else>完成</el-button>
      </div>
    </div>
    <div class="infos-container">
      <h2>学籍</h2>
      <div class="infos flex-1">
        <div class="info">
          <h3>省</h3>
          <div class="info-content">江苏省</div>
        </div>
        <div class="info">
          <h3>市</h3>
          <div class="info-content">南京市</div>
        </div>
        <div class="info">
          <h3>区</h3>
          <div class="info-content">鼓楼区</div>
        </div>
      </div>
      <div class="infos flex-1">
        <div class="info">
          <h3>学校</h3>
          <div class="info-content" v-if="isEdit.region === false">{{ userinfo.school }}</div>
          <el-input class="info-content" v-else v-model="editInfo.username"></el-input>
        </div>
        <div class="info">
          <h3>学段</h3>
          <div class="info-content">{{ userinfo.period }}</div>
        </div>
        <div class="info">
          <h3>年级</h3>
          <div class="info-content">{{ userinfo.grade }}</div>
        </div>
      </div>
      <div class="edit">
        <el-button type="primary" size="medium" plain v-if="isEdit.region === false">编辑</el-button>
        <el-button type="primary" size="medium" v-else>完成</el-button>
      </div>
    </div>
  </div>
</div>
</template>

<script>
export default {
  name: 'User',
  data () {
    return {
      isEdit: {
        basic: false,
        region: false
      },
      userinfo: {
        username: 'NOBUG',
        user_email: 'test@test.com',
        school: '南师小',
        period: '小学',
        grade: '二年级'
      },
      editInfo: {
        username: 'NOBUG'
      }
    }
  },
  methods: {
    getUserInfo () {
    }
  }
}
</script>

<style scoped>
.main-container {
  /*margin-right: 200px;*/
  max-width: 960px;
}

.user-info {
  margin-top: 1rem;
}

.show-basic-infos {
  display: flex;
}

.show-basic-infos .basic-info {
  margin-left: 3rem;
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
}

.show-basic-infos .basic-info h2 {
  font-size: 3rem;
}

.show-basic-infos .basic-info .email {
  font-size: 1.5rem;
}

.show-basic-infos,
.infos-container {
  padding-top: 1.5rem;
  padding-bottom: 1.5rem;
  border-top: 1px solid #dcdfe6;
}

.infos-container {
  display: flex;
  flex-wrap: wrap;
}

.infos-container h2 {
  font-size: 2.5rem;
  color: #515151;
  font-weight: 400;
}

.infos-container .infos {
  margin-left: 4.3rem;
}

.infos-container .infos .info h3 {
  font-size: 1.2rem;
  color: #666;
}

.infos-container .infos .info .info-content {
  margin-top: .5rem;
  font-size: 1.2rem;
}

.change-password-btn {
  font-size: 1.2rem;
}

.infos-container .infos .info {
  margin-bottom: 1.5rem;
}

.edit-input {
  font-size: 1.2rem;
}
</style>
