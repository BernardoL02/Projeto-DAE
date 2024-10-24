<script setup>
const icon = '/Images/IconJBM.png';
</script>

<template>
    <nav class="bg-PrimaryColor p-4 shadow-xl">
      <div class="container mx-auto flex justify-between items-center md:px-16">
        
        <div class="flex items-center justify-center">
          <div class="relative w-[110px] h-[110px] rounded-full bg-white p-[-2]">
            <img :src="icon" class="rounded-full w-full h-full object-cover"> 
          </div>
        </div>
        
  
        <!-- Páginas de navegação -->
        <div class="space-x-12 hidden md:flex">
          <a href="#" class="text-white font-semibold hover:text-SecundaryColor transition-colors duration-300">Pagina1</a>
          <a href="#" class="text-white font-semibold hover:text-SecundaryColor transition-colors duration-300">Pagina2</a>
          <a href="#" class="text-white font-semibold hover:text-SecundaryColor transition-colors duration-300">Pagina3</a>
        </div>
  
        <!-- Nome do sistema -->
        <div class="text-right">
          <div class="text-white font-bold">Amazon JBM</div>
          <div class="text-white">Sistema de Apoio ao Cliente</div>
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
</style>