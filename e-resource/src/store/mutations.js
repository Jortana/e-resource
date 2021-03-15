export default {
  login (state, user) {
    user['avatar'] = 'http://127.0.0.1:9000/e-resource/api/file/avatar/' + user['avatar']
    state.user = user
    window.localStorage.setItem('user', JSON.stringify(user))
  }
}
