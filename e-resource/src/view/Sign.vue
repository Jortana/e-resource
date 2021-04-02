<template>
  <section>
    <div :class="signUpActive === true ? 'container active' : 'container'">
      <div class="user sign-in-box">
        <div class="img-box"><img src="@/assets/sign/in.jpg" alt=""></div>
        <div class="form-box" v-loading="signInLoading" @keyup.enter="login">
          <el-form class="form" ref="signInForm" :model="signIn">
            <h2>登录</h2>
            <el-form-item>
              <el-input v-model="signIn.username" placeholder="用户名"></el-input>
            </el-form-item>
            <el-form-item>
              <el-input type="password" v-model="signIn.password" placeholder="密码"></el-input>
            </el-form-item>
            <el-button type="primary" class="submit-btn" @click="login">登录</el-button>
            <p class="signup">还没有账号吗？<span @click="toggle">点击注册</span></p>
          </el-form>
        </div>
      </div>
      <div class="user sign-up-box">
        <div class="form-box" v-loading="signUpLoading">
          <el-form class="form" ref="signUpForm" :model="signUp" :rules="signUpRules">
            <h2>创建你的账号</h2>
            <el-form-item prop="username">
              <el-input v-model="signUp.username" placeholder="用户名"></el-input>
            </el-form-item>
            <el-form-item prop="email">
              <el-input v-model="signUp.email" placeholder="邮箱"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input type="password" v-model="signUp.password" placeholder="密码"></el-input>
            </el-form-item>
            <el-form-item prop="confirmPassword">
              <el-input type="password" v-model="signUp.confirmPassword" placeholder="确认密码"></el-input>
            </el-form-item>
            <el-form-item prop="grade">
              <el-select class="full-width" v-model="signUp.grade" placeholder="请选择年级">
                <el-option-group
                  v-for="period in grades"
                  :key="period.label"
                  :label="period.label">
                  <el-option
                    v-for="grade in period.grades"
                    :key="period.label + grade"
                    :label="period.label + numberZh[grade] + '年级'"
                    :value="[period.code, grade]">
                  </el-option>
                </el-option-group>
              </el-select>
            </el-form-item>
            <el-button type="primary" class="submit-btn" @click="validateSignUp">注册</el-button>
            <p class="signup">已有账号吗？<span @click="toggle">点击登录</span></p>
          </el-form>
        </div>
        <div class="img-box"><img src="@/assets/sign/up.jpg" alt=""></div>
      </div>
    </div>
  </section>
</template>

<script>
import { register, login } from '@/api/auth'

