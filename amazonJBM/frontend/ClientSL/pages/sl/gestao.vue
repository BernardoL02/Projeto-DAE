<script setup>
import Template from "../template.vue";
import Table from "../table.vue";
import { ref, onMounted } from "vue";
import { useRuntimeConfig, useRoute } from "#app";

const route = useRoute();
const config = useRuntimeConfig();
const api = config.public.API_URL;

// Definindo os títulos da tabela e os dados para as encomendas do utilizador
const encomendasTableTitles = [
  "ID Encomenda",
  "Utilizador",
  "Data de Expedição",
  "Data de Entrega",
  "Estado",
];
const encomendasTableData = ref([]);
const currentPage = "gestao";
const showConfirmModal = ref(false);
const selectedEncomendaId = ref(null);

// Função para obter o token do sessionStorage
const getToken = () => sessionStorage.getItem("token");

const errorMessages = ref([]);
// Função para exibir a mensagem de erro como um alerta estilizado
const showError = (message) => {
  errorMessages.value.push(message);

  // Remove o erro automaticamente após 5 segundos
  setTimeout(() => {
    errorMessages.value.shift();
  }, 5000);
};

const formatEstado = (estado) => {
  switch (estado) {
    case "EmProcessamento":
      return "Em Processamento";
    case "PorEntregar":
      return "Por Entregar";
    default:
      return estado;
  }
};

const fetchEncomendasEmProcessamento = async () => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/encomenda/estado/EmProcessamento`, {
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: `Bearer ${token}`,
      },
    });
    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }

    const data = await response.json();
    encomendasTableData.value = data.map((encomenda) => ({
      id: encomenda.id,
      username: encomenda.username,
      dataExpedicao: encomenda.data_expedicao
        ? new Date(encomenda.data_entrega).toLocaleString("pt-PT", {
          day: "2-digit",
          month: "2-digit",
          year: "numeric",
          hour: "2-digit",
          minute: "2-digit",
          second: "2-digit",
        })
        : "Não definido",
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
      estado: formatEstado(encomenda.estado),
    }));
  } catch (error) {
    showError(error.message);
  }
};

const expedirEncomenda = async (id) => {
  try {
    const token = getToken();
    const payload = { estado: "PorEntregar" };

    const response = await fetch(`${api}/encomenda/${id}`, {
      method: "PATCH",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(payload),
    });

    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }

    // Atualiza a lista de encomendas após sucesso
    fetchEncomendasEmProcessamento();
  } catch (error) {
    showError(error.message);
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
  <Template :currentPage="currentPage"></Template>

  <div class="flex justify-center mt-20 px-4">
    <h1 class="text-center text-lg md:text-xl lg:text-2xl font-bold break-words">
      Sistema de Logística - Encomendas Em Processamento
    </h1>
  </div>

  <!-- Mensagens de erro estilizadas -->
  <div v-if="errorMessages.length" class="fixed bottom-4 right-4 space-y-2 z-50">
    <div v-for="(error, index) in errorMessages" :key="index"
      class="bg-red-500 text-white py-4 px-6 rounded shadow-lg w-96">
      <h3 class="font-semibold text-lg mb-2">Erro</h3>
      <p>{{ error }}</p>
    </div>
  </div>

  <Table :tableTitles="encomendasTableTitles" :tableData="encomendasTableData"
    @expedirEncomenda="handleExpedirEncomenda" />

  <div v-if="showConfirmModal" class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white w-1/3 p-6 rounded shadow-lg">
      <h2 class="text-xl font-semibold mb-4">Confirmar Entrega</h2>
      <p>
        Tem certeza que deseja marcar a encomenda ID
        {{ selectedEncomendaId }} como "Por Entregar"?
      </p>
      <div class="mt-4 flex justify-end space-x-2">
        <button @click="showConfirmModal = false" class="bg-gray-500 text-white py-1 px-4 rounded hover:bg-gray-700">
          Cancelar
        </button>
        <button @click="
          expedirEncomenda(selectedEncomendaId);
        showConfirmModal = false;
        " class="bg-green-500 text-white py-1 px-4 rounded hover:bg-green-700">
          Confirmar
        </button>
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
