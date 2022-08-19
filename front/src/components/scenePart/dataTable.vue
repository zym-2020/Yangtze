<template>
  <div class="data-table-wrapper">
    <table class="tb-container">
      <thead>
        <tr>
          <th v-for="head in tbHead" :key="head.index">
            <h1>{{ head }}</h1>
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="row in tbBody" :key="row.index">
          <td>{{ row.index }}</td>
          <td>{{ row.datA }}</td>
          <td>{{ row.datB }}</td>
          <td>{{ row.datC }}</td>
          <td>{{ row.datD }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script lang='ts'>
export default {
  name: "dataTable",
};
</script>

<script setup lang="ts">
import { ref } from "vue";
import { frontData } from "../../frontData";

interface Props {
  tableId: string;
}

const props = defineProps<Props>();

const tableIndex = ref(props.tableId);

const tableData = frontData["tables"][+tableIndex.value];

const tbHead = ref(tableData["tHead"]);

const tbBody = ref(tableData["tBody"]);
</script>


<style lang='scss' scoped>
div.data-table-wrapper {
  background-color: transparent;
  height: 50%;
  width: 23%;

  table.tb-container {
    overflow: hidden;
    width: 100%;
    margin: -0.1em -0.3em -0.1em -0.1em;
    display: table;
    h1 {
      font-size: 1.5em;
      line-height: 1em;
      text-align: center;
      color: rgba(255, 255, 255, 0.9);
    }
    td {
      font-weight: normal;
      font-size: 1em;
      box-shadow: 0 2px 2px -2px #0e1119;
    }
    td:first-child {
      color: #b7dfff;
      font-weight: 10em;
    }
    td,
    th {
      padding-bottom: 1.2%;
      padding-top: 1.2%;
      padding-left: 2%;
      padding-right: 2%;
      text-align: center;
      color: rgba(255, 255, 255, 0.8);
    }
    th {
      padding-bottom: 0.5%;
      padding-top: 0.5%;
    }
    thead {
      tr:first-child {
        th:first-child {
          border-top-left-radius: 0.6em;
        }
        th:last-child {
          border-top-right-radius: 0.6em;
        }
      }
    }
    tr:nth-child(odd) {
      background-color: rgba(5, 28, 56, 0.4);
    }
    tr:nth-child(even) {
      background-color: rgba(9, 30, 80, 0.4);
    }
    tr:last-child {
      td:first-child {
        border-bottom-left-radius: 0.6em;
      }
      td:last-child {
        border-bottom-right-radius: 0.6em;
      }
    }
    th {
      background-color: rgba(0, 41, 155, 0.3);
    }
    tr:hover {
      background-color: rgba(10, 76, 255, 0.3);
      box-shadow: 0 4px 4px -4px #0e1119;
    }
    td:hover {
      background-color: #42aaff;
      color: #dceaff;
      font-weight: bold;
      box-shadow: #21427f -1px 1px, #21427f -2px 2px, #21427f -3px 3px,
        #21427f -4px 4px, #21427f -5px 5px, #21427f -6px 6px;
      transform: translate3d(3px, -3px, 0);
      transition-delay: 0s;
      transition-duration: 0.4s;
      transition-property: all;
      transition-timing-function: line;
    }
  }
}
</style>