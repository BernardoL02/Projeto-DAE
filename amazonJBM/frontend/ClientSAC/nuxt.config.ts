// nuxt.config.ts ou nuxt.config.js
export default defineNuxtConfig({
  compatibilityDate: '2024-04-03',
  devtools: { enabled: true },
  modules: ['@nuxtjs/tailwindcss', '@pinia/nuxt', '@pinia/nuxt'],  // Adicionado o módulo Tailwind CSS

  runtimeConfig: {
    public: {
    API_URL: process.env.API_URL || 'http://localhost:3001/backend/api'
    }
  }
  
})