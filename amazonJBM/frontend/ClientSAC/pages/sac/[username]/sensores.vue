<script setup>
import Template from '../../template.vue';
import Table from '../../table.vue';

import { useRoute } from 'vue-router';
import { ref, computed } from 'vue';

const currentPage = "Sensores";

const route = useRoute()
const config = useRuntimeConfig()
const api = config.public.API_URL
const username = route.params.username;




</script>

<template>

    <Template :username="username" :currentPage="currentPage"></Template> <!-- Importar o Template -->

    <div class="flex flex-col items-start ml-28 mt-20 space-y-6">
        <h1 class="text-xl font-semibold text-gray-800">Alertas</h1>

        <!-- Input para o ID da encomenda -->
        <div class=" w-60">
            <label for="orderId" class="w-96 block text-sm font-medium text-gray-700 mb-2">Insira o ID da Encomenda para ver alertas da memsa:</label>
            <input type="text" id="orderId" class="block w-full p-2 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-SecundaryColor focus:border-SecundaryColor text-gray-700" placeholder="ID da Encomenda" />
        </div>
            
        <!-- Botão para buscar alertas -->
        <button @click="buscarAlertas" type="submit" class="bg-PrimaryColor hover:bg-SecundaryColor text-white font-semibold py-2 px-8 rounded-full transition duration-300 ease-in-out shadow-md focus:outline-none focus:ring-2 focus:ring-SecundaryColor">
            Ver Alertas
        </button>
    </div>


</template>

<style scoped>
body {
  background-color: white;
  height: 100vh;
  margin: 0;
}

h1 {
  font-size: 1.5rem;
  font-weight: bold;
}
</style>
