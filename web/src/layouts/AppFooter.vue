<script setup lang="ts">
import { CopyrightOutlined } from '@ant-design/icons-vue'

const currentYear = new Date().getFullYear()
</script>

<template>
  <div class="app-footer">
    <div class="footer-copyright">
      Copyright
      <CopyrightOutlined style="margin: 0 4px" />
      {{ currentYear }} voxAI Node. All rights reserved
    </div>
  </div>
</template>

<style scoped lang="scss">
.app-footer {
  text-align: center;
  padding: 2px 0;
}

.footer-links {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 40px;
  
  .footer-link {
    color: var(--ant-color-text-secondary);
    font-size: 16px;
    transition: all 0.3s;
    cursor: pointer;
    
    &:hover {
      color: var(--ant-color-text);
    }
    
    .icon-svg {
      width: 1em;
      height: 1em;
      vertical-align: -0.125em;
      fill: currentColor;
    }
  }
}

.footer-copyright {
  color: var(--ant-color-text-secondary);
  font-size: 14px;
  transition: all 0.3s;
}

.wechat-qrcode-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>

