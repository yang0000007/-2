<template>
  <div class="home-container">
    <!-- 顶部导航栏 -->
    <nav class="navbar">
      <h1>老年在线学习系统</h1>
      <div>当前用户：123 | 角色：学员</div>
    </nav>

    <!-- 主要模块 -->
    <div class="modules">
      <!-- 用户管理模块 -->
      <div class="module-card">
        <h3>用户管理</h3>
        <ul>
          <li>修改个人信息</li>
          <li>权限：学员（不可更改）</li>
        </ul>
      </div>

      <!-- 课程管理模块 -->
      <div class="module-card">
        <h3>课程管理</h3>
        <input v-model="searchQuery" placeholder="输入关键词搜索课程" />
        <ul>
          <li v-for="course in filteredCourses" :key="course.id">
            {{ course.title }}
          </li>
        </ul>
      </div>

      <!-- 学习管理模块 -->
      <div class="module-card">
        <h3>学习管理</h3>
        <p>当前课程进度：</p>
        <div class="progress-bar">
          <div class="progress" :style="{ width: learningProgress + '%' }"></div>
        </div>
        <ul>
          <li>学习记录：累计学习 {{ learningHours }} 小时</li>
          <li>学习笔记：已保存 {{ notesCount }} 条</li>
        </ul>
      </div>

      <!-- 互动交流模块 -->
      <div class="module-card">
        <h3>互动交流</h3>
        <ul>
          <li>消息通知：您有 {{ unreadMessages }} 条新消息</li>
          <li>加入学习群组：点击进入讨论区</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

// 模拟数据
const searchQuery = ref('')
const courses = [
  { id: 1, title: '健康养生基础' },
  { id: 2, title: '智能手机使用指南' },
  { id: 3, title: '书法入门教程' }
]
const learningProgress = ref(60)
const learningHours = ref(12)
const notesCount = ref(3)
const unreadMessages = ref(2)

// 搜索过滤
const filteredCourses = computed(() => {
  return courses.filter(course =>
      course.title.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
})
</script>

<style scoped>
.home-container {
  background: #f8f9fa;
  min-height: 100vh;
  padding: 20px;
}

.navbar {
  display: flex;
  justify-content: space-between;
  padding: 20px;
  background: white;
  border-radius: 10px;
  margin-bottom: 30px;
}

.modules {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.module-card {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0,0,0,0.05);
}

.module-card h3 {
  margin-top: 0;
  color: #1b263b;
}

.progress-bar {
  width: 100%;
  background: #e9ecef;
  border-radius: 5px;
  overflow: hidden;
  height: 20px;
}

.progress {
  background: #0d6efd;
  height: 100%;
}

ul {
  padding-left: 20px;
}

input {
  width: 100%;
  padding: 8px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
</style>