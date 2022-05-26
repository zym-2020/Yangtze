<template>
  <div class="invite-wrapper">
    <h2>Ready to get started?</h2>
    <p>Create an account to join us in sharing data.</p>
    <div class="button-wrapper">
      <button class="button">Sign up</button>
    </div>
  </div>
</template>

<script>
export default {
  name: "invitePart",
};
</script>

<style lang='scss'>
div.invite-wrapper {
  width: 40%;
  height: 24em;
  margin-left: 30%;
  margin-right: 30%;
  margin-top: 4%;
  background-color: transparent;
  border-radius: 5px 5px 5px 5px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2em;

  h2 {
    margin-top: 2em;
    font-size: 2.8em;
    color: rgb(226, 226, 226);
  }

  p {
    font-size: 1.4em;
    color: rgba(199, 199, 199, 0.85);
  }

  div.button-wrapper {
    height: 3em;
    display: flex;
    align-items: center;
    justify-content: center;

    button.button {
      width: 9em;
      height: 3em;
      font-family: "Roboto", sans-serif;
      font-size: 1em;
      letter-spacing: 2.5px;
      font-weight: 500;
      color: #000;
      background-color: #fff;
      border: none;
      border-radius: 45px;
      box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);
      transition: all 0.3s ease 0s;
      cursor: pointer;
      outline: none;

      &:hover {
        background-color: #2e83e5;
        box-shadow: 0px 15px 20px rgba(76, 86, 228, 0.4);
        color: rgb(226, 231, 255);
        transform: translateY(-7px);
      }
    }
  }
}
</style>