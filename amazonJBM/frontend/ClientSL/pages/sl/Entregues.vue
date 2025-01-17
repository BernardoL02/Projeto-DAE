<script setup>
import Template from '../template.vue';
import Table from '../table.vue';
import { ref, onMounted } from 'vue';
import { useRuntimeConfig } from '#app';

const config = useRuntimeConfig();
const api = config.public.API_URL;

const encomendasTableTitles = ['ID Encomenda', 'Utilizador', 'Data de Expedição', 'Data de Entrega', 'Estado'];
const encomendasTableData = ref([]);
const currentPage = 'Entregues';
const errorMessages = ref([]);
const showConfirmModal = ref(false);
const selectedEncomendaId = ref(null);

// Função para obter o token do sessionStorage
const getToken = () => sessionStorage.getItem('token');

// Função para exibir a mensagem de erro como um alerta estilizado
const showError = (message) => {
  errorMessages.value.push(message);

  setTimeout(() => {
    errorMessages.value.shift();
  }, 5000);
};

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
    const token = getToken();
    const response = await fetch(`${api}/encomendas/estado/Entregue`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }

    const data = await response.json();
    encomendasTableData.value = data.map(encomenda => ({
      id: encomenda.id,
      username: encomenda.username,
      dataExpedicao: new Date(encomenda.data_expedicao).toLocaleString(),
      dataEntrega: encomenda.data_entrega
        ? new Date(encomenda.data_entrega).toLocaleString("pt-PT", {
          day: "2-digit",
          month: "2-digit",
          year: "numeric",
          hour: "2-digit",
          minute: "2-digit",
          second: "2-digit",
        })
        : "Não definido",
      estado: formatEstado(encomenda.estado)
    }));
  } catch (error) {
    showError(error.message);
  }
};

const confirmarEntrega = async (id) => {
  try {
    const token = getToken();
    const payload = { estado: "Entregue" };

    const response = await fetch(`${api}/encomendas/${id}`, {
      method: 'PATCH',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(payload)
    });

    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }

    // Atualiza a lista de encomendas após sucesso
    fetchEncomendasPendentes();
  } catch (error) {
    showError(error.message);
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
    <h1>Sistema de Logistica - Encomendas Entregues</h1>
  </div>

  <!-- Mensagens de erro estilizadas -->
  <div v-if="errorMessages.length" class="fixed bottom-4 right-4 space-y-2 z-50">
    <div v-for="(error, index) in errorMessages" :key="index"
      class="bg-red-500 text-white py-4 px-6 rounded shadow-lg w-96">
      <h3 class="font-semibold text-lg mb-2">Erro</h3>
      <p>{{ error }}</p>
    </div>
  </div>

  <Table :tableTitles="encomendasTableTitles" :tableData="encomendasTableData" :currentPage="currentPage"
    @confirmEntrega="handleConfirmEntrega" />

  <!-- Modal de Confirmação -->
  <div v-if="showConfirmModal" class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white w-1/3 p-6 rounded shadow-lg">
      <h2 class="text-xl font-semibold mb-4">Confirmar Entrega</h2>
      <p>Tem certeza que deseja marcar a encomenda ID {{ selectedEncomendaId }} como "Entregue"?</p>
      <div class="mt-4 flex justify-end space-x-2">
        <button @click="showConfirmModal = false"
          class="bg-gray-500 text-white py-1 px-4 rounded hover:bg-gray-700">Cancelar</button>
        <button @click="confirmarEntrega(selectedEncomendaId); showConfirmModal = false"
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

.fixed div {
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
