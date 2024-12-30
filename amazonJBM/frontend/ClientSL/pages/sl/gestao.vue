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
const showConfirmModal = ref(false);
const selectedEncomendaId = ref(null);

// Função para obter o token do sessionStorage
const getToken = () => sessionStorage.getItem('token');

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
    const token = getToken();
    const response = await fetch(`${api}/encomendas/estado/EmProcessamento`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
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

const expedirEncomenda = async (id) => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/encomendas/${id}`, {
      method: 'PATCH',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ estado: "PorEntregar" })
    });
    if (!response.ok) throw new Error(`Erro ao entregar encomenda ${id}`);
    fetchEncomendasEmProcessamento();
  } catch (error) {
    console.error(`Erro ao entregar encomenda ${id}:`, error);
  }
};

const handleExpedirEncomenda = (id) => {
  selectedEncomendaId.value = id;
  showConfirmModal.value = true;
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

  <Table :tableTitles="encomendasTableTitles" :tableData="encomendasTableData"
    @expedirEncomenda="handleExpedirEncomenda" />

  <div v-if="showConfirmModal" class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white w-1/3 p-6 rounded shadow-lg">
      <h2 class="text-xl font-semibold mb-4">Confirmar Entrega</h2>
      <p>Tem certeza que deseja marcar a encomenda ID {{ selectedEncomendaId }} como "Por Entregar"?</p>
      <div class="mt-4 flex justify-end space-x-2">
        <button @click="showConfirmModal = false"
          class="bg-gray-500 text-white py-1 px-4 rounded hover:bg-gray-700">Cancelar</button>
        <button @click="expedirEncomenda(selectedEncomendaId); showConfirmModal = false"
          class="bg-green-500 text-white py-1 px-4 rounded hover:bg-green-700">Confirmar</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
h1 {
  font-size: 1.5rem;
  font-weight: bold;
}
</style>
