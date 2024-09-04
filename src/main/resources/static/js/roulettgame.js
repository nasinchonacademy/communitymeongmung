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

    start: function (uid) {
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
            this.sendPointsToServer(reward.points, uid);

        }, 4000);
    },

    sendPointsToServer: function (points, uid) {
        $.ajax({
            url: '/game_list/update_jelly_points',
            method: 'POST',
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8', // 요청의 Content-Type 설정
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
        var uid = target.getAttribute('data-uid');
        console.log('UID:', uid); // 확인용 로그 추가
        rouletter.start(uid);
    }
});
