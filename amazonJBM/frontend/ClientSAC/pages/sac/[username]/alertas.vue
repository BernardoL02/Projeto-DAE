<script setup>
import Template from '../../template.vue';
import Table from '../../table.vue';

import { useRoute } from 'vue-router';
import { ref, computed } from 'vue';

const currentPage = "Alertas";

const route = useRoute()
const config = useRuntimeConfig()
const api = config.public.API_URL
const username = route.params.username;

const tableTitles = ['Horario', 'Valor', 'Mensagem'];
const tableData = ref([
  // Objeto com Horário, Valor e Mensagem
  {
    horario: "2024-11-01T12:30:00.000Z",
    valor: "30°C",
    mensagem: "O sensor detectou temperatura elevada."
  },
  {
    horario: "2024-11-01T12:35:00.000Z",
    valor: "85%",
    mensagem: "Umidade está acima do limite seguro."
  },
  {
    horario: "2024-11-01T12:40:00.000Z",
    valor: "7.5 m/s²",
    mensagem: "Vibração anormal detectada."
  },
  {
    horario: "2024-11-01T12:45:00.000Z",
    valor: "50 ppm",
    mensagem: "O nível de gás está elevado."
  }
]);

// Mapeando os dados para a estrutura que a tabela requer
const formattedTableData = tableData.value.map(data => [
  data.horario,  // Para a coluna Horário
  data.valor,    // Para a coluna Valor
  data.mensagem  // Para a coluna Mensagem
]);



</script>

<template>

    <Template :username="username" :currentPage="currentPage"></Template> <!-- Importar o Template -->

    <div class="flex flex-row">
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
        
        <div class="flex flex-col items-start ml-28 mt-12 space-y-6 w-full">
            <Table :tableTitles="tableTitles" :tableData="tableData" :mostrarOperacoes="false"/>
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
