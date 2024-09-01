var rouletter = {
    rewards: [
        "100 포인트 젤리",    // 0 ~ 44도
        "10 포인트 젤리",   // 45 ~ 89도
        "10,000 포인트 젤리",     // 90 ~ 134도
        "1 포인트 젤리",  // 135 ~ 179도
        "1000 포인트 젤리",   // 180 ~ 224도
        "100 포인트 젤리",  // 225 ~ 269도
        "2000 포인트 젤리",     // 270 ~ 314도
        "1 포인트 젤리" // 315 ~ 359도
    ],

    getRewardByAngle: function (angle) {
        // 각도를 45도씩 나눠 섹션 구분
        var index = Math.floor(angle / 45);
        return this.rewards[index];
    },

    randomAngleWithinSection: function () {
        // 45도 단위로 시작점 설정
        var baseAngle = this.random() * 45;
        // 섹션 내에서 랜덤한 0 ~ 44도 추가
        var offset = Math.floor(Math.random() * 45);
        return baseAngle + offset;
    },

    random: function () {
        return Math.floor(Math.random() * 8);
    },

    // 하루에 한번만 게임을 돌릴 수 있도록
    canPlayToday: function () {
        var lastPlayed = localStorage.getItem('lastPlayed');
        if (!lastPlayed) return true;
        var lastPlayedDate = new Date(parseInt(lastPlayed));
        var today = new Date();
        return today.toDateString() !== lastPlayedDate.toDateString();
    },

    savePlayTime: function () {
        localStorage.setItem('lastPlayed', Date.now());
    },

    start: function () {
        if (!this.canPlayToday()) {
            alert("오늘은 이미 룰렛을 돌리셨네요! 내일 또 돌리러 와주세요!");
            return;
        }

        var btn = document.querySelector('.rouletter-btn');
        var panel = document.querySelector('.rouletter-wacu');

        // 애니메이션 시작
        var randomDeg = (360 * 4) + this.randomAngleWithinSection();
        panel.style.transition = 'transform 4s cubic-bezier(0.33, 1, 0.68, 1)';
        panel.style.transform = 'rotate(' + randomDeg + 'deg)';

        // 애니메이션 끝난 후 결과 표시
        setTimeout(() => {
            var actualAngle = randomDeg % 360;
            var reward = this.getRewardByAngle(actualAngle);
            alert('축하합니다! ' + reward + '를 받으셨습니다!');
            this.savePlayTime();
            btn.innerText = 'start';
        }, 4000);  // 4초 뒤에 실행 (애니메이션 끝나는 시간과 맞춤)
    }
}

document.addEventListener('click', function (e) {
    var target = e.target;
    if (target.classList.contains('rouletter-btn')) {
        rouletter.start();
    }
});

