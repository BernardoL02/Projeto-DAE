<script setup>
import Template from '../template.vue';
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useRuntimeConfig } from '#app';

const config = useRuntimeConfig();
const api = config.public.API_URL;
const router = useRouter();

const clientes = ref([]);
const produtos = ref([]);
const selectedCliente = ref(null);
const produtoSelecionado = ref(null);
const volumes = reactive([]);
const volumeAtual = ref(null);
const successMessage = ref('');
const errorMessage = ref('');
const dropdownAberto = ref(false);

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

// Função para adicionar um novo volume
let volumeCounter = 1;

const adicionarVolume = () => {
  const novoVolume = {
    id: volumeCounter,
    produtos: [],
  };
  volumes.push(novoVolume);

  volumeCounter++;

  volumeAtual.value = novoVolume.id;
};

// Função para selecionar um volume
const selecionarVolume = (id) => {
  volumeAtual.value = id;
};

// Função para remover um volume
const removerVolume = (id) => {
  const index = volumes.findIndex((v) => v.id === id);
  if (index !== -1) {
    volumes.splice(index, 1);

    volumes.forEach((volume, idx) => {
      volume.id = idx + 1;
    });

    volumeCounter = volumes.length + 1;

    volumeAtual.value = volumes.length > 0 ? volumes[0].id : null;
  }
};

// Função para adicionar um produto ao volume selecionado
const adicionarProdutoAoVolume = (produto) => {
  if (!produto || !volumeAtual.value) {
    errorMessage.value = "Selecione um volume antes de adicionar produtos.";
    setTimeout(() => (errorMessage.value = ""), 3000);
    return;
  }
  const volume = volumes.find((v) => v.id === volumeAtual.value);
  if (!volume) return;

  const produtoExistente = volume.produtos.find((p) => p.id === produto.id);
  if (!produtoExistente) {
    volume.produtos.push({ ...produto, quantidade: 1 });
  }
  produtoSelecionado.value = null; // Resetar o dropdown
};

// Função para remover um produto de um volume
const removerProdutoDoVolume = (produtoId, volumeId) => {
  const volume = volumes.find((v) => v.id === volumeId);
  if (!volume) return;

  const index = volume.produtos.findIndex((p) => p.id === produtoId);
  if (index !== -1) {
    volume.produtos.splice(index, 1);
  }
};

// Função para criar uma nova encomenda
const criarEncomenda = async () => {
  if (!selectedCliente.value) {
    errorMessage.value = "Selecione um cliente.";
    setTimeout(() => (errorMessage.value = ''), 3000);
    return;
  }

  if (volumes.length === 0 || volumes.every((v) => v.produtos.length === 0)) {
    errorMessage.value = "Adicione ao menos um produto a um volume.";
    setTimeout(() => (errorMessage.value = ''), 3000);
    return;
  }

  const dataExpedicao = new Date().toISOString();

  const encomendaData = {
    username: selectedCliente.value,
    data_expedicao: dataExpedicao,
    volumes: volumes.map((v) => ({
      produtos: v.produtos.map((p) => ({
        id: p.id,
        quantidade_de_produtos_comprados: p.quantidade,
      })),
    })),
  };

  console.log("Enviando JSON:", encomendaData);

  try {
    const token = getToken();
    const response = await fetch(`${api}/encomendas`, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(encomendaData),
    });

    if (!response.ok) throw new Error("Erro ao criar encomenda");

    successMessage.value = "Encomenda criada com sucesso!";
    setTimeout(() => {
      successMessage.value = '';
      router.push('./gestao');
    }, 1000);

    // Resetar formulário
    selectedCliente.value = null;
    volumes.splice(0);
  } catch (error) {
    errorMessage.value = "Erro ao criar encomenda.";
    setTimeout(() => (errorMessage.value = ''), 3000);
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

  <div
    class="flex flex-col justify-center mx-auto mt-10 p-6 mb-10 bg-white shadow-md rounded-lg border border-gray-300 w-full max-w-5xl">
    <div class="mb-8">
      <h1 class="text-center text-2xl font-semibold mb-4">Criar Nova Encomenda</h1>

      <div class="mb-4">
        <label class="block text-gray-700 font-semibold mb-1">Cliente:</label>
        <select v-model="selectedCliente" class="w-full p-2 border border-gray-300 rounded">
          <option value="" disabled>Selecione um cliente</option>
          <option v-for="cliente in clientes" :key="cliente.username" :value="cliente.username">
            {{ cliente.username }}
          </option>
        </select>
      </div>

      <div class="mb-4">
        <h3 class="text-lg font-semibold">Volumes</h3>
        <button @click="adicionarVolume"
          class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700 transition">
          Adicionar Volume
        </button>

        <div v-for="volume in volumes" :key="volume.id" class="p-4 bg-gray-100 rounded my-4 border">
          <div class="flex justify-between items-center">
            <h4 class="text-md font-semibold">
              Volume {{ volume.id }}
              <span v-if="volumeAtual === volume.id" class="text-sm text-blue-500">(Selecionado)</span>
            </h4>
            <div>
              <button v-if="volumeAtual !== volume.id" @click="selecionarVolume(volume.id)"
                class="bg-blue-500 text-white px-2 py-1 rounded hover:bg-blue-700 transition">
                Selecionar
              </button>
              <button @click="removerVolume(volume.id)"
                class="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-700 transition ml-2">
                Remover
              </button>
            </div>
          </div>

          <ul v-if="volume.produtos.length > 0" class="mt-2">
            <li v-for="produto in volume.produtos" :key="produto.id"
              class="flex justify-between items-center p-2 bg-white rounded my-2">
              <div>
                <strong>{{ produto.nome }}</strong> - Quantidade:
                <input v-model="produto.quantidade" type="number" min="1"
                  class="w-16 p-1 border border-gray-300 rounded text-center" />
              </div>
              <button @click="removerProdutoDoVolume(produto.id, volume.id)"
                class="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-700 transition">
                Remover
              </button>
            </li>
          </ul>
        </div>
      </div>

      <div class="mb-4">
        <label class="block text-gray-700 font-semibold mb-1">Produtos:</label>
        <div class="relative">
          <button @click="dropdownAberto = !dropdownAberto"
            class="w-full p-2 border border-gray-300 rounded bg-white text-left">
            {{ produtoSelecionado?.nome || 'Selecione um produto' }}
          </button>
          <ul v-show="dropdownAberto"
            class="absolute z-10 w-full bg-white border border-gray-300 rounded max-h-48 overflow-y-auto shadow-lg">
            <li v-for="produto in produtos" :key="produto.id"
              @click="produtoSelecionado = produto; dropdownAberto = false"
              class="p-2 hover:bg-gray-100 cursor-pointer">
              {{ produto.nome }}
            </li>
          </ul>
        </div>
        <button @click="adicionarProdutoAoVolume(produtoSelecionado)" :disabled="!volumeAtual"
          class="mt-2 bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700 transition disabled:bg-gray-400">
          Adicionar ao Volume
        </button>
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

.custom-select {
  max-height: 150px;
  overflow-y: auto;
}
</style>
