<template>
  <button class="btn pl-1 pr-1 btn-outline-danger border-0" @click="onClick">
    <span v-if="label" class="mr-2">{{ label }}</span>
    <i :class="'fa fa-fw fa-' + icon"></i>
  </button>
</template>

<script>
/** Button that is shown as a icon */
export default {
  props: {
    icon: String,
    label: String,
  },
  methods: {
    onClick() {
      /** emitted on click */
      this.$emit("click");
    },
  },
};
</script>

<docs>
Example

```
<template>
  <IconDanger icon="times" @click="alert('clicked')"/>
</template>
<script>
  export default {
    methods: {
      alert(text) {
        alert(text)
      }
    }
  }
</script>
```

</docs>
