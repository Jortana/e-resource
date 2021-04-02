export default {
  user: window.localStorage.getItem('user' || '[]') === null ? '' : JSON.parse(window.localStorage.getItem('user' || '[]')),
  cart: window.localStorage.getItem('cart' || '[]') === null ? {} : JSON.parse(window.localStorage.getItem('cart' || '[]'))
}
