<script setup>
import Pagination from "../components/gallery/Pagination.vue";
import { onBeforeMount, ref, computed } from "vue";
import ApiRepository from "./../assets/ApiRepository/ApiRepository.js";
import { userStore } from "../stores/userStore";
import { imageUser } from "../stores/imageUser";
import router from "../router/index";

const userItem = userStore();
const imageUserItem = imageUser();

function update(id, member, memberImage) {
  userItem.userObject = member;
  imageUserItem.imageUserObject = memberImage;
  router.push("/modificarusuario" + "/" + id);
}

function updateImage(id, member, memberImage) {
  userItem.userObject = member;
  imageUserItem.userImageObject = memberImage;
  router.push("/registrofotousuario" + "/" + id)
}

const repository = new ApiRepository("quienesSomos");
const api = repository.chooseApi();

const memberCardxPage = 2;
const start = ref(0);
const end = computed(() =>
  Math.min(start.value + memberCardxPage, membersList.value.length)
);

let membersList = ref([]);
onBeforeMount(async () => {
  membersList.value = await api.getAll();
});

const membersToShow = computed(() => {
  return membersList.value.slice(start.value, end.value);
});

const next = () => {
  start.value += memberCardxPage;
};

const prev = () => {
  start.value = Math.max(start.value - memberCardxPage, 0);
};

const page = (algo) => {
  start.value = algo;
};

function deletePost(id) {
  if (confirm("¿Está seguro de que quiere borrar a este usuario?") == true) {
    deleteThis(id);
  }
}

async function deleteThis(id) {
  const respone = fetch(`http://localhost:8080/api/users/${id}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
  }).then((response) => {
    if (response.status == 200) {
      alert("Usuario borrado correctamente.");
      location.reload();
    } else {
      alert(
        "Se ha producido un error. \nPor favor, inténtelo de nuevo en unos minutos."
      );
    }
  });
}

</script>
<template>
  <main>
    <div class="card mb-3" v-for="member in membersToShow" :key="member.id" :member="member">
      <div class="row g-0">
        <div v-if="member.imageUser">
          <img :src="`http://localhost:8080/images/user-photos/${member.imageUser.image}`" class="img-fluid" alt="..." />
        </div>

        <div class="gap-3">
          <div class="text-name">
            <p class="font-name"><b>{{ member.lastname }}</b>, {{ member.name }}</p>

            <p class="font-italic">Fecha admisión: {{ member.dateadmission }}</p>

          </div>
          <div class="card-body">
            <p class="btnsUser">
              <button type="button" class="btn btn-danger" @click="deletePost(member.id)">
                Borrar
              </button>
              <button type="button" class="btn btn-warning"
                @click="update(member.id, member, member.imageUser)">Modificar</button>
              <button type="button" class="btn btn-success"
                @click="updateImage(member.id, member, member.imageUser)">Imagen</button>
            </p>
          </div>
        </div>
      </div>
    </div>

    <Pagination :pageSize="memberCardxPage" :start="start" :end="end" :maxLength="membersList.length" @change="page"
      @prev="prev" @next="next" />
  </main>
</template>

<style scoped lang="scss">
@import "../assets/sass/styles.scss";
@import "../assets/sass/galleryStyles/gallerystyles.scss";

.row {
  width: 90%;
  margin: auto;
  margin-top: 5vw;
  display: grid;
  grid-template-columns: 2fr 8fr;
}

img {
  aspect-ratio: 16/9;
  object-fit: cover;
  width: 200px;
}

.btn {
  margin: 0.3em;
  width: 5.4em;
}

.card-body {
  display: flex;
  justify-content: end;
  align-items: flex-end;
}

.col-md-1 {
  display: flex;
  justify-content: center;
}

.gap-3 {
  width: 90%;
  display: flex;
  padding: 0.7rem;
  background-color: $background-card;
}

@media (max-width: 767px) {
  .row {
    grid-template-columns: 1fr;
  }

  img {
    width: 100%;
    margin-top: 5vw;
    aspect-ratio: 16/9;
    object-fit: cover;
  }

  .btnsUser {
    display: flex;
    justify-content: space-around;
    // width: 100%;
    // justify-content: center;
  }

  .font-name {
    font-size: 1rem;

  }

  .gap-3 {
    display: block;
    width: 100%;
    margin: auto;
    margin-top: 1rem;
  }

  .card-body {
    display: flex;
    justify-content: center;
    margin-top: 0.7rem;
  }
}

.card {
  background-color: $background-component
}
</style>
