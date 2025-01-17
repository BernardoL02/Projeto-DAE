<template>
  <div class="table-container p-8">
    <div :class="{ 'overflow-y-auto max-h-[550px]': tableData.length > 9 }" class="shadow-lg rounded-lg relative">
      <table class="min-w-full bg-white rounded-lg border border-gray-300">
        <thead class="bg-gradient-to-r from-purple-600 to-indigo-600 sticky top-0 z-10">
          <tr>
            <th v-for="(title, index) in tableTitles" :key="index"
              class="py-4 px-6 text-white font-bold text-center uppercase border border-gray-300">
              {{ title }}
            </th>
            <th class="py-4 px-6 text-white font-bold text-center uppercase border border-gray-300">Actions</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="(row, rowIndex) in tableData" :key="rowIndex"
            class="odd:bg-gray-100 even:bg-white hover:bg-indigo-50 transition duration-300">
            <td class="py-4 px-6 text-center border border-gray-300">{{ row.id }}</td>
            <td class="py-4 px-6 text-center border border-gray-300">{{ row.tipoNome }}</td>

            <!-- Valor com valores máximo e mínimo lado a lado -->
            <td class="py-4 px-6 text-center border border-gray-300 relative">
              <input v-model="row.valor" :disabled="row.estado === 'inativo'"
                class="border rounded px-2 py-1 text-center w-full" />
              <div class="absolute bottom-0 left-0 right-0 flex justify-between px-2 text-xs font-semibold">
                <span v-if="row.valMax !== undefined" class="text-red-600">Max: {{ row.valMax }}</span>
                <span v-if="row.valMin !== undefined" class="text-blue-600">Min: {{ row.valMin }}</span>
              </div>
            </td>

            <td class="py-4 px-6 text-center border border-gray-300">{{ row.estado }}</td>
            <td class="py-4 px-6 text-center border border-gray-300">{{ row.timeStamp }}</td>
            <td class="py-4 px-6 text-center border border-gray-300">{{ row.bateria }}</td>
            <td class="py-4 px-6 text-center border border-gray-300">{{ row.idEncomenda }}</td>
            <td class="py-4 px-6 text-center border border-gray-300">{{ row.idVolume }}</td>

            <td class="py-4 px-6 text-center border border-gray-300">
              <button v-if="row.estado !== 'inativo'" @click="emit('update', row)"
                class="bg-green-500 text-white py-1 px-3 rounded hover:bg-green-700 transition mr-2">Confirmar</button>
              <button v-if="row.estado !== 'inativo'" @click="emit('cancel', row)"
                class="bg-red-500 text-white py-1 px-3 rounded hover:bg-red-700 transition">Desativar</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>


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

// Define os eventos para enviar a atualização e o cancelamento ao componente pai
const emit = defineEmits(['update', 'cancel']);
</script>

<style scoped>
.table-container {
  max-width: 90%;
  margin: 0 auto;
}

th,
td {
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
