var rouletter = {
    rewards: [
        { text: "100 포인트 젤리", points: 100 },    // 0 ~ 44도
        { text: "10 포인트 젤리", points: 10 },   // 45 ~ 89도
        { text: "10,000 포인트 젤리", points: 10000 },     // 90 ~ 134도
        { text: "1 포인트 젤리", points: 1 },  // 135 ~ 179도
        { text: "1000 포인트 젤리", points: 1000 },   // 180 ~ 224도
        { text: "100 포인트 젤리", points: 100 },  // 225 ~ 269도
        { text: "2000 포인트 젤리", points: 2000 },     // 270 ~ 314도
        { text: "1 포인트 젤리", points: 1 } // 315 ~ 359도
    ],

    getRewardByAngle: function (angle) {
        var index = Math.floor(angle / 45);
        return this.rewards[index];
    },

    randomAngleWithinSection: function () {
        var baseAngle = this.random() * 45;
        var offset = Math.floor(Math.random() * 45);
        return baseAngle + offset;
    },

    random: function () {
        return Math.floor(Math.random() * 8);
    },

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

        var randomDeg = (360 * 4) + this.randomAngleWithinSection();
        panel.style.transition = 'transform 4s cubic-bezier(0.33, 1, 0.68, 1)';
        panel.style.transform = 'rotate(' + randomDeg + 'deg)';

        setTimeout(() => {
            var actualAngle = randomDeg % 360;
            var reward = this.getRewardByAngle(actualAngle);
            alert('축하합니다! ' + reward.text + '를 받으셨습니다!');
            this.savePlayTime();
            btn.innerText = 'start';

            // 포인트를 서버에 전송
            this.sendPointsToServer(reward.points);

        }, 4000);
    },

    sendPointsToServer: function (points) {
        const uid = '[[${user.uid}]]';   // 사용자 ID 가져오기

        $.ajax({
            url: '/game_list/update-jelly-points', // 서버의 젤리 포인트 업데이트
            method: 'POST',
            data: {
                points: points, // 얻은 젤리 포인트
                uid: uid // 사용자 ID
            },
            success: function (response) {
                console.log('젤리 적립 성공!');
            },
            error: function (xhr, status, error) {
                console.error('포인트 적립 중 오류가 발생했습니다.', error);
            }
        });
    }
}

document.addEventListener('click', function (e) {
    var target = e.target;
    if (target.classList.contains('rouletter-btn')) {
        rouletter.start();
    }
});

document.addEventListener('click', function (e) {
    var target = e.target;
    if (target.classList.contains('rouletter-btn')) {
        // 버튼에서 유저 ID를 가져옴
        var uid = target.getAttribute('data-uid');
        rouletter.start(uid);
    }
});
