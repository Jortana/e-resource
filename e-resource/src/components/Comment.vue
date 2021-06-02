<template>
  <!-- 打分和评论 -->
  <div class="comment-container">
    <h2>评论</h2>
    <div class="comments">
    </div>
  </div>
</template>

<script>
export default {
  name: 'Comment',
  props: {
    id: Number
  },
  watch: {
    id: {
      handler () {
        this.getComment()
      },
      immediate: true
    }
  },
  data () {
    return {
      comments: []
    }
  },
  methods: {
    getComment () {
      console.log(this.id)
      this.comments = []
    }
  }
}
</script>

<style scoped>
.comment-container h2 {
  font-weight: 500;
}
</style>
