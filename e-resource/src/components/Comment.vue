<template>
  <!-- 打分和评论 -->
  <div class="comment-container">
    <h2>评论</h2>
    <div class="edit-comment-container comments flex">
      <el-avatar
        :size="50"
        :src="$store.state.user.avatar"
        class="avatar"
        icon="el-icon-user-solid"
      ></el-avatar>
      <div class="edit-comment-info-container flex-1">
        <el-rate
          v-model="editComment['rate']"
          :colors="colors"
          class="rate info"
          show-score
          score-template="{value}"
        ></el-rate>
        <div class="flex">
          <el-input
            :disabled="!isLogin"
            :rows="3"
            :placeholder="isLogin ? '请输入内容' : '请登录后发表评论'"
            v-model="editComment.content"
            class="textarea"
            type="textarea"
          ></el-input>
          <el-button class="commit-btn" type="primary">
            <div>发表</div>
            <div>评论</div>
          </el-button>
        </div>
      </div>
    </div>
    <div class="comments">
      <div
        v-for="(comment, index) in comments"
        :key="index"
        class="comment flex"
      >
        <el-avatar
          :size="50"
          :src="comment.avatar"
          class="avatar"
          icon="el-icon-user-solid"
        ></el-avatar>
        <div class="comment-info-container flex-1">
          <div class="username">{{ comment['username'] }}</div>
          <!--          <div class="username">NOBUG</div>-->
          <el-rate
            v-model="comment['rate']"
            :colors="colors"
            class="rate info"
            disabled
            show-score
            score-template="{value}"
          ></el-rate>
          <div class="content info">{{ comment['content'] }}</div>
          <div class="date info">{{ comment['date'] }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getComment } from '@/api/resource'
import { authentication } from '@/api/auth'

const fileBaseURL = 'http://127.0.0.1:9000/e-resource/api/file/avatar/'

export default {
  name: 'Comment',
  props: {
    id: Number
  },
  data() {
    return {
      comments: [],
      colors: ['#99A9BF', '#F7BA2A', '#FF9900'],
      editComment: {
        rate: 0,
        content: ''
      },
      isLogin: false
    }
  },
  watch: {
    id: {
      handler() {
        this.authentication()
        this.getComment()
      }
    }
  },
  methods: {
    authentication() {
      authentication().then((response) => {
        if (response.data.code === 200) {
          this.isLogin = true
        }
      })
    },
    getComment() {
      getComment(this.id).then((response) => {
        if (response.data.code === 200) {
          console.log(response.data.data)
          response.data.data.forEach((comment) => {
            comment['avatar'] = fileBaseURL + comment['avatar']
          })
          this.comments = response.data.data
        }
      })
    }
  }
}
</script>

<style scoped>
.avatar {
  margin-right: 2rem;
}

.comment-container h2 {
  font-weight: 500;
}

.edit-comment-info-container .rate {
  margin-bottom: 0.5rem;
}

.commit-btn {
  margin-left: 0.8rem;
}

.commit-btn div {
  letter-spacing: 0.1rem;
}

.commit-btn div:first-child {
  margin-bottom: 0.3rem;
}

.comments {
  margin-top: 1.2rem;
}

.comment {
  padding-top: 1.5rem;
  padding-bottom: 1.5rem;
  border-bottom: 1px solid #ebeef5;
}

.comment-info-container .info {
  margin-top: 3px;
}

.comment-info-container .username {
  font-weight: bold;
}

.comment-info-container .rate {
  margin-left: -2px;
}

.comment-info-container .content {
  margin-top: 5px;
}

.comment-info-container .date {
  margin-top: 1rem;
  font-size: 0.8rem;
  color: #909399;
}

.textarea >>> textarea {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB',
    'Microsoft YaHei', '微软雅黑', Arial, sans-serif !important;
}
</style>
