<script setup>

const props = defineProps({
  currentPage: {
    type: String,
    required: false
  }
});

const icon = '/Images/IconJBM.png';
</script>

<template>
  <nav class="fundoNavBar flex justify-center items-center bg-PrimaryColor p-4 shadow-xl">
    <div class="container mx-auto flex justify-between items-center md:px-16">

      <div class="flex items-center justify-center">
        <div class="relative w-[110px] h-[110px] rounded-full bg-white p-[-2] pl-[1px]">
          <img :src="icon" class="rounded-full w-full h-full object-cover">
        </div>
      </div>

      <div class="space-x-16 hidden md:flex mr-28">
        <a href="/sensores" :class="currentPage === 'Sensores' ? 'highlighted' : ''"
          class="text-white font-semibold text-base relative hover:font-bold transition duration-150 ease-in-out group">
          Sensores
          <span
            class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"
            :class="currentPage === 'Sensores' ? 'w-full' : ''"></span>
        </a>
      </div>

      <!-- Nome do sistema -->
      <div class="text-right">
        <div class="text-white font-bold">Amazon JBM</div>
        <div class="text-white">Sensores</div>
      </div>
    </div>
  </nav>

  <div>

  </div>
</template>

<style scoped>
body {
  background-color: white;
  height: 100vh;
  margin: 0;
}

.fundoNavBar {
  background: linear-gradient(40deg, #202c38 25%, #66c981 120%);
}
</style>