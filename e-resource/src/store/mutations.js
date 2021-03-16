export default {
  login (state, user) {
    user['avatar'] = 'http://127.0.0.1:9000/e-resource/api/file/avatar/' + user['avatar']
    state.user = user
    window.localStorage.setItem('user', JSON.stringify(user))
  },
  logout (state) {
    // 注意不能用 null 清除，否则将无法判断 user 里具体的内容
    state.user = ''
    window.localStorage.removeItem('user')
  }
}
