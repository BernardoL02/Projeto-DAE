<script setup>
import { defineProps } from 'vue';

// Props to receive titles and data from parent component
const props = defineProps({
  tableTitles: {
    type: Array,
    required: true
  },
  tableData: {
    type: Array,
    required: true
  }
});
</script>

<template>
  <div class="table-container p-8">
    <table class="min-w-full bg-white shadow-lg rounded-lg overflow-hidden border border-gray-300">
      <!-- Table Headings -->
      <thead class="bg-gradient-to-r from-purple-600 to-indigo-600">
        <tr>
          <th v-for="(title, index) in tableTitles" :key="index" class="py-4 px-6 text-white font-bold text-center uppercase border border-gray-300">
            {{ title }}
          </th>
          <th class="py-4 px-6 text-white font-bold text-center uppercase border border-gray-300">Actions</th>
        </tr>
      </thead>

      <!-- Table Data -->
      <tbody>
        <tr v-for="(row, rowIndex) in tableData" :key="rowIndex" class="odd:bg-gray-100 even:bg-white hover:bg-indigo-50 transition duration-300">
          <td v-for="(cell, cellIndex) in row" :key="cellIndex" class="py-4 px-6 text-center border border-gray-300">
            {{ cell }}
          </td>
          <td class="py-4 px-6 text-center border border-gray-300">
            <div v-if="row.includes('Em Processamento')">
              <button class="bg-blue-500 text-white py-1 px-3 rounded mr-2 hover:bg-blue-700 transition">Ver Detalhes</button>
              <button class="bg-red-500 text-white py-1 px-3 rounded hover:bg-red-700 transition">Cancelar</button>
            </div>
            <div v-else-if="row.includes('Por entregar')">
              <button class="bg-blue-500 text-white py-1 px-3 rounded mr-2 hover:bg-blue-700 transition">Ver Detalhes</button>
              <button class="bg-green-600 text-white py-1 px-3 rounded hover:bg-green-700 transition">Tracking</button>
            </div>
            <div v-else>
              <button class="bg-blue-500 text-white py-1 px-3 rounded hover:bg-blue-700 transition">Ver Detalhes</button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>
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
