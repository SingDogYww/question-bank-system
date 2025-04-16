<script setup lang="ts">
import { ref, reactive } from 'vue';
import { UserOutlined, LockOutlined, MobileOutlined, MailOutlined } from '@ant-design/icons-vue';
import { Form, Input, Button, Checkbox, Tabs, message } from 'ant-design-vue';
import { useRouter } from 'vue-router';

// 定义主题颜色
const themeColors = {
  primary: '#cf1322',
  hover: '#ff4d4f',
  active: '#a8071a',
  light: '#fff1f0'
};

interface LoginForm {
  username?: string;
  password?: string;
  mobile?: string;
  code?: string;
  email?: string;
  remember?: boolean;
}

const router = useRouter();
const activeKey = ref<'account' | 'mobile' | 'email'>('account');
const countdown = ref(0);
const loading = ref(false);

const formState = reactive<LoginForm>({
  username: '',
  password: '',
  mobile: '',
  code: '',
  email: '',
  remember: false
});

// 表单提交处理
const handleSubmit = async (values: LoginForm) => {
  try {
    loading.value = true;
    
    // 根据登录方式构建不同参数
    let params = {};
    switch (activeKey.value) {
      case 'account':
        params = { username: values.username, password: values.password };
        break;
      case 'mobile':
        params = { mobile: values.mobile, code: values.code };
        break;
      case 'email':
        params = { email: values.email, password: values.password };
        break;
    }
    
    // TODO: 调用实际登录API
    // 模拟API调用延迟
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    // 保存记住登录状态
    if (values.remember) {
      localStorage.setItem('rememberLogin', 'true');
      // 实际项目中应该用更安全的方式存储凭证
    }
    
    message.success('登录成功');
    // 登录成功后跳转到首页
    router.push('/');
  } catch (error) {
    console.error('登录失败:', error);
    message.error('登录失败，请检查账号密码');
  } finally {
    loading.value = false;
  }
};

// 发送验证码
const startCountdown = () => {
  countdown.value = 60;
  const timer = setInterval(() => {
    countdown.value -= 1;
    if (countdown.value <= 0) clearInterval(timer);
  }, 1000);
  
  // TODO: 调用发送验证码API
  message.success('验证码已发送');
};

// 处理忘记密码
const handleForgotPassword = () => {
  router.push('/forgot-password');
};

// 检查是否记住登录
const checkRememberLogin = () => {
  const remembered = localStorage.getItem('rememberLogin');
  if (remembered === 'true') {
    formState.remember = true;
    // 在实际项目中还需要从安全存储中获取用户名等信息
  }
};

// 组件挂载时检查记住登录状态
checkRememberLogin();
</script>

<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-header">
        <h1>考试系统登录</h1>
        <p>欢迎使用在线考试平台</p>
      </div>

      <Form
        :model="formState"
        layout="vertical"
        @finish="handleSubmit"
        class="login-form"
        @keypress.enter="handleSubmit(formState)"
      >
        <Tabs v-model:activeKey="activeKey" centered>
          <!-- 账号密码登录 -->
          <Tabs.TabPane key="account" tab="账号登录">
            <Form.Item
              name="username"
              :rules="[{ required: true, message: '请输入用户名' }]"
            >
              <Input
                v-model:value="formState.username"
                placeholder="用户名"
                size="large"
                :disabled="loading"
                autocomplete="username"
              />
            </Form.Item>

            <Form.Item
              name="password"
              :rules="[{ required: true, message: '请输入密码' }]"
            >
              <Input.Password
                v-model:value="formState.password"
                placeholder="密码"
                size="large"
                :disabled="loading"
                autocomplete="current-password"
              />
            </Form.Item>
          </Tabs.TabPane>

          <!-- 手机验证码登录 -->
          <Tabs.TabPane key="mobile" tab="手机登录">
            <Form.Item
              name="mobile"
              :rules="[
                { required: true, message: '请输入手机号' },
                { pattern: /^1[3-9]\d{9}$/, message: '手机号格式错误' }
              ]"
            >
              <Input
                v-model:value="formState.mobile"
                placeholder="手机号"
                size="large"
                :disabled="loading"
                autocomplete="tel"
              />
            </Form.Item>

            <Form.Item
              name="code"
              :rules="[{ required: true, message: '请输入验证码' }]"
            >
              <Input
                v-model:value="formState.code"
                placeholder="验证码"
                size="large"
                :disabled="loading"
              >
                <template #suffix>
                  <Button
                    type="link"
                    @click="startCountdown"
                    :disabled="countdown > 0 || loading"
                    :style="{ color: countdown || loading ? '#666' : themeColors.primary }"
                  >
                    {{ countdown ? `${countdown}s后重发` : '获取验证码' }}
                  </Button>
                </template>
              </Input>
            </Form.Item>
          </Tabs.TabPane>

          <!-- 邮箱登录 -->
          <Tabs.TabPane key="email" tab="邮箱登录">
            <Form.Item
              name="email"
              :rules="[
                { required: true, message: '请输入邮箱' },
                { type: 'email', message: '邮箱格式错误' }
              ]"
            >
              <Input
                v-model:value="formState.email"
                placeholder="邮箱"
                size="large"
                :disabled="loading"
                autocomplete="email"
              />
            </Form.Item>

            <Form.Item
              name="password"
              :rules="[{ required: true, message: '请输入密码' }]"
            >
              <Input.Password
                v-model:value="formState.password"
                placeholder="密码"
                size="large"
                :disabled="loading"
                autocomplete="current-password"
              />
            </Form.Item>
          </Tabs.TabPane>
        </Tabs>

        <Form.Item>
          <Button
            type="primary"
            html-type="submit"
            block
            class="submit-btn"
            :loading="loading"
          >
            {{ loading ? '登录中...' : '立即登录' }}
          </Button>
        </Form.Item>

        <div class="login-extra">
          <Checkbox v-model:checked="formState.remember" :disabled="loading">自动登录</Checkbox>
          <div class="login-links">
            <router-link to="/register" :style="{ color: themeColors.primary }" :disabled="loading">注册账号</router-link>
            <span class="divider">|</span>
            <a @click="handleForgotPassword" :style="{ color: themeColors.primary, cursor: loading ? 'not-allowed' : 'pointer' }">忘记密码</a>
          </div>
        </div>
      </Form>
    </div>
  </div>
