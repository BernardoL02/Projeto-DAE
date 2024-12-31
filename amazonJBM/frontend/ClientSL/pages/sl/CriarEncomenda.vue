<script setup>
import Template from '../template.vue';
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useRuntimeConfig } from '#app';

const config = useRuntimeConfig();
const api = config.public.API_URL;
const router = useRouter();

const clientes = ref([]);
const searchCliente = ref("");
const showSuggestions = ref(false);

const produtos = ref([]);
const tiposEmbalagem = ref([]); // Tipos de embalagem
const selectedCliente = ref(null);
const volumes = reactive([]);
const volumeAtual = ref(null);
const successMessage = ref('');
const errorMessages = ref([]);

const currentPage = 'CriarEncomenda';

// Função para obter o token do sessionStorage
const getToken = () => sessionStorage.getItem('token');

// Função para exibir a mensagem de erro como um alerta estilizado
const showError = (message) => {
  errorMessages.value.push(message);

  // Remove o erro automaticamente após 5 segundos
  setTimeout(() => {
    errorMessages.value.shift();
  }, 5000);
};

// Buscar todos os clientes
const fetchClientes = async () => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/clientes`, {
      method: 'GET',
      headers: {
        Accept: 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });
    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }
    clientes.value = await response.json();
  } catch (error) {
    showError(error.message);
  }
};

// Computed para filtrar clientes com base no `searchCliente`
const filteredClientes = computed(() => {
  if (!searchCliente.value) {
    return clientes.value;
  }
  return clientes.value.filter((cliente) =>
    cliente.username.toLowerCase().includes(searchCliente.value.toLowerCase())
  );
});

// Função para selecionar o cliente e definir no modelo
const selectCliente = (username) => {
  selectedCliente.value = username;
  searchCliente.value = username;
  showSuggestions.value = false;
};

// Função para esconder sugestões ao perder o foco
const hideSuggestions = () => {
  setTimeout(() => {
    showSuggestions.value = false;
  }, 200);
};

// Buscar todos os produtos
const fetchProdutos = async () => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/produtos`, {
      method: 'GET',
      headers: {
        Accept: 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });
    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }
    produtos.value = await response.json();
  } catch (error) {
    showError(error.message);
  }
};

const filteredProdutos = (searchTerm) => {
  if (!searchTerm) {
    return produtos.value;
  }
  return produtos.value.filter((produto) =>
    produto.nome.toLowerCase().includes(searchTerm.toLowerCase())
  );
};

const selectProduto = (produto, embalagem) => {
  embalagem.produto = produto;
  embalagem.searchProduto = produto.nome;
  embalagem.showSuggestions = false;
};

const hideProdutoSuggestions = (embalagem) => {
  setTimeout(() => {
    embalagem.showSuggestions = false;
  }, 200);
};

