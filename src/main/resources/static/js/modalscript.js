const buttons = document.querySelectorAll(".cardbtn");
const modal = document.getElementById("modalWrap");
const closeBtn = document.getElementById("closeBtn");

buttons.forEach((btn) => {
    btn.onclick = function () {
        modal.style.display = "block";
    };
});

closeBtn.onclick = function () {
    modal.style.display = "none";
};

window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
};
