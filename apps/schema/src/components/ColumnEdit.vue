<template>
  <LayoutForm v-if="value">
    <InputString
      v-model="column.name"
      :errorMessage="validateName(column.name)"
      label="Name"
    />
    <InputSelect
      v-model="column.columnType"
      :options="columnTypes"
      label="Column type"
    />
    <InputSelect
      v-if="column.columnType == 'STRING'"
      v-model="column.columnFormat"
      :options="['', 'HYPERLINK']"
      label="Column format"
    />
    <InputText v-model="column.description" label="Description" />
    <div
      v-if="
        column.columnType == 'REF' ||
        column.columnType == 'REF_ARRAY' ||
        column.columnType == 'MREF' ||
        column.columnType == 'REFBACK'
      "
    >
      <InputString
        v-model="column.refSchema"
        label="refSchema (only needed if referencing outside schema)"
      />
      <InputSelect
        v-model="column.refTable"
        :errorMessage="
          column.refTable == undefined || column.name == ''
            ? 'Referenced table is required'
            : undefined
        "
        :options="tables"
        label="Referenced table"
      />
      <InputString
        v-if="column.columnType == 'REFBACK'"
        v-model="column.refBack"
        label="refBack"
      />
      <InputString
        v-if="column.columnType == 'REF' || column.columnType == 'REF_ARRAY'"
        v-model="column.refLink"
        label="refLink"
      />
    </div>
    <InputSelect
      v-if="column.columnType != 'CONSTANT'"
      v-model="column.key"
      :options="[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]"
      label="Key"
    />
    <InputBoolean
      v-if="column.columnType != 'CONSTANT'"
      v-model="column.required"
      label="required"
    />

    <InputText
      v-if="column.columnType != 'CONSTANT'"
      v-model="column.validation"
      label="validation"
      help="Example: if(row.name != 'John') return 'name must be John'"
    />
    <InputText
      v-model="column.visible"
      label="visible"
      help="Example: if(row.other > '') return true;"
    />
    <InputString
      v-model="column.semantics"
      :list="true"
      label="semantics (should be command separated list of IRI, or keyword 'id')"
    />
  </LayoutForm>
</template>

<script>
import {
  LayoutForm,
  InputText,
  InputString,
  InputBoolean,
  InputSelect,
} from "@mswertz/emx2-styleguide";
import columnTypes from "../columnTypes";

export default {
  components: {
    LayoutForm,
    InputText,
    InputString,
    InputBoolean,
    InputSelect,
  },
  props: {
    /** Column metadata object entered as v-model */
    value: Object,
    /** table column is part of */
    table: Object,
    /** listof tables for references */
    tables: Array,
  },
  methods: {
    validateName(name) {
      if (
        Array.isArray(this.table.columns) &&
        this.table.columns.filter((c) => c.name == name).length != 1
      ) {
        return "Name should be unique";
      }
      if (name == undefined) {
        return "Name is required";
      }
      if (!name.match(/^[a-zA-Z][a-zA-Z0-9_]+$/)) {
        return "Name should start with letter, followed by letter, number or underscore ([a-zA-Z][a-zA-Z0-9_]*)";
      }
    },
  },
  data() {
    return {
      // of type 'column metadata'
      column: null,
      //the options
      columnTypes,
    };
  },
  created() {
    this.column = this.value;
  },
  watch: {
    column() {
      if (this.column != null) {
        this.$emit("input", this.column);
      }
    },
    value() {
      this.column = this.value;
    },
  },
};
</script>
