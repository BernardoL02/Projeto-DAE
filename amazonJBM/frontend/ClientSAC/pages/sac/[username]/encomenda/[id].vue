<script setup>
import Template from '../../../template.vue';
import Table from '../../../table.vue';

import { useRoute } from 'vue-router';

const currentPage = "Encomenda";

const route = useRoute()
const config = useRuntimeConfig()
const username = route.params.username;
const encomendaId = route.params.id

const api = config.public.API_URL


const encomendaData = ref({});
const tableDataVolumes = ref([]);
const tableTitlesVolumes = ['ID', 'Produto', 'Quantidade',"Sensor","Útima leitura"];

// Obtenha os dados da encomenda com o ID e username fornecidos
const { data, error } = await useFetch(`${api}/sac/encomendas/${encomendaId}/${username}`);

// WatchEffect para processar os dados da encomenda
watchEffect(() => {
  if (data.value) {
    const fetchedData = data.value;

    // Mapeie os volumes da encomenda, incluindo tipos de sensores e valores lidos
    tableDataVolumes.value = fetchedData.volumes.flatMap(volume => {
      // Verifique se há sensores e mapeie-os
      if (volume.sensores.length > 0) {
        return volume.sensores.map(sensor => ({
          id: volume.id,
          nome_produto: volume.nome_produto,
          quantidade: volume.quantidade,
          tipo_sensor: sensor.tipoNome,
          valor_sensor: sensor.valor
        }));
      } else {
        // Caso não haja sensores, ainda precisamos incluir a linha com valores vazios
        return [{
          id: volume.id,
          nome_produto: volume.nome_produto,
          quantidade: volume.quantidade,
          tipo_sensor: 'Nenhum',
          valor_sensor: 'N/A'
        }];
      }
    });

    // Você pode armazenar os dados da encomenda em uma variável para uso no template
    encomendaData.value = {
      data_expedicao: fetchedData.data_expedicao,
      data_entrega: fetchedData.data_entrega,
      estado: fetchedData.estado
    };
  }
});

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

</script>

<template>
  
  <Template :username="username" :currentPage="currentPage"></Template> <!-- Importar o Template -->

  <!-- Contêiner para os detalhes da encomenda e a tabela -->
  <div class="flex flex-row justify-between mx-auto mt-10 p-6 mb-10 bg-white shadow-md rounded-lg border border-gray-300 w-full max-w-5xl">

    <!-- Detalhes da encomenda -->
    <div class="flex flex-col items-start p-4 w-1/2">
      <h1 class="text-center text-xl font-semibold mb-8">Detalhes da Encomenda</h1>
      <p class="text-gray-700"><strong>ID</strong>  {{ encomendaId }}</p><br> 
      <p class="text-gray-700"><strong>Data de Expedição</strong> <br> {{ formatDate(encomendaData.data_expedicao) }}</p><br> 
      <p class="text-gray-700"><strong>Data de Entrega</strong> <br> {{ formatDate(encomendaData.data_entrega) }}</p><br> 
      <p class="text-gray-700"><strong>Estado</strong> <br> {{ formateEstado(encomendaData.estado) }}</p><br> 
    </div>

    <!-- Tabela dos Volumes -->
    <div class="p-4">
      <h1 class=" ml-16 text-lg font-semibold mb-2">Volumes</h1>
      <Table class="m-80" :tableTitles="tableTitlesVolumes" :tableData="tableDataVolumes" :mostrarOperacoes="false" /> 
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
