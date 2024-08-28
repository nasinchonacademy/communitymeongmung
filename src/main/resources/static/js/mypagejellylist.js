function updateProgress(circleElement, progress) {
    const angle = progress * 3.6; // 퍼센트를 각도로 변환
    circleElement.style.background = `conic-gradient(
            #00c292 ${angle}deg,
            #e0e0e0 ${angle}deg 360deg
        )`;
    circleElement.setAttribute('data-progress', progress);
}

// 예시로 50%로 진행률을 설정합니다.
const progressCircle = document.querySelector('.progress-circle');
updateProgress(progressCircle, 50); // 50%로 업데이트