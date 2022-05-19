<template>
  <div class="graph-container">
    <div class="subject-container">
      <div v-for="(subject, index) in subjects" :key="index">
        <span
          :class="
            subject === curInfo.subject
              ? 'subject-label active-subject'
              : 'subject-label'
          "
          @click="changeSubject(subject)"
        >
          {{ subject }}
        </span>
      </div>
    </div>
    <div class="graph-card">
      <card-graph :info="curInfo"></card-graph>
    </div>
  </div>
</template>

<script>
import { recommend } from '@/api/entity'
import CardGraph from '@/components/Chart/CardGraph'
export default {
  name: 'KGCard',
  components: { CardGraph },
  data() {
    return {
      graphInfos: [],
      subjects: [],
      curInfo: {}
    }
  },
  mounted() {
    this.getInfo()
  },
  methods: {
    getInfo() {
      recommend().then((response) => {
        const { data } = response.data
        this.graphInfos = data
        data.forEach((graphInfo) => {
          this.subjects.push(graphInfo['subject'])
        })
        this.curInfo = this.graphInfos[0]
      })
    },
    changeSubject(subject) {
      this.curInfo = this.graphInfos.filter((graphInfo) => {
        return graphInfo.subject === subject
      })[0]
    }
  }
}
</script>

<style>
/* .graph-container {
  display: flex;
  justify-content: space-between;
  flex-flow: row wrap;
} */

.subject-container {
  display: flex;
  align-items: center;
  justify-content: space-around;
  height: 2rem;
  margin: 0.5rem 0;
  font-size: 0.9rem;
  background-color: #f7f5f4;
  color: #909399;
}

.subject-label {
  cursor: pointer;
}

.active-subject {
  color: #b92e2e;
}

.graph-card {
  flex: 0 0 32%;
  height: 280px;
  padding: 0.5rem;
  /* margin-bottom: 1.2rem; */
  background: linear-gradient(
    to left top,
    rgba(255, 255, 255, 1),
    rgba(255, 255, 255, 0.8)
  );
  box-shadow: 6px 6px 20px rgba(122, 122, 122, 0.1);
  overflow: hidden;
}
</style>
