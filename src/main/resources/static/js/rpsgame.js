const rspArr = ['r', 's', 'p'];

// 전역 변수로 이미지 경로를 설정
const imagePath = '/image/game/';

let score = 0;
let win = 0;
let lose = 0;

function winningRate() {
    if (win !== 0) {
        return (win / (win + lose)) * 100;
    }
    return 0;
}

function rps(userName, userChoice, computer) {
    const resultDisplay = document.querySelector('.result-display');
    let result = '';

    if (userChoice === 's' && computer === 'p'
        || userChoice === 'r' && computer === 's'
        || userChoice === 'p' && computer === 'r') {
        result = '승리! 젤리 30개가 적립되었어요.';
        win += 1;
        score += 1;
    } else if (userChoice === "s" && computer === 'r'
        || userChoice === "r" && computer === 'p'
        || userChoice === "p" && computer === 's') {
        result = '아쉽네요. 다음에 또 도전해보세요!';
        lose += 1;
        score -= 1;
    } else {
        result = '무승부';
    }

    resultDisplay.textContent = `결과: ${result}`;
}

function checkIfGamePlayedToday() {
    const today = new Date().toISOString().slice(0, 10);
    const lastPlayedDate = localStorage.getItem('lastPlayedDateRSP');
    return today === lastPlayedDate;
}

function setGamePlayedToday() {
    const today = new Date().toISOString().slice(0, 10);
    localStorage.setItem('lastPlayedDateRSP', today);
}

function initializeGame() {
    if (checkIfGamePlayedToday()) {
        disableButtons();
        document.querySelector('.result-display').textContent = "오늘은 이미 게임을 하셨군요! 내일 또 시도해주세요.";
    }
}

function disableButtons() {
    const rpsButtons = document.querySelectorAll('.rps-button');
    rpsButtons.forEach(button => button.disabled = true);
}

const rps1 = document.querySelector('#rps1');
let i = 0;
let intervalId1 = setInterval(function () {
    rps1.src = imagePath + `${rspArr[i % 3]}.png`;
    i += 1;
}, 100);

const rps2 = document.querySelector('#rps2');
let j = 0;
let intervalId2 = setInterval(function () {
    rps2.src = imagePath + `${rspArr[j % 3]}.png`;
    j += 1;
}, 120);

const sbtn = document.querySelector('#sbtn');
sbtn.addEventListener('click', function () {
    if (checkIfGamePlayedToday()) {
        alert("오늘은 이미 게임을 하셨군요! 내일 또 시도해주세요.");
        return;
    }

    clearInterval(intervalId1);
    clearInterval(intervalId2);

    rps1.src = imagePath + 's.png'; // imagePath 절대경로 지정!!!!!

    const computer = Math.floor(Math.random() * 3);
    rps2.src = imagePath + rspArr[computer] + '.png';

    rps('유나', 's', rspArr[computer]);

    setGamePlayedToday();
    disableButtons();
});

const rbtn = document.querySelector('#rbtn');
rbtn.addEventListener('click', function () {
    if (checkIfGamePlayedToday()) {
        alert("오늘은 이미 게임을 하셨군요! 내일 또 시도해주세요.");
        return;
    }

    clearInterval(intervalId1);
    clearInterval(intervalId2);

    rps1.src = imagePath + 'r.png';

    const computer = Math.floor(Math.random() * 3);
    rps2.src = imagePath + rspArr[computer] + '.png';

    rps('유나', 'r', rspArr[computer]);

    setGamePlayedToday();
    disableButtons();
});

const pbtn = document.querySelector('#pbtn');
pbtn.addEventListener('click', function () {
    if (checkIfGamePlayedToday()) {
        alert("오늘은 이미 게임을 하셨군요! 내일 또 시도해주세요.");
        return;
    }

    clearInterval(intervalId1);
    clearInterval(intervalId2);

    rps1.src = imagePath + 'p.png';

    const computer = Math.floor(Math.random() * 3);
    rps2.src = imagePath + rspArr[computer] + '.png';

    rps('유나', 'p', rspArr[computer]);

    setGamePlayedToday();
    disableButtons();
});

// Initialize game status on page load
initializeGame();
