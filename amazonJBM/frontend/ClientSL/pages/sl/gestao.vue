<script setup>
import Template from '../template.vue';
import Table from '../table.vue';
import { ref, onMounted } from 'vue';
import { useRuntimeConfig, useRoute } from '#app';

const route = useRoute();
const config = useRuntimeConfig();
const api = config.public.API_URL;

// Definindo os títulos da tabela e os dados para as encomendas do utilizador
const encomendasTableTitles = ['ID Encomenda', 'Utilizador', 'Data de Expedição', 'Data de Entrega', 'Estado'];
const encomendasTableData = ref([]);
const currentPage = 'gestao';

const formatEstado = (estado) => {
  switch (estado) {
    case 'EmProcessamento':
      return 'Em Processamento';
    case 'PorEntregar':
      return 'Por Entregar';
    default:
      return estado;
  }
};

const formatDate = (dateString) => {
  return dateString.replace('T', ' '); 
};

const fetchEncomendasEmProcessamento = async () => {
  try {
    const response = await fetch(`${api}/sl/encomendas/estado/EmProcessamento`);
    if (!response.ok) throw new Error("Erro ao buscar encomendas Em Processamento");

    const data = await response.json();
    encomendasTableData.value = data.map(encomenda => ({
      id: encomenda.id,
      username: encomenda.username,
      dataExpedicao: new Date(encomenda.data_expedicao).toLocaleString(),
      dataEntrega: new Date(encomenda.data_entrega).toLocaleString(),
      estado: formatEstado(encomenda.estado)
    }));
  } catch (error) {
    console.error("Erro ao carregar encomendas Em Processamento:", error);
  }
};

onMounted(async () => {
  await fetchEncomendasEmProcessamento();
});
</script>

<template>
  <Template :currentPage="currentPage"></Template> <!-- Importar o Template -->

  <div class="flex justify-center mr-24 mt-20">
    <h1>Sistema de Logistica - Encomendas Em Processamento</h1>
  </div>
  
  <Table :tableTitles="encomendasTableTitles" :tableData="encomendasTableData" />
</template>

<style scoped>
  h1 {
    font-size: 1.5rem;
    font-weight: bold;
  }
</style>
