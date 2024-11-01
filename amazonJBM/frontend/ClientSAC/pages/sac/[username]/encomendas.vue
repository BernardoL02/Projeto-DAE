<script setup>
import Template from '../../template.vue';
import Table from '../../table.vue';

import { useRoute } from 'vue-router';

const currentPage = "Encomendas";

const route = useRoute()
const config = useRuntimeConfig()
const api = config.public.API_URL
const username = route.params.username;

const tableData = ref([]);
const tableTitles = ['ID Encomenda', 'Data de Expedição', 'Data de Entrega', 'Estado'];

const { data, error } = await useFetch(`${api}/sac/encomendas/${username}`);

// Function to format the date
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

</script>

<template>

  <Template :username="username" :currentPage="currentPage"></Template> <!-- Importar o Template -->

  <div class="flex justify-center mr-24 mt-20">
    <h1>Bem vindo {{ username }}</h1>
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
