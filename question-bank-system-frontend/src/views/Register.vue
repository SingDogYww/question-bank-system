<script setup lang="ts">
import { ref, reactive, computed } from 'vue';
import { UserOutlined, LockOutlined, MobileOutlined, MailOutlined, SafetyOutlined } from '@ant-design/icons-vue';
import { Form, Input, Button, Checkbox, Tabs, message, Progress } from 'ant-design-vue';
import { useRouter } from 'vue-router';

// 定义主题颜色
const themeColors = {
  primary: '#cf1322',
  hover: '#ff4d4f',
  active: '#a8071a',
  light: '#fff1f0'
};

// 定义注册表单数据结构
interface RegisterForm {
  username: string;
  password: string;
  confirmPassword: string;
  mobile: string;
  email: string;
  mobileCode: string;
  emailCode: string;
  agreement: boolean;
}

const router = useRouter();
const activeKey = ref<'mobile' | 'email'>('mobile');
const mobileCountdown = ref(0);
const emailCountdown = ref(0);
const loading = ref(false);

// 注册表单数据
const formState = reactive<RegisterForm>({
  username: '',
  password: '',
  confirmPassword: '',
  mobile: '',
  email: '',
  mobileCode: '',
  emailCode: '',
  agreement: false
});

// 添加手机号和邮箱验证的计算属性
const isValidMobile = computed(() => {
  return formState.mobile && /^1[3-9]\d{9}$/.test(formState.mobile);
});

const isValidEmail = computed(() => {
  return formState.email && /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formState.email);
});

// 计算密码强度
const passwordStrength = computed(() => {
  const password = formState.password;
  if (!password) return 0;
  
  let strength = 0;
  // 长度检查
  if (password.length >= 8) strength += 20;
  // 包含小写字母
  if (/[a-z]/.test(password)) strength += 20;
  // 包含大写字母
  if (/[A-Z]/.test(password)) strength += 20;
  // 包含数字
  if (/[0-9]/.test(password)) strength += 20;
  // 包含特殊字符
  if (/[^A-Za-z0-9]/.test(password)) strength += 20;
  
  return strength;
});

// 密码强度状态
const passwordStrengthStatus = computed(() => {
  const strength = passwordStrength.value;
  if (strength < 40) return 'exception';
  if (strength < 70) return 'normal';
  return 'success';
});

// 密码强度文字描述
const passwordStrengthText = computed(() => {
  const strength = passwordStrength.value;
  if (strength < 40) return '弱';
  if (strength < 70) return '中';
  return '强';
});