</template>

<style scoped lang="scss">
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  width: 100%;
  background: linear-gradient(90deg, 
    #ffc6c6, 
    #ff9999, 
    #f77777, 
    #ee5555, 
    #e34747);
  padding: 20px;
  position: relative;
  
  &::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: radial-gradient(circle at center, rgba(255,215,0,0.1) 0%, rgba(255,215,0,0.03) 40%, transparent 70%);
    z-index: 1;
    pointer-events: none; /* 确保伪元素不会捕获点击事件 */
  }
}

.login-container {
  max-width: 580px;
  width: 100%;
  padding: 65px 45px;
  background: rgba(255, 255, 255, 0.82);
  backdrop-filter: blur(8px);
  border-radius: 16px;
  box-shadow: 0 12px 36px rgba(0, 0, 0, 0.35);
  transform: none;
  position: relative;
  z-index: 2;
  
  .login-header {
    text-align: center;
    margin-bottom: 40px;
    color: v-bind('themeColors.primary');

    h1 {
      font-size: 40px;
      margin-bottom: 16px;
      text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.08);
      font-weight: 600;
      -webkit-font-smoothing: antialiased;
      -moz-osx-font-smoothing: grayscale;
    }

    p {
      color: #666;
      font-size: 22px;
      font-weight: 400;
      -webkit-font-smoothing: antialiased;
      -moz-osx-font-smoothing: grayscale;
    }
  }

  :deep(.ant-tabs-nav) {
    margin-bottom: 25px;
  }

  :deep(.ant-tabs-tab) {
    color: #666 !important;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    font-size: 19px;
    
    &.ant-tabs-tab-active .ant-tabs-tab-btn {
      color: v-bind('themeColors.primary') !important;
      font-weight: 500;
    }
  }

  :deep(.ant-form-item-label > label) {
    font-size: 18px;
  }

  /* 重置所有输入框样式 */
  :deep(.ant-input),
  :deep(.ant-input-affix-wrapper) {
    height: 46px;
    font-size: 18px;
    padding: 0 12px;
    border-radius: 4px;
    background-color: rgba(255, 255, 255, 0.8);
    box-sizing: border-box;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    display: flex;
    align-items: center;
    transition: all 0.3s ease;
  }
  
  /* 输入框聚焦样式 */
  :deep(.ant-input:focus),
  :deep(.ant-input-focused),
  :deep(.ant-input-affix-wrapper:focus),
  :deep(.ant-input-affix-wrapper-focused) {
    box-shadow: 0 0 0 2px rgba(207, 19, 34, 0.1);
    border-color: v-bind('themeColors.hover');
  }

  /* 密码框内部input调整 */
  :deep(.ant-input-affix-wrapper .ant-input) {
    height: 30px;
    background-color: transparent;
    padding: 0;
    box-shadow: none;
    align-items: center;
  }

  /* 确保密码框触发图标垂直居中 */
  :deep(.ant-input-affix-wrapper .ant-input-suffix) {
    margin-left: 4px;
    display: flex;
    align-items: center;
  }

  /* 所有输入框的提示词样式 */
  :deep(.ant-input::placeholder),
  :deep(.ant-input-password input::placeholder) {
    text-align: left;
    color: #999;
    line-height: normal;
    vertical-align: middle;
  }
  
  /* 直接调整输入框内部元素垂直居中 */
  :deep(.ant-input) {
    display: flex !important;
    align-items: center !important;
  }

  /* 针对验证码输入框的特殊处理 */
  :deep(.ant-input-affix-wrapper input) {
    display: flex;
    align-items: center;
  }

  :deep(.ant-checkbox + span) {
    font-size: 18px;
  }

  .submit-btn {
    height: 52px;
    background-color: v-bind('themeColors.primary');
    border: none;
    border-radius: 6px;
    transition: background-color 0.3s;
    font-size: 20px;
    margin-top: 18px;

    &:hover {
      background-color: v-bind('themeColors.hover') !important;
    }

    &:active {
      background-color: v-bind('themeColors.active') !important;
    }
  }

  .login-extra {
    display: flex;
    justify-content: space-between;
    color: #666;
    margin-top: 15px;
    font-size: 18px;
    align-items: center;

    .login-links {
      display: flex;
      align-items: center;
      
      .divider {
        margin: 0 15px;
        color: #999;
        font-weight: 300;
        font-size: 16px;
      }

      a, router-link {
        transition: color 0.3s;
        padding: 2px 0;
        position: relative;
        
        &:hover {
          color: v-bind('themeColors.hover') !important;
        }
        
        &:after {
          content: '';
          position: absolute;
          bottom: 0;
          left: 0;
          width: 0;
          height: 1px;
          background-color: v-bind('themeColors.hover');
          transition: width 0.3s;
        }
        
        &:hover:after {
          width: 100%;
        }
      }
    }
    
    @media (max-width: 576px) {
      flex-direction: column;
      align-items: center;
      gap: 10px;
      
      .login-links {
        margin-top: 10px;
      }
    }
  }
}
</style>