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
const mostrarAlertasModal = ref(false); 
const alertasData = ref([]); 

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

const verAlertasEncomenda = async (id) => {
  try {
    const response = await fetch(`${api}/sac/encomendas/${id}/alertas`);
    if (!response.ok) throw new Error("Erro ao buscar alertas da encomenda");

    const data = await response.json();
    alertasData.value = data.sensores.map(sensor => ({
      id: sensor.id,
      tipo: sensor.tipo,
      alertas: sensor.alertas.map(alerta => ({
        id: alerta.id,
        mensagem: alerta.mensagem,
        timeStamp: alerta.timeStamp,
        valor: alerta.valor
      }))
    }));
    
    mostrarAlertasModal.value = true;

  } catch (error) {
    console.error("Erro ao buscar alertas da encomenda:", error);
  }
};


</script>

<template>

  <Template :username="username" :currentPage="currentPage"></Template> <!-- Importar o Template -->

  <div class="flex justify-between items-center ml-28 mr-28 mt-20">
    <div>
      <h1 class="text-xl font-semibold text-gray-800">Bem-vindo, {{ username }}</h1>
      <p class="mt-1 text-lg text-gray-700">Encomendas</p>
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
  
  <Table @verAlertas="verAlertasEncomenda" :tableTitles="tableTitles" :tableData="tableData" :mostrarOperacoes="true"/>

    <!-- Modal de Alertas -->
    <div v-if="mostrarAlertasModal" class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white w-1/2 p-6 rounded shadow-lg relative">
        <button @click="mostrarAlertasModal = false" class="m-4 absolute top-2 right-2 text-gray-600 hover:text-gray-900">
          <i class="fas fa-times"></i>
        </button>
        <h2 class="text-xl font-semibold mb-4">Alertas da Encomenda</h2>
    
        <div v-if="alertasData.length === 0" class="flex flex-col items-center text-gray-600 p-6 border border-gray-300 bg-gray-50 rounded-lg">
          <i class="fas fa-info-circle text-3xl text-blue-500 mb-2"></i>
          <p class="text-lg font-medium">Encomenda sem alertas</p>
        </div>
        <div v-else>
          <div v-for="sensor in alertasData" :key="sensor.id" class="mb-4 p-4 bg-gray-100 rounded-lg border">
            <p class="font-semibold">Sensor ID: {{ sensor.id }} - Tipo: {{ sensor.tipo }}</p>
            <ul class="mt-2 space-y-2">
              <li v-for="alerta in sensor.alertas" :key="alerta.id" class="p-3 bg-yellow-100 rounded-lg border">
                <p><strong>ID do Alerta:</strong> {{ alerta.id }}</p>
                <p><strong>Data:</strong> {{ formatDate(alerta.timeStamp) }}</p>
                <p><strong>Mensagem:</strong> {{ alerta.mensagem }}</p>
                <p><strong>Valor:</strong> {{ alerta.valor }}</p>
              </li>
            </ul>
          </div>
        </div>
      </div>
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
