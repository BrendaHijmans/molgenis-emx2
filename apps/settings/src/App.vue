<template>
  <div id="app">
    <Molgenis title="Settings" v-model="session">
      <div
        v-if="
          session.email == 'admin' ||
            (session.roles && session.roles.includes('Manager'))
        "
        class="card"
      >
        <div class="card-header">
          <ul class="nav nav-tabs card-header-tabs">
            <li
              class="nav-item"
              v-for="(label, key) in {
                members: 'Members',
                layout: 'Layout',
                menu: 'Menu',
                pages: 'Pages'
              }"
            >
              <router-link
                class="nav-link"
                :class="{ active: selected == label }"
                :to="key"
                >{{ label }}
              </router-link>
            </li>
          </ul>
        </div>
        <div class="card-body">
          <router-view :session="session" />
        </div>
      </div>
      <div v-else>
        You have to be logged in with right permissionsto see settings
      </div>
    </Molgenis>
  </div>
</template>

<script>
import {Molgenis} from "@mswertz/emx2-styleguide";

export default {
  components: {
    Molgenis
  },
  data() {
    return {
      session: {}
    };
  },
  computed: {
    selected() {
      return this.$route.name;
    }
  }
};
</script>
