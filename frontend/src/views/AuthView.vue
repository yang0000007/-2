<template>
  <div class="auth-container">
    <!-- 左侧深色背景 -->
    <div class="left-panel">
      <h1>老年在线学习系统</h1>
      <p>注册账号 123，密码 123，手机号 8888</p>
    </div>

    <!-- 右侧表单 -->
    <div class="right-panel">
      <div class="form-wrapper">
        <!-- 切换按钮 -->
        <div class="toggle-buttons">
          <button
              @click="isLogin = true"
              :class="{ active: isLogin }"
          >登录</button>
          <button
              @click="isLogin = false"
              :class="{ active: !isLogin }"
          >注册</button>
        </div>

        <!-- 登录表单 -->
        <form v-if="isLogin" @submit.prevent="login">
          <h2>用户登录</h2>
          <input v-model="loginForm.username" placeholder="用户名" required />
          <input v-model="loginForm.password" type="password" placeholder="密码" required />
          <button type="submit">立即登录</button>
        </form>

        <!-- 注册表单 -->
        <form v-else @submit.prevent="register">
          <h2>用户注册</h2>
          <input v-model="registerForm.username" placeholder="用户名" required />
          <input v-model="registerForm.password" type="password" placeholder="密码" required />
          <input v-model="registerForm.phone" placeholder="手机号" required />
          <button type="submit">立即注册</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'

const isLogin = ref(true)

// 登录表单数据
const loginForm = ref({
  username: '123',
  password: '123'
})

// 注册表单数据
const registerForm = ref({
  username: '',
  password: '',
  phone: ''
})

// 模拟登录
const login = async () => {
  try {
    const res = await axios.post('/api/auth/login', loginForm.value)
    localStorage.setItem('token', res.data.token)
    window.location.href = '/home'
  } catch (error) {
    alert('登录失败')
  }
}

// 模拟注册
const register = async () => {
  try {
    await axios.post('/api/auth/register', registerForm.value)
    alert('注册成功，请登录')
    isLogin.value = true
  } catch (error) {
    alert('注册失败')
  }
}
</script>

<style scoped>
.auth-container {
  display: flex;
  height: 100vh;
}

.left-panel {
  width: 40%;
  background: linear-gradient(135deg, #0d1b2a, #1b263b);
  color: white;
  padding: 40px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.right-panel {
  width: 60%;
  background: linear-gradient(135deg, #2a2d2e, #43464b);
  display: flex;
  align-items: center;
  justify-content: center;
}

.form-wrapper {
  background: white;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
  width: 80%;
}

form {
  display: flex;
  flex-direction: column;
}

input {
  padding: 10px;
  margin: 10px 0 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

button {
  padding: 12px;
  background: #1b263b;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.toggle-buttons button {
  background: #526d82;
  margin-right: 10px;
}

.toggle-buttons .active {
  background: #0d6efd;
}
</style>