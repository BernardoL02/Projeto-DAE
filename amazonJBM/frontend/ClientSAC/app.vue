<template>
  <div class="h-screen flex items-center justify-center">
    <div class="bg-white rounded-lg shadow-lg p-8 w-96">
      <h2 class="text-2xl font-bold text-center mb-6">Login</h2>
      
      <form>
        <div class="mb-4">
          <input
            type="email"
            placeholder="email address"
            class="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-400"
          />
        </div>

        <div class="mb-6">
          <input
            type="password"
            placeholder="password"
            class="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-400"
          />
        </div>

        <div class="text-center">
          <button
            type="submit"
            class="bg-purple-400 hover:bg-purple-500 text-white font-semibold py-2 px-4 rounded-full transition duration-300 ease-in-out shadow-md"
          >
            LOGIN
          </button>
        </div>
      </form>

      <p class="text-center text-sm text-gray-600 mt-6">
        <a href="#" class="text-blue-500 hover:underline">Sign Up</a>
      </p>
    </div>
  </div>
</template>

<script setup>
// Aqui você pode adicionar a lógica do Vue se precisar
</script>

<style>
body {
  background: linear-gradient(45deg, #66c981, #202c38);
}
</style>
