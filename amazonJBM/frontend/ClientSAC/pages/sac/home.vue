<script setup>
import Template from '../template.vue';
import Table from '../table.vue';

const route = useRoute()
const config = useRuntimeConfig()
const api = config.public.API_URL

const tableData = ref([]);
const tableTitles = ['ID Encomenda', 'Data de Expedição', 'Data de Entrega', 'Estado'];

const { data, error } = await useFetch(`${api}/sac/encomendas/Bernardo`);

// Function to format the date
const formatDate = (dateString) => {
  return dateString.replace('T', ' '); // Replace 'T' with a space
};

watchEffect(() => {
  if (data.value) {

    const fetchedData = data.value;

    tableData.value = fetchedData.map(order => [
      order.id,                            
      formatDate(order.data_expedicao),        
      formatDate(order.data_entrega),   
      order.estado           
    ]);
  }
});

const currentPage = "Home";
</script>

<template>
  <Template :currentPage="currentPage"></Template> <!-- Importar o Template -->

  <div class="flex justify-center mr-24 mt-20">
    <h1>Encomendas dos Utilizadores</h1>
  </div>
  
  <Table :tableTitles="tableTitles" :tableData="tableData" />
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