export default {
  name: 'Sign',
  data () {
    // 确认密码的validator
    let validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else if (!/^(?=.*[a-zA-Z])(?=.*\d)[^]{8,18}$/.test(value)) {
        callback(new Error('密码需要8-18位，且需要包含数字和字母'))
      } else {
        if (this.signUp.confirmPassword !== '') {
          this.$refs.signUpForm.validateField('confirmPassword')
        }
        callback()
      }
    }
    let validatePassword2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.signUp.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      signUpActive: false,
      signInLoading: false,
      signUpLoading: false,
      signIn: {
        username: '',
        password: ''
      },
      signUp: {
        username: '',
        email: '',
        grade: '',
        password: '',
        confirmPassword: ''
      },
      grades: [{
        label: '小学',
        code: 2,
        grades: 6
      }, {
        label: '初中',
        code: 3,
        grades: 3
      }, {
        label: '高中',
        code: 4,
        grades: 3
      }],
      numberZh: ['零', '一', '二', '三', '四', '五', '六'],
      signUpRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { pattern: /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/, message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        password: [
          { validator: validatePassword, trigger: 'blur' }
        ],
        confirmPassword: [
          { validator: validatePassword2, trigger: 'blur' }
        ],
        grade: [
          { required: true, message: '请选择年级', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    toggle () {
      this.signUpActive = !this.signUpActive
    },
    validateSignUp () {
      this.$refs.signUpForm.validate(valid => {
        if (valid) {
          let signUp = this.signUp
          this.signUpLoading = true
          register({
            username: signUp.username,
            email: signUp.email,
            password: signUp.password,
            period: Number(signUp.grade[0]),
            grade: Number(signUp.grade[1])
          })
            .then(response => {
              console.log(response)
              this.signUpLoading = false
              if (response.data.code === 200) {
                this.$message.success(response.data.message)
                this.signIn.username = signUp.username
                this.signUpActive = false
                this.signUp.grade = ''
                this.$refs.signUpForm.resetFields()
              } else {
                this.$message.warning(response.data.message)
              }
            })
        } else {
          return false
        }
      })
    },
    login () {
      let signIn = this.signIn
      for (let userInfo in signIn) {
        if (signIn[userInfo] === '') {
          this.$message.warning('请填写用户名和密码')
          return false
        }
      }
      login({
        username: signIn.username,
        password: signIn.password
      })
        .then(response => {
          console.log(response)
          if (response.data.code === 200) {
            this.$message({
              message: response.data.message,
              type: 'success',
              duration: 1500
            })
            this.$store.commit('login', response.data.data)
            let path = this.$route.query.redirect
            this.$router.replace({ path: path === '/' || path === undefined ? '/' : path })
          } else {
            this.$message.warning(response.data.message)
          }
        })
    }
  }
}
</script>

<style scoped>
section {
  position: relative;
  min-height: 100vh;
  background: rgb(121 187 255);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px ;
}

section .container {
  position: relative;
  width: 800px;
  height: 500px;
  background: #fff;
  box-shadow: 0 15px 50px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

section .container .user{
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
}

section .container .user .img-box{
  position: relative;
  width: 50%;
  height: 100%;
  background: #409EFF;
  transition: .5s;
}

section .container .user .img-box img{
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

section .container .user .form-box{
  position: relative;
  width: 50%;
  height: 100%;
  background: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px;
  transition: .5s;
}

section .container .user .form-box form h2{
  font-size: 18px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 2px;
  text-align: center;
  width: 100%;
  margin-bottom: 10px;
  color: #555;
}

.form {
  width: 100%;
}

.form .el-form-item {
  margin-bottom: 0;
}

.form >>> .el-form-item__error {
  margin-top: -10px;
}

section .container .user .form-box >>> input {
  position: relative;
  width: 100%;
  padding: 10px;
  background: #f5f5f5;
  color: #333;
  border: none;
  outline: none;
  box-shadow: none;
  margin: 8px 0;
  font-size: 14px;
  letter-spacing: 1px;
  border-radius: 0;
}

.submit-btn {
  margin: 8px 0;
  padding: 10px 30px;
  border-radius: 0;
  letter-spacing: 1px;
  transition: .3s;
}

section .container .user .form-box form .signup{
  position: relative;
  margin-top: 20px;
  font-size: 12px;
  letter-spacing: 1px;
  color: #555;
  text-transform: uppercase;
  font-weight: 300;
}

section .container .user .form-box form .signup span{
  font-weight: 600;
  cursor: pointer;
  text-decoration: none;
  color: #409EFF;
}

section .container .sign-up-box{
  pointer-events: none;
}

section .container.active .sign-up-box{
  pointer-events: initial;
}

section .container .sign-up-box .form-box{
  left: 100%;
}

section .container.active .sign-up-box .form-box{
  left: 0;
}

section .container .sign-up-box .img-box{
  left: -100%;
}

section .container.active .sign-up-box .img-box{
  left: 0;
}

section .container .sign-in-box .form-box{
  left: 0;
}

section .container.active .sign-in-box .form-box{
  left: 100%;
}

section .container .sign-in-box .img-box{
  left: 0;
}

section .container.active .sign-in-box .img-box{
  left: 100%;
}

@media (max-width: 991px){
  section .container{
    max-width: 400px;
  }
  section .container .img-box{
    display: none;
  }
  section .container .user .form-box{
    width: 100%;
  }
}
</style>
