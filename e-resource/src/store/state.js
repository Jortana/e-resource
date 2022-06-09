export default {
  user:
    window.localStorage.getItem('user' || '[]') === null
      ? ''
      : JSON.parse(window.localStorage.getItem('user' || '[]')),
  cart:
    window.localStorage.getItem('cart' || '[]') === null
      ? {}
      : JSON.parse(window.localStorage.getItem('cart' || '[]')),
  // 知识图谱路径
  graphHistory: []
}
