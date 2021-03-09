export default {
  user: {
    username: window.localStorage.getItem('user' || '[]') == null ? '' : JSON.parse(window.localStorage.getItem('user' || '[]')).username
  }
}
