<script setup>
import Template from '../../template.vue';
import Table from '../../table.vue';

import { useRoute } from 'vue-router';
import { ref, computed } from 'vue';

const currentPage = "Encomendas";

const route = useRoute()
const config = useRuntimeConfig()
const api = config.public.API_URL
const username = route.params.username;

const tableData = ref([]);
const tableTitles = ['ID Encomenda', 'Data de Expedição', 'Data de Entrega', 'Estado'];

const { data, error } = await useFetch(`${api}/sac/encomendas`);

const formatDate = (dateString) => {
  return dateString.replace('T', ' '); 
};

const formateEstado = (estado) => {

  if (estado === "EmProcessamento") {
    return estado.replace("EmProcessamento", "Em Processamento"); 
  }
  else if (estado === "PorEntregar") {
    return estado.replace("PorEntregar", "Por Entregar"); 
  }

  return estado;
};


watchEffect(() => {
  if (data.value) {

    const fetchedData = data.value;

    tableData.value = fetchedData.map(order => [
      order.id,                            
      formatDate(order.data_expedicao),        
      formatDate(order.data_entrega),   
      formateEstado(order.estado)           
    ]);
  }
});


//Filtros por estado das encomendas
let estado = ref("Todas");
watch(
  estado,
  async (newValue, oldValue) => {
    if (newValue !== oldValue) {
      try {

        let fetchedData; 

        if (newValue === "Todas") {
          fetchedData = await $fetch(`${api}/sac/encomendas`);
        } else {
          fetchedData = await $fetch(`${api}/sac/encomendas/estado/${newValue}`);
        }

        tableData.value = fetchedData.map(order => [
          order.id,                             
          formatDate(order.data_expedicao),    
          formatDate(order.data_entrega),       
          formateEstado(order.estado)            
        ]);

      } catch (error) {
        console.error("Erro ao buscar encomendas!");
      }
    }
  }
);
</script>

<template>

  <Template :username="username" :currentPage="currentPage"></Template> <!-- Importar o Template -->

  <div class="flex justify-between items-center ml-28 mr-28 mt-20">
    <div>
      <h1 class="text-xl font-semibold text-gray-800">Bem-vindo, {{ username }}</h1>
      <p class="text-lg text-gray-700">Encomendas</p>
    </div>
        
    <!-- Filtro de Encomendas -->
    <div class="w-64">
      <select v-model="estado" name="Encomendas" id="encomendas" class="block w-full p-2 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-SecundaryColor focus:border-SecundaryColor text-gray-700">
        <option value="Todas" class="text-gray-700">Todas</option>
        <option value="Entregue" class="text-gray-700">Entregue</option>
        <option value="EmProcessamento" class="text-gray-700">Em Processamento</option>
        <option value="PorEntregar" class="text-gray-700">Por Entregar</option>
        <option value="Cancelada" class="text-gray-700">Cancelada</option>
      </select>
    </div>
  </div>
  
  <Table :tableTitles="tableTitles" :tableData="tableData" :mostrarOperacoes="true"/>

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
