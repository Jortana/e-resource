export default {
  login(state, user) {
    user['avatar'] = 'http://127.0.0.1:9000/e-resource/api/file/avatar/' + user['avatar']
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
  }
}
