<script setup>
import Template from '../template.vue';
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router'; // Importar useRouter
import { useRuntimeConfig } from '#app';

const config = useRuntimeConfig();
const api = config.public.API_URL;
const router = useRouter(); // Inicializar o router

const clientes = ref([]);
const produtos = ref([]);
const selectedCliente = ref(null);
const selectedProdutos = reactive([]);
const produtoSelecionado = ref(null);
const successMessage = ref('');
const errorMessage = ref('');
const currentPage = 'CriarEncomenda';

// Função para obter o token do sessionStorage
const getToken = () => sessionStorage.getItem('token');

// Função para buscar todos os clientes
const fetchClientes = async () => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/clientes`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
    if (!response.ok) throw new Error("Erro ao buscar clientes");

    clientes.value = await response.json();
  } catch (error) {
    console.error("Erro ao carregar clientes:", error);
  }
};

// Função para buscar todos os produtos
const fetchProdutos = async () => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/produtos`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
    if (!response.ok) throw new Error("Erro ao buscar produtos");

    produtos.value = await response.json();
  } catch (error) {
    console.error("Erro ao carregar produtos:", error);
  }
};

// Função para adicionar um produto à lista de produtos selecionados
const adicionarProduto = (produto) => {
  if (!produto) return;
  const existingProduct = selectedProdutos.find(p => p.id === produto.id);
  if (!existingProduct) {
    selectedProdutos.push({ ...produto, quantidade_por_volume: 1 });
  }
};

// Função para remover um produto da lista de produtos selecionados
const removerProduto = (produtoId) => {
  const index = selectedProdutos.findIndex(p => p.id === produtoId);
  if (index !== -1) {
    selectedProdutos.splice(index, 1);
  }
};

// Função para criar uma nova encomenda
const criarEncomenda = async () => {
  if (!selectedCliente.value) {
    errorMessage.value = "Selecione um cliente.";
    setTimeout(() => errorMessage.value = '', 3000);
    return;
  }

  if (selectedProdutos.length === 0) {
    errorMessage.value = "Adicione pelo menos um produto.";
    setTimeout(() => errorMessage.value = '', 3000);
    return;
  }

  const dataExpedicao = new Date().toISOString().split('.')[0]; // Remove frações de segundo
  const dataEntrega = new Date();
  dataEntrega.setDate(dataEntrega.getDate() + 5);
  const dataEntregaISO = dataEntrega.toISOString().split('.')[0]; // Remove frações de segundo

  const encomendaData = {
    username: selectedCliente.value,
    estado: "EmProcessamento",
    data_expedicao: dataExpedicao,
    data_entrega: dataEntregaISO,
    produtos: selectedProdutos.map(p => ({
      id: p.id,
      nome: p.nome,
      quantidade_por_volume: p.quantidade_por_volume
    }))
  };

  console.log("Enviando JSON para o backend:", encomendaData);

  try {
    const token = getToken();
    const response = await fetch(`${api}/encomendas`, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(encomendaData)
    });

    if (!response.ok) throw new Error("Erro ao criar encomenda");

    successMessage.value = "Encomenda criada com sucesso!";
    setTimeout(() => {
      successMessage.value = '';
      router.push('./gestao');
    }, 1000);

    // Reset form
    selectedCliente.value = null;
    selectedProdutos.splice(0);
    
  } catch (error) {
    errorMessage.value = "Erro ao criar encomenda.";
    setTimeout(() => errorMessage.value = '', 3000);
  }
};

onMounted(() => {
  fetchClientes();
  fetchProdutos();
});
</script>

<template>
  <Template :currentPage="currentPage" />

  <div v-if="successMessage" class="fixed top-0 left-0 w-full flex justify-center mt-4 z-50">
    <div class="bg-green-500 text-white py-2 px-4 rounded shadow-md">
      {{ successMessage }}
    </div>
  </div>

  <div v-if="errorMessage" class="fixed top-0 left-0 w-full flex justify-center mt-4 z-50">
    <div class="bg-red-500 text-white py-2 px-4 rounded shadow-md">
      {{ errorMessage }}
    </div>
  </div>

  <div class="flex flex-col justify-center mx-auto mt-10 p-6 mb-10 bg-white shadow-md rounded-lg border border-gray-300 w-full max-w-5xl">
    <div class="mb-8">
      <h1 class="text-center text-2xl font-semibold mb-4">Criar Nova Encomenda</h1>

      <div class="mb-4">
        <label class="block text-gray-700 font-semibold mb-1">Cliente:</label>
        <select v-model="selectedCliente" class="w-full p-2 border border-gray-300 rounded">
          <option value="" disabled selected>Selecione um cliente</option>
          <option v-for="cliente in clientes" :key="cliente.username" :value="cliente.username">
            {{ cliente.username }}
          </option>
        </select>
      </div>

      <div class="mb-4">
        <label class="block text-gray-700 font-semibold mb-1">Produtos:</label>
        <div>
          <select v-model="produtoSelecionado" class="w-full p-2 border border-gray-300 rounded mb-2">
            <option value="" disabled selected>Selecione um produto</option>
            <option v-for="produto in produtos" :key="produto.id" :value="produto">{{ produto.nome }}</option>
          </select>
          <button @click="adicionarProduto(produtoSelecionado)" class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700 transition">
            Adicionar Produto
          </button>
        </div>
      </div>

      <div v-if="selectedProdutos.length > 0" class="mb-4">
        <h3 class="text-lg font-semibold">Produtos Selecionados:</h3>
        <ul>
          <li v-for="produto in selectedProdutos" :key="produto.id" class="p-2 bg-gray-100 rounded my-2">
            <div class="flex justify-between items-center">
              <div>
                <strong>{{ produto.nome }}</strong> - Quantidade:
                <input v-model="produto.quantidade_por_volume" type="number" min="1" class="w-16 p-1 border border-gray-300 rounded text-center" />
              </div>
              <button @click="removerProduto(produto.id)" class="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-700 transition">
                Remover
              </button>
            </div>
          </li>
        </ul>
      </div>

      <button @click="criarEncomenda" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700 transition">
        Criar Encomenda
      </button>
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
