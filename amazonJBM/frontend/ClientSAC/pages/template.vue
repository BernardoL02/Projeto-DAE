<script setup>
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
  
        <!-- Páginas de navegação -->
        <div class="space-x-16 hidden md:flex">
          <a href="page1" class="text-white font-semibold text-base relative  hover:font-bold transition duration-150 ease-in-out group">
              Pagina1
              <span class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"></span>
          </a>
          <a href="#" class="text-white font-semibold text-base relative  hover:font-bold transition duration-150 ease-in-out group">
              Pagina2
              <span class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"></span>
          </a>
          <a href="#" class="text-white font-semibold text-base relative  hover:font-bold transition duration-150 ease-in-out group">
              Pagina3
              <span class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"></span>
          </a>
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

.fundoNavBar {
  background: linear-gradient(40deg, #202c38 25%, #66c981 120%);
}

</style>