// 注册表单提交处理
const handleSubmit = async (values: RegisterForm) => {
  try {
    loading.value = true;
    
    // 构建注册参数
    const registerData = {
      username: values.username,
      password: values.password,
      ...activeKey.value === 'mobile' 
        ? { mobile: values.mobile, code: values.mobileCode } 
        : { email: values.email, code: values.emailCode }
    };
    
    // TODO: 调用实际注册API
    console.log('注册数据:', registerData);
    // 模拟API调用延迟
    await new Promise(resolve => setTimeout(resolve, 1500));
    
    message.success('注册成功，请登录');
    router.push('/login');
  } catch (error) {
    console.error('注册失败:', error);
    message.error('注册失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};

// 表单验证规则
const validateConfirmPassword = async (_rule: any, value: string) => {
  if (!value) {
    return Promise.reject('请再次输入密码');
  }
  if (value !== formState.password) {
    return Promise.reject('两次输入的密码不一致');
  }
  return Promise.resolve();
};

// 发送短信验证码
const sendMobileCode = () => {
  if (!isValidMobile.value) {
    message.error('请输入有效的手机号');
    return;
  }
  
  mobileCountdown.value = 60;
  const timer = setInterval(() => {
    mobileCountdown.value -= 1;
    if (mobileCountdown.value <= 0) clearInterval(timer);
  }, 1000);
  
  // TODO: 调用发送短信验证码API
  message.success('验证码已发送');
};

// 发送邮箱验证码
const sendEmailCode = () => {
  if (!isValidEmail.value) {
    message.error('请输入有效的邮箱');
    return;
  }
  
  emailCountdown.value = 60;
  const timer = setInterval(() => {
    emailCountdown.value -= 1;
    if (emailCountdown.value <= 0) clearInterval(timer);
  }, 1000);
  
  // TODO: 调用发送邮箱验证码API
  message.success('验证码已发送');
};

// 返回登录页
const goToLogin = () => {
  router.push('/login');
};
</script>

<template>
  <div class="register-page">
    <div class="register-container">
      <div class="register-header">
        <h1>注册账号</h1>
        <p>欢迎加入在线考试平台</p>
      </div>

      <Form
        :model="formState"
        layout="vertical"
        @finish="handleSubmit"
        class="register-form"
      >
        <!-- 基本信息 -->
        <Form.Item
          name="username"
          :rules="[{ required: true, message: '请设置用户名' }]"
        >
          <Input
            v-model:value="formState.username"
            placeholder="请设置用户名"
            size="large"
            :disabled="loading"
            autocomplete="username"
          >
            <template #prefix>
              <UserOutlined style="color: rgba(0, 0, 0, 0.25)" />
            </template>
          </Input>
        </Form.Item>

        <Form.Item
          name="password"
          :rules="[
            { required: true, message: '请设置密码' },
            { min: 8, message: '密码长度至少8位' }
          ]"
        >
          <div>
            <Input.Password
              v-model:value="formState.password"
              placeholder="请设置密码（8位以上，包含字母和数字）"
              size="large"
              :disabled="loading"
              autocomplete="new-password"
            >
              <template #prefix>
                <LockOutlined style="color: rgba(0, 0, 0, 0.25)" />
              </template>
            </Input.Password>
            <div v-if="formState.password" class="password-strength">
              <span>密码强度：{{ passwordStrengthText }}</span>
              <Progress :percent="passwordStrength" :status="passwordStrengthStatus" :showInfo="false" size="small" />
            </div>
          </div>
        </Form.Item>

        <Form.Item
          name="confirmPassword"
          :rules="[{ validator: validateConfirmPassword }]"
        >
          <Input.Password
            v-model:value="formState.confirmPassword"
            placeholder="请确认密码"
            size="large"
            :disabled="loading"
            autocomplete="new-password"
          >
            <template #prefix>
              <SafetyOutlined style="color: rgba(0, 0, 0, 0.25)" />
            </template>
          </Input.Password>
        </Form.Item>

        <!-- 验证方式选项卡 -->
        <Tabs v-model:activeKey="activeKey" centered>
          <!-- 手机号验证 -->
          <Tabs.TabPane key="mobile" tab="手机号验证">
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
              >
                <template #prefix>
                  <MobileOutlined style="color: rgba(0, 0, 0, 0.25)" />
                </template>
              </Input>
            </Form.Item>

            <Form.Item
              name="mobileCode"
              :rules="[{ required: true, message: '请输入验证码' }]"
            >
              <Input
                v-model:value="formState.mobileCode"
                placeholder="验证码"
                size="large"
                :disabled="loading"
              >
                <template #suffix>
                  <Button
                    type="link"
                    @click="sendMobileCode"
                    :disabled="mobileCountdown > 0 || loading || !isValidMobile"
                    :style="{ color: mobileCountdown || loading || !isValidMobile ? '#666' : themeColors.primary }"
                  >
                    {{ mobileCountdown ? `${mobileCountdown}s后重发` : '获取验证码' }}
                  </Button>
                </template>
              </Input>
            </Form.Item>
          </Tabs.TabPane>

          <!-- 邮箱验证 -->
          <Tabs.TabPane key="email" tab="邮箱验证">
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
              >
                <template #prefix>
                  <MailOutlined style="color: rgba(0, 0, 0, 0.25)" />
                </template>
              </Input>
            </Form.Item>

            <Form.Item
              name="emailCode"
              :rules="[{ required: true, message: '请输入验证码' }]"
            >
              <Input
                v-model:value="formState.emailCode"
                placeholder="验证码"
                size="large"
                :disabled="loading"
              >
                <template #suffix>
                  <Button
                    type="link"
                    @click="sendEmailCode"
                    :disabled="emailCountdown > 0 || loading || !isValidEmail"
                    :style="{ color: emailCountdown || loading || !isValidEmail ? '#666' : themeColors.primary }"
                  >
                    {{ emailCountdown ? `${emailCountdown}s后重发` : '获取验证码' }}
                  </Button>
                </template>
              </Input>
            </Form.Item>
          </Tabs.TabPane>
        </Tabs>

        <Form.Item
          name="agreement"
          :rules="[{ required: true, message: '请阅读并同意用户协议和隐私政策', transform: (value) => !!value }]"
        >
          <Checkbox 
            v-model:checked="formState.agreement" 
            :disabled="loading"
          >
            我已阅读并同意
            <a :style="{ color: themeColors.primary }">《用户协议》</a>
            和
            <a :style="{ color: themeColors.primary }">《隐私政策》</a>
          </Checkbox>
        </Form.Item>

        <Form.Item>
          <Button
            type="primary"
            html-type="submit"
            block
            class="submit-btn"
            :loading="loading"
          >
            {{ loading ? '注册中...' : '立即注册' }}
          </Button>
        </Form.Item>

        <div class="register-extra">
          <span>已有账号？</span>
          <a @click="goToLogin" :style="{ color: themeColors.primary, cursor: loading ? 'not-allowed' : 'pointer' }">
            立即登录
          </a>
        </div>
      </Form>
    </div>
  </div>
</template>

<style scoped lang="scss">
.register-page {
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
    pointer-events: none;
  }
}

.register-container {
  max-width: 580px;
  width: 100%;
  padding: 50px 45px;
  background: rgba(255, 255, 255, 0.82);
  backdrop-filter: blur(8px);
  border-radius: 16px;
  box-shadow: 0 12px 36px rgba(0, 0, 0, 0.35);
  position: relative;
  z-index: 2;
  
  .register-header {
    text-align: center;
    margin-bottom: 30px;
    color: v-bind('themeColors.primary');

    h1 {
      font-size: 40px;
      margin-bottom: 12px;
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

  .password-strength {
    margin-top: 8px;
    display: flex;
    flex-direction: column;
    gap: 4px;
    
    span {
      font-size: 14px;
      color: #666;
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
    font-size: 17px;
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

  .register-extra {
    text-align: center;
    margin-top: 20px;
    font-size: 18px;
    color: #666;
    
    a {
      margin-left: 6px;
      transition: color 0.3s;
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
    padding: 40px 20px;
    border-radius: 10px;
    
    .register-header {
      margin-bottom: 30px;
      
      h1 {
        font-size: 32px;
        margin-bottom: 10px;
      }
      
      p {
        font-size: 18px;
      }
    }
  }
}
</style> 