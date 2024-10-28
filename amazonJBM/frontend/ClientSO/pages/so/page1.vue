<script setup>
import Template from '../template.vue';
import Table from '../table.vue';

// Definindo os títulos da tabela e os dados para as encomendas em processamento ou por entregar
const encomendasTableTitles = ['ID Encomenda', 'Utilizador', 'Data de Expedição', 'Data de Entrega', 'Estado'];
const encomendasTableData = [
  ['1', 'user1', '2024-10-01', '2024-10-05', 'Por entregar'],
  ['2', 'user2', '2024-10-03', '2024-10-08', 'Por entregar'],
  ['3', 'user3', '2024-10-10', '2024-10-15', 'Em processamento'],
  ['4', 'user4', '2024-10-12', '2024-10-18', 'Em processamento'],
  ['5', 'user1', '2024-10-15', '2024-10-20', 'Em processamento'],
  ['5', 'user1', '2024-10-15', '2024-10-20', 'Em processamento'],
  ['5', 'user1', '2024-10-15', '2024-10-20', 'Em processamento'],
  ['5', 'user1', '2024-10-15', '2024-10-20', 'Em processamento'],
];

// Definindo os títulos da tabela e os dados para os alertas em tempo real
const alertasTableTitles = ['ID Alerta', 'Mensagem', 'ID Sensor', 'Time Stamp', 'ID Utilizador', 'ID Encomenda'];
const alertasTableData = [
  ['1', 'Temperatura acima do limite', '2', '2024-10-25 14:23:00', 'user1', '3'],
  ['2', 'Pressão atmosférica baixa', '3', '2024-10-25 15:10:00', 'user2', '4'],
  ['3', 'Aceleração excessiva detectada', '1', '2024-10-25 16:00:00', 'user3', '5'],
];
</script>

<template>
  <Template></Template> <!-- Importar o Template -->

  <div class="flex justify-center mr-24 mt-20">
    <h1>Sistema de Gestão</h1>
  </div>

  <Table :tableTitles="encomendasTableTitles" :tableData="encomendasTableData" />

  <Table :tableTitles="alertasTableTitles" :tableData="alertasTableData" />
</template>

<style scoped>
  h1 {
    font-size: 1.5rem;
    font-weight: bold;
  }

  .ml-10 {
    margin-left: 2.5rem;
  }
</style>