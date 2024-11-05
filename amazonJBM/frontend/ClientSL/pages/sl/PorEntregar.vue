<script setup>
import Template from '../template.vue';
import Table from '../table.vue';
import { ref, onMounted } from 'vue';
import { useRuntimeConfig } from '#app';

const config = useRuntimeConfig();
const api = config.public.API_URL;

const encomendasTableTitles = ['ID Encomenda', 'Utilizador', 'Data de Expedição', 'Data de Entrega', 'Estado'];
const encomendasTableData = ref([]);
const currentPage = 'Por Entregar';
const errorMessages = ref([]);
const showConfirmModal = ref(false);
const selectedEncomendaId = ref(null);

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

const fetchEncomendasPendentes = async () => {
  try {
    const response = await fetch(`${api}/sl/encomendas/estado/PorEntregar`);
    if (!response.ok) throw new Error("Erro ao buscar encomendas 'Por Entregar'");

    const data = await response.json();
    encomendasTableData.value = data.map(encomenda => ({
      id: encomenda.id,
      username: encomenda.username,
      dataExpedicao: new Date(encomenda.data_expedicao).toLocaleString(),
      dataEntrega: new Date(encomenda.data_entrega).toLocaleString(),
      estado: formatEstado(encomenda.estado)
    }));
  } catch (error) {
    errorMessages.value.push(`Erro ao carregar encomendas 'Por Entregar': ${error.message}`);
  }
};

const confirmarEntrega = async (id) => {
  try {
    const response = await fetch(`${api}/sl/encomendas/${id}`, {
      method: 'PATCH',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ estado: "Entregue" })
    });
    if (!response.ok) throw new Error(`Erro ao entregar encomenda ${id}`);
    fetchEncomendasPendentes();
  } catch (error) {
    errorMessages.value.push(`Erro ao entregar encomenda ${id}: ${error.message}`);
  }
};

const handleConfirmEntrega = (id) => {
  selectedEncomendaId.value = id;
  showConfirmModal.value = true;
};

onMounted(() => {
  fetchEncomendasPendentes();
});
</script>

<template>
  <Template :currentPage="currentPage" />

  <div class="flex justify-center mr-24 mt-20">
    <h1>Sistema de Logistica - Encomendas Por Entregar</h1>
  </div>

  <div v-if="errorMessages.length" class="text-red-500 text-center mt-4">
    <p v-for="(error, index) in errorMessages" :key="index">{{ error }}</p>
  </div>

  <Table 
    :tableTitles="encomendasTableTitles" 
    :tableData="encomendasTableData" 
    @confirmEntrega="handleConfirmEntrega"
  />

  <!-- Modal de Confirmação -->
  <div v-if="showConfirmModal" class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white w-1/3 p-6 rounded shadow-lg">
      <h2 class="text-xl font-semibold mb-4">Confirmar Entrega</h2>
      <p>Tem certeza que deseja marcar a encomenda ID {{ selectedEncomendaId }} como "Entregue"?</p>
      <div class="mt-4 flex justify-end space-x-2">
        <button @click="showConfirmModal = false" class="bg-gray-500 text-white py-1 px-4 rounded hover:bg-gray-700">Cancelar</button>
        <button @click="confirmarEntrega(selectedEncomendaId); showConfirmModal = false" class="bg-green-500 text-white py-1 px-4 rounded hover:bg-green-700">Confirmar</button>
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
