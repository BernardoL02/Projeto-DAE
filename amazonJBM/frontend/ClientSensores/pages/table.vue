<script setup>
import { defineProps, defineEmits } from 'vue';

// Recebe os títulos e dados da tabela como props
const props = defineProps({
  tableTitles: {
    type: Array,
    required: true,
  },
  tableData: {
    type: Array,
    required: true,
  },
});

// Define o evento para enviar a atualização ao componente pai
const emit = defineEmits(['update']);
</script>

<template>
  <div class="table-container p-8">
    <div :class="{'overflow-y-auto max-h-96': tableData.length > 7}" class="shadow-lg rounded-lg relative">
      <table class="min-w-full bg-white rounded-lg border border-gray-300">
        <!-- Cabeçalho da Tabela -->
        <thead class="bg-gradient-to-r from-purple-600 to-indigo-600 sticky top-0 z-10">
          <tr>
            <th v-for="(title, index) in tableTitles" :key="index" class="py-4 px-6 text-white font-bold text-center uppercase border border-gray-300">
              {{ title }}
            </th>
            <th class="py-4 px-6 text-white font-bold text-center uppercase border border-gray-300">Actions</th>
          </tr>
        </thead>

        <!-- Dados da Tabela -->
        <tbody>
          <tr v-for="(row, rowIndex) in tableData" :key="rowIndex" class="odd:bg-gray-100 even:bg-white hover:bg-indigo-50 transition duration-300">
            <td class="py-4 px-6 text-center border border-gray-300">{{ row.id }}</td>
            <td class="py-4 px-6 text-center border border-gray-300">{{ row.tipoNome }}</td>
            <td class="py-4 px-6 text-center border border-gray-300">
              <input v-model="row.valor" class="border rounded px-2 py-1 text-center w-full" />
            </td>
            <td class="py-4 px-6 text-center border border-gray-300">{{ row.estado }}</td>
            <td class="py-4 px-6 text-center border border-gray-300">{{ row.timeStamp }}</td>
            <td class="py-4 px-6 text-center border border-gray-300">
              <input v-model="row.bateria" class="border rounded px-2 py-1 text-center w-full" />
            </td>
            <td class="py-4 px-6 text-center border border-gray-300">{{ row.idEncomenda }}</td>
            <td class="py-4 px-6 text-center border border-gray-300">{{ row.idVolume }}</td>
            <td class="py-4 px-6 text-center border border-gray-300">
              <button @click="emit('update', row)" class="bg-green-500 text-white py-1 px-3 rounded hover:bg-green-700 transition">Confirmar</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped>
.table-container {
  max-width: 90%;
  margin: 0 auto;
}
th, td {
  padding: 16px;
}
.bg-gradient-to-r {
  background: #202c38;
}
.text-white {
  color: #ffffff;
}
.odd\:bg-gray-100 {
  background-color: #f7fafc;
}
.even\:bg-white {
  background-color: #ffffff;
}
.hover\:bg-indigo-50:hover {
  background-color: #ebf4ff;
}
button {
  cursor: pointer;
}
</style>
