<script setup>
const icon = '/Images/IconJBM.png';
const title = "Sistema de Logistica"
</script>

<template>
  <div class="login-container h-screen flex items-center justify-center">
    <div class="bg-white rounded-lg shadow-3xl p-8 w-96">

      <div class="flex items-center justify-center mb-6">
        <img :src="icon" alt="Logo do Site" width="200px" height="200px"> 
      </div>
      
      <h2 class="text-2xl font-bold text-center mb-6"> {{ title }}</h2>

      <form >
        <div class="mb-4">
          <input type="username" placeholder="Username" class="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-SecundaryColor"/>
        </div>

        <div class="mb-6">
          <input type="password" placeholder="Password" class="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-SecundaryColor"/>
        </div>

        <div class="flex justify-center items-center text-sm mb-4 space-x-28 mt-6">
          
          <nuxt-link to="./page1">
            <button type="submit" class="bg-PrimaryColor hover:bg-SecundaryColor text-white font-semibold py-2 px-8 rounded-full transition duration-300 ease-in-out shadow-md focus:outline-none focus:ring-2 focus:ring-SecundaryColor">
              Login
            </button>
          </nuxt-link>

        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>

.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(40deg, #202c38 10%, #66c981 130%);
}

</style>