// Buscar tipos de embalagem
const fetchTiposEmbalagem = async () => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/embalagem/tipos`, {
      method: 'GET',
      headers: {
        Accept: 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });
    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }
    tiposEmbalagem.value = await response.json();
  } catch (error) {
    showError(error.message);
  }
};

const filteredTiposEmbalagem = (searchTerm) => {
  if (!searchTerm) {
    return tiposEmbalagem.value;
  }
  return tiposEmbalagem.value.filter((tipo) =>
    tipo.tipo.toLowerCase().includes(searchTerm.toLowerCase())
  );
};

const selectTipo = (tipo, embalagem) => {
  embalagem.tipo = tipo.id;
  embalagem.searchTipo = tipo.tipo;
  embalagem.showTipoSuggestions = false;
};

const hideTipoSuggestions = (embalagem) => {
  setTimeout(() => {
    embalagem.showTipoSuggestions = false;
  }, 200);
};

// Função para adicionar um novo volume
let volumeCounter = 1;
const adicionarVolume = () => {
  const novoVolume = {
    id: volumeCounter,
    embalagens: [], // Contém embalagens
  };
  volumes.push(novoVolume);
  volumeCounter++;
  volumeAtual.value = novoVolume.id;
};

// Função para remover um novo volume
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


// Adicionar uma embalagem ao volume selecionado
const adicionarEmbalagemAoVolume = (volumeId) => {
  const volume = volumes.find((v) => v.id === volumeId);
  if (!volume) {
    showError("Volume não encontrado.");
    return;
  }

  const novaEmbalagem = {
    id: Date.now(),
    produto: null,
    searchProduto: "",
    showSuggestions: false,
    quantidade: 1,
    tipo: null,
    searchTipo: "",
    showTipoSuggestions: false,
  };

  volume.embalagens.push(novaEmbalagem);
};



// Remover uma embalagem de um volume
const removerEmbalagemDoVolume = (embalagemId, volumeId) => {
  const volume = volumes.find((v) => v.id === volumeId);
  if (!volume) return;

  const index = volume.embalagens.findIndex((e) => e.id === embalagemId);
  if (index !== -1) {
    volume.embalagens.splice(index, 1);
  }
};

// Função para criar a encomenda
const criarEncomenda = async () => {
  if (!selectedCliente.value) {
    showError('Selecione um cliente.');
    return;
  }

  if (volumes.length === 0 || volumes.every((v) => v.embalagens.length === 0)) {
    showError('Adicione ao menos uma embalagem a um volume.');
    return;
  }

  // Verificar se todas as embalagens possuem tipo e produto selecionados
  for (const volume of volumes) {
    for (const embalagem of volume.embalagens) {
      if (!embalagem.tipo) {
        showError('Selecione um tipo de embalagem para todas as embalagens.');
        return;
      }
      if (!embalagem.produto) {
        showError('Selecione um produto para todas as embalagens.');
        return;
      }
    }
  }

  const encomendaData = {
    username: selectedCliente.value,
    volumes: volumes.map((v) => ({
      embalagens: v.embalagens.map((e) => ({
        tipo: e.tipo,
        produto: { id: e.produto.id },
        quantidade: e.quantidade,
      })),
    })),
  };

  try {
    const token = getToken();
    const response = await fetch(`${api}/encomendas`, {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(encomendaData),
    });

    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }

    successMessage.value = 'Encomenda criada com sucesso!';
    setTimeout(() => {
      successMessage.value = '';
      router.push('./gestao');
    }, 1000);

    selectedCliente.value = null;
    volumes.splice(0);
  } catch (error) {
    showError(error.message);
  }
};

onMounted(() => {
  fetchClientes();
  fetchProdutos();
  fetchTiposEmbalagem();
});
</script>


<template>
  <Template :currentPage="currentPage" />

  <div v-if="successMessage" class="fixed top-0 left-0 w-full flex justify-center mt-4 z-50">
    <div class="bg-green-500 text-white py-2 px-4 rounded shadow-md">
      {{ successMessage }}
    </div>
  </div>

  <!-- Mensagens de erro estilizadas -->
  <div v-if="errorMessages.length" class="fixed bottom-4 right-4 space-y-2 z-50">
    <div v-for="(error, index) in errorMessages" :key="index"
      class="bg-red-500 text-white py-4 px-6 rounded shadow-lg w-96">
      <h3 class="font-semibold text-lg mb-2">Erro</h3>
      <p>{{ error }}</p>
    </div>
  </div>

  <div
    class="flex flex-col justify-center mx-auto mt-10 p-6 mb-10 bg-white shadow-md rounded-lg border border-gray-300 w-full max-w-5xl">
    <div class="mb-8">
      <h1 class="text-center text-2xl font-semibold mb-4">Criar Nova Encomenda</h1>

      <div class="mb-4">
        <label class="block text-gray-700 font-semibold mb-1">Cliente:</label>
        <input type="text" v-model="searchCliente" @focus="showSuggestions = true" @blur="hideSuggestions"
          placeholder="Pesquisar cliente pelo username"
          class="w-full p-2 border border-gray-300 rounded mb-2 focus:ring-green-500 focus:border-green-500" />
        <div v-if="showSuggestions && filteredClientes.length > 0" class="relative">
          <ul class="absolute z-10 w-full bg-white border border-gray-300 rounded max-h-48 overflow-y-auto shadow-lg">
            <li v-for="cliente in filteredClientes" :key="cliente.username" @mousedown="selectCliente(cliente.username)"
              class="p-2 hover:bg-gray-100 cursor-pointer">
              {{ cliente.username }}
            </li>
          </ul>
        </div>
      </div>

      <div class="mb-4">
        <h3 class="text-lg font-semibold mb-4 text-gray-700">Volumes</h3>
        <button @click="adicionarVolume"
          class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 transition">
          Adicionar Volume
        </button>

        <div v-for="volume in volumes" :key="volume.id" class="p-4 bg-gray-50 rounded-lg shadow border mt-4">
          <div class="flex justify-between items-center mb-4">
            <h4 class="text-md font-semibold text-gray-700">Volume {{ volume.id }}</h4>
            <div class="space-x-2">
              <button @click="adicionarEmbalagemAoVolume(volume.id)"
                class="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600 transition">
                Adicionar Embalagem
              </button>
              <button @click="removerVolume(volume.id)"
                class="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 transition">
                Remover Volume
              </button>
            </div>
          </div>

          <ul v-if="volume.embalagens.length > 0" class="space-y-4">
            <li v-for="embalagem in volume.embalagens" :key="embalagem.id"
              class="p-4 bg-white rounded-lg shadow border">
              <div class="grid grid-cols-2 gap-4 mb-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">Produto:</label>
                  <input type="text" v-model="embalagem.searchProduto" @focus="embalagem.showSuggestions = true"
                    @blur="hideProdutoSuggestions(embalagem)" placeholder="Pesquisar produto"
                    class="block w-full border border-gray-300 rounded px-2 py-1 focus:ring-green-500 focus:border-green-500" />
                  <div v-if="embalagem.showSuggestions && filteredProdutos(embalagem.searchProduto).length > 0"
                    class="relative">
                    <ul
                      class="absolute z-10 w-full bg-white border border-gray-300 rounded max-h-48 overflow-y-auto shadow-lg">
                      <li v-for="produto in filteredProdutos(embalagem.searchProduto)" :key="produto.id"
                        @mousedown="selectProduto(produto, embalagem)" class="p-2 hover:bg-gray-100 cursor-pointer">
                        {{ produto.nome }}
                      </li>
                    </ul>
                  </div>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">Tipo de Embalagem:</label>
                  <input type="text" v-model="embalagem.searchTipo" @focus="embalagem.showTipoSuggestions = true"
                    @blur="hideTipoSuggestions(embalagem)" placeholder="Pesquisar tipo de embalagem"
                    class="block w-full border border-gray-300 rounded px-2 py-1 focus:ring-green-500 focus:border-green-500" />
                  <div v-if="embalagem.showTipoSuggestions && filteredTiposEmbalagem(embalagem.searchTipo).length > 0"
                    class="relative">
                    <ul
                      class="absolute z-10 w-full bg-white border border-gray-300 rounded max-h-48 overflow-y-auto shadow-lg">
                      <li v-for="tipo in filteredTiposEmbalagem(embalagem.searchTipo)" :key="tipo.tipo"
                        @mousedown="selectTipo(tipo, embalagem)" class="p-2 hover:bg-gray-100 cursor-pointer">
                        {{ tipo.tipo }}
                      </li>
                    </ul>
                  </div>
                </div>
              </div>

              <div class="flex justify-between items-center">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">Quantidade:</label>
                  <input v-model="embalagem.quantidade" type="number" min="1"
                    class="border border-gray-300 rounded px-2 py-1 w-24 text-center focus:ring-green-500 focus:border-green-500" />
                </div>
                <button @click="removerEmbalagemDoVolume(embalagem.id, volume.id)"
                  class="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 transition">
                  Remover Embalagem
                </button>
              </div>
            </li>
          </ul>
        </div>
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
