import { defineStore } from 'pinia'
import { setToken, removeToken } from '@/utils/auth'
import { getUserInfo, login } from '@/api/sys/user'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: '',
    userInfo: null
  }),

  actions: {
    async login(userInfo) {
      const response = await login(userInfo)
      this.token = response.data.token
      this.userInfo = response.data.user
      setToken(this.token)
      return response
    },

    async getUserInfo() {
      const response = await getUserInfo()
      this.userInfo = response.data
      return response
    },

    logout() {
      this.token = ''
      this.userInfo = null
      removeToken()
    }
  }
})
