export default {
  login(state, user) {
    user['avatar'] =
      'http://223.2.55.39:9000/e-resource/api/file/avatar/' + user['avatar']
    state.user = user
    window.localStorage.setItem('user', JSON.stringify(user))
  },
  logout(state) {
    // 注意不能用 null 清除，否则将无法判断 user 里具体的内容
    state.user = ''
    window.localStorage.removeItem('user')
    // 清空购物车
    state.cart = {}
    window.localStorage.removeItem('cart')
  },
  /**
   * 清空购物车
   * @param state
   */
  clearCart(state) {
    state.cart = {}
    window.localStorage.removeItem('cart')
  },
  /**
   * 将资源ID添加到购物车
   * - resourceID: 资源ID
   * @param state
   * @param {String} resourceID
   */
  addToCart(state, resourceID) {
    if (state.cart.resources === undefined) {
      state.cart.resources = [resourceID]
    } else if (state.cart.resources.indexOf(resourceID) === -1) {
      state.cart.resources.push(resourceID)
    }
    window.localStorage.setItem('cart', JSON.stringify(state.cart))
    console.log(state.cart.resources)
  },
  /**
   * 将当前节点记录进节点浏览记录
   * @param state
   * @param {Array} nodes - 需要放进浏览记录的节点数组
   */
  pushHistory(state, nodes) {
    nodes.forEach((node) => {
      state.graphHistory.push(node)
    })
  },
  /**
   * 将当前点击节点之后的节点删除
   * @param state
   * @param {String} node - 当前点击的节点
   */
  backHistory(state, node) {
    for (let i = 0; i < state.graphHistory.length; i++) {
      if (state.graphHistory[i] === node) {
        state.graphHistory.length = i + 1
      }
    }
  },
  /**
   * 清空历史记录
   * @param state
   */
  clearHistorty(state) {
    state.graphHistory = []
  },
  /**
   * 初始化学科列表
   * @param state
   * @param {Array} subjects
   */
  initSubjects(state, subjects) {
    state.subjects = [{ ID: '', name: '全部' }, ...subjects]
  }
}
